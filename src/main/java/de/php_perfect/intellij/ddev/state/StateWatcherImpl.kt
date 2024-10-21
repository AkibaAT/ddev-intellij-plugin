package de.php_perfect.intellij.ddev.state

import com.intellij.openapi.Disposable
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project
import com.intellij.util.concurrency.AppExecutorUtil
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit

class StateWatcherImpl(private val project: Project) : StateWatcher, Disposable {
    private var scheduledFuture: ScheduledFuture<*>? = null

    override fun startWatching() {
        if (isWatching) {
            stopWatching()
        }

        scheduledFuture = AppExecutorUtil.getAppScheduledExecutorService().scheduleWithFixedDelay({
            LOG.debug("DDEV state watcher triggering update")
            val ddevStateManager = DdevStateManager.getInstance(project)
            ddevStateManager.updateConfiguration()
            ddevStateManager.updateDescription()
        }, 10L, 10L, TimeUnit.SECONDS)
        LOG.info("DDEV state watcher started")
    }

    override fun stopWatching() {
        scheduledFuture?.cancel(true)
        LOG.info("DDEV state watcher stopped")
    }

    override fun isWatching(): Boolean {
        return scheduledFuture != null && !scheduledFuture!!.isCancelled
    }

    override fun dispose() {
        stopWatching()
    }

    companion object {
        private val LOG = Logger.getInstance(StateWatcherImpl::class.java)
    }
}
