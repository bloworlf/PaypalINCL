package io.drdroid.paypalincl.ui.pages.profile

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import io.drdroid.paypalincl.R
import io.drdroid.paypalincl.data.vm.UserViewModel
import io.drdroid.paypalincl.ui.components.ProfileImage

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    userViewModel: UserViewModel = hiltViewModel()
) {
    val user by remember { mutableStateOf(userViewModel.currentUser) }

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
        ) {
            ProfileImage(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(128.dp),
                imgUrl = user?.photoUrl.toString(),
                updatable = true,
                onClicked = {},
                onImageSelected = {
//                    user.photoUtils = Utils.uriToBase64(it)
                    displayUpdateSnack = true
                }
            )
        }

        if (displayUpdateSnack) {
            Box(
                modifier = Modifier.align(Alignment.BottomCenter),
            ) {
                SnackBarComponent(
                    message = stringResource(id = R.string.apply_profile_update),
                    actionLabel = stringResource(id = R.string.update),
                    duration = SnackbarDuration.Indefinite,
                    onDismiss = {
                        if (it == SnackbarResult.ActionPerformed) {
//                            userViewModel.updateProfile(user)
                            displayUpdateSnack = false
                        } else {
                            //nothing
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun SnackBarComponent(
    message: String,
    actionLabel: String,
    duration: SnackbarDuration = SnackbarDuration.Short,
//    modifier: Modifier = Modifier,
    onDismiss: (SnackbarResult) -> Unit
) {
    val snackBarHostState = remember { SnackbarHostState() }
    var snackBarResult by remember { mutableStateOf<SnackbarResult?>(null) }

    LaunchedEffect(snackBarHostState) {
        snackBarResult = snackBarHostState.showSnackbar(message, actionLabel, duration = duration)
    }

    LaunchedEffect(snackBarResult) {
        snackBarResult?.let {
            onDismiss(it)
        }
    }

    SnackbarHost(
        hostState = snackBarHostState,
    )
}