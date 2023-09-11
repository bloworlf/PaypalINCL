package io.drdroid.paypalincl.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.simform.ssjetpackcomposeprogressbuttonlibrary.SSButtonState
import com.simform.ssjetpackcomposeprogressbuttonlibrary.SSButtonType
import com.simform.ssjetpackcomposeprogressbuttonlibrary.SSJetPackComposeProgressButton
import io.drdroid.paypalincl.R
import io.drdroid.paypalincl.ui.theme.PrimaryDark
import io.drdroid.paypalincl.ui.theme.SecondaryDark

@Composable
fun CustomDialog(
    modifier: Modifier = Modifier,
    title: String? = null,
    message: String? = null,
    icon: ImageVector? = null,
    backgroundColor: Color = Color.White,
    content: @Composable (ColumnScope.() -> Unit)? = null,
    cancellable: Boolean = true,
    positiveText: String? = null,
    onPositiveClick: () -> Unit = {},
    negativeText: String? = null,
    onNegativeClick: () -> Unit = {},
    onDismiss: () -> Unit = {},
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnBackPress = cancellable,
            dismissOnClickOutside = cancellable
        )
    ) {
        Card(
            //shape = MaterialTheme.shapes.medium,
            shape = RoundedCornerShape(25.dp),
            // modifier = modifier.size(280.dp, 240.dp)
            modifier = Modifier.padding(10.dp, 5.dp, 10.dp, 10.dp),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 8.dp
            )
        ) {
            Column(
                modifier
                    .background(backgroundColor)
            ) {

                //.......................................................................
                if (icon != null) {
                    Image(
                        imageVector = icon,
                        contentDescription = null, // decorative
                        contentScale = ContentScale.Fit,
                        colorFilter = ColorFilter.tint(
                            color = PrimaryDark
                        ),
                        modifier = Modifier
                            .padding(top = 35.dp)
                            .height(70.dp)
                            .fillMaxWidth(),

                        )
                }

                content?.let {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceBetween,
                        modifier = if (icon == null) {
                            Modifier.padding(top = 15.dp)
                        } else {
                            Modifier.padding(top = 35.dp)
                        }
                            .fillMaxWidth()
//                            .height(70.dp)
                        ,
                        content = it
                    )
                }

                Column(modifier = Modifier.padding(16.dp)) {
                    if (!title.isNullOrEmpty()) {
                        Text(
                            text = title,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .padding(top = 5.dp)
                                .fillMaxWidth(),
                            style = MaterialTheme.typography.labelLarge,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    if (!message.isNullOrEmpty()) {
                        Text(
                            text = message,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .padding(top = 10.dp, start = 25.dp, end = 25.dp)
                                .fillMaxWidth(),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
                //.......................................................................
                if (positiveText != null && negativeText != null) {
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp)
                            .background(PrimaryDark),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        if (positiveText.isNotEmpty()) {
                            TextButton(onClick = {
                                onNegativeClick()
                                onDismiss()
                            }) {

                                Text(
                                    negativeText,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black,
                                    modifier = Modifier.padding(top = 5.dp, bottom = 5.dp)
                                )
                            }
                        }
                        if (negativeText.isNotEmpty()) {
                            TextButton(onClick = {
                                onPositiveClick()
                                onDismiss()
                            }) {
                                Text(
                                    positiveText,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color.White,
                                    modifier = Modifier.padding(top = 5.dp, bottom = 5.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun WaitDialog() {
    CustomDialog(
        cancellable = false,
        title = stringResource(R.string.please_wait),
        content = {
            SSJetPackComposeProgressButton(
                type = SSButtonType.CLOCK,
                width = 300.dp,
                height = 50.dp,
                onClick = {
                    //Perform action on click of button and make it's state to LOADING
                },
                assetColor = PrimaryDark,
                buttonState = SSButtonState.LOADING
            )
        }
    )
}