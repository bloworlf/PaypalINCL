package io.drdroid.paypalincl

import android.app.Application
import com.paypal.checkout.PayPalCheckout
import com.paypal.checkout.config.CheckoutConfig
import com.paypal.checkout.config.Environment
import com.paypal.checkout.config.SettingsConfig
import com.paypal.checkout.createorder.CurrencyCode
import com.paypal.checkout.createorder.UserAction
import dagger.hilt.android.HiltAndroidApp
import io.drdroid.paypalincl.utils.Common
import java.util.Locale

@HiltAndroidApp
class App:Application() {

    companion object {
        lateinit var instance: App
    }

    override fun onCreate() {
        super.onCreate()

        instance = this

        val locale = Locale(
            Common.getPreferences()
                .getString(Common.PREFERENCES.LANGUAGE_KEY, Common.DEFAULT_LANGUAGE)!!
        )
        resources.configuration.locale = locale
        resources.updateConfiguration(resources.configuration, resources.displayMetrics)

        val config = CheckoutConfig(
            application = this,
            clientId = "AaIXyytZPmRFiYJe8w_xgECl4XXrlLkduWFLltM0amc95rYmQXGLPA6rQeB1ccAXmRInjEfOkEb7U_oR",
            environment = Environment.SANDBOX,
            returnUrl = "${BuildConfig.APPLICATION_ID}://paypalpay",
            currencyCode = CurrencyCode.USD,
            userAction = UserAction.PAY_NOW,
            settingsConfig = SettingsConfig(
                loggingEnabled = true
            )
        )
        PayPalCheckout.setConfig(config)
    }
}