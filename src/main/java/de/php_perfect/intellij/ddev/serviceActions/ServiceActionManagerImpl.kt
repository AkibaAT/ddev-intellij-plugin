package de.php_perfect.intellij.ddev.serviceActions

import com.intellij.icons.AllIcons
import com.intellij.openapi.Disposable
import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.serviceContainer.NonInjectable
import de.php_perfect.intellij.ddev.DdevIntegrationBundle
import de.php_perfect.intellij.ddev.actions.OpenServiceAction
import de.php_perfect.intellij.ddev.cmd.Description
import de.php_perfect.intellij.ddev.cmd.Service
import org.jetbrains.annotations.TestOnly
import java.net.MalformedURLException
import java.net.URI
import java.net.URISyntaxException
import java.net.URL
import java.util.AbstractMap
import java.util.Map
import java.util.Optional
import java.util.concurrent.ConcurrentHashMap
import java.util.function.Function
import java.util.logging.Level
import java.util.logging.Logger
import java.util.stream.Collectors

class ServiceActionManagerImpl @NonInjectable @TestOnly constructor(existingActionsMap: MutableMap<String, AnAction>) :
    ServiceActionManager, Disposable {
    private val actionMap: MutableMap<String, AnAction> = existingActionsMap

    constructor() : this(ConcurrentHashMap<String, AnAction>())

    override fun getServiceActions(): Array<AnAction?> {
        return this.actionMap.values.toTypedArray<AnAction?>()
    }

    override fun updateActionsByDescription(description: Description?) {
        if (description == null) {
            return
        }

        val newActionsMap: MutableMap<String, AnAction> = description.getServices()
            .entries
            .stream()
            .map<Optional<MutableMap.MutableEntry<String?, AnAction?>?>?> { serviceNameToActionEntry: MutableMap.MutableEntry<String?, Service?>? ->
                this.mapToServiceNameWithAction(
                    serviceNameToActionEntry!!
                )
            }
            .flatMap<MutableMap.MutableEntry<String?, AnAction?>?> { obj: Optional<MutableMap.MutableEntry<String?, AnAction?>?>? -> obj!!.stream() }
            .collect(Collectors.toMap(Function { Map.Entry.key }, Function { Map.Entry.value }))

        this.actionMap.clear()
        this.actionMap.putAll(newActionsMap)
    }

    // Map.Entry<ServiceName, AnAction>
    private fun mapToServiceNameWithAction(
        serviceNameToActionEntry: MutableMap.MutableEntry<String?, Service?>
    ): Optional<MutableMap.MutableEntry<String?, AnAction?>?> {
        val fullName = serviceNameToActionEntry.value!!.getFullName()
        var url: URL?
        try {
            url = extractServiceUrl(serviceNameToActionEntry.value!!)
        } catch (exception: MalformedURLException) {
            LOGGER.log(
                Level.WARNING,
                String.format("Skipping open action for service %s because of its invalid URL", fullName), exception
            )
            return Optional.empty<MutableMap.MutableEntry<String?, AnAction?>?>()
        } catch (exception: URISyntaxException) {
            LOGGER.log(
                Level.WARNING,
                String.format("Skipping open action for service %s because of its invalid URL", fullName), exception
            )
            return Optional.empty<MutableMap.MutableEntry<String?, AnAction?>?>()
        }

        if (url == null) {
            return Optional.empty<MutableMap.MutableEntry<String?, AnAction?>?>()
        }

        val actionId = ACTION_PREFIX + fullName
        val action = buildAction(serviceNameToActionEntry.key!!, url, fullName!!)

        return Optional.of<MutableMap.MutableEntry<String?, AnAction?>?>(
            AbstractMap.SimpleImmutableEntry<String?, AnAction?>(
                actionId,
                action
            )
        ) as Optional<MutableMap.MutableEntry<String?, AnAction?>?>
    }

    @Throws(MalformedURLException::class, URISyntaxException::class)
    private fun extractServiceUrl(service: Service): URL? {
        var address = service.getHttpsUrl()
        if (address == null) {
            address = service.getHttpUrl()
        }

        if (address != null) {
            return URI(address).toURL()
        }

        return null
    }

    private fun buildAction(key: String, url: URL, fullName: String): AnAction {
        val text = buildActionText(key, fullName)
        val descriptionText = DdevIntegrationBundle.message("action.services.open.description", fullName)
        return OpenServiceAction(url, text, descriptionText, AllIcons.General.Web)
    }

    private fun buildActionText(key: String, fullName: String): String {
        return when (key) {
            "web" -> DdevIntegrationBundle.message("action.services.open.web")
            "mailhog" -> DdevIntegrationBundle.message("action.services.open.mailHog")
            "mailpit" -> DdevIntegrationBundle.message("action.services.open.mailpit")
            else -> DdevIntegrationBundle.message("action.services.open.any", fullName)
        }
    }

    override fun dispose() {
        for (actionId in this.actionMap.keys) {
            ActionManager.getInstance().unregisterAction(actionId)
        }
    }

    companion object {
        private val LOGGER: Logger = Logger.getLogger(
            ServiceActionManagerImpl::class.java.name,
            DdevIntegrationBundle.getName()
        )
        private const val ACTION_PREFIX = "DdevIntegration.Services."
    }
}
