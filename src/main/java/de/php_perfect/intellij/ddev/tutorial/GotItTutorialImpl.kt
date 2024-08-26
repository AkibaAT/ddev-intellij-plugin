package de.php_perfect.intellij.ddev.tutorial

import com.intellij.openapi.Disposable
import com.intellij.openapi.diagnostic.Logger
import com.intellij.ui.GotItTooltip
import de.php_perfect.intellij.ddev.DdevIntegrationBundle
import de.php_perfect.intellij.ddev.icons.DdevIntegrationIcons
import java.net.MalformedURLException
import java.net.URI
import java.net.URISyntaxException
import javax.swing.JComponent

class GotItTutorialImpl : GotItTutorial {
    override fun showStatusBarTutorial(component: JComponent, disposable: Disposable) {
        try {
            GotItTooltip(
                ID_PREFIX + "status",
                DdevIntegrationBundle.message("tutorial.status.text"),
                disposable
            )
                .withHeader(DdevIntegrationBundle.message("tutorial.status.title"))
                .withIcon(DdevIntegrationIcons.DdevLogoColor)
                .withBrowserLink(
                    DdevIntegrationBundle.message("tutorial.status.link"),
                    URI("https://github.com/php-perfect/ddev-intellij-plugin/wiki/Features#quick-access-to-ddev-services").toURL()
                )
                .show(component, GotItTooltip.TOP_MIDDLE)
        } catch (e: MalformedURLException) {
            LOG.error(e)
        } catch (e: URISyntaxException) {
            LOG.error(e)
        }
    }

    override fun showTerminalTutorial(component: JComponent, disposable: Disposable) {
        try {
            GotItTooltip(
                ID_PREFIX + "terminal",
                DdevIntegrationBundle.message("tutorial.terminal.text"),
                disposable
            )
                .withHeader(DdevIntegrationBundle.message("tutorial.terminal.title"))
                .withIcon(DdevIntegrationIcons.DdevLogoColor)
                .withBrowserLink(
                    DdevIntegrationBundle.message("tutorial.terminal.link"),
                    URI("https://github.com/php-perfect/ddev-intellij-plugin/wiki/Features#integrated-ddev-terminal").toURL()
                )
                .show(component, GotItTooltip.BOTTOM_MIDDLE)
        } catch (e: MalformedURLException) {
            LOG.error(e)
        } catch (e: URISyntaxException) {
            LOG.error(e)
        }
    }

    companion object {
        private val LOG = Logger.getInstance(GotItTutorialImpl::class.java)

        private const val ID_PREFIX = "ddev.features."
    }
}
