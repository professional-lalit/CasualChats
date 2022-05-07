package com.casualchats.app.search_users

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.casualchats.app.R
import com.casualchats.app.models.User
import com.casualchats.app.ui.widgets.CommonViews

@Preview(showBackground = true)
@Composable
fun RenderListUI() {
    SearchUsersUI(
        loading = remember {
            mutableStateOf(false)
        },
        list = remember {
            mutableStateOf(
                listOf(
                    User(firstName = "Lalit", lastName = "Hajare"),
                    User(firstName = "Avishkar", lastName = "Harne"),
                    User(firstName = "Yashnil", lastName = "Sawant"),
                    User(firstName = "Nilam", lastName = "Sawant"),
                    User(firstName = "Poonam", lastName = "Hajare")
                )
            )
        },
        {},
        {}
    )
}

@Composable
fun SearchUsersUI(
    loading: MutableState<Boolean>,
    list: MutableState<List<User>>,
    onUserItemClicked: (User) -> Unit,
    onBackClicked: () -> Unit
) {

    Column {
        CommonViews.AppToolbar(
            title = "Search Users",
            onBackClicked = onBackClicked
        )
        LazyColumn {
            items(list.value.size) {
                SearchItemUI(list.value[it], onUserItemClicked)
            }
        }
    }

}

@Composable
fun SearchItemUI(user: User, onUserItemClicked: (User) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .clickable(enabled = true, onClick = { onUserItemClicked.invoke(user) })
    ) {

        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(user.imageUrl)
                .crossfade(true)
                .build(),
            placeholder = painterResource(R.drawable.ic_profile_placeholder),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .size(30.dp)
                .clip(CircleShape)
        )

        Text(
            text = user.firstName + " " + user.lastName,
            modifier = Modifier
                .padding(start = 10.dp)
                .align(Alignment.CenterVertically),
            fontSize = 15.sp,
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.Bold
        )
    }
}

