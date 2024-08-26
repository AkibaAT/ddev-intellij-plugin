package de.php_perfect.intellij.ddev.index

import org.jetbrains.annotations.NonNls

@JvmRecord
data class IndexEntry(id: @NonNls String, hash: @NonNls String?) {
    fun hashEquals(hash: Int): Boolean {
        return this.hashEquals(Integer.toHexString(hash))
    }

    fun hashEquals(hash: String?): Boolean {
        return this.hash == hash
    }

    val id: @NonNls String
    val hash: @NonNls String?

    init {
        this.id = id
        this.hash = hash
    }
}
