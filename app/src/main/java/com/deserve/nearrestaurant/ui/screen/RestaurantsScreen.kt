package com.deserve.nearrestaurant.ui.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.deserve.nearrestaurant.R
import com.deserve.nearrestaurant.RestaurantViewModel
import com.deserve.nearrestaurant.domain.AppErrors
import com.deserve.nearrestaurant.domain.Restaurant
import com.deserve.nearrestaurant.restaurant_detail.RestaurantScreenState
import com.deserve.nearrestaurant.ui.common.AppAlertDialog
import com.deserve.nearrestaurant.ui.common.Loader

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun RestaurantsScreen(
    restaurantViewModel: RestaurantViewModel,
    navController: NavController
) {
    Scaffold(topBar = {
        TopAppBar(title = { Text(text = stringResource(id = R.string.restaurants)) },
            colors = topAppBarColors(containerColor = colorResource(id = R.color.purple_200))
        )
    }, content = {
        val restaurantScreenState by restaurantViewModel.state

        Loader(title = stringResource(id = R.string.loading),
                show = restaurantScreenState.isLoading)
        handleError(restaurantScreenState = restaurantScreenState) {
            restaurantViewModel.updateRestaurantScreenState()
            navController.popBackStack()
        }
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 70.dp),
            color = MaterialTheme.colorScheme.background
        ) {
            LazyColumn {
                items(restaurantViewModel.state.value.restaurantList) { restaurant ->
                    RestaurantItem(restaurantViewModel, restaurant)
                }
            }
        }
    })

    //Launched effect avoid calling multiple times after recomposition. To call suspend function safely from inside composable
    LaunchedEffect(key1 = Unit, block = {
        restaurantViewModel.findRestaurant()
    })
}

@Composable
private fun RestaurantItem(restaurantViewModel: RestaurantViewModel,
                           restaurant: Restaurant) {
    restaurantViewModel.onUpdate.value
    val imageState by remember { mutableStateOf(restaurant.image) }
    Surface {
        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primary,
            ),
            modifier = Modifier.padding(
                start = 8.dp,
                end = 8.dp,
                top = 4.dp,
                bottom = 4.dp
            ),
        ) {
            Column {
                Row(modifier = Modifier
                    .padding(
                        start = 24.dp,
                        end = 20.dp, top = 14.dp, bottom = 16.dp
                    )) {
                    Box(modifier = Modifier
                        .height(100.dp)
                        .width(100.dp)
                        .padding(top = 8.dp)
                    ) {
                        Image(painterResource(id = R.drawable.ic_launcher_background),
                            contentDescription = "Restaurant Backdround Icon",
                            modifier = Modifier
                                .matchParentSize()
                        )
                        Image(
                            painter = imageState?.let {
                                rememberAsyncImagePainter(
                                    model = ImageRequest.Builder(LocalContext.current)
                                        .data(it)
                                        .crossfade(true)
                                        .fallback(R.drawable.ic_restaurant)
                                        .build(),
                                    error = painterResource(id = R.drawable.ic_restaurant),
                                    fallback = painterResource(id = R.drawable.ic_restaurant),
                                    contentScale = ContentScale.FillBounds
                                )
                            } ?: painterResource(id = R.drawable.ic_restaurant),
                            contentDescription = "Restaurant Icon",
                            modifier = Modifier
                                .matchParentSize(),
                            alignment = Alignment.Center
                        )

                    }

                    Column(modifier = Modifier
                        .weight(1f)
                        .padding(bottom = 8.dp, start = 24.dp)
                    ) {
                        Text(text = restaurant.name ?: "NA",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(2.dp),
                            color = colorResource(id = R.color.white)
                        )
                        Spacer(modifier = Modifier.padding(4.dp))
                        Text(
                            text = restaurant.status?.let { "Status : $it" } ?: "Status : NA",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.ExtraBold,
                            modifier = Modifier.padding(2.dp),
                            color = colorResource(id = R.color.white)
                        )
                        Divider(thickness = 0.5.dp,
                            color = Color.White,
                            modifier = Modifier.padding(top = 2.dp, bottom = 2.dp)
                        )
                        Text(
                            text = restaurant.address?.let { "Address : $it" } ?: "Address : NA",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Normal,
                            modifier = Modifier.padding(2.dp),
                            color = colorResource(id = R.color.white)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun handleError(restaurantScreenState: RestaurantScreenState,
                        onDismiss: () -> Unit) {
    restaurantScreenState.error?.let {
        val errorMessage = if (it is AppErrors.NoLocationFound) {
            stringResource(id = R.string.location_not_found)
        } else if (it is AppErrors.InvalidApiKey) {
            stringResource(id = R.string.api_key_invalid)
        } else {
            stringResource(id = R.string.unknown_error)
        }
        AppAlertDialog(
            onDismissRequest = {
                onDismiss()
            },
            onConfirmation = {
                onDismiss()
            },
            dialogText = errorMessage,
            icon = Icons.Default.Info
        )
    }
}