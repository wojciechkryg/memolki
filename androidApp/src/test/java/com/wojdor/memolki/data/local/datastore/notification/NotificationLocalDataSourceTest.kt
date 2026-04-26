package com.wojdor.memolki.data.local.datastore.notification

import com.wojdor.memolki.test.AppTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.BeforeTest
import kotlin.test.Test
import org.koin.test.inject

@ExperimentalCoroutinesApi
class NotificationLocalDataSourceTest : AppTest() {

    private val sut: NotificationLocalDataSource by inject()

    @BeforeTest
    override fun setup() {
        super.setup()
    }

    @Test
    fun `initially encrypted last shown timestamp is null`() = runTest {
        // when
        val result = sut.encryptedLastShownTimestamp.first()

        // then
        assertNull(result)
    }

    @Test
    fun `when timestamp is set then it can be read`() = runTest {
        // when
        sut.setEncryptedLastShownTimestamp("encrypted_123")

        // then
        val result = sut.encryptedLastShownTimestamp.first()
        assertEquals("encrypted_123", result)
    }

    @Test
    fun `when timestamp is updated then new value is read`() = runTest {
        // given
        sut.setEncryptedLastShownTimestamp("old_value")

        // when
        sut.setEncryptedLastShownTimestamp("new_value")

        // then
        val result = sut.encryptedLastShownTimestamp.first()
        assertEquals("new_value", result)
    }

    @Test
    fun `initially encrypted next daily challenge notification timestamp is null`() = runTest {
        // when
        val result = sut.encryptedNextDailyChallengeNotificationTimestamp.first()

        // then
        assertNull(result)
    }

    @Test
    fun `when next daily challenge notification timestamp is set then it can be read`() = runTest {
        // when
        sut.setEncryptedNextDailyChallengeNotificationTimestamp("encrypted_456")

        // then
        val result = sut.encryptedNextDailyChallengeNotificationTimestamp.first()
        assertEquals("encrypted_456", result)
    }
}
