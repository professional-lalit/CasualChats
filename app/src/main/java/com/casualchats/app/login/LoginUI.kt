package com.casualchats.app.login

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.casualchats.app.ui.widgets.CommonViews
import com.casualchats.app.ui.widgets.CommonViews.AppTextField
import com.casualchats.app.ui.widgets.CommonViews.Title
import com.casualchats.app.ui.widgets.CommonViews.ButtonWithLoader

@Preview(showBackground = true)
@Composable
fun Visualize() {

    val isLoading = remember { mutableStateOf(false) }
    val phoneNumber = remember { mutableStateOf("") }

    LoginUI(
        phoneNumber = phoneNumber,
        onSignUpNoteClicked = { /*TODO*/ },
        isLoadingState = isLoading,
        onPhoneNumberChanged = {}) {

    }
}

@Composable
fun LoginUI(
    phoneNumber: MutableState<String>,
    isLoadingState: MutableState<Boolean>,
    onSignUpNoteClicked: () -> Unit,
    onPhoneNumberChanged: (String) -> Unit,
    onLoginClicked: () -> Unit
) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = CommonViews.commonGradientBrush())
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .align(Alignment.Center)
                .padding(20.dp)
        ) {

            Title()

            Spacer(modifier = Modifier.padding(30.dp))

            AppTextField(
                text = phoneNumber.value,
                hint = "Enter mobile number",
                keyBoardType = KeyboardType.Number,
                onPhoneNumberChanged
            )

            Spacer(modifier = Modifier.padding(30.dp))

            Divider(thickness = 1.dp)

            Spacer(modifier = Modifier.padding(30.dp))

            ButtonWithLoader(buttonLabel = "Login", isLoading = isLoadingState.value) {
                onLoginClicked.invoke()
            }
        }

        SignUpNote(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(20.dp),
            onSignUpNoteClicked
        )
    }
}

@Composable
fun SignUpNote(modifier: Modifier, onSignUpNoteClicked: () -> Unit) {
    Text(
        text = "Don't have an account? Sign up",
        modifier = modifier.clickable(enabled = true, onClick = onSignUpNoteClicked),
        fontFamily = FontFamily.Monospace, fontSize = 15.sp,
        textDecoration = TextDecoration.Underline,
        color = Color.White,
        fontWeight = FontWeight.Bold
    )
}