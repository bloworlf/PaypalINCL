package io.drdroid.paypalincl.utils

import android.Manifest
import android.content.SharedPreferences
import android.os.Build
import android.preference.PreferenceManager
import dagger.hilt.android.qualifiers.ApplicationContext
import io.drdroid.paypalincl.App
import javax.inject.Inject

object Common {

    val DEFAULT_LANGUAGE: String = "en"

    object PAYPAL_API {
        const val BASE_URL = "https://api-m.sandbox.paypal.com/"
        const val API_VERSION = "v1"
        const val BASE_API = "$BASE_URL$API_VERSION/"

        const val AUTH_TOKEN_ENDPOINT = "$BASE_API/oauth2/token"
    }

    object TV_SHOW_API{
        const val BASE_URL: String = "https://api.tvmaze.com/"

        private const val LIST_SHOW: String = "shows"
        private const val SEARCH_SHOW: String = "search/shows"
        private const val SHOW_SCHEDULE: String = "schedule"
        private const val SHOW_SEASONS: String = "shows/{id}/seasons"
        private const val SHOW_EPISODES: String = "seasons/{id}/episodes"

        const val SHOWS: String = "$BASE_URL$LIST_SHOW"
        const val SEARCH: String = "$BASE_URL$SEARCH_SHOW"
        const val SCHEDULE: String = "$BASE_URL$SHOW_SCHEDULE"
        const val SEASONS: String = "$BASE_URL$SHOW_SEASONS"
        const val EPISODES: String = "$BASE_URL$SHOW_EPISODES"
    }

    object PERMISSIONS {
        const val PERMISSION_NOTIFICATION: String = Manifest.permission.POST_NOTIFICATIONS

        val PERMISSION_STORAGE = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arrayOf(
                Manifest.permission.READ_MEDIA_IMAGES,
//                Manifest.permission.READ_MEDIA_VIDEO
            )
        } else {
            arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
//                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        }
        const val PERMISSION_STORAGE_CODE: Int = 121

        const val PERMISSION_CAMERA: String = Manifest.permission.CAMERA
        private const val PERMISSION_MICROPHONE: String = Manifest.permission.RECORD_AUDIO

        val PERMISSION_CAMERA_ALL = arrayOf(
            PERMISSION_CAMERA,
            PERMISSION_MICROPHONE
        )
        const val PERMISSION_CAMERA_ALL_CODE: Int = 111

    }

    fun getPreferences(): SharedPreferences {
        return PREFERENCES.preferences
    }

    object PREFERENCES {
        val LANGUAGE_KEY: String = "LANGUAGE_KEY"
        val USER_KEY: String = "USER_KEY"
        val WELCOME_SCREEN_KEY: String = "WELCOME_SCREEN_KEY"
        val SPONSORED_KEY: String = "SPONSORED_KEY"
        val DISPLAY_TYPE_KEY: String = "DISPLAY_TYPE_KEY"
        val THEME_KEY: String = "THEME_KEY"
        val EMULATE_SELF_KEY: String = "EMULATE_SELF_KEY"
        val DISABLE_REQUEST: String = "DISABLE_REQUEST"

        val preferences = PreferenceManager.getDefaultSharedPreferences(App.instance)
    }
}