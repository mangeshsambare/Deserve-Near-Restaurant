package com.deserve.nearrestaurant.ui.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.deserve.nearrestaurant.R
import com.deserve.nearrestaurant.RestaurantViewModel
import com.deserve.nearrestaurant.log_in.LogInScreenState
import com.deserve.nearrestaurant.ui.common.Loader
import com.deserve.nearrestaurant.ui.common.PasswordTextField

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogInScreen(viewModel: RestaurantViewModel,
                logIn: (String) -> Unit) {
    Scaffold(topBar = {
        TopAppBar(title = { Text(text = stringResource(id = R.string.log_in)) },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = colorResource(id = R.color.purple_200))
        )
    }, content = {
        LogInView(viewModel, logIn)
    })
}

@Composable
private fun LogInView(viewModel: RestaurantViewModel,
    logIn: (String) -> Unit
) {
    val logInScreenState by viewModel.logInState
    var apiKey by rememberSaveable { mutableStateOf("") }

    Column(modifier = Modifier.padding(
        start = 16.dp,
        end = 16.dp,
        bottom = 16.dp,
        top = 100.dp
    ), verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LogInLoader(
            logInScreenState = logInScreenState
        )
        PasswordTextField(
            value = apiKey,
            label = stringResource(id = R.string.api_key),
            onValueChanged = { apiKey = it },
        )
        LogInButton(apiKey = apiKey, logIn = logIn)
    }
}


@Composable
private fun LogInButton(apiKey: String,
                        logIn: (String) -> Unit
){
    val enabled = logInButtonEnabled(apiKey = apiKey)

    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(top = 16.dp, bottom = 8.dp),
        horizontalArrangement = Arrangement.Center) {
        Button(
            onClick = {
                logIn(apiKey)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(45.dp),
            enabled = enabled,
            shape = RoundedCornerShape(3.dp),
            colors = ButtonDefaults.buttonColors(
                contentColor = colorResource(id = R.color.white),
                disabledContentColor = colorResource(id = R.color.white),
            ) ){
            Text(
                text = stringResource(id = R.string.log_in).uppercase(),
                color = colorResource(id = R.color.white),
                fontSize = 15.sp,
                textAlign = TextAlign.Center,
                lineHeight = 18.sp
            )
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun LogInLoader(logInScreenState: LogInScreenState) {
    val loaderMessage = remember { mutableStateOf<String?>( null) }
    if (logInScreenState.isLoading) {
        if (loaderMessage.value == null) {
            loaderMessage.value = stringResource(id = R.string.log_in_progress)
        }
        loaderMessage.value?.let {
            Loader(
                title = it,
                show = logInScreenState.isLoading
            )
        }
    }
}
private fun logInButtonEnabled(apiKey: String?): Boolean {
    return !apiKey.isNullOrEmpty()
}