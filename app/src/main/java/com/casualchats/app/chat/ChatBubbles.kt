package com.casualchats.app.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
import com.casualchats.app.models.MessageDetail
import com.casualchats.app.models.User


@Preview(showBackground = true)
@Composable
fun RenderChatItems() {
    Column {
        SenderItem(
            MessageDetail(
                User(), "lalit",
                "Hello there!", null, 7825638123
            )
        )
        ReceiverItem(
            MessageDetail(
                User(), "Sylvester",
                "What happened, everything fine?", null, 7825638154
            )
        )
    }
}

@Composable
fun SenderItem(message: MessageDetail) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color.Transparent)
            .padding(vertical = 20.dp, horizontal = 20.dp)
    ) {

        Card(
            elevation = 4.dp,
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier
                .width(IntrinsicSize.Max)
                .align(Alignment.CenterEnd)
        ) {
            Row(
                modifier = Modifier
                    .background(color = Color.Yellow)
                    .width(IntrinsicSize.Max)
                    .padding(10.dp)
            ) {


                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(message.from.imageUrl)
                        .crossfade(true)
                        .build(),
                    placeholder = painterResource(R.drawable.ic_profile_placeholder),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(20.dp)
                        .clip(CircleShape)
                )

                Column(Modifier.padding(start = 10.dp)) {
                    Text(
                        text = message.from.firstName!!,
                        modifier = Modifier.padding(end = 10.dp),
                        fontSize = 10.sp,
                        fontFamily = FontFamily.Default,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = message.message,
                        modifier = Modifier.padding(end = 10.dp),
                        fontSize = 15.sp
                    )
                }
            }

        }
    }
}

@Composable
fun ReceiverItem(message: MessageDetail) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color.Transparent)
            .padding(vertical = 20.dp, horizontal = 20.dp)
    ) {

        Card(
            elevation = 4.dp,
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier
                .align(Alignment.CenterStart)
                .width(IntrinsicSize.Max)
        ) {
            Row(
                modifier = Modifier
                    .background(color = Color.Yellow)
                    .width(IntrinsicSize.Max)
                    .padding(10.dp)
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(message.from.imageUrl)
                        .crossfade(true)
                        .build(),
                    placeholder = painterResource(R.drawable.ic_profile_placeholder),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(20.dp)
                        .clip(CircleShape)
                )
                Column(Modifier.padding(start = 10.dp)) {
                    Text(
                        text = message.from.firstName!!,
                        fontSize = 10.sp,
                        fontFamily = FontFamily.Default,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = message.message,
                        fontSize = 15.sp
                    )
                }
            }
        }
    }
}