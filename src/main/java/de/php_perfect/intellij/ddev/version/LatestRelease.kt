package de.php_perfect.intellij.ddev.version

import java.util.Objects

class LatestRelease(tagName: String?) {
    private val tagName: String?

    init {
        this.tagName = tagName
    }

    fun getTagName(): String? {
        return tagName
    }

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val that = o as LatestRelease
        return getTagName() == that.getTagName()
    }

    override fun hashCode(): Int {
        return Objects.hash(getTagName())
    }

    override fun toString(): String {
        return "LatestRelease{" +
                "tagName='" + tagName + '\'' +
                '}'
    }
}
