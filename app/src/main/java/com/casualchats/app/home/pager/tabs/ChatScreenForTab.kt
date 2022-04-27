package com.casualchats.app.home.pager.tabs

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.casualchats.app.models.MessageHeader

@Preview(showBackground = true)
@Composable
fun Render() {

    val dummyList = listOf(
        MessageHeader("ABCDEF", listOf("ABC", "DEF"), "Fitness Club", "This dumb-bells are cool!"),
        MessageHeader(
            "ABCDEF",
            listOf("ABC", "DEF"),
            "Pets",
            "Doberman are not cute",
            unReadMsgCount = 100
        ),
        MessageHeader("ABCDEF", listOf("ABC", "DEF"), "Cars", "I bought a Porshe"),
    )
    val messageHeaders = remember { mutableStateOf(dummyList) }
    ChatScreenForTab(
        messageHeaders = messageHeaders,
        isLoading = remember { mutableStateOf(false) },
        onChatHeaderClicked = {}
    )
}

@Composable
fun ChatScreenForTab(
    messageHeaders: MutableState<List<MessageHeader>>,
    isLoading: MutableState<Boolean>,
    onChatHeaderClicked: (String) -> Unit
) {

    Box(modifier = Modifier.fillMaxSize()) {

        LazyColumn {
            items(messageHeaders.value.size) {
                ChatHeaderItem(messageHeader = messageHeaders.value[it], onChatHeaderClicked)
            }
        }

        if (isLoading.value) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
    }
}

@Composable
fun ChatHeaderItem(messageHeader: MessageHeader, onChatHeaderClicked: (String) -> Unit) {

    val msgFontWt = if (messageHeader.isRead!!) {
        FontWeight.Normal
    } else {
        FontWeight.Bold
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                enabled = true,
                onClick = { onChatHeaderClicked.invoke(messageHeader.headerId) })
            .padding(top = 10.dp, bottom = 10.dp)
    ) {
        Column {
            Text(
                text = messageHeader.groupName!!,
                modifier = Modifier.padding(start = 20.dp),
                fontSize = 18.sp,
                fontFamily = FontFamily.Serif
            )
            Text(
                text = messageHeader.latestMessage,
                modifier = Modifier.padding(start = 20.dp),
                fontSize = 15.sp,
                fontWeight = msgFontWt
            )
            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
                color = Color.LightGray
            )
        }

        if (messageHeader.unReadMsgCount ?: 0 > 0) {
            val count = if (messageHeader.unReadMsgCount ?: 0 > 9) {
                "9+"
            } else {
                messageHeader.unReadMsgCount?.toString() ?: ""
            }
            Text(
                text = count,
                color = Color.White,
                fontSize = 13.sp,
                lineHeight = 10.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 30.dp)
                    .size(20.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colors.primary)
            )
        }
    }
}