package io.drdroid.paypalincl.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.layout.layout
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import java.util.regex.Pattern

data class Language(
    val title: String,
    val value: String,
    val country: String
)

object Utils {

    fun String.isValidEmail(): Boolean {
        return this.isNotEmpty() && android.util.Patterns.EMAIL_ADDRESS.matcher(this)
            .matches()
    }

    fun String.isValidPassword(enforce: Boolean = false): Boolean {
        val letter: Pattern = Pattern.compile("[a-zA-Z]")
        val digit: Pattern = Pattern.compile("[0-9]")
        val special: Pattern = Pattern.compile("[!@#$%&*()_+=|<>?{}\\[\\]~-]")

        val hasLetter = letter.matcher(this)
        val hasDigit = digit.matcher(this)
        val hasSpecial = special.matcher(this)

        return if (this.isEmpty()) {
            false
        } else !(
                this.length < 8
                        ||
                        (
                                enforce
                                        &&
                                        (
                                                !hasLetter.find()
                                                        ||
                                                        !hasDigit.find()
                                                        ||
                                                        !hasSpecial.find()
                                                )
                                )
                )
    }

    fun Color.isDark(): Boolean {
        return this.luminance() < .5f
    }

    fun Int.isDark(): Boolean {
        if (this == 0) {
            return false
        }
        return ColorUtils.calculateLuminance(this) < .5f
    }

    fun Modifier.badgeLayout() =
        layout { measurable, constraints ->
            val placeable = measurable.measure(constraints)

            // based on the expectation of only one line of text
            val minPadding = placeable.height / 4

            val width = maxOf(placeable.width + minPadding, placeable.height)
            layout(width, placeable.height) {
                placeable.place((width - placeable.width) / 2, 0)
            }
        }

    fun hasPermissions(context: Context?, permissions: Array<String>): Boolean {
        if (context != null && permissions != null) {
            for (permission in permissions) {
                if (ContextCompat.checkSelfPermission(
                        context,
                        permission!!
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return false
                }
            }
        }
        return true
    }

    @SuppressLint("PermissionLaunchedDuringComposition")
    @OptIn(ExperimentalPermissionsApi::class)
    @Composable
    fun RequestPermission(
        context: Context?,
        permission: String,
        onPermissionResult: (Boolean) -> Unit,
    ) {
        val permissionAlreadyRequested = rememberSaveable {
            mutableStateOf(false)
        }

        val permissionState = rememberPermissionState(
            permission = permission
        ) {
            permissionAlreadyRequested.value = true

            onPermissionResult(it)
        }

        if (!permissionAlreadyRequested.value && !permissionState.status.shouldShowRationale) {
            SideEffect {
                permissionState.launchPermissionRequest()
            }
        } else if (permissionState.status.shouldShowRationale) {
//        ShowRationaleContent {
            permissionState.launchPermissionRequest()
//        }

        } else {
//        ShowOpenSettingsContent {
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            val uri: Uri = Uri.fromParts("package", context?.packageName, null)
            intent.data = uri
            context?.startActivity(intent)
//        }
        }

    }

    @SuppressLint("PermissionLaunchedDuringComposition")
    @OptIn(ExperimentalPermissionsApi::class)
    @Composable
    fun RequestMultiplePermissions(
        context: Context?,
        permissions: List<String>,
        onPermissionsResult: (Boolean) -> Unit,
    ) {
        val permissionAlreadyRequested = rememberSaveable {
            mutableStateOf(false)
        }

        val permissionState = rememberMultiplePermissionsState(
            permissions = permissions
        ) {
            permissionAlreadyRequested.value = true
//        if (!it.values.contains(false)) {
//            onPermissionsGranted.invoke()
//        }
            onPermissionsResult(!it.values.contains(false))
        }

        if (!permissionAlreadyRequested.value && !permissionState.shouldShowRationale) {
            SideEffect {
                permissionState.launchMultiplePermissionRequest()
            }
        } else if (permissionState.shouldShowRationale) {
//        ShowRationaleContent {
            permissionState.launchMultiplePermissionRequest()
//        }

        } else {
//        ShowOpenSettingsContent {
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            val uri: Uri = Uri.fromParts("package", context?.packageName, null)
            intent.data = uri
            context?.startActivity(intent)
//        }
        }

    }
}