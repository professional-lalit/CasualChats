package com.casualchats.app.home.pager.tabs

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.casualchats.app.R
import com.casualchats.app.home.models.ProfileData
import com.casualchats.app.ui.widgets.CommonViews


@Preview(showBackground = true)
@Composable
fun Visualize() {

    val profileData = ProfileData(
        remember {
            mutableStateOf("")
        },
        remember {
            mutableStateOf("")
        },
        remember {
            mutableStateOf("")
        },
        remember {
            mutableStateOf("")
        },
        remember {
            mutableStateOf(false)
        }
    )

    ProfileScreenForTab(
        profileData,
        onUpdateClicked = {},
        onPhotoClicked = {},
        onLogoutClicked = {}
    )
}

@Composable
fun ProfileScreenForTab(
    profileData: ProfileData,
    onPhotoClicked: () -> Unit,
    onUpdateClicked: () -> Unit,
    onLogoutClicked: () -> Unit,
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
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

                CommonViews.Title("Your Profile")
                Spacer(modifier = Modifier.padding(30.dp))

                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(profileData.userImageUri.value)
                        .crossfade(true)
                        .build(),
                    placeholder = painterResource(R.drawable.ic_profile_placeholder),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(80.dp)
                        .border(2.dp, Color.Gray, CircleShape)
                        .clip(CircleShape)
                        .clickable(enabled = true, onClick = {
                            profileData.isImageUpdated.value = true
                            onPhotoClicked.invoke()
                        })
                )
                Spacer(modifier = Modifier.padding(20.dp))
                CommonViews.AppTextField(
                    text = profileData.firstName.value,
                    hint = "Enter your first name",
                    keyBoardType = KeyboardType.Text
                ) {
                    profileData.isDataUpdated.value = true
                    profileData.firstName.value = it
                }
                Spacer(modifier = Modifier.padding(10.dp))
                CommonViews.AppTextField(
                    text = profileData.lastName.value,
                    hint = "Enter your last name",
                    keyBoardType = KeyboardType.Text
                ) {
                    profileData.isDataUpdated.value = true
                    profileData.lastName.value = it
                }
                Spacer(modifier = Modifier.padding(10.dp))
                CommonViews.AppTextField(
                    text = profileData.phoneNumber.value,
                    hint = "Enter your mobile number",
                    keyBoardType = KeyboardType.Text
                ) {
                    profileData.isDataUpdated.value = true
                    profileData.phoneNumber.value = it
                }
                Spacer(modifier = Modifier.padding(30.dp))
                Divider(thickness = 1.dp)
                Spacer(modifier = Modifier.padding(10.dp))

                if (profileData.isDataUpdated.value || profileData.isImageUpdated.value) {
                    CommonViews.ButtonWithLoader(
                        buttonLabel = "Update",
                        isLoading = profileData.isLoading.value
                    ) {
                        onUpdateClicked.invoke()
                    }
                }
            }

            Icon(
                painter = painterResource(id = R.drawable.ic_logout_2),
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(30.dp)
                    .clickable(enabled = true, onClick = onLogoutClicked),
                contentDescription = ""
            )
        }

    }
}