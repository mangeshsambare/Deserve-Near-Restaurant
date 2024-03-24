package com.deserve.nearrestaurant

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.deserve.nearrestaurant.domain.NearByRestaurantsUseCase
import com.deserve.nearrestaurant.domain.NetworkRequest
import com.deserve.nearrestaurant.domain.Resource
import com.deserve.nearrestaurant.restaurant_detail.RestaurantScreenState
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class RestaurantViewModel: ViewModel() {
    private val _state = mutableStateOf(RestaurantScreenState())
    val state: State<RestaurantScreenState> = _state

    private val nearByRestaurantsUseCase = NearByRestaurantsUseCase()

    fun findRestaurant(latitude: String, longitude: String) {
        nearByRestaurantsUseCase.invoke(
            latitude,
            longitude,
            NetworkRequest.API_KEY
        ).onEach { result ->
            updateState(result) {
                it?.let {
                    _state.value = state.value.copy(restaurantList = it)
                }

            }
        }.launchIn(viewModelScope)
    }

    /*update request-response state here */
    private fun <T> updateState(result: Resource<T>, successState: (T?) -> Unit) {
        when (result) {
            is Resource.Success -> {
                _state.value = state.value.copy(isLoading = false)
                successState(result.data)
            }
            is Resource.Error -> {
                _state.value = state.value.copy(error = result.error, isLoading = false)
            }
            is Resource.Loading -> {
                _state.value = state.value.copy(isLoading = true)
            }
        }
    }
}