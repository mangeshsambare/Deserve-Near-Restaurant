package com.deserve.nearrestaurant.ui.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.deserve.nearrestaurant.R


@Composable
fun PasswordTextField(
    value: String,
    label: String = "",
    onValueChanged: (String) -> Unit,
    width: Dp? = null,
    enableValidation: Boolean = true,
    errorMessage: String = "",
    minChars: Int = 6,
    maxChars: Int = 100
) {
    var isError = rememberSaveable { mutableStateOf(false) }
    var passwordVisible by rememberSaveable { mutableStateOf(false) }

    var modifier = Modifier.height(45.dp)
    modifier = if (width == null) modifier.then(Modifier.fillMaxWidth()) else modifier.then(
        Modifier.width(width)
    )

    val error =
        errorMessage.ifEmpty { stringResource(id = R.string.api_key_invalid, minChars, maxChars) }

    TextFieldImp(
        modifier = modifier,
        value = value,
        label = label,
        onValueChanged = {
            if (it.length <= maxChars) {
                isError.value = enableValidation && it.length < minChars
                onValueChanged(it)
            }
        },
        keyboardOptions = KeyboardOptions(
            autoCorrect = false,
            keyboardType = KeyboardType.Password,
            capitalization = KeyboardCapitalization.None,
            imeAction = ImeAction.Done
        ),
        textStyle = TextStyle(
            fontSize = 15.sp,
            fontWeight = FontWeight.Normal,
            color = Color.Black,
        ),
        errorMessage = error,
        isError = isError,
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
            val painter = if (passwordVisible)
                painterResource(id = R.drawable.ic_visible)
            else painterResource(id = R.drawable.ic_visibility_off)

            // Please provide localized description for accessibility services
            val description = if (passwordVisible) "Hide password" else "Show password"
            Box(modifier = Modifier
                .then(Modifier.size(24.dp))
                .clickable { passwordVisible = !passwordVisible },
                contentAlignment = Alignment.CenterStart,
            ) {
                IconButton(modifier = Modifier.then(Modifier.size(14.dp)),
                    onClick = {passwordVisible = !passwordVisible}){
                    Image(painter = painter, description)
                }
            }

        }
    )
}
@Composable
internal fun TextFieldImp(
    modifier: Modifier,
    value: String,
    label: String,
    isError: MutableState<Boolean>? = null,
    errorMessage: String,
    onValueChanged: (String) -> Unit,
    keyboardOptions: KeyboardOptions,
    keyboardActions: KeyboardActions? = null,
    readOnly: Boolean = false,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    showTrailingIconForEmptyValue: Boolean = true,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    textStyle: TextStyle? = null
) {
    val focusManager = LocalFocusManager.current

    val labelFontSize = if (value.isNotEmpty()) 10.sp else 15.sp
    val topPadding = if (value.isNotEmpty()) 1.dp else 15.dp
    val leftPadding = if (leadingIcon != null) 35.dp else 0.dp // have to check leading icon padding here

    val greyColor = colorResource(id = R.color.grey_4)
    val color = remember { mutableStateOf(greyColor) }
    val customTextStyle = textStyle
        ?: TextStyle(
            fontSize = 15.sp,
            fontWeight = FontWeight.Normal,
            color = Color.Black,
            fontFamily = FontFamily.SansSerif
        )

    Column {
        BasicTextField(
            modifier = modifier.then(
                Modifier.drawIndicatorLine(
                    1.dp,
                    color = color
                )
            ),
            value = value,
            onValueChange = onValueChanged,
            singleLine = false,
            maxLines = 2,
            textStyle = customTextStyle,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions ?: KeyboardActions(onDone = { focusManager.clearFocus() }),
            visualTransformation = visualTransformation,
            decorationBox = { innerTextField ->
                Column() {
                    Text(
                        modifier = Modifier.padding(top = topPadding, start = leftPadding),
                        text = label,
                        color = colorResource(id = R.color.grey_4),
                        fontSize = labelFontSize
                    )
                    if (value.isNotEmpty()) {
                        Row(
                            Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                modifier = Modifier.width(IntrinsicSize.Min)
                            ) {
                                if (leadingIcon != null)
                                    leadingIcon()

                                innerTextField()
                            }
                            if (trailingIcon != null)
                                trailingIcon()
                        }
                    }

                }
                if (value.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .padding(top = 16.dp)
                            .fillMaxWidth()
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                if (leadingIcon != null)
                                    leadingIcon()

                                innerTextField()
                            }
                            if (trailingIcon != null && showTrailingIconForEmptyValue)
                                trailingIcon()
                        }
                    }
                }
            },
            enabled = !readOnly,
            readOnly = readOnly
        )
        if (isError?.value == true)
            Text(
                modifier = Modifier.padding(top = 1.dp, start = 0.dp),
                text = errorMessage,
                color = colorResource(id = R.color.color_red),
                fontSize = labelFontSize,
                fontFamily = FontFamily.SansSerif
            )
        color.value =
            if (isError?.value == true) colorResource(id = R.color.color_red) else greyColor
    }
}

fun Modifier.drawIndicatorLine(lineWidth: Dp, color: MutableState<Color>): Modifier {
    return drawBehind {
        val strokeWidth = lineWidth.value * density
        val y = size.height - strokeWidth / 2
        drawLine(
            color.value,
            Offset(0f, y),
            Offset(size.width, y),
            strokeWidth
        )
    }
}