package com.casualchats.app.chat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.casualchats.app.common.Utils.closeKeyboard
import com.casualchats.app.home.vm.MessagesVM
import com.casualchats.app.ui.theme.CasualChatsTheme
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class ChatDetailsActivity : ComponentActivity() {

    private val messagesVM: MessagesVM by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CasualChatsTheme {
                MessageList(
                    currentUser = Firebase.auth.currentUser!!,
                    messages = messagesVM.messages,
                    isLoading = messagesVM.isLoading,
                    onBackClicked = { finish() },
                    onSend = { msg: String, otherUserId: String ->
                        closeKeyboard()
                        messagesVM.sendMessage(
                            msg,
                            otherUserId
                        )
                    }
                )
            }
            messagesVM.loadMessages()
        }
    }
}
