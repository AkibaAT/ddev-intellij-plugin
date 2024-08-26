package de.php_perfect.intellij.ddev.settings

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.openapi.project.Project
import com.intellij.util.xmlb.XmlSerializerUtil

@State(name = "de.php_perfect.intellij.ddev.settings.DdevSettingsState", storages = [Storage("DdevIntegration.xml")])
@Service(Service.Level.PROJECT)
class DdevSettingsState : PersistentStateComponent<DdevSettingsState?> {
    @JvmField
    var ddevBinary: String = ""
    @JvmField
    var checkForUpdates: Boolean = true
    @JvmField
    var watchDdev: Boolean = true
    var autoConfigureDataSource: Boolean = true
    var autoConfigurePhpInterpreter: Boolean = true
    var autoConfigureNodeJsInterpreter: Boolean = true

    override fun getState(): DdevSettingsState {
        return this
    }

    override fun loadState(state: DdevSettingsState) {
        XmlSerializerUtil.copyBean<DdevSettingsState>(state, this)
    }

    companion object {
        @JvmStatic
        fun getInstance(project: Project): DdevSettingsState {
            return project.getService<DdevSettingsState>(DdevSettingsState::class.java)
        }
    }
}
