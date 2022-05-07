package com.casualchats.app.home.pager.tabs

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.graphics.vector.ImageVector
import com.casualchats.app.home.models.ProfileData
import com.casualchats.app.models.DownloadItem
import com.casualchats.app.models.MessageHeader

sealed class TabItem(
    val index: Int,
    val icon: ImageVector,
    val title: String,
    val screenToLoad: @Composable () -> Unit
) {
    class Downloads(downloads: MutableState<List<DownloadItem>>, isLoading: MutableState<Boolean>) :
        TabItem(0, Icons.Default.CheckCircle, "Downloads", {
            DownloadScreenForTab(downloads, isLoading)
        })

    class Chats(
        messageHeaders: MutableState<List<MessageHeader>>,
        isLoading: MutableState<Boolean>,
        onChatHeaderClicked: (MessageHeader) -> Unit
    ) : TabItem(1, Icons.Default.Email, "Messages", {
        ChatScreenForTab(messageHeaders, isLoading, onChatHeaderClicked)
    })

    class Profile(
        profileData: ProfileData,
        onPhotoClicked: () -> Unit,
        onUpdateClicked: () -> Unit,
        onLogoutClicked: () -> Unit,
    ) : TabItem(2, Icons.Default.Face, "Profile", {
        ProfileScreenForTab(profileData, onPhotoClicked, onUpdateClicked, onLogoutClicked)
    })
}