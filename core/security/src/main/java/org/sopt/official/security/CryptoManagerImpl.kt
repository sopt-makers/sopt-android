package org.sopt.official.security

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec
import javax.inject.Inject

class CryptoManagerImpl @Inject constructor() : CryptoManager {
    private val keyStore = KeyStore.getInstance(KEY_STORE_TYPE).apply { load(null) }

    private val keyGenerator by lazy { KeyGenerator.getInstance(KEY_ALGORITHM, KEY_STORE_TYPE) }

    private val cipher by lazy { Cipher.getInstance(TRANSFORMATION) }

    private fun getDecryptCipherForIv(keyAlias: String, iv: ByteArray): Cipher =
        Cipher.getInstance(TRANSFORMATION)
            .apply { init(Cipher.DECRYPT_MODE, getOrCreateSecretKey(keyAlias = keyAlias), IvParameterSpec(iv)) }

    private fun getOrCreateSecretKey(keyAlias: String): SecretKey =
        (keyStore.getEntry(keyAlias, null) as? KeyStore.SecretKeyEntry)?.secretKey ?: createSecretKey(keyAlias = keyAlias)

    private fun createSecretKey(keyAlias: String): SecretKey = keyGenerator.apply {
        init(
            KeyGenParameterSpec.Builder(keyAlias, KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT)
                .setBlockModes(BLOCK_MODE)
                .setEncryptionPaddings(PADDING)
                .build()
        )
    }.generateKey()

    override fun encrypt(keyAlias: String, bytes: ByteArray): Pair<ByteArray, ByteArray> {
        val secretKey = getOrCreateSecretKey(keyAlias = keyAlias)
        cipher.init(Cipher.ENCRYPT_MODE, secretKey)
        return Pair(cipher.iv, cipher.doFinal(bytes))
    }

    override fun decrypt(keyAlias: String, iv: ByteArray, encryptedBytes: ByteArray): ByteArray =
        getDecryptCipherForIv(keyAlias = keyAlias, iv = iv).doFinal(encryptedBytes)

    companion object {
        private const val KEY_STORE_TYPE = "AndroidKeyStore"
        private const val KEY_ALGORITHM = KeyProperties.KEY_ALGORITHM_AES
        private const val BLOCK_MODE = KeyProperties.BLOCK_MODE_GCM
        private const val PADDING = KeyProperties.ENCRYPTION_PADDING_PKCS7
        private const val TRANSFORMATION = "$KEY_ALGORITHM/$BLOCK_MODE/$PADDING"
    }
}