package com.deserve.nearrestaurant

import android.location.Location
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.deserve.nearrestaurant.domain.AppErrors
import com.deserve.nearrestaurant.domain.GetNearByRestaurantsUseCase
import com.deserve.nearrestaurant.domain.GetPlaceImageUseCase
import com.deserve.nearrestaurant.domain.Resource
import com.deserve.nearrestaurant.domain.Restaurant
import com.deserve.nearrestaurant.log_in.LogInScreenState
import com.deserve.nearrestaurant.restaurant_detail.RestaurantScreenState
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class RestaurantViewModel: ViewModel() {

    private val _logInState = mutableStateOf(LogInScreenState())
    val logInState = _logInState

    private val _state = mutableStateOf(RestaurantScreenState())
    val state: State<RestaurantScreenState> = _state

    var onUpdate = mutableStateOf(0)

    private val getNearByRestaurantsUseCase = GetNearByRestaurantsUseCase()
    private val getPlaceImageUseCase = GetPlaceImageUseCase()
    var apiKey: String? = null

    fun updateApiKey(apiKey: String) {
        this.apiKey = apiKey
    }

    fun updateLocation(location: Location) {
        _state.value = state.value.copy(location = location)
    }

    fun updateRestaurantScreenState() {
        _state.value = state.value.copy(isLoading = false, error = null, restaurantList = emptyList())
    }

    private fun updateUI() {
        onUpdate.value = (0..1_000_000).random()
    }
    fun findRestaurant() {
        apiKey?.let { apiKey ->
            state.value.location?.let { location ->
                getNearByRestaurantsUseCase.invoke(
                    location.latitude.toString(),
                    location.longitude.toString(),
                    apiKey
                ).onEach { result ->
                    updateState(result) {
                        it?.let { restaurantList ->
                            _state.value = state.value.copy(restaurantList = restaurantList)
                            for (restaurant in restaurantList) {
                                getPlaceImageUrl(restaurant)
                            }
                        }
                    }
                }.launchIn(viewModelScope)
            } ?: run {
                _state.value = state.value.copy(error = AppErrors.NoLocationFound)
            }
        } ?: run {
            _state.value = state.value.copy(error = AppErrors.InvalidApiKey)
        }

    }

    fun getPlaceImageUrl(restaurant: Restaurant) {
        apiKey?.let { apiKey ->
            restaurant.id?.let {
                getPlaceImageUseCase.invoke(apiKey, restaurant.id)
                    .onEach {
                        updateState(it, showLoading = false, updateError = false) { url ->
                            url?.let {
                               restaurant.image = url
                                _state.value = state.value.copy(isLoading = false)
                                updateUI()
                            }
                        }
                    }.launchIn(viewModelScope)
            }
        } ?: run {
            _state.value = state.value.copy(error = AppErrors.InvalidApiKey)
        }
    }

    /*update request-response state here */
    private fun <T> updateState(result: Resource<T>,
                                showLoading: Boolean = true,
                                updateError: Boolean = true,
                                successState: (T?) -> Unit) {
        when (result) {
            is Resource.Success -> {
                _state.value = state.value.copy(isLoading = false, error = null)
                successState(result.data)
            }
            is Resource.Error -> {
                if (updateError) {
                    _state.value = state.value.copy(error = result.error, isLoading = false)
                } else {
                    _state.value = state.value.copy(isLoading = false)
                }

            }
            is Resource.Loading -> {
                if (showLoading) {
                    _state.value = state.value.copy(isLoading = true)
                }
            }
        }
    }
}