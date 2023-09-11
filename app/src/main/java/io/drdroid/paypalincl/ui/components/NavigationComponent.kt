package io.drdroid.paypalincl.ui.components

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector4D
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import androidx.palette.graphics.Palette
import com.google.gson.Gson
import io.drdroid.paypalincl.R
import io.drdroid.paypalincl.data.model.tv_show.ShowModel
import io.drdroid.paypalincl.data.vm.UserViewModel
import io.drdroid.paypalincl.ui.navigation.AppNavigationActions
import io.drdroid.paypalincl.ui.navigation.Screen
import io.drdroid.paypalincl.ui.pages.details.DetailScreen
import io.drdroid.paypalincl.ui.pages.donate.DonateScreen
import io.drdroid.paypalincl.ui.pages.home.HomeScreen
import io.drdroid.paypalincl.ui.pages.login.LoginRegisterScreen
import io.drdroid.paypalincl.ui.pages.search.SearchScreen
import io.drdroid.paypalincl.ui.pages.settings.SettingsScreen
import io.drdroid.paypalincl.ui.pages.splash.SplashScreen
import io.drdroid.paypalincl.ui.theme.Background
import io.drdroid.paypalincl.ui.theme.OnPrimaryDark
import io.drdroid.paypalincl.ui.theme.PrimaryDark
import io.drdroid.paypalincl.ui.theme.TertiaryDark
import io.drdroid.paypalincl.utils.Utils.isDark

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(
//    navActions: AppNavigationActions,
    color: Animatable<Color, AnimationVector4D>,
    currentDestination: NavDestination?,
    onNavigationClick: () -> Unit,
    scrollBehavior: TopAppBarScrollBehavior,
    onNavigate: (String) -> Unit,
    userViewModel: UserViewModel = hiltViewModel()
) {
    var title by remember { mutableStateOf(currentDestination?.route.toString()) }
    var displayHomeButton by remember { mutableStateOf(false) }
    var displayMenu by remember { mutableStateOf(true) }

    displayHomeButton = when (currentDestination?.route) {
        Screen.Home.route -> {
            false
        }

        Screen.Donate.route,
        Screen.Settings.route,
        "${Screen.Details.route}/{show}",
        -> {
            true
        }

        else -> {
            false
        }
    }

    displayMenu = when (currentDestination?.route) {
        Screen.Home.route -> {
            true
        }

        Screen.Donate.route,
        Screen.Settings.route,
        "${Screen.Details.route}/{show}",
        -> {
            false
        }

        else -> {
            true
        }
    }

    title = when (currentDestination?.route) {
        Screen.Splash.route -> {
            ""
        }

        Screen.Donate.route -> {
            stringResource(id = Screen.Donate.resourceId)
        }

        Screen.Settings.route -> {
            stringResource(id = Screen.Settings.resourceId)
        }

        "${Screen.Details.route}/{show}" -> {
            stringResource(id = Screen.Details.resourceId)
        }

        else -> {
//            currentDestination?.route.toString().split("/")[0]
            stringResource(R.string.hi, userViewModel.currentUser?.displayName!!)
        }
    }

    var displayProfileDialog by remember { mutableStateOf(false) }
    if (displayProfileDialog) {
        CustomDialog(
            cancellable = true,
            backgroundColor = PrimaryDark,
            onDismiss = { displayProfileDialog = false },
            content = {
                MenuDialog(
                    onClose = { displayProfileDialog = false },
                    onNavigate = onNavigate
                )
            }
        )
    }

    TopAppBar(
        scrollBehavior = scrollBehavior,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = color.value
        ),
        title = {
            Text(
                text = title,
                modifier = Modifier
                    .padding(end = 12.dp),
                color = if (color.value.isDark()) Color.White else Color(0xFF00233C),
                textAlign = TextAlign.Start,
                style = MaterialTheme.typography.titleMedium
            )
        },
        navigationIcon = {
            if (displayHomeButton) {
                Button(
                    onClick = onNavigationClick,
                    Modifier
                        .background(Color.Transparent)
                        .fillMaxHeight(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        contentColor = Color.Transparent
                    ),
                ) {
                    Icon(
                        modifier = Modifier,
                        tint = if (color.value.isDark()) Color.White else Color.Black,
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            }
        },
        actions = {
            if (displayMenu) {
                ProfileImage(
                    modifier = Modifier
                        .height(48.dp)
                        .padding(end = 16.dp)
                        .scale(1f)
                        .clip(shape = CircleShape),
                    imgUrl = userViewModel.currentUser?.photoUrl.toString(),
                    onClicked = { displayProfileDialog = true }
                )
            }
        },
    )
}

@Composable
fun MenuDialog(
    onClose: () -> Unit,
    onNavigate: (String) -> Unit,
    userViewModel: UserViewModel = hiltViewModel()
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 8.dp, end = 8.dp)
//            .background(color = PrimaryDark)
    ) {
        //Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(
                imageVector = Icons.Filled.Close,
                contentDescription = "",
                tint = Color.White,
                modifier = Modifier
                    .weight(1f)
                    .clickable { onClose.invoke() }
            )
            Text(
                text = stringResource(id = R.string.app_name),
                style = MaterialTheme.typography.displaySmall,
                textAlign = TextAlign.Center,
                color = Color.White,
                modifier = Modifier.weight(9f)
            )
        }

        //Profile
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(shape = RoundedCornerShape(25.dp))
                .border(
                    border = BorderStroke(0.dp, Color.Transparent),
                    shape = RoundedCornerShape(25.dp)
                )
                .background(color = Background)
                .padding(8.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            var expanded by remember { mutableStateOf(false) }
            val icon = if (expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore

            Text(
                text = stringResource(R.string.profile),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp, bottom = 8.dp),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .animateContentSize(animationSpec = tween(250)),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                ProfileImage(
                    modifier = Modifier
                        .height(32.dp)
                        .weight(3f)
                        .padding(end = 16.dp)
                        .scale(1f)
                        .clip(shape = CircleShape),
                    imgUrl = userViewModel.currentUser?.photoUrl.toString(),
                    onClicked = {}
                )
                Column(modifier = Modifier.weight(8f)) {
                    Text(text = userViewModel.currentUser?.displayName!!)
                    Text(
                        text = userViewModel.currentUser.email!!,
                        style = MaterialTheme.typography.labelSmall
                    )
                }

                Icon(
                    imageVector = icon,
                    contentDescription = "",
                    modifier = Modifier
                        .weight(2f)
                        .padding(4.dp)
//                        .size(32.dp)
                        .border(border = BorderStroke(2.dp, Color.Gray), shape = CircleShape)
                        .clickable { expanded = !expanded })

            }
            if (expanded) {
                //display extra settings
                MenuItem(
                    title = stringResource(R.string.switch_account),
                    icon = Icons.Filled.SwapHoriz,
                    onClick = {}
                )

                Spacer(modifier = Modifier.height(16.dp))
            }
            OutlinedButton(
                border = BorderStroke(2.dp, Color.Black),
                shape = RoundedCornerShape(7.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = Color.Transparent
                ),
                onClick = {
                    userViewModel.signOut {
                        onNavigate.invoke(Screen.LoginRegister.route)
                    }
                }
            ) {
                Text(
                    text = stringResource(R.string.log_out),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        //Menus
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(shape = RoundedCornerShape(25.dp))
                .border(
                    border = BorderStroke(0.dp, Color.Transparent),
                    shape = RoundedCornerShape(25.dp)
                )
                .background(color = Background)
                .padding(8.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.menu),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp, bottom = 8.dp),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge
            )
            MenuItem(
                title = stringResource(R.string.notifications),
                icon = Icons.Filled.Notifications,
                onClick = {}
            )
            MenuItem(
                title = stringResource(id = R.string.settings),
                icon = Icons.Filled.Settings,
                onClick = {
                    onNavigate.invoke("settings")
                    onClose.invoke()
                }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        //Extras
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(shape = RoundedCornerShape(25.dp))
                .border(
                    border = BorderStroke(0.dp, Color.Transparent),
                    shape = RoundedCornerShape(25.dp)
                )
                .background(color = Background)
                .padding(8.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.extras),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp, bottom = 8.dp),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge
            )
//            //paypal donation
//            PaypalButton(
//                modifier = Modifier,
//                onClick = {}
//            )
            MenuItem(
                title = stringResource(R.string.about),
                icon = Icons.Filled.Info,
                onClick = {}
            )
            MenuItem(
                title = stringResource(id = R.string.donate),
                icon = Icons.Filled.AttachMoney,
                onClick = {
                    onNavigate.invoke(Screen.Donate.route)
                    onClose.invoke()
                }
            )
        }


    }
}

@Composable
fun MenuItem(
    modifier: Modifier = Modifier,
    title: String,
    icon: ImageVector? = null,
    onClick: () -> Unit,
) {
    Row(
        modifier = modifier
            .padding(bottom = 8.dp, top = 8.dp)
            .clickable { onClick.invoke() },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        icon?.let {
            Icon(
                imageVector = it,
                contentDescription = "",
                modifier = Modifier
                    .weight(2f)
                    .height(32.dp)
            )
        }
        Text(
            text = title,
            modifier = Modifier.weight(8f),
            textAlign = TextAlign.Start,
            style = MaterialTheme.typography.titleMedium
        )
    }
//    TextFieldComponent(
//        enabled = false,
//        startIcon = icon,
//        labelValue = title, onValueChanged = {},
//        modifier = modifier
//            .fillMaxWidth()
//            .clickable { onClick.invoke() }
//    )
}

fun NavGraphBuilder.verticalSlideComposable(
    route: String,
    arguments: List<NamedNavArgument> = listOf(),
    enterTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition =
        {
            slideInVertically(
                animationSpec = tween(400),
                initialOffsetY = { 100 }
            ) + fadeIn(
                animationSpec = tween(400)
            )
        },
    exitTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition =
        {
            slideOutVertically(
                animationSpec = tween(400),
                targetOffsetY = { -100 }
            ) + fadeOut(
                animationSpec = tween(200)
            )
        },
    popExitTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition =
        {
            slideOutVertically(
                animationSpec = tween(400),
                targetOffsetY = { 100 }
            ) + fadeOut(
                animationSpec = tween(200)
            )
        },
    popEnterTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition =
        {
            slideInVertically(
                animationSpec = tween(400),
                initialOffsetY = { -100 }
            ) + fadeIn(
                animationSpec = tween(400)
            )
        },
    content: @Composable (NavBackStackEntry) -> Unit,
) {
    composable(
        route = route,
        arguments = arguments,
        enterTransition = enterTransition,
        popEnterTransition = popEnterTransition,
        popExitTransition = popExitTransition,
        exitTransition = exitTransition
    ) {
        content(it)
    }
}

fun NavGraphBuilder.horizontalSlideComposable(
    route: String,
    arguments: List<NamedNavArgument> = listOf(),
    enterTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition =
        {
            slideInHorizontally(
                animationSpec = tween(400),
                initialOffsetX = { 100 }
            ) + fadeIn(
                animationSpec = tween(400)
            )
        },
    exitTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition =
        {
            slideOutHorizontally(
                animationSpec = tween(400),
                targetOffsetX = { -100 }
            ) + fadeOut(
                animationSpec = tween(200)
            )
        },
    popExitTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition =
        {
            slideOutHorizontally(
                animationSpec = tween(400),
                targetOffsetX = { 100 }
            ) + fadeOut(
                animationSpec = tween(200)
            )
        },
    popEnterTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition =
        {
            slideInHorizontally(
                animationSpec = tween(400),
                initialOffsetX = { -100 }
            ) + fadeIn(
                animationSpec = tween(400)
            )
        },
    content: @Composable (NavBackStackEntry) -> Unit,
) {
    composable(
        route = route,
        arguments = arguments,
        enterTransition = enterTransition,
        popEnterTransition = popEnterTransition,
        popExitTransition = popExitTransition,
        exitTransition = exitTransition
    ) {
        content(it)
    }
}

fun NavGraphBuilder.fadeInOutComposable(
    route: String,
    arguments: List<NamedNavArgument> = listOf(),
    enterTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition =
        {
            fadeIn(
                animationSpec = tween(400)
            )
        },
    exitTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition =
        {
            fadeOut(
                animationSpec = tween(200)
            )
        },
    popExitTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition =
        {
            fadeOut(
                animationSpec = tween(200)
            )
        },
    popEnterTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition =
        {
            fadeIn(
                animationSpec = tween(400)
            )
        },
    content: @Composable (NavBackStackEntry) -> Unit,
) {
    composable(
        route = route,
        arguments = arguments,
        enterTransition = enterTransition,
        popEnterTransition = popEnterTransition,
        popExitTransition = popExitTransition,
        exitTransition = exitTransition
    ) {
        content(it)
    }
}

@Composable
fun AppNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    navActions: AppNavigationActions,
    onColorChanged: (Palette) -> Unit,
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = Screen.Splash.route,
        builder = {
            fadeInOutComposable(
                route = Screen.Splash.route
            ) {
                SplashScreen(
                    navActions = navActions,
                )
            }

            loginGraph(navAction = navActions)
            homeGraph(navAction = navActions)

            settingsGraph(navAction = navActions)

            verticalSlideComposable(route = Screen.Donate.route) {
                DonateScreen(
//                    navActions = navActions
                )
            }

            horizontalSlideComposable(
                route = "${Screen.Details.route}/{show}",
                arguments = listOf(
                    navArgument("show") {
                        type = NavType.StringType
                    }
                )
            ) { navBackStackEntry ->
                val sh = navBackStackEntry.arguments?.getString("show")?.replace("+"," ")
                sh?.let { str ->
                    val pre = "show="
                    var value = str
                    if (value.startsWith(pre)) {
                        value = value.substring(pre.length)
                    }
                    val show = Gson().fromJson(
                        value,
                        ShowModel::class.java
                    )
//                    show?.let {
                    DetailScreen(
                        show = show,
                        onColorChanged = onColorChanged
                    )
//                    }
                }
            }
        }
    )
}

fun NavGraphBuilder.loginGraph(
    navAction: AppNavigationActions
) {
    navigation(startDestination = Screen.LoginRegister.route, route = "login") {
        horizontalSlideComposable(Screen.LoginRegister.route) {
            LoginRegisterScreen(
                navAction = navAction
            )
        }
    }
}

fun NavGraphBuilder.homeGraph(
    navAction: AppNavigationActions
) {
    navigation(startDestination = Screen.Home.route, route = "home") {
        horizontalSlideComposable(Screen.Home.route) {
            HomeScreen(
                navigateTo = { route, arguments ->
                    navAction.navigateTo(route, arguments)
                }
            )
        }
        horizontalSlideComposable(Screen.Search.route) {
            SearchScreen(
                navigateTo = { route, arguments ->
                    navAction.navigateTo(route, arguments)
                }
            )
        }
    }
}

fun NavGraphBuilder.settingsGraph(
    navAction: AppNavigationActions
) {
    navigation(startDestination = Screen.Settings.route, route = "settings") {
        horizontalSlideComposable(Screen.Settings.route) {
            SettingsScreen()
        }
    }
}

@Composable
fun BottomBar(
    modifier: Modifier = Modifier,
//    navActions: AppNavigationActions,
    items: List<Screen>,
    currentDestination: NavDestination?,
    onNavigate: (String) -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(color = Color.Transparent)
    ) {
        Card(
            modifier = Modifier.padding(bottom = 18.dp, start = 32.dp, end = 32.dp),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 3.dp
            ),
            shape = RoundedCornerShape(32.dp),
            colors = CardDefaults.cardColors(
                containerColor = OnPrimaryDark
            )
        ) {
            BottomNavigation(
                modifier = Modifier.fillMaxWidth(),
                //            .border(
                //                border = BorderStroke(0.dp, color = Color.Transparent),
                //                shape = RoundedCornerShape(16.dp)
                //            )
                ////            .clip(shape = RoundedCornerShape(16.dp))
                backgroundColor = OnPrimaryDark,
            ) {
                items.forEach { screen ->
                    BottomNavigationItem(
                        icon = {
                            Icon(
                                imageVector = screen.icon ?: Icons.Filled.Favorite,
                                contentDescription = null,
                                tint = if (screen.route == currentDestination?.route) PrimaryDark else TertiaryDark
                            )
                        },
                        label = {
                            Text(
                                text = stringResource(screen.resourceId),
                                maxLines = 1,
                                softWrap = true,
                                color = PrimaryDark
                            )
                        },
                        alwaysShowLabel = false,
                        selected = screen.route == currentDestination?.route,
                        onClick = {
                            if (currentDestination?.route == screen.route) {
                                return@BottomNavigationItem
                            }
                            //                    navController.navigate(screen.route) {
                            //                        // Pop up to the start destination of the graph to
                            //                        // avoid building up a large stack of destinations
                            //                        // on the back stack as users select items
                            //                        popUpTo(navController.graph.findStartDestination().id) {
                            //                            saveState = true
                            //                        }
                            //                        // Avoid multiple copies of the same destination when
                            //                        // reselecting the same item
                            //                        launchSingleTop = true
                            //                        // Restore state when reselecting a previously selected item
                            //                        restoreState = true
                            //                    }
//                            navActions.navigateTo(screen)
                            onNavigate(screen.route)
                        }
                    )
                }
            }
        }
    }
}