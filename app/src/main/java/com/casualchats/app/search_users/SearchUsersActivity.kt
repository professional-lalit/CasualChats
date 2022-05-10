package com.casualchats.app.search_users

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.casualchats.app.common.Prefs
import com.casualchats.app.common.Screen
import com.casualchats.app.models.User
import com.casualchats.app.search_users.ui.theme.CasualChatsTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SearchUsersActivity : ComponentActivity() {

    private val searchUsersVM: SearchUsersVM by viewModels()

    @Inject
    lateinit var prefs: Prefs

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            CasualChatsTheme {
                SearchUsersUI(
                    searchUsersVM.loading,
                    searchUsersVM.users,
                    onUserItemClicked = {
                        if (it.userId != prefs.user?.userId) {
                            searchUsersVM.createMessageHeader(it)
                        }
                    },
                    onBackClicked = { finish() }
                )
            }
        }

        searchUsersVM.fetchUsers()

        searchUsersVM.chatDetails.observe(this) {
            Screen.Messages().open(this, Bundle().apply {
                putString("headerId", it.first)
                putString("otherUserId", it.second)
            })
        }
    }
}