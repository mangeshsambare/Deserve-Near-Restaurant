package com.deserve.nearrestaurant.ui.common

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.deserve.nearrestaurant.R

@ExperimentalComposeUiApi
@Composable
fun Loader(title: String?, show: Boolean) {

    if (show) {
        Dialog(

            onDismissRequest = { },
            DialogProperties(
                dismissOnBackPress = false,
                dismissOnClickOutside = false,
                usePlatformDefaultWidth = false
            )
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize(Alignment.BottomStart)
                    .padding(bottom = 70.dp)

            ) {
                LinearProgressIndicator(
                    modifier = Modifier.fillMaxWidth(),
                    color = colorResource(id = R.color.purple_700),
                    trackColor = Color.White,
                    )
                Spacer(Modifier.height(8.dp))
                title?.let {
                    Text(text = title, fontSize = 13.sp, color = Color.White)
                }
            }
        }
    }
}