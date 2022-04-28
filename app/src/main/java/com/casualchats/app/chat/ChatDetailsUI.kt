package com.casualchats.app.chat

import android.annotation.SuppressLint
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.casualchats.app.R
import com.casualchats.app.common.Utils
import com.casualchats.app.models.Attachment
import com.casualchats.app.models.MessageDetail
import com.casualchats.app.ui.theme.Yellow1
import com.casualchats.app.ui.widgets.CommonViews
import com.casualchats.app.ui.widgets.CommonViews.AppTextFieldGrey
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

@Preview(showBackground = true)
@Composable
fun Render() {

    val dummyList = listOf(
        MessageDetail(
            from = "Sylvester",
            to = "Lalit",
            message = "Hi, what are you doing?",
            sentAt = 1825362323
        ),
        MessageDetail(
            from = "Arnold",
            to = "Lalit",
            message = "When will you be available?",
            sentAt = 1825362323
        ),
        MessageDetail(
            from = "Tom",
            to = "Lalit",
            message = "Let's meet at 8:15 PM tomorrow",
            sentAt = 1825362323
        )
    )
    val messages = remember { mutableStateOf(dummyList) }
    val attachment = remember { mutableStateOf(Attachment("", "", "")) }

//    MessageList(
//        Firebase.auth.currentUser!!,
//        messages = messages,
//        attachment = null,
//        isLoading = remember { mutableStateOf(false) },
//        {},
//        {},
//        { s: String, s1: String -> }
//    )
}

@SuppressLint("RestrictedApi")
@Composable
fun MessageList(
    currentUser: FirebaseUser,
    messages: MutableState<List<MessageDetail>>,
    attachment: MutableState<Attachment?>,
    isLoading: MutableState<Boolean>,
    isFileUploading: MutableState<Boolean>,
    onBackClicked: () -> Unit,
    onAttachClicked: () -> Unit,
    onSend: (String, String) -> Unit
) {

    val messageTyped = remember { mutableStateOf("") }
    var otherUserId = ""

    Box(modifier = Modifier.fillMaxSize()) {

        Column(modifier = Modifier.align(Alignment.TopCenter)) {
            CommonViews.AppToolbar(
                title = "Messages",
                onBackClicked = onBackClicked
            )
            LazyColumn {
                items(messages.value.size) {
                    if (currentUser.uid == messages.value[it].from) {
                        SenderItem(message = messages.value[it])
                    } else {
                        if (otherUserId.isEmpty()) {
                            otherUserId = messages.value[it].from
                        }
                        ReceiverItem(message = messages.value[it])
                    }
                }
            }
        }

        if (isLoading.value) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }

        Column(
            Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(10.dp)
                .background(color = Yellow1)
        ) {

            if (attachment.value != null) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                        .height(50.dp)
                ) {
                    Image(
                        modifier = Modifier
                            .width(50.dp)
                            .height(50.dp)
                            .align(Alignment.CenterStart)
                            .padding(start = 5.dp, end = 5.dp),
                        painter = painterResource(
                            Utils.getIconBy(
                                attachment.value?.mimeType ?: ""
                            )
                        ),
                        contentDescription = null,
                    )
                    Text(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .fillMaxWidth(0.6f),
                        text = attachment.value?.name ?: "Test Resource"
                    )
                    if (isFileUploading.value) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .align(Alignment.CenterEnd)
                                .height(20.dp)
                                .width(20.dp)
                        )
                    }
                }
            }

            Box(
                Modifier
                    .fillMaxWidth()
                    .background(color = Color.White)
                    .border(BorderStroke(width = 1.dp, color = Color.Black))
                    .padding(5.dp)
            ) {
                AppTextFieldGrey(
                    text = messageTyped.value,
                    hint = "Enter your message here",
                    keyBoardType = KeyboardType.Text,
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .align(Alignment.CenterStart)
                ) {
                    messageTyped.value = it
                }

                Row(Modifier.align(Alignment.CenterEnd)) {
                    Image(
                        modifier = Modifier
                            .fillMaxWidth(0.1f)
                            .padding(start = 5.dp, end = 5.dp)
                            .clickable(
                                enabled = true,
                                onClick = onAttachClicked
                            ),
                        painter = painterResource(R.drawable.ic_attach),
                        contentDescription = null,
                    )
                    Image(
                        modifier = Modifier
                            .fillMaxWidth(0.1f)
                            .clickable(
                                enabled = true,
                                onClick = {
                                    onSend.invoke(messageTyped.value, otherUserId)
                                    messageTyped.value = ""
                                }),
                        painter = painterResource(R.drawable.ic_send),
                        contentDescription = null,
                    )
                }

            }
        }

    }
}
