package com.wojdor.memolki.data.repository

import com.wojdor.memolki.data.crypto.Encryptor
import com.wojdor.memolki.data.local.datastore.notification.NotificationLocalDataSource
import com.wojdor.memolki.test.AppTest
import com.wojdor.memolki.test.di.TestInjector
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import javax.inject.Inject

@ExperimentalCoroutinesApi
class NotificationRepositoryTest : AppTest() {

    @Inject
    lateinit var notificationLocalDataSource: NotificationLocalDataSource

    @Inject
    lateinit var encryptor: Encryptor

    private lateinit var sut: NotificationRepository

    @Before
    override fun setup() {
        super.setup()
        sut = NotificationRepository(encryptor, notificationLocalDataSource)
    }

    override fun inject(injector: TestInjector) {
        injector.inject(this)
    }

    @Test
    fun `when getLastShownTimestamp with no data then returns default zero`() = runTest {
        // when
        val result = sut.getLastShownTimestamp()

        // then
        assertEquals(0L, result)
    }

    @Test
    fun `when getLastShownTimestamp with stored value then returns decrypted value`() = runTest {
        // given
        val expected = 1234567890L
        notificationLocalDataSource.setEncryptedLastShownTimestamp(encryptor.encrypt(expected))

        // when
        val result = sut.getLastShownTimestamp()

        // then
        assertEquals(expected, result)
    }

    @Test
    fun `when setLastShownTimestamp then value can be retrieved`() = runTest {
        // given
        val timestamp = 9876543210L

        // when
        sut.setLastShownTimestamp(timestamp)

        // then
        val result = sut.getLastShownTimestamp()
        assertEquals(timestamp, result)
    }
}
