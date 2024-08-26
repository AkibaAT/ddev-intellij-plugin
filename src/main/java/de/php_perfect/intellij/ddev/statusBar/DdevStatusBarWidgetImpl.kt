package de.php_perfect.intellij.ddev.statusBar

import com.intellij.openapi.actionSystem.ActionGroup
import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.actionSystem.ActionPlaces
import com.intellij.openapi.actionSystem.DataContext
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.popup.JBPopupFactory
import com.intellij.openapi.ui.popup.ListPopup
import com.intellij.openapi.util.Key
import com.intellij.openapi.util.NlsContexts.StatusBarText
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.wm.StatusBar
import com.intellij.openapi.wm.StatusBarWidget
import com.intellij.openapi.wm.impl.status.EditorBasedStatusBarPopup
import com.intellij.util.concurrency.EdtExecutorService
import com.intellij.util.messages.Topic
import com.intellij.util.ui.update.UiNotifyConnector
import de.php_perfect.intellij.ddev.DdevIntegrationBundle
import de.php_perfect.intellij.ddev.StateChangedListener
import de.php_perfect.intellij.ddev.cmd.Description
import de.php_perfect.intellij.ddev.icons.DdevIntegrationIcons
import de.php_perfect.intellij.ddev.state.State
import de.php_perfect.intellij.ddev.tutorial.GotItTutorial
import org.jetbrains.annotations.NonNls
import java.util.concurrent.TimeUnit
import javax.swing.JComponent

class DdevStatusBarWidgetImpl(project: Project) : EditorBasedStatusBarPopup(project, false) {
    override fun ID(): @NonNls String {
        return WIDGET_ID
    }

    override fun install(statusBar: StatusBar) {
        if (statusBar.project != null && statusBar.project != this.project) {
            LOG.warn("Cannot install widget from one project on status bar of another project")
            return
        }

        super.install(statusBar)
        registerUpdateListener()
        UiNotifyConnector.doWhenFirstShown(super.getComponent(), Runnable { delayTutorial(super.getComponent()) }, this)
    }

    private fun registerUpdateListener() {
        val messageBus = this.project.messageBus
        messageBus.connect(this).subscribe(StateChangedListener.DDEV_CHANGED, StatusBarUpdateListener())
    }

    private inner class StatusBarUpdateListener : StateChangedListener {
        override fun onDdevChanged(state: State) {
            this@DdevStatusBarWidgetImpl.putState(state)
            this@DdevStatusBarWidgetImpl.update()
        }
    }

    protected override fun createPopup(context: DataContext): ListPopup {
        val group = ActionManager.getInstance().getAction(ACTION_GROUP) as ActionGroup
        val place = ActionPlaces.getPopupPlace(ActionPlaces.STATUS_BAR_PLACE)

        return JBPopupFactory.getInstance()
            .createActionGroupPopup(null, group, context, JBPopupFactory.ActionSelectionAid.SPEEDSEARCH, false, place)
    }

    protected override fun createInstance(project: Project): StatusBarWidget {
        return DdevStatusBarWidgetImpl(project)
    }

    protected override fun getWidgetState(file: VirtualFile?): WidgetState {
        val state = this.fetchState()
        if (state == null || !state.isAvailable() || !state.isConfigured()) {
            return WidgetState.HIDDEN
        }

        val toolTipText = DdevIntegrationBundle.message("statusBar.toolTip")
        val statusText = this.getStatusText()
        val widgetState = WidgetState(toolTipText, statusText, true)
        widgetState.icon = DdevIntegrationIcons.DdevLogoMono

        return widgetState
    }

    private fun delayTutorial(component: JComponent) {
        EdtExecutorService.getScheduledExecutorInstance().schedule(Runnable {
            if (!this.isDisposed) {
                GotItTutorial.getInstance()?.showStatusBarTutorial(component, this)
            }
        }, 3000, TimeUnit.MILLISECONDS)
    }

    private fun getStatusText(): @StatusBarText String {
        var description: Description? = null
        var status: Description.Status? = null
        val state = this.fetchState()

        if (state != null) {
            description = state.getDescription()
        }

        if (description != null) {
            status = description.getStatus()
        }

        return this.buildStatusMessage(status)
    }

    private fun buildStatusMessage(status: Description.Status?): @StatusBarText String {
        if (status == null) {
            return DdevIntegrationBundle.message("status.Undefined")
        }

        return when (status) {
            Description.Status.RUNNING -> DdevIntegrationBundle.message("status.Running")
            Description.Status.STARTING -> DdevIntegrationBundle.message("status.Starting")
            Description.Status.STOPPED -> DdevIntegrationBundle.message("status.Stopped")
            Description.Status.DIR_MISSING -> DdevIntegrationBundle.message("status.DirMissing")
            Description.Status.CONFIG_MISSING -> DdevIntegrationBundle.message("status.ConfigMissing")
            Description.Status.PAUSED -> DdevIntegrationBundle.message("status.Paused")
            Description.Status.UNHEALTHY -> DdevIntegrationBundle.message("status.Unhealthy")
        }
    }

    private fun putState(state: State?) {
        this.project.putUserData<State?>(DDEV_STATE_KEY, state)
    }

    private fun fetchState(): State? {
        return this.project.getUserData<State?>(DDEV_STATE_KEY)
    }

    companion object {
        private val LOG = Logger.getInstance(DdevStatusBarWidgetImpl::class.java)
        private val DDEV_STATE_KEY = Key<State?>("DdevIntegration.State")
        private const val ACTION_GROUP = "DdevIntegration.Services"
        const val WIDGET_ID: String = "DdevStatusBarWidget"
    }
}
