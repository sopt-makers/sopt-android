package org.sopt.official.sharedpreference.fake

import java.security.Provider
import java.security.Security

object FakeAndroidKeyStore {
    val setUp by lazy {
        Security.addProvider(
            object : Provider("AndroidKeyStore", 1.0, "") {
                init {
                    put("KeyStore.AndroidKeyStore", FakeKeyStore::class.java.name)
                    put("KeyGenerator.AES", FakeAESKeyGenerator::class.java.name)
                }
            }
        )
    }
}