package com.deserve.nearrestaurant.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardElevation
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.deserve.nearrestaurant.R
import com.deserve.nearrestaurant.RestaurantViewModel
import com.deserve.nearrestaurant.domain.NetworkRequest
import com.deserve.nearrestaurant.domain.Restaurant

@Composable
fun RestaurantsScreen(restaurantViewModel: RestaurantViewModel) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        LazyColumn {
            items(restaurantViewModel.state.value.restaurantList) { restaurant ->
                RestaurantItem(restaurant)
            }
        }
    }
}

@Composable
private fun RestaurantItem(restaurant: Restaurant) {
    Surface {
        Card(
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier.padding(
                start = 8.dp,
                end = 8.dp,
                top = 4.dp,
                bottom = 4.dp
            )
        ) {
            Column {
                Row(modifier = Modifier
                    .padding(
                        start = 24.dp,
                        end = 20.dp, top = 14.dp, bottom = 16.dp
                    )) {
                    Image(
                        painter = restaurant.image?.let {
                            rememberAsyncImagePainter(
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(it)
                                    .addHeader("Authorization", NetworkRequest.API_KEY)
                                    .fallback(R.drawable.ic_launcher_background)
                                    .size(Size.ORIGINAL) // Set the target size to load the image at.
                                    .build()
                            )
                        } ?: painterResource(id = R.drawable.ic_launcher_background),
                        contentDescription = "Restaurant Icon",
                        modifier = Modifier
                            .wrapContentWidth()
                            .wrapContentHeight()
                            .padding(top = 12.dp),
                        alignment = Alignment.Center
                    )
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
                        Text(
                            text = restaurant.address ?: "NA",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Normal,
                            modifier = Modifier.padding(2.dp),
                            color = colorResource(id = R.color.white)
                        )

                        Text(
                            text = restaurant.status ?: "NA",
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