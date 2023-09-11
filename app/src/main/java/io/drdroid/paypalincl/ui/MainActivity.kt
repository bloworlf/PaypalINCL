package io.drdroid.paypalincl.ui

import android.content.Context
import android.os.Bundle
import android.preference.PreferenceManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint
import io.drdroid.paypalincl.ui.components.AppBar
import io.drdroid.paypalincl.ui.components.AppNavigation
import io.drdroid.paypalincl.ui.components.BottomBar
import io.drdroid.paypalincl.ui.navigation.AppNavigationActions
import io.drdroid.paypalincl.ui.navigation.Screen
import io.drdroid.paypalincl.ui.pages.view.FullScreenHandleableApplication
import io.drdroid.paypalincl.ui.theme.Background
import io.drdroid.paypalincl.ui.theme.PaypalINCLTheme
import io.drdroid.paypalincl.ui.theme.PrimaryDark
import io.drdroid.paypalincl.ui.theme.SecondaryDark
import io.drdroid.paypalincl.utils.BaseContextWrapper
import io.drdroid.paypalincl.utils.Common
import io.drdroid.paypalincl.utils.Utils.isDark
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

//    private val config: PayPalConfiguration =
//        PayPalConfiguration() // Start with mock environment.  When ready,
//            // switch to sandbox (ENVIRONMENT_SANDBOX)
//            // or live (ENVIRONMENT_PRODUCTION)
//            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX) // on below line we are passing a client id.
//            .clientId(clientKey)

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FullScreenHandleableApplication {
                PaypalINCLTheme {
                    val coroutine = rememberCoroutineScope()

                    var statusColor by remember { mutableStateOf(Color.White) }
                    var navigationColor by remember { mutableStateOf(Color.White) }
                    var backColor by remember { mutableStateOf(Color.White) }
                    val statusBarColor = remember { Animatable(statusColor) }
                    val navigationBarColor = remember { Animatable(navigationColor) }
                    val backgroundColor = remember { Animatable(backColor) }

                    LaunchedEffect(key1 = statusColor, block = {
                        coroutine.launch {
                            statusBarColor.animateTo(
                                targetValue = statusColor,
                                animationSpec = tween(500)
                            )
                        }
                    })
                    LaunchedEffect(key1 = navigationColor, block = {
                        coroutine.launch {
                            navigationBarColor.animateTo(
                                targetValue = navigationColor,
                                animationSpec = tween(500)
                            )
                        }
                    })
                    LaunchedEffect(key1 = backColor, block = {
                        coroutine.launch {
                            backgroundColor.animateTo(
                                targetValue = backColor,
                                animationSpec = tween(500)
                            )
                        }
                    })

                    val systemUiController = rememberSystemUiController()
                    systemUiController.setStatusBarColor(
                        color = statusBarColor.value,
                        darkIcons = !statusBarColor.value.isDark()
                    )
                    systemUiController.setNavigationBarColor(
                        color = navigationBarColor.value,
                        darkIcons = !navigationBarColor.value.isDark()
                    )

                    val navController: NavHostController = rememberNavController()
                    val currentNavBackStackEntry by navController.currentBackStackEntryAsState()
                    var currentDestination by remember {
                        mutableStateOf(currentNavBackStackEntry?.destination)
                    }
                    val navigationActions = remember(navController) {
                        AppNavigationActions(navController)
                    }

                    var displayAppBar by remember {
                        mutableStateOf(false)
                    }
                    var displayBottomBar by remember {
                        mutableStateOf(false)
                    }

                    val destinationChangedListener =
                        NavController.OnDestinationChangedListener { _, destination, _ ->
                            println(destination.route)
                            when (destination.route) {
                                Screen.Splash.route -> {
                                    displayAppBar = false
                                    displayBottomBar = false
                                    statusColor = Color.White
                                    navigationColor = Color.White
                                    backColor = Color.White
                                }

                                Screen.LoginRegister.route,
                                -> {
                                    displayAppBar = false
                                    displayBottomBar = false
                                    statusColor = PrimaryDark
                                    navigationColor = SecondaryDark
                                    backColor = PrimaryDark
                                }

                                Screen.Home.route,
                                Screen.Search.route,
                                -> {
                                    displayAppBar = true
                                    displayBottomBar = true
                                    statusColor = Background
                                    navigationColor = Background
                                    backColor = Background
                                }

                                Screen.Settings.route,
                                -> {
                                    displayAppBar = true
                                    displayBottomBar = false
                                    statusColor = Color.White
                                    navigationColor = Color.White
                                    backColor = Color.White
                                }

                                Screen.Donate.route,
                                -> {
                                    displayAppBar = true
                                    displayBottomBar = false
                                    statusColor = Color(0xFF009CDE)
                                    navigationColor = Color(0xFF009CDE)
                                    backColor = Color(0xFF009CDE)
                                }

                                "${Screen.Details.route}/{show}" -> {
                                    displayAppBar = true
                                    displayBottomBar = false
                                }

                                else -> {
                                    displayAppBar = true
                                    displayBottomBar = true
                                    statusColor = Color.White
                                    navigationColor = Color.White
                                    backColor = Color.White
                                }
                            }
                            currentDestination = destination
                        }

                    LaunchedEffect(Unit) {
                        navController.addOnDestinationChangedListener(destinationChangedListener)
                    }

                    val appBarState = rememberTopAppBarState()
                    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(appBarState)

                    Scaffold(
                        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
                        containerColor = backgroundColor.value,
                        topBar = {
                            if (displayAppBar) {
                                AppBar(
//                                    navActions = navigationActions,
                                    scrollBehavior = scrollBehavior,
                                    color = statusBarColor,
                                    currentDestination = currentDestination,
                                    onNavigationClick = { navigationActions.navigateBack() },
                                    onNavigate = { navigationActions.navigateTo(it) }
                                )
                            }
                        },
                        bottomBar = {
                            if (displayBottomBar) {
                                BottomBar(
//                                    navActions = navigationActions,
                                    items = listOf(
                                        Screen.Home,
                                        Screen.Search
                                    ),
                                    currentDestination = currentDestination,
                                    onNavigate = { navigationActions.navigateTo(it) }
                                )
                            }
                        },
                        content = { paddingValues ->
                            Box {
                                AppNavigation(
                                    modifier = Modifier.padding(top = paddingValues.calculateTopPadding()),
                                    navController = navController,
                                    navActions = navigationActions,
                                    onColorChanged = {
                                        statusColor = Color(it.dominantSwatch?.rgb!!)
                                        navigationColor = Color(it.dominantSwatch?.rgb!!)
                                        backColor = Color(it.darkVibrantSwatch?.rgb!!)
                                    }
                                )
                            }
                        }
                    )
                }
            }
        }
    }

    override fun attachBaseContext(base: Context) {
        // fetch from shared preference also save the same when applying. Default here is en = English
        val language = PreferenceManager.getDefaultSharedPreferences(base)
            .getString(Common.PREFERENCES.LANGUAGE_KEY, Common.DEFAULT_LANGUAGE)!!
        super.attachBaseContext(BaseContextWrapper.wrap(base, language))
    }
}