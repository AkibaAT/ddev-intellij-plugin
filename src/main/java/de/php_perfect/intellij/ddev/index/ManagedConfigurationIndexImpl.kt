package de.php_perfect.intellij.ddev.index

import com.intellij.ide.util.PropertiesComponent
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project
import org.apache.commons.lang3.ObjectUtils
import org.jetbrains.annotations.NonNls
import java.util.ArrayList

class ManagedConfigurationIndexImpl(project: Project) : ManagedConfigurationIndex {
    private val project: Project

    init {
        this.project = project
    }

    override fun set(id: @NonNls String, type: Class<out IndexableConfiguration?>, hash: Int) {
        this.setManagedConfiguration(id, type.getName(), Integer.toHexString(hash))
    }

    private fun setManagedConfiguration(id: @NonNls String, type: @NonNls String, hash: @NonNls String) {
        val key = ManagedConfigurationIndexImpl.Companion.buildKey(type)
        val propertiesComponent = PropertiesComponent.getInstance(this.project)

        propertiesComponent.setValue(key, id)
        propertiesComponent.setValue(ManagedConfigurationIndexImpl.Companion.buildHashKey(key), hash)

        val list = ObjectUtils.firstNonNull<MutableList<String?>>(
            propertiesComponent.getList(ManagedConfigurationIndexImpl.Companion.INDEX_KEY),
            mutableListOf<String?>()
        )
        val mutableList = ArrayList<String?>(list)
        mutableList.add(key)
        propertiesComponent.setList(ManagedConfigurationIndexImpl.Companion.INDEX_KEY, mutableList)

        ManagedConfigurationIndexImpl.Companion.LOG.debug(
            String.format(
                "Set managed configuration id '%s' for %s",
                id,
                type
            )
        )
    }

    override fun get(type: Class<out IndexableConfiguration?>): IndexEntry? {
        return this.getManagedConfiguration(type.getName())
    }

    private fun getManagedConfiguration(type: @NonNls String): IndexEntry? {
        val key = ManagedConfigurationIndexImpl.Companion.buildKey(type)
        val propertiesComponent = PropertiesComponent.getInstance(this.project)

        val id = propertiesComponent.getValue(key)

        if (id == null) {
            return null
        }

        val hash = propertiesComponent.getValue(ManagedConfigurationIndexImpl.Companion.buildHashKey(key))

        return IndexEntry(id, hash)
    }

    override fun isManaged(id: @NonNls String, type: Class<out IndexableConfiguration?>): Boolean {
        return this.isManagedConfiguration(id, type.getName())
    }

    private fun isManagedConfiguration(id: @NonNls String, type: @NonNls String): Boolean {
        val key = ManagedConfigurationIndexImpl.Companion.buildKey(type)
        val propertiesComponent = PropertiesComponent.getInstance(this.project)

        if (!propertiesComponent.isValueSet(key)) {
            return false
        }

        return id == propertiesComponent.getValue(key)
    }

    override fun isUpToDate(type: Class<out IndexableConfiguration?>, hash: Int): Boolean {
        return this.isUpToDate(type.getName(), Integer.toHexString(hash))
    }

    private fun isUpToDate(type: @NonNls String, hash: @NonNls String): Boolean {
        val key = ManagedConfigurationIndexImpl.Companion.buildKey(type)
        val hashKey = ManagedConfigurationIndexImpl.Companion.buildHashKey(key)
        val propertiesComponent = PropertiesComponent.getInstance(this.project)

        return propertiesComponent.isValueSet(key) &&
                propertiesComponent.isValueSet(hashKey) &&
                propertiesComponent.getValue(hashKey, "") == hash
    }

    override fun remove(type: Class<out IndexableConfiguration?>) {
        this.removeManagedConfiguration(type.getName())
    }

    private fun removeManagedConfiguration(type: @NonNls String) {
        val key = ManagedConfigurationIndexImpl.Companion.buildKey(type)
        val hashKey = ManagedConfigurationIndexImpl.Companion.buildHashKey(key)
        val propertiesComponent = PropertiesComponent.getInstance(this.project)

        propertiesComponent.unsetValue(key)
        propertiesComponent.unsetValue(hashKey)

        val list = ObjectUtils.firstNonNull<MutableList<String?>>(
            propertiesComponent.getList(ManagedConfigurationIndexImpl.Companion.INDEX_KEY),
            mutableListOf<String?>()
        )
        val mutableList = ArrayList<String?>(list)
        mutableList.remove(key)
        propertiesComponent.setList(ManagedConfigurationIndexImpl.Companion.INDEX_KEY, mutableList)

        ManagedConfigurationIndexImpl.Companion.LOG.debug(
            String.format(
                "Removed managed configuration id for %s",
                type
            )
        )
    }

    override fun purge() {
        val propertiesComponent = PropertiesComponent.getInstance(this.project)
        val list = ObjectUtils.firstNonNull<MutableList<String>>(
            propertiesComponent.getList(ManagedConfigurationIndexImpl.Companion.INDEX_KEY),
            mutableListOf<String?>()
        )

        for (key in list) {
            val hashKey = ManagedConfigurationIndexImpl.Companion.buildHashKey(key)
            propertiesComponent.unsetValue(key)
            propertiesComponent.unsetValue(hashKey)
        }

        propertiesComponent.setList(ManagedConfigurationIndexImpl.Companion.INDEX_KEY, mutableListOf<String?>())

        ManagedConfigurationIndexImpl.Companion.LOG.debug("Configuration index purged")
    }

    companion object {
        private const val NAMESPACE = "de.php_perfect.intellij.ddev."
        private val INDEX_KEY = ManagedConfigurationIndexImpl.Companion.NAMESPACE + "index"
        private const val HASH_SUFFIX = ".hash"
        private val LOG = Logger.getInstance(ManagedConfigurationIndexImpl::class.java)
        private fun buildKey(type: @NonNls String): String {
            return ManagedConfigurationIndexImpl.Companion.NAMESPACE + type
        }

        private fun buildHashKey(key: @NonNls String): String {
            return key + ManagedConfigurationIndexImpl.Companion.HASH_SUFFIX
        }
    }
}
