package de.php_perfect.intellij.ddev.index

import com.intellij.openapi.project.Project
import org.jetbrains.annotations.NonNls

interface ManagedConfigurationIndex {
    fun set(id: @NonNls String, type: Class<out IndexableConfiguration?>, hash: Int)

    fun get(type: Class<out IndexableConfiguration?>): IndexEntry?

    fun remove(type: Class<out IndexableConfiguration?>)

    fun isManaged(id: @NonNls String, type: Class<out IndexableConfiguration?>): Boolean

    fun isUpToDate(type: Class<out IndexableConfiguration?>, hash: Int): Boolean

    fun purge()

    companion object {
        @JvmStatic
        fun getInstance(project: Project): ManagedConfigurationIndex? {
            return project.getService<ManagedConfigurationIndex?>(ManagedConfigurationIndex::class.java)
        }
    }
}
