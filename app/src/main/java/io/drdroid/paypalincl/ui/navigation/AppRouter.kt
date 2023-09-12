package io.drdroid.paypalincl.ui.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Details
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import io.drdroid.paypalincl.R
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

object Destinations {

    const val PROFILE = "Profile"
    const val DETAILS = "Details"
    const val SETTINGS = "Settings"
    const val DONATE = "Donate"
    const val SEARCH = "Search"
    const val HOME = "Home"
//    const val REGISTER = "Register"
//    const val LOGIN = "Login"
    const val LOGIN_REGISTER = "LoginRegister"
    const val SPLASH = "Splash"

}

sealed class Screen(val route: String, @StringRes val resourceId: Int, val icon: ImageVector?) {
    object Splash : Screen(Destinations.SPLASH, R.string.splash, Icons.Filled.Star)
//    object Login : Screen(Destinations.LOGIN, R.string.login, Icons.Filled.Key)
//    object Register : Screen(Destinations.REGISTER, R.string.register, Icons.Filled.Key)
    object LoginRegister : Screen(Destinations.LOGIN_REGISTER, R.string.login_register, Icons.Filled.Key)
    object Home : Screen(Destinations.HOME, R.string.home, Icons.Filled.Home)
    object Search : Screen(Destinations.SEARCH, R.string.search, Icons.Filled.Search)
    object Donate : Screen(Destinations.DONATE, R.string.donate, Icons.Filled.AttachMoney)
    object Settings : Screen(Destinations.SETTINGS, R.string.settings, Icons.Filled.Settings)
    object Details : Screen(Destinations.DETAILS, R.string.details, Icons.Filled.Details)
    object Profile : Screen(Destinations.PROFILE, R.string.profile, Icons.Filled.Person)
}

class AppNavigationActions(private val navController: NavHostController) {
    fun navigateTo(route: String, arguments: Map<String, Any?>) {
        var args1 = ""
        var args2 = ""
        val iterator = arguments.entries.iterator()
        while (iterator.hasNext()) {
            var (key, value) = iterator.next()
            if (value == null) {
                continue
            }
            if (value is String && value.contains("/")) {
                value = URLEncoder.encode(value, StandardCharsets.UTF_8.toString())
            }
            args1 += "$value/"
            args2 += "$key=$value/"
        }
        args1 = args1.substring(0, args1.length - 1)
        args2 = args2.substring(0, args2.length - 1)
        try {
            navController.navigate("${route}/$args1") {
                popUpTo(route)
            }
        } catch (e: Exception) {
            navController.navigate("${route}/$args2") {
                popUpTo(route)
            }
        }
    }

    fun navigateTo(screen: Screen, arguments: Map<String, Any?>) {
        var args1 = ""
        var args2 = ""
        val iterator = arguments.entries.iterator()
        while (iterator.hasNext()) {
            var (key, value) = iterator.next()
            if (value == null) {
                continue
            }
            if (value is String && value.contains("/")) {
                value = URLEncoder.encode(value, StandardCharsets.UTF_8.toString())
            }
            args1 += "$value/"
            args2 += "$key=$value/"
        }
        args1 = args1.substring(0, args1.length - 1)
        args2 = args2.substring(0, args2.length - 1)
        try {
            navController.navigate("${screen.route}/$args1") {
                popUpTo(screen.route)
            }
        } catch (e: Exception) {
            navController.navigate("${screen.route}/$args2") {
                popUpTo(screen.route)
            }
        }
    }

    fun navigateTo(screen: Screen) {
        navController.navigate(screen.route) {
            popUpTo(screen.route)
        }
    }

    fun navigateTo(route: String) {
        navController.navigate(route) {
            popUpTo(route)
        }
    }

    fun navigateBack() {
        navController.popBackStack()
    }
}