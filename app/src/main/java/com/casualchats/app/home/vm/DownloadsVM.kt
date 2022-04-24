package com.casualchats.app.home.vm

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.casualchats.app.models.DownloadItem
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*

class DownloadsVM : ViewModel() {

    val downloads = mutableStateOf<List<DownloadItem>>(listOf())
    val isLoading = mutableStateOf(false)

    val dummyList = listOf(
        DownloadItem("friendship day", Date().time, ""),
        DownloadItem("teachers day", Date().time, ""),
        DownloadItem("father's day", Date().time, ""),
        DownloadItem("mother's day", Date().time, ""),
        DownloadItem("dog's day", Date().time, "")
    )

    fun loadDownloads() {
        isLoading.value = true
        viewModelScope.launch {
            delay(2000L)
            downloads.value = dummyList
            isLoading.value = false
        }
    }
}