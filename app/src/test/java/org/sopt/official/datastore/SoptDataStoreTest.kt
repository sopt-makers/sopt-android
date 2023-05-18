package org.sopt.official.datastore

import androidx.security.crypto.MasterKey
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import org.junit.BeforeClass
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config
import org.sopt.official.data.persistence.SoptDataStore
import org.sopt.official.datastore.fake.FakeAndroidKeyStore

@RunWith(RobolectricTestRunner::class)
@HiltAndroidTest
@Config(manifest = Config.NONE, sdk = [30, 32], application = HiltTestApplication::class)
class SoptDataStoreTest {
    private val context = RuntimeEnvironment.getApplication().applicationContext

    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val dataStore = SoptDataStore(context, "testPreference", masterKey)

    @Test
    fun `key-value 구조의 데이터를 입력했을 때 저장이 된다`() {
        // given
        val value = "sample"

        // when
        dataStore.accessToken = value

        // then
        assertThat(dataStore.accessToken).isEqualTo(value)
    }

    companion object {
        @JvmStatic
        @BeforeClass
        fun beforeClass() {
            FakeAndroidKeyStore.setUp
        }
    }
}
