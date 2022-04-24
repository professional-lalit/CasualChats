package com.casualchats.app.home.pager.tabs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.casualchats.app.models.DownloadItem

@Composable
fun DownloadScreenForTab(
    downloads: MutableState<List<DownloadItem>>,
    isLoading: MutableState<Boolean>
) {
    Column(
        content = {
            Text(text = "You are in Download Screen")
        }, modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    )
}
