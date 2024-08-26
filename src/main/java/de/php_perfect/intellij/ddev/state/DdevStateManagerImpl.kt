package de.php_perfect.intellij.ddev.state

import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project
import de.php_perfect.intellij.ddev.DatabaseInfoChangedListener
import de.php_perfect.intellij.ddev.DescriptionChangedListener
import de.php_perfect.intellij.ddev.StateChangedListener
import de.php_perfect.intellij.ddev.StateInitializedListener
import de.php_perfect.intellij.ddev.cmd.BinaryLocator
import de.php_perfect.intellij.ddev.cmd.BinaryLocator.Companion.getInstance
import de.php_perfect.intellij.ddev.cmd.CommandFailedException
import de.php_perfect.intellij.ddev.cmd.DatabaseInfo
import de.php_perfect.intellij.ddev.cmd.Ddev
import de.php_perfect.intellij.ddev.cmd.Ddev.Companion.getInstance
import de.php_perfect.intellij.ddev.cmd.Docker
import de.php_perfect.intellij.ddev.cmd.Docker.Companion.getInstance
import de.php_perfect.intellij.ddev.notification.DdevNotifier
import de.php_perfect.intellij.ddev.notification.DdevNotifier.Companion.getInstance
import de.php_perfect.intellij.ddev.settings.DdevSettingsState
import de.php_perfect.intellij.ddev.settings.DdevSettingsState.Companion.getInstance
import java.util.Objects

class DdevStateManagerImpl(private val project: Project) : DdevStateManager {
    private val state = StateImpl()

    override fun getState(): State {
        return state
    }

    override fun initialize() {
        this.initialize(false)
    }

    override fun reinitialize() {
        this.initialize(true)
    }

    fun initialize(reinitialize: Boolean) {
        if (!reinitialize && !Docker.getInstance()!!.isRunning(this.project.basePath)) {
            LOG.debug("Docker not available. Skipping initialization")
            DdevNotifier.getInstance(this.project)!!
                .notifyDockerNotAvailable(Docker.getInstance()!!.getContext(this.project.basePath))

            return
        }

        this.checkChanged(Runnable {
            this.resetState()
            this.checkIsInstalled(!reinitialize)
            this.checkVersion()
            this.checkConfiguration()
            this.checkDescription()
        })

        LOG.debug("DDEV state initialised " + this.state)
        val messageBus = this.project.messageBus
        messageBus.syncPublisher<StateInitializedListener>(StateInitializedListener.STATE_INITIALIZED)
            .onStateInitialized(this.state)
    }

    override fun updateConfiguration() {
        LOG.debug("Updating DDEV configuration data")
        this.checkChanged(Runnable {
            this.checkConfiguration()
            this.checkDescription()
        })
    }

    override fun updateDescription() {
        LOG.debug("Updating DDEV description data")
        this.checkChanged(Runnable { this.checkDescription() })
    }

    override fun resetState() {
        this.state.reset()
    }

    private fun checkChanged(runnable: Runnable) {
        val oldState = this.state.hashCode()
        val oldDescription = Objects.hashCode(this.state.getDescription())
        var oldDatabaseInfoHash = 0
        if (this.state.getDescription() != null) {
            oldDatabaseInfoHash = Objects.hashCode(this.state.getDescription()!!.getDatabaseInfo())
        }

        runnable.run()

        if (oldState != this.state.hashCode()) {
            LOG.debug("DDEV state changed: " + this.state)
            val messageBus = this.project.messageBus
            messageBus.syncPublisher<StateChangedListener>(StateChangedListener.DDEV_CHANGED)
                .onDdevChanged(this.state)

            val newDescription = this.state.getDescription()

            if (oldDescription != Objects.hashCode(newDescription)) {
                messageBus.syncPublisher<DescriptionChangedListener>(DescriptionChangedListener.DESCRIPTION_CHANGED)
                    .onDescriptionChanged(this.state.getDescription())

                var newDatabaseInfo: DatabaseInfo? = null
                if (newDescription != null) {
                    newDatabaseInfo = newDescription.getDatabaseInfo()
                }

                if (oldDatabaseInfoHash != Objects.hashCode(newDatabaseInfo)) {
                    messageBus.syncPublisher<DatabaseInfoChangedListener>(DatabaseInfoChangedListener.DATABASE_INFO_CHANGED_TOPIC)
                        .onDatabaseInfoChanged(newDatabaseInfo)
                }
            }
        }
    }

    private fun checkIsInstalled(autodetect: Boolean) {
        val configurable = DdevSettingsState.getInstance(this.project)

        if (autodetect && configurable.ddevBinary.isEmpty()) {
            val detectedDdevBinary = BinaryLocator.getInstance()!!.findInPath(this.project)

            if (detectedDdevBinary != null) {
                configurable.ddevBinary = detectedDdevBinary
                DdevNotifier.getInstance(this.project)!!.notifyDdevDetected(detectedDdevBinary)
            }
        }

        this.state.setDdevBinary(configurable.ddevBinary)
    }

    private fun checkVersion() {
        if (!this.state.isBinaryConfigured()) {
            this.state.setDdevVersion(null)
            this.state.setDescription(null)
            return
        }

        try {
            this.state.setDdevVersion(
                Ddev.getInstance()!!.version(Objects.requireNonNull<String?>(this.state.getDdevBinary()), this.project)
            )
        } catch (exception: CommandFailedException) {
            LOG.error(exception)
            this.state.setDdevVersion(null)
        }
    }

    private fun checkConfiguration() {
        this.state.setConfigured(DdevConfigLoader.Companion.getInstance(this.project)?.exists() == true)
    }

    private fun checkDescription() {
        if (!this.state.isAvailable() || !this.state.isConfigured()) {
            this.state.setDescription(null)
            return
        }

        try {
            this.state.setDescription(
                Ddev.getInstance()!!.describe(Objects.requireNonNull<String?>(this.state.getDdevBinary()), this.project)
            )
        } catch (exception: CommandFailedException) {
            LOG.error(exception)
            this.state.setDescription(null)
        }
    }

    companion object {
        private val LOG = Logger.getInstance(DdevStateManagerImpl::class.java)
    }
}
