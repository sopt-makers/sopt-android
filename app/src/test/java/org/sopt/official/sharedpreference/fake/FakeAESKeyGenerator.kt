package org.sopt.official.sharedpreference.fake

import java.security.SecureRandom
import java.security.spec.AlgorithmParameterSpec
import javax.crypto.KeyGenerator
import javax.crypto.KeyGeneratorSpi
import javax.crypto.SecretKey

class FakeAESKeyGenerator : KeyGeneratorSpi() {
    private val wrapped = KeyGenerator.getInstance("AES")

    override fun engineInit(random: SecureRandom?) = Unit
    override fun engineInit(params: AlgorithmParameterSpec?, random: SecureRandom?) = Unit
    override fun engineInit(keysize: Int, random: SecureRandom?) = Unit
    override fun engineGenerateKey(): SecretKey = wrapped.generateKey()
}
