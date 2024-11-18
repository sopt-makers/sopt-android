package org.sopt.official.security.util

import android.util.Base64
import org.sopt.official.security.CryptoManager
import org.sopt.official.security.model.EncryptedContent

fun ByteArray.toEncryptedContent(initializationVectorSize: Int): EncryptedContent =
    EncryptedContent(
        initializationVector = this.copyOfRange(0, initializationVectorSize),
        data = this.copyOfRange(initializationVectorSize, this.size)
    )

fun ByteArray.toBase64(): String = Base64.encodeToString(this, Base64.DEFAULT)

fun String.toByteArray(): ByteArray = Base64.decode(this, Base64.DEFAULT)

fun String.getEncryptedDataOrDefault(keyAlias: String) = CryptoManager.encrypt(
    keyAlias = keyAlias,
    bytes = this.toByteArray(Charsets.UTF_8)
).mapCatching { encryptedContent ->
    encryptedContent.concatenate().toBase64()
}.getOrDefault(this).toString()

fun String.getDecryptedDataOrDefault(keyAlias: String, initializationVectorSize: Int = 12) = CryptoManager.decrypt(
    keyAlias = keyAlias,
    encryptedContent = this.toByteArray().toEncryptedContent(initializationVectorSize = initializationVectorSize)
).mapCatching { decryptedContent ->
    decryptedContent.toString(Charsets.UTF_8)
}.getOrDefault(this).toString()