package de.php_perfect.intellij.ddev.state

import de.php_perfect.intellij.ddev.cmd.Description
import de.php_perfect.intellij.ddev.version.Version
import java.util.Objects
import java.util.concurrent.locks.ReadWriteLock
import java.util.concurrent.locks.ReentrantReadWriteLock

internal class StateImpl : State {
    private var version: Version? = null

    private var description: Description? = null

    private var ddevBinary: String? = null

    private var configured = false

    private val readWriteLock: ReadWriteLock = ReentrantReadWriteLock()

    override fun isBinaryConfigured(): Boolean {
        return this.ddevBinary != null && !this.ddevBinary!!.isEmpty()
    }

    override fun isAvailable(): Boolean {
        return this.version != null
    }

    override fun getDdevBinary(): String? {
        return ddevBinary
    }

    fun setDdevBinary(ddevBinary: String?) {
        this.ddevBinary = ddevBinary
    }

    override fun isConfigured(): Boolean {
        return this.configured
    }

    fun setConfigured(configured: Boolean) {
        this.configured = configured
    }

    fun setDdevVersion(ddevVersion: Version?) {
        this.readWriteLock.writeLock().lock()
        try {
            this.version = ddevVersion
        } finally {
            this.readWriteLock.writeLock().unlock()
        }
    }

    override fun getDdevVersion(): Version? {
        this.readWriteLock.readLock().lock()
        try {
            return this.version
        } finally {
            this.readWriteLock.readLock().unlock()
        }
    }

    override fun getDescription(): Description? {
        this.readWriteLock.readLock().lock()
        try {
            return this.description
        } finally {
            this.readWriteLock.readLock().unlock()
        }
    }

    fun setDescription(description: Description?) {
        this.readWriteLock.writeLock().lock()
        try {
            this.description = description
        } finally {
            this.readWriteLock.writeLock().unlock()
        }
    }

    fun reset() {
        this.readWriteLock.writeLock().lock()
        try {
            this.version = null
            this.description = null
            this.ddevBinary = null
            this.configured = false
        } finally {
            this.readWriteLock.writeLock().unlock()
        }
    }

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val state = o as StateImpl
        return isConfigured() == state.isConfigured() && version == state.version && getDescription() == state.getDescription() && getDdevBinary() == state.getDdevBinary()
    }

    override fun hashCode(): Int {
        return Objects.hash(version, getDescription(), getDdevBinary(), isConfigured())
    }

    override fun toString(): String {
        return "StateImpl{" +
                "version=" + version +
                ", description=" + description +
                ", ddevBinary='" + ddevBinary + "'" +
                ", configured=" + configured +
                '}'
    }
}
