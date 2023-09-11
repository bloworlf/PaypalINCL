package io.drdroid.paypalincl.ui.pages.donate

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.paypal.checkout.approve.OnApprove
import com.paypal.checkout.cancel.OnCancel
import com.paypal.checkout.createorder.CurrencyCode
import com.paypal.checkout.error.OnError
import com.paypal.checkout.order.Amount
import com.paypal.checkout.order.PurchaseUnit
import io.drdroid.paypalincl.R
import io.drdroid.paypalincl.ui.components.ButtonComponent
import io.drdroid.paypalincl.ui.components.Orientation
import io.drdroid.paypalincl.ui.components.PaypalButton
import io.drdroid.paypalincl.ui.components.RadioGroupComponent
import io.drdroid.paypalincl.ui.navigation.AppNavigationActions
import io.drdroid.paypalincl.ui.pages.settings.OptionsItem
import io.drdroid.paypalincl.ui.theme.PrimaryDark
import io.drdroid.paypalincl.utils.Utils.isDark

@Composable
fun DonateScreen(
//    navActions: AppNavigationActions
) {

    val context = LocalContext.current

    val recurent = listOf(
        "One Time",
        "Monthly"
    )

    val amountList = listOf(
        10,
        20,
        30,
        40,
        50,
        60
    )
    var selectedAmount by remember { mutableIntStateOf(-1) }

//    val amounts: MutableList<PurchaseUnit> = mutableListOf()
//    var amount by remember {
//        mutableStateOf(
//            PurchaseUnit(
//                amount = Amount(
//                    currencyCode = CurrencyCode.USD,
//                    value = try {
//                        amountList[selectedAmount]
//                    } catch (e: Exception) {
//                        0
//                    }.toString()
//                )
//            )
//        )
//    }

    var ready by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.donate_now),
            style = MaterialTheme.typography.displayMedium
        )

        OptionsItem(
            title = stringResource(R.string.donation_frequency),
            subTitle = stringResource(R.string.choose_donation_frequency),
            icon = Icons.Filled.AttachMoney,
            content = { paddingValues ->
                var selectedIndex by remember {
                    mutableIntStateOf(0)
                }

                RadioGroupComponent(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(paddingValues),
                    items = recurent,
                    selectedIndex = selectedIndex,
                    selectedItemToString = { it },
                    onItemSelected = { index, item ->
                        if (selectedIndex != index) {
                            selectedIndex = index
                        }
                    }
                )
            }
        )

        Spacer(modifier = Modifier.height(8.dp))

        OptionsItem(
            title = stringResource(R.string.amount),
            subTitle = stringResource(
                R.string.choose_the_amount_to_pay_usd,
                try {
                    amountList[selectedAmount]
                } catch (e: Exception) {
                    0
                }
            ),
            icon = Icons.Filled.AttachMoney,
            content = { paddingValues ->
//                var selectedIndex by remember {
//                    mutableIntStateOf(0)
//                }

                RadioGroupComponent(
                    orientation = Orientation.HORIZONTAL,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(paddingValues),
                    items = amountList,
                    selectedIndex = selectedAmount,
                    selectedItemToString = { "$$it USD" },
                    onItemSelected = { index, item ->
                        if (selectedAmount != index) {
                            selectedAmount = index
                        }
                    },
                    drawItem = { modifier, label, selected, enabled, onClick ->
                        CardAmount(
                            modifier = modifier,
                            label = label,
                            selected = selected,
                            enabled = enabled,
                            onClick = onClick
                        )
                    }
                )
            }
        )

        Spacer(modifier = Modifier.height(8.dp))

        if (ready) {
            Spacer(modifier = Modifier.height(8.dp))
            val amount = PurchaseUnit(
                amount = Amount(
                    currencyCode = CurrencyCode.USD,
                    value = try {
                        amountList[selectedAmount]
                    } catch (e: Exception) {
                        0
                    }.toString()
                )
            )
            PaypalButton(
                amounts = listOf(amount),
                onApprove = OnApprove {
//                    navActions.navigateBack()
                    Toast.makeText(
                        context,
                        context.getString(R.string.thank_you),
                        Toast.LENGTH_SHORT
                    ).show()
                    ready = false
                },
                onCancel = OnCancel {
                    Toast.makeText(
                        context,
                        context.getString(R.string.operation_canceled), Toast.LENGTH_SHORT
                    ).show()
                    ready = false
                },
                onError = OnError {
                    Toast.makeText(
                        context,
                        context.getString(R.string.an_error_occurred), Toast.LENGTH_SHORT
                    ).show()
                    ready = false
                }
            )
        } else {
            ButtonComponent(
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFDFA72),
                    contentColor = Color.Black
                ),
                textColor = Color.Black,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                text = stringResource(R.string.ready),
                onClick = {
                    if (selectedAmount < 0) {
                        Toast.makeText(
                            context,
                            context.getString(R.string.please_select_an_amount), Toast.LENGTH_SHORT
                        )
                            .show()
                        return@ButtonComponent
                    }
                    ready = true
                }
            )
        }
    }
}

@Composable
fun CardAmount(
    modifier: Modifier = Modifier,
    label: String,
    selected: Boolean,
    enabled: Boolean,
    onClick: () -> Unit,
) {
    val color = if (selected) PrimaryDark else Color.White
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = color
        ),
        border = BorderStroke(width = 2.dp, color = Color.Gray)
    ) {

        Box(
            modifier = if (enabled) {
                Modifier.clickable { onClick.invoke() }
            } else {
                Modifier
            }
                .aspectRatio(16 / 9f)
                .height(92.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                color = if (color.isDark()) Color.White else Color.Black,
                text = label,
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}
