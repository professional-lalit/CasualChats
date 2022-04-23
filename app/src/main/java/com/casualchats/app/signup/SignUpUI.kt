package com.casualchats.app.signup

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.casualchats.app.R
import com.casualchats.app.ui.theme.PurpleDark
import com.casualchats.app.ui.widgets.CommonViews
import com.casualchats.app.ui.widgets.CommonViews.ButtonWithLoader


@Preview(showBackground = true)
@Composable
fun Visualize() {

    val firstName = remember { mutableStateOf("") }
    val lastName = remember { mutableStateOf("") }
    val phoneNumber = remember { mutableStateOf("") }
    val userImageUrl = remember { mutableStateOf("") }
    val isLoadingState = remember { mutableStateOf(false) }

    SignUpUI(
        firstName = firstName,
        lastName = lastName,
        phoneNumber = phoneNumber,
        userImageUrl = userImageUrl,
        isLoading = isLoadingState,
        onLoginNoteClicked = { /*TODO*/ }, {}, {})
}

@Composable
fun SignUpUI(
    firstName: MutableState<String>,
    lastName: MutableState<String>,
    phoneNumber: MutableState<String>,
    userImageUrl: MutableState<String>,
    isLoading: MutableState<Boolean>,
    onLoginNoteClicked: () -> Unit,
    onSignUpClicked: () -> Unit,
    onPhotoClicked: () -> Unit
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

            CommonViews.AppTitle()
            Spacer(modifier = Modifier.padding(30.dp))

            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(userImageUrl.value)
                    .crossfade(true)
                    .build(),
                placeholder = painterResource(R.drawable.ic_profile_placeholder),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(80.dp)
                    .border(2.dp, Color.Gray, CircleShape)
                    .clip(CircleShape)
                    .clickable(enabled = true, onClick = onPhotoClicked)
            )
            Spacer(modifier = Modifier.padding(20.dp))
            CommonViews.AppTextField(
                text = firstName.value,
                hint = "Enter your first name",
                keyBoardType = KeyboardType.Text
            ) {
                firstName.value = it
            }
            Spacer(modifier = Modifier.padding(10.dp))
            CommonViews.AppTextField(
                text = lastName.value,
                hint = "Enter your last name",
                keyBoardType = KeyboardType.Text
            ) {
                lastName.value = it
            }
            Spacer(modifier = Modifier.padding(10.dp))
            CommonViews.AppTextField(
                text = phoneNumber.value,
                hint = "Enter your mobile number",
                keyBoardType = KeyboardType.Text
            ) {
                phoneNumber.value = it
            }
            Spacer(modifier = Modifier.padding(30.dp))
            Divider(thickness = 1.dp)
            Spacer(modifier = Modifier.padding(10.dp))
            ButtonWithLoader(buttonLabel = "Sign up", isLoading = isLoading.value) {
                onSignUpClicked.invoke()
            }
        }
        LoginNote(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(20.dp),
            onLoginNoteClicked
        )
    }
}

@Composable
fun LoginNote(modifier: Modifier, onLoginNoteClicked: () -> Unit) {
    Text(
        text = "Already have an account? Login",
        modifier = modifier.clickable(enabled = true, onClick = onLoginNoteClicked),
        fontFamily = FontFamily.Monospace, fontSize = 15.sp,
        textDecoration = TextDecoration.Underline,
        color = Color.White,
        fontWeight = FontWeight.Bold
    )
}
