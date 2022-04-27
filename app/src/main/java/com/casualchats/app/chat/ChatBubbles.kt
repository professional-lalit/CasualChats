package com.casualchats.app.chat

import androidx.compose.foundation.background
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.casualchats.app.models.MessageDetail


@Preview(showBackground = true)
@Composable
fun RenderChatItems() {
    Column {
        SenderItem(
            MessageDetail(
                "Sylvester", "lalit",
                "Hello there!", null, 7825638123
            )
        )
        ReceiverItem(
            MessageDetail(
                "Lalit", "Sylvester",
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
            Column(
                modifier = Modifier
                    .background(color = Color.Yellow)
                    .width(IntrinsicSize.Max)
                    .padding(10.dp)
            ) {
                Text(
                    text = message.from,
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
            Column(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .background(color = Color.LightGray)
                    .width(IntrinsicSize.Max)
                    .padding(10.dp)
            ) {
                Text(
                    text = message.from,
                    modifier = Modifier.padding(start = 10.dp),
                    fontSize = 10.sp,
                    fontFamily = FontFamily.Default,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = message.message,
                    modifier = Modifier.padding(start = 10.dp),
                    fontSize = 15.sp
                )
            }
        }
    }
}