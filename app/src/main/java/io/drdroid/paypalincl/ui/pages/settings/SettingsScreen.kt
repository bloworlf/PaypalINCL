package io.drdroid.paypalincl.ui.pages.settings

import android.app.Activity
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.PermMedia
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material.icons.outlined.ExpandLess
import androidx.compose.material.icons.outlined.ExpandMore
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.drdroid.paypalincl.R
import io.drdroid.paypalincl.ui.components.RadioGroupComponent
import io.drdroid.paypalincl.utils.Common
import io.drdroid.paypalincl.utils.Language

@Composable
fun SettingsScreen() {
    val context = LocalContext.current

    val languageList = listOf(
        Language(stringResource(id = R.string.english), "en", "US"),
        Language(stringResource(id = R.string.french), "fr", "FR"),
//        Language(stringResource(id = R.string.creole), "ht", "HT"),
    )

    Column(
        modifier = Modifier
            .animateContentSize()
            .fillMaxSize()
            .padding(top = 8.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OptionsItem(
            title = stringResource(R.string.manage_permissions),
            subTitle = stringResource(R.string.manage_app_permissions),
            icon = Icons.Filled.PermMedia,
            onItemClick = {}
        )
        OptionsItem(
            title = stringResource(R.string.language),
            subTitle = stringResource(R.string.change_the_application_s_display_language),
            icon = Icons.Filled.Language,
//            onItemClick = {},
            content = { paddingValues ->
                var selectedIndex by remember {
                    mutableIntStateOf(
                        languageList.indexOf(
                            languageList.find {
                                it.value == Common.getPreferences()
                                    .getString(
                                        Common.PREFERENCES.LANGUAGE_KEY,
                                        Common.DEFAULT_LANGUAGE
                                    )
                            }
                        )
                    )
                }

                RadioGroupComponent(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(paddingValues),
                    items = languageList,
                    selectedIndex = selectedIndex,
                    selectedItemToString = { it.title },
                    onItemSelected = { index, item ->
                        if (selectedIndex != index) {
                            selectedIndex = index
                            Common.getPreferences()
                                .edit()
                                .putString(Common.PREFERENCES.LANGUAGE_KEY, item.value)
                                .apply()
                            (context as? Activity)?.recreate()
                        }
                    }
                )
            }
        )
    }
}

@Composable
fun OptionsItem(
    enabled: Boolean = true,
    title: String,
    subTitle: String,
    icon: ImageVector,
    onItemClick: (() -> Unit)? = null,
    content: @Composable (ColumnScope.(PaddingValues) -> Unit)? = null
) {
    var expanded by remember { mutableStateOf(false) }
    val endIcon = if (content == null) {
        Icons.Outlined.ChevronRight
    } else {
        if (expanded) {
            Icons.Outlined.ExpandLess
        } else {
            Icons.Outlined.ExpandMore
        }
    }

    Surface(
        modifier = if (enabled) {
            Modifier
        } else {
            Modifier
                .alpha(.45f)
        }
            .animateContentSize()
            .fillMaxWidth(),
        elevation = if (expanded) 7.dp else 0.dp
    ) {
        Column {
            Row(
                modifier = if (enabled) {
                    Modifier
                        .clickable(enabled = true) {
                            if (content != null) {
                                expanded = !expanded
                            } else {
                                onItemClick?.invoke()
                            }
                        }
                } else {
                    Modifier
                }
                    .fillMaxWidth()
                    .padding(all = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                // Icon
                Icon(
                    modifier = Modifier
                        .size(32.dp),
                    imageVector = icon,
                    contentDescription = title,
                    tint = MaterialTheme.colors.primary
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier
                            .weight(weight = 3f, fill = false)
                            .padding(start = 16.dp)
                    ) {

                        // Title
                        Text(
                            text = title,
                            style = TextStyle(
                                fontSize = 18.sp,
                            )
                        )

                        Spacer(modifier = Modifier.height(2.dp))

                        // Sub title
                        Text(
                            text = subTitle,
                            style = TextStyle(
                                fontSize = 14.sp,
                                letterSpacing = (0.8).sp,
                                color = Color.Gray
                            )
                        )
                    }

                    // Right arrow icon
                    Icon(
                        modifier = Modifier
                            .weight(weight = 1f, fill = false),
                        imageVector = endIcon,
                        contentDescription = title,
                        tint = Color.Black.copy(alpha = 0.70f)
                    )
                }

            }
            Spacer(modifier = Modifier.height(8.dp))
            if (expanded && content != null) {
                content(PaddingValues(start = 24.dp, end = 24.dp, bottom = 8.dp))
            }
        }
    }

}