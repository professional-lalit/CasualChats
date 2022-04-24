package com.casualchats.app.otp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.casualchats.app.ui.widgets.CommonViews
import com.casualchats.app.ui.widgets.CommonViews.ButtonWithLoader


@Preview(showBackground = true)
@Composable
fun Visualize() {
    val otp = remember { mutableStateOf("") }
    val isLoadingState = remember { mutableStateOf(false) }

    OtpUI(otp = otp, isLoading = isLoadingState, onSubmitted = { /*TODO*/ }) {

    }
}

@Composable
fun OtpUI(
    otp: MutableState<String>,
    isLoading: MutableState<Boolean>,
    onSubmitted: (String) -> Unit,
    onBackClicked: () -> Unit
) {


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = CommonViews.commonGradientBrush())
    ) {

        CommonViews.AppToolbar(
            title = "OTP Validation",
            modifier = Modifier.align(Alignment.TopCenter),
            onBackClicked = onBackClicked
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .align(Alignment.Center)
                .padding(20.dp)
        ) {

            CommonViews.Title()
            Spacer(modifier = Modifier.padding(30.dp))
            CommonViews.AppTextField(
                text = otp.value,
                hint = "Enter one time password",
                keyBoardType = KeyboardType.Number
            ) {
                otp.value = it
            }
            Spacer(modifier = Modifier.padding(30.dp))
            Divider(thickness = 1.dp)
            Spacer(modifier = Modifier.padding(30.dp))

            ButtonWithLoader(buttonLabel = "Submit", isLoading = isLoading.value) {
                onSubmitted.invoke(otp.value)
            }
        }
    }
}

