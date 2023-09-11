package io.drdroid.paypalincl.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import io.drdroid.paypalincl.ui.theme.PrimaryDark

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