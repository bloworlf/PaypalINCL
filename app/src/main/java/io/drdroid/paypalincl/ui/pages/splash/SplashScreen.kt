package io.drdroid.paypalincl.ui.pages.splash

import android.os.Handler
import android.os.Looper
import android.view.ViewGroup
import android.widget.ImageView
import android.window.SplashScreen
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import io.drdroid.paypalincl.R
import io.drdroid.paypalincl.data.vm.UserViewModel
import io.drdroid.paypalincl.ui.navigation.AppNavigationActions
import io.drdroid.paypalincl.ui.navigation.Screen
import kotlinx.coroutines.delay
import java.util.Random

@Composable
fun SplashScreen(
    modifier: Modifier = Modifier,
    navActions: AppNavigationActions,
//    onStart: () -> Unit,
//    onSplashEndedValid: () -> Unit,
//    onSplashEndedInvalid: () -> Unit,
    userViewModel: UserViewModel = hiltViewModel()
) {
//    val lifecycleOwner = LocalLifecycleOwner.current
//    val user by userViewModel.userLiveData.observeAsState()
    val user = userViewModel.currentUser
//    var timeoutTrigger by remember { mutableStateOf(user != null) }
//    var displayLoading by remember { mutableStateOf(false) }
//
//    LaunchedEffect(user) {
//        val handler = Handler(Looper.getMainLooper())
//        val timeoutMillis = 3000L //Random(5000).nextLong()
//
//        handler.postDelayed({
//            if (!timeoutTrigger) {
//                if (user == null) {
//                    navActions.navigateTo("login")
//                } else {
//                    navActions.navigateTo(Screen.Home)
//                }
//            }
//        }, timeoutMillis)
//    }

    LaunchedEffect(key1 = Unit, block = {
        Handler(Looper.getMainLooper()).postDelayed({
            if (user == null) {
                navActions.navigateTo("login")
            } else {
                navActions.navigateTo("home")
            }
        }, 3000)
    })


    Column(
        modifier = modifier
            .animateContentSize(
                animationSpec = tween(500)
            )
            .fillMaxSize()
            .background(color = Color.White),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AndroidView(
            modifier = Modifier.size(120.dp),
            factory = {
                val image = ImageView(it)
                image.setImageResource(R.mipmap.ic_launcher)
                image.layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )

                image
            }
        )

//        if (displayLoading) {
//            CircularProgressIndicator(
//                modifier = Modifier
//                    .padding(8.dp)
//            )
//        }
    }
}