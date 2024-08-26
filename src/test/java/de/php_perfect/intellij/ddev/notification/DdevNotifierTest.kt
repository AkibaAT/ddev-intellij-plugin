package de.php_perfect.intellij.ddev.notification

import com.intellij.notification.Notification
import com.intellij.notification.NotificationsManager
import com.intellij.openapi.application.ApplicationManager
import com.intellij.testFramework.PlatformTestUtil
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.lang.Exception

internal class DdevNotifierTest : BasePlatformTestCase() {
    @BeforeEach
    @Throws(Exception::class)
    override fun setUp() {
        super.setUp()
    }

    @Test
    fun notifyInstallDdev() {
        val project = getProject()

        val notificationManager = NotificationsManager.getNotificationsManager()
        var notifications = notificationManager.getNotificationsOfType<Notification?>(Notification::class.java, project)
        assertEmpty(notifications)

        DdevNotifierImpl(project).notifyInstallDdev()

        this.waitForEventQueue()

        notifications = notificationManager.getNotificationsOfType<Notification?>(Notification::class.java, project)
        assertSize(1, notifications)
    }

    @Test
    fun notifyNewVersionAvailable() {
        val project = getProject()

        val notificationManager = NotificationsManager.getNotificationsManager()
        var notifications = notificationManager.getNotificationsOfType<Notification?>(Notification::class.java, project)
        assertEmpty(notifications)

        DdevNotifierImpl(project).notifyNewVersionAvailable("1.0.0", "2.0.0")

        this.waitForEventQueue()

        notifications = notificationManager.getNotificationsOfType<Notification?>(Notification::class.java, project)
        assertSize(1, notifications)
    }

    @Test
    fun notifyAlreadyLatestVersion() {
        val project = getProject()

        val notificationManager = NotificationsManager.getNotificationsManager()
        var notifications = notificationManager.getNotificationsOfType<Notification?>(Notification::class.java, project)
        assertEmpty(notifications)

        DdevNotifierImpl(project).notifyAlreadyLatestVersion()

        this.waitForEventQueue()

        notifications = notificationManager.getNotificationsOfType<Notification?>(Notification::class.java, project)
        assertSize(1, notifications)
    }

    @Test
    fun notifyMissingPlugin() {
        val project = getProject()

        val notificationManager = NotificationsManager.getNotificationsManager()
        var notifications = notificationManager.getNotificationsOfType<Notification?>(Notification::class.java, project)
        assertEmpty(notifications)

        DdevNotifierImpl(project).notifyMissingPlugin("Some Plugin")

        this.waitForEventQueue()

        notifications = notificationManager.getNotificationsOfType<Notification?>(Notification::class.java, project)
        assertSize(1, notifications)
    }

    @Test
    fun notifyPhpInterpreterUpdated() {
        val project = getProject()

        val notificationManager = NotificationsManager.getNotificationsManager()
        var notifications = notificationManager.getNotificationsOfType<Notification?>(Notification::class.java, project)
        assertEmpty(notifications)

        DdevNotifierImpl(project).notifyPhpInterpreterUpdated("php99.9")

        this.waitForEventQueue()

        notifications = notificationManager.getNotificationsOfType<Notification?>(Notification::class.java, project)
        assertSize(1, notifications)
    }

    @Test
    fun notifyUnknownStateEntered() {
        val project = getProject()

        val notificationManager = NotificationsManager.getNotificationsManager()
        var notifications = notificationManager.getNotificationsOfType<Notification?>(Notification::class.java, project)
        assertEmpty(notifications)

        DdevNotifierImpl(project).notifyUnknownStateEntered()

        this.waitForEventQueue()

        notifications = notificationManager.getNotificationsOfType<Notification?>(Notification::class.java, project)
        assertSize(1, notifications)
    }

    @Test
    fun notifyDdevDetected() {
        val project = getProject()

        val notificationManager = NotificationsManager.getNotificationsManager()
        var notifications = notificationManager.getNotificationsOfType<Notification?>(Notification::class.java, project)
        assertEmpty(notifications)

        DdevNotifierImpl(project).notifyDdevDetected("/some/path/ddev")

        this.waitForEventQueue()

        notifications = notificationManager.getNotificationsOfType<Notification?>(Notification::class.java, project)
        assertSize(1, notifications)
    }

    @Test
    fun notifyErrorReportSent() {
        val project = getProject()

        val notificationManager = NotificationsManager.getNotificationsManager()
        var notifications = notificationManager.getNotificationsOfType<Notification?>(Notification::class.java, project)
        assertEmpty(notifications)

        DdevNotifierImpl(project).notifyErrorReportSent("cc83481fd7b74744afdd7f36ba827f7b")

        this.waitForEventQueue()

        notifications = notificationManager.getNotificationsOfType<Notification?>(Notification::class.java, project)
        assertSize(1, notifications)
        assertTrue(notifications[0]!!.getContent().contains("cc83481fd7b74744afdd7f36ba827f7b"))
    }

    @Test
    fun notifyDockerNotAvailable() {
        val project = getProject()

        val notificationManager = NotificationsManager.getNotificationsManager()
        var notifications = notificationManager.getNotificationsOfType<Notification?>(Notification::class.java, project)
        assertEmpty(notifications)

        DdevNotifierImpl(project).notifyDockerNotAvailable("default")

        this.waitForEventQueue()

        notifications = notificationManager.getNotificationsOfType<Notification?>(Notification::class.java, project)
        assertSize(1, notifications)
    }

    private fun waitForEventQueue() {
        ApplicationManager.getApplication()
            .invokeAndWait(Runnable { PlatformTestUtil.dispatchAllInvocationEventsInIdeEventQueue() })
    }

    @AfterEach
    @Throws(Exception::class)
    override fun tearDown() {
        val notificationManager = NotificationsManager.getNotificationsManager()
        val notifications =
            notificationManager.getNotificationsOfType<Notification?>(Notification::class.java, getProject())
        for (notification in notifications) {
            notificationManager.expire(notification)
        }

        super.tearDown()
    }
}
