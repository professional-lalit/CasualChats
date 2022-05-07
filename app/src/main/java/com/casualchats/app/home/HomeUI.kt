package com.casualchats.app.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import com.casualchats.app.home.models.ProfileData
import com.casualchats.app.home.pager.tabs.TabItem
import com.casualchats.app.models.DownloadItem
import com.casualchats.app.models.MessageHeader
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch

@ExperimentalPagerApi
@Preview(showBackground = true)
@Composable
fun Visualize() {

    val profileData = ProfileData(
        remember {
            mutableStateOf("")
        },
        remember {
            mutableStateOf("")
        },
        remember {
            mutableStateOf("")
        },
        remember {
            mutableStateOf("")
        },
        remember {
            mutableStateOf(false)
        }
    )

    val downloads = remember { mutableStateOf<List<DownloadItem>>(listOf()) }
    val messageHeaders = remember { mutableStateOf<List<MessageHeader>>(listOf()) }
    val isLoading = remember { mutableStateOf(false) }

    HomeUI(downloads, isLoading, messageHeaders, isLoading, profileData, {}, {}, {}, {}, {})
}

private var tabs: List<TabItem>? = null

@ExperimentalPagerApi
@Composable
fun HomeUI(
    downloads: MutableState<List<DownloadItem>>,
    downloadsLoading: MutableState<Boolean>,
    messageHeaders: MutableState<List<MessageHeader>>,
    messagesLoading: MutableState<Boolean>,
    profileData: ProfileData,
    onPhotoClicked: () -> Unit,
    onUpdateUserClicked: () -> Unit,
    onLogoutClicked: () -> Unit,
    onMessageHeaderClicked: (MessageHeader) -> Unit,
    onSearchUsersClicked: () -> Unit
) {

    val pagerState = rememberPagerState()

    tabs = listOf(
        TabItem.Downloads(downloads, downloadsLoading),
        TabItem.Chats(messageHeaders, messagesLoading, onMessageHeaderClicked),
        TabItem.Profile(profileData, onPhotoClicked, onUpdateUserClicked, onLogoutClicked)
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        content = {
            TabPage(tabItems = tabs!!, pagerState = pagerState)
        },
        topBar = {
            val coroutineScope = rememberCoroutineScope()
            Column(content = {
                TopAppBar(title = { Text("Casual Chats", fontFamily = FontFamily.Cursive) }
                )
                //Replace here with TextTabLayout or ScrollableTabLayout or IconTabLayout
                IconWithTextTabLayout(
                    tabs!!,
                    selectedIndex = pagerState.currentPage,
                    onPageSelected = { tabItem: TabItem ->
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(tabItem.index)
                        }
                    })
            })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onSearchUsersClicked) {
                Icon(
                    Icons.Filled.Add, "",
                    tint = Color.White
                )
            }
        }
    )
}

@ExperimentalPagerApi
@Composable
fun IconWithTextTabLayout(
    tabs: List<TabItem>,
    selectedIndex: Int,
    onPageSelected: ((tabItem: TabItem) -> Unit)
) {
    TabRow(selectedTabIndex = selectedIndex) {
        tabs.forEachIndexed { index, tabItem ->
            Tab(selected = index == selectedIndex, onClick = {
                onPageSelected(tabItem)
            }, text = {
                Text(text = tabItem.title)
            }, icon = {
                Icon(tabItem.icon, "")
            })
        }
    }
}


@ExperimentalPagerApi
@Composable
fun TabPage(pagerState: PagerState, tabItems: List<TabItem>) {
    HorizontalPager(
        count = tabs!!.size,
        state = pagerState
    ) { index ->
        tabItems[index].screenToLoad()
    }
}

@Composable
fun TextTabLayout(
    tabs: List<TabItem>,
    selectedIndex: Int,
    onPageSelected: ((tabItem: TabItem) -> Unit)
) {
    TabRow(selectedTabIndex = selectedIndex) {
        tabs.forEachIndexed { index, tabItem ->
            Tab(selected = index == selectedIndex, onClick = {
                onPageSelected(tabItem)
            }, text = {
                Text(text = tabItem.title)
            })
        }
    }
}