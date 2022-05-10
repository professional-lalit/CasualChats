package com.casualchats.app.chat

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.casualchats.app.common.Utils
import com.casualchats.app.common.Utils.closeKeyboard
import com.casualchats.app.common.Utils.showToast
import com.casualchats.app.home.vm.MessagesVM
import com.casualchats.app.models.Attachment
import com.casualchats.app.ui.theme.CasualChatsTheme
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChatDetailsActivity : ComponentActivity() {

    private val messagesVM: MessagesVM by viewModels()

    private val startForProfileImageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data

            when (resultCode) {
                Activity.RESULT_OK -> {
                    //Image Uri will not be null for RESULT_OK
                    val fileUri = data?.data!!
                    val file = Utils.fileFromContentUri(this, fileUri)
                    messagesVM.attachment.value = Attachment(
                        name = file.name,
                        path = file.path,
                        mimeType = file.extension
                    )
                }
                ImagePicker.RESULT_ERROR -> {
                    showToast(ImagePicker.getError(data))
                }
                else -> {
                    showToast("Task Cancelled")
                }
            }
        }

    private val headerId by lazy {
        intent.getBundleExtra("bundle")?.getString("headerId") ?: ""
    }
    private val otherUserId by lazy {
        intent.getBundleExtra("bundle")?.getString("otherUserId") ?: ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CasualChatsTheme {
                MessageList(
                    currentUser = Firebase.auth.currentUser!!,
                    messages = messagesVM.messages,
                    attachment = messagesVM.attachment,
                    isLoading = messagesVM.isLoading,
                    isFileUploading = messagesVM.isFileUploading,
                    onBackClicked = { finish() },
                    onAttachClicked = { getAttachment() },
                    onSend = { msg: String ->
                        messagesVM.sendMessage(
                            msg,
                            otherUserId,
                            headerId
                        )
                        closeKeyboard()
                    }
                )
            }
            messagesVM.loadMessages(headerId)
        }
    }

    private fun getAttachment() {
        ImagePicker.with(this)
            .compress(1024)
            .maxResultSize(
                1080,
                1080
            )
            .createIntent { intent ->
                startForProfileImageResult.launch(intent)
            }
    }
}
