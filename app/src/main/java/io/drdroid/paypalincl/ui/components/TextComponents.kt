package io.drdroid.paypalincl.ui.components

import android.util.Patterns
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateSizeAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActionScope
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ContentAlpha
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.LocalMinimumInteractiveComponentEnforcement
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Password
import androidx.compose.material.icons.filled.RadioButtonChecked
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import io.drdroid.paypalincl.R
import io.drdroid.paypalincl.ui.theme.Purple80
import io.drdroid.paypalincl.ui.theme.SecondaryDark
import io.drdroid.paypalincl.ui.theme.TertiaryDark
import io.drdroid.paypalincl.utils.Utils.isValidEmail
import io.drdroid.paypalincl.utils.Utils.isValidPassword
import kotlinx.coroutines.delay

@Composable
fun TextFieldComponent(
    modifier: Modifier = Modifier,
    shape: Shape = OutlinedTextFieldDefaults.shape,
    colors: TextFieldColors? = null,
    labelValue: String,
    textValue: String? = null,
    onValueChanged: (String) -> Unit,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.None,
    onActionKey: (KeyboardActionScope.() -> Unit)? = null,
    startIcon: ImageVector? = Icons.Filled.TextFields,
    endIcon: ImageVector? = null,
    iconAction: () -> Unit = {},
    iconTint: Color? = null,
    pattern: String? = null,
    enabled: Boolean = true,
    singleLine: Boolean = false,
    errorEnabled: Boolean = false,
    errorMessage: String? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None
) {
    val isError = remember {
        mutableStateOf(false)
    }
    var value by remember { mutableStateOf(textValue ?: "") }
    var valueChanged by remember { mutableStateOf(value.trim().isNotEmpty()) }

    LaunchedEffect(valueChanged) {
        delay(1500L)
        isError.value = value.isNotEmpty() && !value.trim().isValidEmail()
        valueChanged = false
    }

    OutlinedTextField(
        shape = shape,
        visualTransformation = visualTransformation,
        enabled = enabled,
        readOnly = !enabled,
        supportingText = if (errorEnabled) {
            {
                if (isError.value) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = errorMessage ?: stringResource(
                            R.string.the_text_doesn_t_match,
                            pattern!!
                        ),
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        } else {
            null
        },
        singleLine = singleLine,
        maxLines = 5,
        modifier = if (errorEnabled) {
            modifier
        } else {
            modifier.padding(bottom = 12.dp)
        }
            .clip(ComponentShape.small),
        label = { Text(text = labelValue) },
        colors = colors
            ?: OutlinedTextFieldDefaults.colors(
                disabledTextColor = LocalContentColor.current.copy(LocalContentAlpha.current),
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
                cursorColor = Purple80,
                focusedBorderColor = Purple80,
                disabledBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = ContentAlpha.medium),
                disabledLeadingIconColor = MaterialTheme.colorScheme.onSurface.copy(alpha = ContentAlpha.medium),
                focusedLabelColor = Purple80,
                disabledLabelColor = MaterialTheme.colorScheme.onSurface.copy(ContentAlpha.medium),
            ),
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType,
            imeAction = if (keyboardType == KeyboardType.Text && singleLine && imeAction == ImeAction.None) {
                ImeAction.Next
            } else {
                imeAction
            }
        ),
        keyboardActions = KeyboardActions(
            onDone = onActionKey,
            onGo = onActionKey,
            onNext = onActionKey,
            onPrevious = onActionKey,
            onSend = onActionKey,
            onSearch = onActionKey,
        ),
        value = value,
        onValueChange = { text ->
//            pattern?.let {
//                isError.value = it.isNotEmpty() && !text.matches(Regex(it))
//            }
            valueChanged = text.trim().isNotEmpty()
            value = text
            onValueChanged(text)
        },
        leadingIcon = {
            startIcon?.let {
                Icon(
                    tint = iconTint ?: LocalContentColor.current,
                    imageVector = it,
                    contentDescription = "",
                    modifier = Modifier.clickable {
                        iconAction()
                    }
                )
            }
        },
        trailingIcon = {
            endIcon?.let {
                Icon(
                    tint = iconTint ?: LocalContentColor.current,
                    imageVector = it,
                    contentDescription = "",
                    modifier = Modifier.clickable {
                        iconAction()
                    }
                )
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmailFieldComponent(
    modifier: Modifier = Modifier,
    labelValue: String,
    textValue: String,
    onValueChanged: (String) -> Unit
) {
    var value by remember { mutableStateOf(textValue) }

    TextFieldComponent(
        modifier = modifier,
        labelValue = labelValue,
        onValueChanged = {
            value = it
            onValueChanged(it)
        },
        textValue = value,
        errorEnabled = true,
        errorMessage = stringResource(R.string.this_doesn_t_seems_to_match_an_email),
        pattern = Patterns.EMAIL_ADDRESS.pattern(),
        singleLine = true,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = TertiaryDark,
            focusedLabelColor = TertiaryDark,
            cursorColor = TertiaryDark,
        ),
        keyboardType = KeyboardType.Email,
        startIcon = Icons.Filled.Email
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordFieldComponent(
    labelValue: String,
    password: String,
    onValueChanged: (String) -> Unit,
    login: Boolean = true
) {
    var passwordVisible by remember {
        mutableStateOf(false)
    }
    var isError by remember {
        mutableStateOf(false)
    }

    OutlinedTextField(
        supportingText = {
            if (isError && !login) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(R.string.password_must_be_),
                    color = MaterialTheme.colorScheme.error
                )
            }
        },
        singleLine = true,
        modifier = Modifier
            .fillMaxWidth()
            .clip(ComponentShape.small),
        label = { Text(text = labelValue) },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = Purple80,
            focusedLabelColor = Purple80,
            cursorColor = Purple80,

            ),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        value = password,
        onValueChange = {
            isError = !it.isValidPassword(enforce = !login)
            onValueChanged(it)
        },
        leadingIcon = {
            Icon(imageVector = Icons.Filled.Password, contentDescription = "")
        },
        trailingIcon = {
            val iconImage = if (passwordVisible) {
                Icons.Filled.Visibility
            } else {
                Icons.Filled.VisibilityOff
            }
            var description = if (passwordVisible) {
                stringResource(id = R.string.hide_password)
            } else {
                stringResource(id = R.string.show_password)
            }
            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                Icon(imageVector = iconImage, contentDescription = description)
            }
        },
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation()
    )
}

@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    onSearch: (String) -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    var expanded by remember { mutableStateOf(true) }
    var textValue by remember { mutableStateOf("") }

    val density = LocalDensity.current
    var size by remember { mutableStateOf(Size.Zero) }
    val width = animateDpAsState(
        targetValue = if (expanded) with(density) { size.width.toDp() } else 48.dp,
        animationSpec = tween(
            durationMillis = 250,
        ), label = ""
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .animateContentSize(animationSpec = tween(250))
    ) {
        TextFieldComponent(
            imeAction = ImeAction.Search,
            onActionKey = {
                onSearch.invoke(textValue)
                keyboardController?.hide()
            },
            shape = RoundedCornerShape(25.dp),
            modifier = if (size == Size.Zero) {
                Modifier
                    .fillMaxWidth()
                    .onGloballyPositioned { coordinates ->
                        size = coordinates.size.toSize()
                    }
            } else {
                if (!expanded) {
                    Modifier.height(with(density) { size.height.toDp() })
                } else {
                    Modifier
                }
                    .width(width.value)
            },
            singleLine = true,
            textValue = textValue,
            labelValue = if (expanded) stringResource(id = R.string.search) else "",
            onValueChanged = {
                textValue = it
            },
            enabled = expanded,
            errorEnabled = false,
            startIcon = if (expanded) Icons.Filled.Close else Icons.Filled.Search,
            iconAction = {
                if (expanded) {
                    if (textValue.isNotEmpty()) {
                        textValue = ""
                    } else {
                        expanded = false
                    }
                } else {
                    expanded = true
                }
            }
        )
    }
}

enum class Orientation {
    VERTICAL,
    HORIZONTAL
}

@Composable
fun <T> RadioGroupComponent(
    modifier: Modifier = Modifier,
    orientation: Orientation = Orientation.VERTICAL,
    enabled: Boolean = true,
    label: String? = null,
    items: List<T>,
    selectedIndex: Int = -1,
    onItemSelected: (index: Int, item: T) -> Unit,
    selectedItemToString: (T) -> String = { it.toString() },
    drawItem: @Composable (Modifier, String, Boolean, Boolean, () -> Unit) -> Unit = { _modifier, label, selected, _enabled, onClick ->
        RadioButtonComponent(
            modifier = _modifier,
            label = label,
            selected = selected,
            enabled = _enabled,
            onClick = onClick
        )
    }
) {
    var selectedItemIndex by remember { mutableIntStateOf(selectedIndex) }
    if (orientation == Orientation.VERTICAL) {
        Column(modifier = modifier) {
            label?.let {
                Text(
                    text = it,
                    modifier = Modifier.padding(top = 4.dp, start = 8.dp)
                )
            }
//            LazyHorizontalGrid(rows = GridCells.Fixed(items.size)) {
//                itemsIndexed(items) { position, item ->
//                    drawItem(
//                        Modifier,
//                        selectedItemToString.invoke(item),
//                        position == selectedItemIndex,
//                        enabled
//                    ) {
//                        if (enabled) {
//                            selectedItemIndex = position
//                            onItemSelected(position, item)
//                        }
//                    }
//                }
//            }
            for (i in items.indices) {
                drawItem(
                    Modifier,
                    selectedItemToString.invoke(items[i]),
                    i == selectedItemIndex,
                    enabled
                ) {
                    if (enabled) {
                        selectedItemIndex = i
                        onItemSelected(i, items[i])
                    }
                }
            }
        }
    } else {
        Column(modifier = modifier) {
            label?.let {
                Text(
                    text = it,
                    modifier = Modifier.padding(top = 4.dp, start = 8.dp)
                )
            }

            LazyVerticalGrid(columns = GridCells.Fixed(3)) {
                itemsIndexed(items) { position, item ->
                    drawItem(
                        Modifier.padding(4.dp),
                        selectedItemToString.invoke(item),
                        position == selectedItemIndex,
                        enabled
                    ) {
                        if (enabled) {
                            selectedItemIndex = position
                            onItemSelected(position, item)
                        }
                    }
                }
            }

//            Row {
//                for (i in items.indices) {
//                    drawItem(
//                        Modifier.weight(1f),
//                        selectedItemToString.invoke(items[i]),
//                        i == selectedItemIndex,
//                        enabled
//                    ) {
//                        if (enabled) {
//                            selectedItemIndex = i
//                            onItemSelected(i, items[i])
//                        }
//                    }
//                }
//            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun RadioButtonComponent(
    modifier: Modifier = Modifier,
    label: String,
    enabled: Boolean = true,
    selected: Boolean,
    onClick: () -> Unit
) {
//    var itemSelected by remember { mutableStateOf(selected) }
    val icon =
        if (selected) Icons.Filled.RadioButtonChecked else Icons.Filled.RadioButtonUnchecked

    CompositionLocalProvider(LocalMinimumInteractiveComponentEnforcement provides selected) {
        TextFieldComponent(
            errorEnabled = false,
            colors = OutlinedTextFieldDefaults.colors(
                disabledTextColor = LocalContentColor.current.copy(LocalContentAlpha.current),
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
                cursorColor = Purple80,
                focusedBorderColor = Color.Transparent,
                disabledBorderColor = Color.Transparent,
                disabledLeadingIconColor = MaterialTheme.colorScheme.onSurface.copy(alpha = ContentAlpha.high),
                focusedLabelColor = Purple80,
                disabledLabelColor = MaterialTheme.colorScheme.onSurface.copy(ContentAlpha.high),
            ),
            labelValue = label,
//        textValue = null,
            onValueChanged = {},
            startIcon = icon,
            iconAction = {
                if (enabled) {
                    onClick()
//                if (!itemSelected) {
////                    itemSelected = !itemSelected
//                    onCheckedChanged(itemSelected)
//                }
                }
            },
            enabled = false,
            singleLine = true,
            modifier = if (enabled) {
                modifier.clickable {
                    onClick()
//                if (!itemSelected) {
////                    itemSelected = !itemSelected
//                    onCheckedChanged(itemSelected)
//                }
                }
            } else {
                modifier
            }
                .fillMaxWidth()
                .clip(ComponentShape.small)
                .focusable(false),
        )
    }
}