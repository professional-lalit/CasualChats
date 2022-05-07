package com.casualchats.app.home

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.casualchats.app.common.Screen
import com.casualchats.app.common.Utils
import com.casualchats.app.common.Utils.showToast
import com.casualchats.app.home.vm.DownloadsVM
import com.casualchats.app.home.vm.MessagesVM
import com.casualchats.app.home.vm.ProfileVM
import com.casualchats.app.ui.theme.CasualChatsTheme
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.accompanist.pager.ExperimentalPagerApi
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : ComponentActivity() {

    private val profileVM: ProfileVM by viewModels()
    private val messagesVM: MessagesVM by viewModels()
    private val downloadsVM: DownloadsVM by viewModels()

    @OptIn(ExperimentalPagerApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            CasualChatsTheme {
                HomeUI(
                    downloadsVM.downloads,
                    downloadsVM.isLoading,
                    messagesVM.messageHeaders,
                    messagesVM.isLoading,
                    profileVM.profileData,
                    onPhotoClicked = {
                        getUserImage()
                    },
                    onUpdateUserClicked = {
                        updateUserDetails()
                    },
                    onLogoutClicked = {
                        logout()
                    },
                    onMessageHeaderClicked = {
                        goToMessages()
                    },
                    onSearchUsersClicked = {
                        goToSearchUsers()
                    }
                )
            }
        }

        profileVM.isLoggedOut.observe(this) {
            if (it) {
                finish()
            }
        }

        loadUserDetails()
        loadMessages()
    }

    private fun goToSearchUsers() {
        Screen.SearchUsers().open(this)
    }

    private fun goToMessages() {
        Screen.Messages().open(this)
    }

    private val startForProfileImageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data

            when (resultCode) {
                Activity.RESULT_OK -> {
                    //Image Uri will not be null for RESULT_OK
                    val fileUri = data?.data!!
                    profileVM.profileData.userImageUri.value =
                        Utils.fileFromContentUri(this, fileUri).absolutePath
                }
                ImagePicker.RESULT_ERROR -> {
                    showToast(ImagePicker.getError(data))
                }
                else -> {
                    showToast("Task Cancelled")
                }
            }
        }

    private fun getUserImage() {
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

    private fun logout() {
        profileVM.logout()
    }

    private fun loadUserDetails() {
        profileVM.fetchUser()
    }

    private fun updateUserDetails() {
        profileVM.updateUser()
    }

    private fun loadMessages() {
        messagesVM.loadMessageHeaders()
    }

    private fun loadDownloads() {
        downloadsVM.loadDownloads()
    }
}
