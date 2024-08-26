package de.php_perfect.intellij.ddev.serviceActions

import com.intellij.icons.AllIcons
import com.intellij.openapi.actionSystem.AnAction
import de.php_perfect.intellij.ddev.actions.OpenServiceAction
import de.php_perfect.intellij.ddev.cmd.DatabaseInfo
import de.php_perfect.intellij.ddev.cmd.Description
import de.php_perfect.intellij.ddev.cmd.Service
import org.assertj.core.api.Assertions
import org.assertj.core.util.Arrays
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.lang.RuntimeException
import java.net.MalformedURLException
import java.net.URL
import java.util.HashMap
import java.util.Map

internal class ServiceActionManagerImplTest {
    @Test
    @DisplayName("should update actions from given description")
    fun testUpdateActionsByDescription() {
        val serviceActionManager = ServiceActionManagerImpl(
            HashMap<String?, AnAction?>(
                Map.of<String?, AnAction?>(
                    "existingAction",
                    anAction("Open existing action", "https://www.existing.com", "existing action")
                )
            )
        )

        serviceActionManager.updateActionsByDescription(aDescription("test", Description.Status.RUNNING))

        Assertions.assertThat<AnAction?>(serviceActionManager.getServiceActions()).isEqualTo(
            Arrays.array<AnAction?>(
                aMailhogAction(),
                anAction("Open test", "https://www.test.com", "Open test service in your browser")
            )
        )
    }

    @Test
    @DisplayName("should update actions from given description")
    fun testUpdateActionsByMailpitDescription() {
        val serviceActionManager = ServiceActionManagerImpl(
            HashMap<String?, AnAction?>(
                Map.of<String?, AnAction?>(
                    "existingAction",
                    anAction("Open existing action", "https://www.existing.com", "existing action")
                )
            )
        )

        serviceActionManager.updateActionsByDescription(aMailpitDescription("test", Description.Status.RUNNING))

        Assertions.assertThat<AnAction?>(serviceActionManager.getServiceActions()).isEqualTo(
            Arrays.array<AnAction?>(
                aMailhogAction(),
                anAction("Open test", "https://www.test.com", "Open test service in your browser")
            )
        )
    }

    private fun anAction(displayText: String, url: String, description: String?): AnAction {
        try {
            return OpenServiceAction(URL(url), displayText, description, AllIcons.General.Web)
        } catch (urlException: MalformedURLException) {
            throw RuntimeException(urlException)
        }
    }

    private fun aMailhogAction(): AnAction {
        return anAction(
            "Open Mailhog", "https://www.test.com",
            "Open Mailhog service in your browser"
        )
    }

    private fun aMailpitAction(): AnAction {
        return anAction(
            "Open Mailpit", "https://www.test.com",
            "Open Mailpit service in your browser"
        )
    }

    private fun aDescription(name: String, status: Description.Status?): Description {
        val dataBaseInfo = DatabaseInfo(
            DatabaseInfo.Type.MYSQL, "5.7", 2133, "db", "localhost",
            "root", "root", 2133
        )

        val httpUrl = String.format("http://www.%s.com", name)
        val httpsUrl = String.format("https://www.%s.com", name)

        return Description(
            name,
            "7.4",
            status,
            httpsUrl,
            httpUrl,
            null,
            null,
            Map.of<String?, Service?>(name, Service(name, httpsUrl, httpUrl)),
            dataBaseInfo,
            null
        )
    }

    private fun aMailpitDescription(name: String, status: Description.Status?): Description {
        val dataBaseInfo = DatabaseInfo(
            DatabaseInfo.Type.MYSQL, "5.7", 2133, "db", "localhost",
            "root", "root", 2133
        )

        val httpUrl = String.format("http://www.%s.com", name)
        val httpsUrl = String.format("https://www.%s.com", name)

        return Description(
            name,
            "7.4",
            status,
            null,
            null,
            httpsUrl,
            httpUrl,
            Map.of<String?, Service?>(name, Service(name, httpsUrl, httpUrl)),
            dataBaseInfo,
            null
        )
    }
}
