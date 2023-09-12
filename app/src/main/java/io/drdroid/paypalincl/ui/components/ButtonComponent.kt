package io.drdroid.paypalincl.ui.components

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.PermCameraMic
import androidx.compose.material.icons.filled.PermMedia
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.google.android.gms.common.SignInButton
import com.paypal.checkout.approve.OnApprove
import com.paypal.checkout.cancel.OnCancel
import com.paypal.checkout.createorder.CreateOrder
import com.paypal.checkout.createorder.OrderIntent
import com.paypal.checkout.createorder.UserAction
import com.paypal.checkout.error.OnError
import com.paypal.checkout.order.AppContext
import com.paypal.checkout.order.OrderRequest
import com.paypal.checkout.order.PurchaseUnit
import com.paypal.checkout.paymentbutton.PaymentButtonContainer
import com.paypal.checkout.paymentbutton.PaymentButtonShape
import com.paypal.checkout.paymentbutton.PaymentButtonSize
import io.drdroid.paypalincl.R
import io.drdroid.paypalincl.ui.theme.PrimaryDark
import io.drdroid.paypalincl.utils.Common
import io.drdroid.paypalincl.utils.CustomFileProvider
import io.drdroid.paypalincl.utils.Utils
import io.drdroid.paypalincl.utils.Utils.RequestMultiplePermissions

@Composable
fun ButtonComponent(
    modifier: Modifier = Modifier,
    text: String = "Button",
    colors: ButtonColors? = null,
    textColor: Color = Color.White,
    onClick: () -> Unit
) {
    OutlinedButton(
        modifier = modifier
            .padding(top = 8.dp, bottom = 8.dp)
            .fillMaxWidth()
            .height(48.dp),
        colors = colors
            ?: ButtonDefaults.outlinedButtonColors(
                containerColor = PrimaryDark
            ),
        shape = RoundedCornerShape(5.dp),
        onClick = onClick
    ) {
        Text(
            text = text,
            color = textColor,
            fontSize = TextUnit(20f, TextUnitType.Sp),
            letterSpacing = TextUnit(2f, TextUnitType.Sp),
            fontWeight = FontWeight(500)
        )
    }
}

@Composable
fun SelectImageButton(
    profile: Boolean = true,
    modifier: Modifier = Modifier,
    context: Context,
    onImageSelected: (Uri) -> Unit
) {
    var imageUri by remember {
        mutableStateOf<Uri?>(null)
    }

    val launcher = rememberLauncherForActivityResult(
        contract =
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
//        imageUri = uri
        //do something with imageUri
        uri?.let { onImageSelected(it) }
    }

    val cameraLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicture(), onResult = {
//            hasImage = it
            if (it) {
                imageUri?.let { uri ->
                    onImageSelected(uri)
                }
            }
        })


    //<editor-fold desc="PERMISSION">
    var displayCameraDialog by remember { mutableStateOf(false) }
    var requestCameraPermission by remember {
        mutableStateOf(false)
    }
    if (requestCameraPermission) {
        RequestMultiplePermissions(
            context = context,
            permissions = listOf(Common.PERMISSIONS.PERMISSION_CAMERA)
        ) { granted ->
            if (granted) {
                try {
                    val uri = CustomFileProvider.getImageUri(context)
                    imageUri = uri
                    cameraLauncher.launch(uri)
                } catch (e: Exception) {
                    e.printStackTrace()
                    Toast.makeText(
                        context,
                        context.getString(R.string.couldn_t_find_a_camera_application),
                        Toast.LENGTH_SHORT
                    ).show()
                }

            } else {
                Toast.makeText(
                    context,
                    context.getString(R.string.permission_not_granted),
                    Toast.LENGTH_SHORT
                ).show()
            }
            requestCameraPermission = false
        }
    }
    if (displayCameraDialog) {
        CustomDialog(
            cancellable = false,
            title = stringResource(R.string.camera_permission_needed),
            message = stringResource(R.string.camera_permission_message),
            icon = Icons.Filled.PermCameraMic,
            positiveText = stringResource(id = R.string.grant),
            negativeText = stringResource(id = R.string.not_now),
            onPositiveClick = {
                requestCameraPermission = true
            },
            onNegativeClick = {},
            onDismiss = {
                displayCameraDialog = false
            }
        )
    }


    var displayImagePickerDialog by remember { mutableStateOf(false) }
    var requestMediaPermission by remember {
        mutableStateOf(false)
    }
    if (displayImagePickerDialog) {
        CustomDialog(
            cancellable = false,
            title = stringResource(R.string.media_access_permission_needed),
            message = stringResource(R.string.feature_requires_access_to_media),
            icon = Icons.Filled.PermMedia,
            positiveText = stringResource(R.string.grant),
            negativeText = stringResource(R.string.not_now),
            onPositiveClick = {
                requestMediaPermission = true
            },
            onNegativeClick = {},
            onDismiss = {
                displayImagePickerDialog = false
            }
        )
    }
    if (requestMediaPermission) {
        RequestMultiplePermissions(
            context = context,
            permissions = Common.PERMISSIONS.PERMISSION_STORAGE.toList()
        ) { granted ->
            if (granted) {
                launcher.launch("image/*")
            } else {
                Toast.makeText(
                    context,
                    context.getString(R.string.permission_not_granted), Toast.LENGTH_SHORT
                ).show()
            }
            requestMediaPermission = false
        }
    }


    var displayDialog by remember { mutableStateOf(false) }
    if (displayDialog) {
        CustomDialog(
            icon = if (profile) {
                Icons.Filled.Person
            } else {
                Icons.Filled.Folder
            },
            title = if (profile) {
                stringResource(R.string.update_profile_picture)
            } else {
                stringResource(id = R.string.update_display_picture)
            },
            message = if (profile) {
                stringResource(R.string.update_profile_picture_from_gallery_or_using_camera)
            } else {
                stringResource(id = R.string.update_display_picture_from_gallery_or_using_camera)
            },
            cancellable = true,
            positiveText = stringResource(R.string.camera),
            negativeText = stringResource(R.string.gallery),
            onPositiveClick = {
                if (!Utils.hasPermissions(
                        context,
                        arrayOf(Common.PERMISSIONS.PERMISSION_CAMERA)
                    )
                ) {
                    //display camera dialog
                    displayCameraDialog = true
                } else {
                    //open camera
                    try {
                        val uri = CustomFileProvider.getImageUri(context)
                        imageUri = uri
                        cameraLauncher.launch(uri)
                    } catch (e: Exception) {
                        e.printStackTrace()
                        Toast.makeText(
                            context,
                            context.getString(R.string.couldn_t_find_a_camera_application),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            },
            onNegativeClick = {
                if (!Utils.hasPermissions(context, Common.PERMISSIONS.PERMISSION_STORAGE)) {
                    //display media access dialog
                    displayImagePickerDialog = true
                } else {
                    //image picker
                    launcher.launch("image/*")
                }
            },
            onDismiss = {
                displayDialog = false
            }
        )
    }
    //</editor-fold>

    OutlinedButton(
        colors = ButtonDefaults.outlinedButtonColors(
//            containerColor = Color.White,
//            disabledContainerColor = Color.White,
            contentColor = PrimaryDark,
            disabledContentColor = PrimaryDark,
        ),
        shape = RoundedCornerShape(10.dp),
        onClick = {
            displayDialog = true
        },
        modifier = modifier
//            .clip(RoundedCornerShape(100.dp))
            .background(color = Color.Transparent)
            .padding(start = 100.dp)
    ) {
        Icon(
            imageVector = Icons.Filled.Edit,
            contentDescription = "",
            modifier = Modifier
                .background(color = Color.Transparent)
                .clip(shape = CircleShape)
//                        .size(32.dp)
        )
    }
}

@Composable
fun GoogleButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    AndroidView(
        modifier = modifier,
        factory = { context ->
            val btn = SignInButton(context)
            btn.contentDescription = "Login with Google"
            btn.setOnClickListener {
                onClick.invoke()
            }
            btn
        }
    )
}

@Composable
fun PaypalButton(
    amounts: List<PurchaseUnit>,
    modifier: Modifier = Modifier,
    onApprove: OnApprove? = null,
    onCancel: OnCancel? = null,
    onError: OnError? = null
) {
    AndroidView(
        modifier = modifier,
        factory = {
//            val btn = PayPalButton(it)
//            btn.shape = PaymentButtonShape.ROUNDED
//            btn.size = PaymentButtonSize.LARGE
//            btn.isEnabled = true
//            btn.setOnClickListener {
//                onClick.invoke()
//            }
//            btn
            val btn = PaymentButtonContainer(it)
            btn.paypalButtonShape = PaymentButtonShape.ROUNDED
            btn.paypalButtonSize = PaymentButtonSize.LARGE
            btn.paypalButtonEnabled = true
//            btn.setOnClickListener {
//                onClick.invoke()
//            }
            btn.setup(
                createOrder =
                CreateOrder { createOrderActions ->
                    val order =
                        OrderRequest(
                            intent = OrderIntent.CAPTURE,
                            appContext = AppContext(userAction = UserAction.PAY_NOW),
                            purchaseUnitList = amounts
                        )
                    createOrderActions.create(order)
                },
                onApprove = onApprove,
                onCancel = onCancel,
                onError = onError
            )
            btn
        }
    )
}

@Composable
fun FaceBookButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    onResult: (token: AccessToken?) -> Unit
) {
    Surface(
        modifier = modifier,
        onClick = onClick,
        shape = ComponentShape.large,
        border = BorderStroke(width = 1.dp, color = Color.Gray),
        color = Color.White
    ) {
        WrappedFacebookLoginButton(onResult)
    }
}

@Composable
fun WrappedFacebookLoginButton(
    onResult: (token: AccessToken?) -> Unit
) {
    AndroidView(
        factory = { context ->
            val btn = LoginButton(context)
            btn.contentDescription = "Login with Facebook"
            btn.registerCallback(
                CallbackManager.Factory.create(),
                object : FacebookCallback<LoginResult> {
                    override fun onSuccess(result: LoginResult) {
//                        Log.d(TAG, "facebook:onSuccess:$result")
                        onResult(result.accessToken)
                    }

                    override fun onCancel() {
//                        Log.d(TAG, "facebook:onCancel")
                    }

                    override fun onError(error: FacebookException) {
//                        Log.d(TAG, "facebook:onError", error)
//                        fLogin.shake()
                    }
                }
            )
            btn
        }
    )
}