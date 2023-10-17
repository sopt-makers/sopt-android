/*
 * MIT License
 * Copyright 2023 SOPT - Shout Our Passion Together
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
package org.sopt.official.datastore.fake

import java.io.InputStream
import java.io.OutputStream
import java.security.Key
import java.security.KeyStore
import java.security.KeyStoreSpi
import java.security.cert.Certificate
import java.util.Date
import java.util.Enumeration

class FakeKeyStore : KeyStoreSpi() {
    private val wrapped = KeyStore.getInstance(KeyStore.getDefaultType())

    override fun engineIsKeyEntry(alias: String?): Boolean = wrapped.isKeyEntry(alias)
    override fun engineIsCertificateEntry(alias: String?): Boolean = wrapped.isCertificateEntry(alias)
    override fun engineGetCertificate(alias: String?): Certificate = wrapped.getCertificate(alias)
    override fun engineGetCreationDate(alias: String?): Date = wrapped.getCreationDate(alias)
    override fun engineDeleteEntry(alias: String?) = wrapped.deleteEntry(alias)
    override fun engineSetKeyEntry(
        alias: String?,
        key: Key?,
        password: CharArray?,
        chain: Array<out Certificate>?
    ) = wrapped.setKeyEntry(alias, key, password, chain)

    override fun engineSetKeyEntry(
        alias: String?,
        key: ByteArray?,
        chain: Array<out Certificate>?
    ) = wrapped.setKeyEntry(alias, key, chain)

    override fun engineStore(stream: OutputStream?, password: CharArray?) = wrapped.store(stream, password)
    override fun engineSize(): Int = wrapped.size()
    override fun engineAliases(): Enumeration<String> = wrapped.aliases()
    override fun engineContainsAlias(alias: String?): Boolean = wrapped.containsAlias(alias)
    override fun engineLoad(stream: InputStream?, password: CharArray?) = wrapped.load(stream, password)
    override fun engineGetCertificateChain(alias: String?): Array<Certificate> = wrapped.getCertificateChain(alias)
    override fun engineSetCertificateEntry(alias: String?, cert: Certificate?) = wrapped.setCertificateEntry(alias, cert)
    override fun engineGetCertificateAlias(cert: Certificate?): String = wrapped.getCertificateAlias(cert)
    override fun engineGetKey(alias: String?, password: CharArray?): Key = wrapped.getKey(alias, password)
}
