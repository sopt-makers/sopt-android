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
