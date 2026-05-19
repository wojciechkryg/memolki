package com.wojdor.memolki.util.notification

import com.wojdor.memolki.util.provider.PermissionProvider
import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import platform.Foundation.NSCalendar
import platform.Foundation.NSCalendarUnitDay
import platform.Foundation.NSCalendarUnitHour
import platform.Foundation.NSCalendarUnitMinute
import platform.Foundation.NSCalendarUnitMonth
import platform.Foundation.NSCalendarUnitSecond
import platform.Foundation.NSCalendarUnitYear
import platform.Foundation.NSDate
import platform.Foundation.NSDateComponents
import platform.Foundation.dateWithTimeIntervalSince1970
import platform.UserNotifications.UNCalendarNotificationTrigger
import platform.UserNotifications.UNMutableNotificationContent
import platform.UserNotifications.UNNotificationRequest
import platform.UserNotifications.UNNotificationSound
import platform.UserNotifications.UNNotificationTrigger
import platform.UserNotifications.UNTimeIntervalNotificationTrigger
import platform.UserNotifications.UNUserNotificationCenter
import kotlin.random.Random
import kotlin.time.Clock
import kotlin.time.Duration.Companion.days
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalForeignApi::class, BetaInteropApi::class, ExperimentalTime::class)
class IosNotificationScheduler(
    coroutineDispatcher: CoroutineDispatcher,
    private val random: Random,
    private val notificationStrings: NotificationStrings,
    private val permissionProvider: PermissionProvider
) : NotificationScheduler {

    private val center = UNUserNotificationCenter.currentNotificationCenter()
    private val scope = CoroutineScope(coroutineDispatcher + SupervisorJob())

    override fun scheduleReminderNotification() {
        launchSchedule(REMINDER_IDENTIFIER, NotificationCopy.REMINDER) {
            randomTriggerInDays(REMINDER_INTERVAL_DAYS)
        }
    }

    override fun scheduleAdRewardNotification() {
        launchSchedule(AD_REWARD_IDENTIFIER, NotificationCopy.AD_REWARD) {
            UNTimeIntervalNotificationTrigger.triggerWithTimeInterval(
                timeInterval = NotificationScheduler.SHOP_AD_COOLDOWN_MS / MS_PER_SECOND,
                repeats = false
            )
        }
    }

    // Android also gates streak notification on `streak > 0` at fire time; iOS schedules
    // it unconditionally because UNUserNotificationCenter has no fire-time hook without
    // a UNNotificationServiceExtension. Acceptable parity gap — streak rarely drops to
    // zero in the 1-day window before firing.
    override fun scheduleStreakNotification() {
        launchSchedule(STREAK_IDENTIFIER, NotificationCopy.STREAK) {
            randomTriggerInDays(STREAK_INTERVAL_DAYS)
        }
    }

    override fun scheduleDailyChallengeNotification(nextNotificationTimestamp: Long) {
        launchSchedule(DAILY_CHALLENGE_IDENTIFIER, NotificationCopy.DAILY_CHALLENGE) {
            UNCalendarNotificationTrigger.triggerWithDateMatchingComponents(
                dateComponents = dateComponentsFromTimestamp(nextNotificationTimestamp),
                repeats = false
            )
        }
    }

    override fun calculateNextDailyChallengeNotificationTimestamp(): Long {
        val timeZone = TimeZone.currentSystemDefault()
        val tomorrowDate = Clock.System.now()
            .plus(1.days)
            .toLocalDateTime(timeZone)
            .date
        val trigger = LocalDateTime(
            year = tomorrowDate.year,
            month = tomorrowDate.month,
            day = tomorrowDate.day,
            hour = DAILY_CHALLENGE_HOUR,
            minute = 0,
            second = 0
        )
        return trigger.toInstant(timeZone).toEpochMilliseconds()
    }

    override fun cancelAdRewardNotification() {
        center.removePendingNotificationRequestsWithIdentifiers(listOf(AD_REWARD_IDENTIFIER))
    }

    override fun dismissVisibleNotifications() {
        center.removeDeliveredNotificationsWithIdentifiers(ALL_IDENTIFIERS)
    }

    override fun createNotificationChannel() {
        // iOS has no per-channel concept; authorization is requested via NotificationPermissionRequester.
    }

    override fun hasNotificationPermission(): Boolean =
        permissionProvider.hasNotificationPermission()

    private fun launchSchedule(
        identifier: String,
        copy: NotificationCopy,
        triggerBuilder: () -> UNNotificationTrigger
    ) {
        scope.launch {
            val content = randomContent(
                notificationStrings.titles(copy),
                notificationStrings.bodies(copy)
            )
            schedule(identifier, content, triggerBuilder())
        }
    }

    private fun randomContent(
        titles: List<String>,
        bodies: List<String>
    ): UNMutableNotificationContent = UNMutableNotificationContent().apply {
        setTitle(titles.random(random))
        setBody(bodies.random(random))
        setSound(UNNotificationSound.defaultSound)
    }

    private fun randomTriggerInDays(daysFromNow: Int): UNNotificationTrigger {
        val calendar = NSCalendar.currentCalendar
        val now = NSDate()
        val futureDate = calendar.dateByAddingUnit(
            unit = NSCalendarUnitDay,
            value = daysFromNow.toLong(),
            toDate = now,
            options = 0uL
        ) ?: now
        val components = calendar.components(
            unitFlags = NSCalendarUnitYear or NSCalendarUnitMonth or NSCalendarUnitDay,
            fromDate = futureDate
        )
        components.hour = random.nextInt(DAILY_WINDOW_START_HOUR, DAILY_WINDOW_END_HOUR).toLong()
        components.minute = random.nextInt(MINUTES_IN_HOUR).toLong()
        components.second = 0
        return UNCalendarNotificationTrigger.triggerWithDateMatchingComponents(
            dateComponents = components,
            repeats = false
        )
    }

    private fun dateComponentsFromTimestamp(timestampMillis: Long): NSDateComponents {
        val date = NSDate.dateWithTimeIntervalSince1970(timestampMillis / MS_PER_SECOND)
        return NSCalendar.currentCalendar.components(
            unitFlags = NSCalendarUnitYear or NSCalendarUnitMonth or NSCalendarUnitDay or
                NSCalendarUnitHour or NSCalendarUnitMinute or NSCalendarUnitSecond,
            fromDate = date
        )
    }

    private fun schedule(
        identifier: String,
        content: UNMutableNotificationContent,
        trigger: UNNotificationTrigger
    ) {
        center.removePendingNotificationRequestsWithIdentifiers(listOf(identifier))
        val request = UNNotificationRequest.requestWithIdentifier(
            identifier = identifier,
            content = content,
            trigger = trigger
        )
        center.addNotificationRequest(request) { _ -> }
    }

    companion object {
        private const val REMINDER_IDENTIFIER = "memolki.notification.reminder"
        private const val AD_REWARD_IDENTIFIER = "memolki.notification.ad_reward"
        private const val STREAK_IDENTIFIER = "memolki.notification.streak"
        private const val DAILY_CHALLENGE_IDENTIFIER = "memolki.notification.daily_challenge"
        private val ALL_IDENTIFIERS = listOf(
            REMINDER_IDENTIFIER,
            AD_REWARD_IDENTIFIER,
            STREAK_IDENTIFIER,
            DAILY_CHALLENGE_IDENTIFIER
        )
        private const val REMINDER_INTERVAL_DAYS = 3
        private const val STREAK_INTERVAL_DAYS = 1
        private const val DAILY_CHALLENGE_HOUR = 20
        private const val DAILY_WINDOW_START_HOUR = 14
        private const val DAILY_WINDOW_END_HOUR = 20
        private const val MINUTES_IN_HOUR = 60
        private const val MS_PER_SECOND = 1000.0
    }
}
