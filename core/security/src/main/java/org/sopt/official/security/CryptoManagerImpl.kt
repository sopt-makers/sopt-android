/*
 * MIT License
 * Copyright 2024 SOPT - Shout Our Passion Together
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.sopt.official.security

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec
import javax.inject.Inject

class CryptoManagerImpl @Inject constructor() : CryptoManager {
    private val keyStore = KeyStore.getInstance(KEY_STORE_TYPE).apply { load(null) }

    private val keyGenerator by lazy { KeyGenerator.getInstance(KEY_ALGORITHM, KEY_STORE_TYPE) }

    private val cipher by lazy { Cipher.getInstance(TRANSFORMATION) }

    private fun getDecryptCipherForIv(keyAlias: String, iv: ByteArray): Cipher =
        Cipher.getInstance(TRANSFORMATION)
            .apply { init(Cipher.DECRYPT_MODE, getOrCreateSecretKey(keyAlias = keyAlias), GCMParameterSpec(T_LEN, iv)) }

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
        private const val PADDING = "NoPadding"
        private const val TRANSFORMATION = "$KEY_ALGORITHM/$BLOCK_MODE/$PADDING"
        private const val T_LEN = 128
    }
}
