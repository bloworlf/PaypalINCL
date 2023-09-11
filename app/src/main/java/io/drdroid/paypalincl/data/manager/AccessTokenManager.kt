package io.drdroid.paypalincl.data.manager

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import com.google.gson.Gson
import io.drdroid.paypalincl.data.model.auth.AccessTokenModel
import java.lang.NullPointerException
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec
import kotlin.jvm.Throws

class AccessTokenManager() {

    private val keyStore: KeyStore = KeyStore.getInstance("AndroidKeyStore")

    init {
        keyStore.load(null)
    }

    private val keyAlias = "AccessTokenKey"

//    fun saveAccessToken(accessToken: AccessTokenModel) {
//        // Generate a secret key for encryption
//        val secretKey = generateSecretKey()
//
//        // Encrypt the access token
//        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
//        cipher.init(Cipher.ENCRYPT_MODE, secretKey)
//        val encryptedToken = cipher.doFinal(Gson().toJson(accessToken).toByteArray())
//
//        // Save the encrypted token in the Keystore
//        val keyEntry = KeyStore.SecretKeyEntry(secretKey)
//        keyStore.setEntry(keyAlias, keyEntry, null)
//    }

    @Throws(NullPointerException::class)
    fun saveAccessToken(accessToken: AccessTokenModel?) {
        if (accessToken == null) {
            throw NullPointerException("AccessTokenModel is null")
        }
        // Generate a secret key for encryption
        val secretKey = generateSecretKey()

        // Encrypt the access token
        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        cipher.init(Cipher.ENCRYPT_MODE, secretKey)
        val encryptedTokenBytes = cipher.doFinal(Gson().toJson(accessToken).toByteArray())

        // Save the encrypted token in the Keystore
        val encryptedToken = String(encryptedTokenBytes)
        val keyEntry = KeyStore.SecretKeyEntry(SecretKeySpec(encryptedToken.toByteArray(), "AES"))
        keyStore.setEntry(keyAlias, keyEntry, null)
    }

    fun retrieveAccessToken(): AccessTokenModel? {
        val keyEntry = keyStore.getEntry(keyAlias, null) as KeyStore.SecretKeyEntry
        val secretKey = keyEntry.secretKey

        // Retrieve the encrypted token from Keystore
        val encryptedTokenBytes = secretKey.encoded

        // Decrypt the access token
        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        cipher.init(Cipher.DECRYPT_MODE, secretKey)
        val decryptedToken = cipher.doFinal(encryptedTokenBytes)

        return Gson().fromJson(String(decryptedToken), AccessTokenModel::class.java)
    }

    private fun generateSecretKey(): SecretKey {
        val keyGenerator =
            KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore")
        val keyGenParameterSpec = KeyGenParameterSpec.Builder(
            keyAlias,
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
        )
            .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
            .setKeySize(256)
            .build()

        keyGenerator.init(keyGenParameterSpec)
        return keyGenerator.generateKey()
    }
}