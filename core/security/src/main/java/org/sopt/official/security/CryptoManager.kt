package org.sopt.official.security

interface CryptoManager {
    fun encrypt(keyAlias: String, bytes: ByteArray): Pair<ByteArray, ByteArray>
    fun decrypt(keyAlias: String, iv: ByteArray, encryptedBytes: ByteArray): ByteArray
}