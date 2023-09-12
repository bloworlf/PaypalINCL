package io.drdroid.paypalincl.ui.pages.profile

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import io.drdroid.paypalincl.data.vm.UserViewModel
import io.drdroid.paypalincl.ui.components.ProfileImage

@Composable
fun ProfileScreen(
    modifier: Modifier=Modifier,
    userViewModel: UserViewModel = hiltViewModel()
) {
    val user by remember{ mutableStateOf(userViewModel.currentUser)}

    val scrollState = rememberScrollState()
    var displayUpdateSnack by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(top = 8.dp, start = 8.dp, end = 8.dp)
                .scrollable(
                    state = scrollState,
                    orientation = Orientation.Vertical
                )
        ){
            ProfileImage(
                modifier = Modifier.fillMaxWidth(),
                imgUrl = user?.photoUrl.toString(),
                updatable = true,
                onClicked = {},
                onImageSelected = {
//                    user.photoUtils = Utils.uriToBase64(it)
                    displayUpdateSnack = true
                }
            )
        }
    }
}