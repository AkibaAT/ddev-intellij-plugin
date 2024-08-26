package de.php_perfect.intellij.ddev.state

import com.intellij.openapi.vfs.VirtualFile

class MockDdevConfigLoader : DdevConfigLoader {
    private var exists = false

    fun setExists(exists: Boolean) {
        this.exists = exists
    }

    override fun exists(): Boolean {
        return this.exists
    }

    override fun load(): VirtualFile? {
        return null
    }
}
