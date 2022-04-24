package com.casualchats.app.ui.widgets

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.casualchats.app.ui.theme.*
import com.casualchats.app.ui.theme.Purple200


object CommonViews {


    @Composable
    fun Title(text: String? = "Casual Chats") {
        Text(
            text = text!!,
            fontFamily = FontFamily.Cursive,
            fontSize = 35.sp,
            color = Color.White
        )
    }

    @Composable
    fun AppTextField(
        text: String,
        hint: String,
        keyBoardType: KeyboardType,
        callback: (String) -> Unit
    ) {
        TextField(
            keyboardOptions = KeyboardOptions(keyboardType = keyBoardType),
            value = text,
            onValueChange = callback,
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    BorderStroke(width = 1.dp, color = Color.White),
                    shape = RoundedCornerShape(5.dp)
                ),
            placeholder = { Text(text = hint, fontSize = 15.sp, color = Color.White) },
            colors = TextFieldDefaults.textFieldColors(
                textColor = Color.White,
                disabledTextColor = Color.Transparent,
                backgroundColor = Color.Transparent,
                cursorColor = Color.White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            )
        )
    }

    @Composable
    fun AppButton(text: String, modifier: Modifier, callback: () -> Unit) {
        TextButton(
            content = { Text(text = text) },
            onClick = callback,
            modifier = modifier.fillMaxWidth(0.5f),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = PurpleDark,
                contentColor = Color.White
            )
        )
    }

    fun commonGradientBrush(): Brush {
        return Brush.verticalGradient(
            colors = listOf(
                Purple200,
                Purple700
            )
        )
    }

    @Composable
    fun AppToolbar(title: String, modifier: Modifier, onBackClicked: () -> Unit) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .background(color = MaterialTheme.colors.primary)
                .padding(10.dp),
        ) {

            IconButton(
                modifier = Modifier
                    .size(24.dp)
                    .align(Alignment.TopStart),
                onClick = onBackClicked
            ) {
                Icon(
                    Icons.Filled.ArrowBack,
                    "contentDescription",
                    tint = Color.White
                )
            }

            Text(
                text = title, color = Color.White, fontSize = 15.sp,
                modifier = Modifier.align(Alignment.Center)
            )

        }
    }

    @Composable
    fun ButtonWithLoader(buttonLabel: String, isLoading: Boolean, callback: () -> Unit) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
        ) {
            if (!isLoading) {
                AppButton(
                    text = buttonLabel,
                    modifier = Modifier.align(Alignment.Center),
                    callback
                )
            } else {
                CircularProgressIndicator(
                    modifier = Modifier
                        .height(30.dp)
                        .width(30.dp)
                        .align(Alignment.Center),
                    color = PurpleDark
                )
            }
        }
    }

}