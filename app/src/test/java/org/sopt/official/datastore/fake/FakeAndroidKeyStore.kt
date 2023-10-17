/*
 * Copyright 2023 SOPT - Shout Our Passion Together
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.sopt.official.datastore.fake

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
