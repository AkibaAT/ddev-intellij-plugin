package de.php_perfect.intellij.ddev.cmd

import java.util.Objects

class Service(fullName: String?, httpsUrl: String?, httpUrl: String?) {
    private val fullName: String?

    private val httpsUrl: String?

    private val httpUrl: String?

    init {
        this.fullName = fullName
        this.httpsUrl = httpsUrl
        this.httpUrl = httpUrl
    }

    fun getFullName(): String? {
        return fullName
    }

    fun getHttpUrl(): String? {
        return httpUrl
    }

    fun getHttpsUrl(): String? {
        return httpsUrl
    }

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o !is Service) return false
        val service = o
        return getFullName() == service.getFullName() && getHttpUrl() == service.getHttpUrl() && getHttpsUrl() == service.getHttpsUrl()
    }

    override fun hashCode(): Int {
        return Objects.hash(getFullName(), getHttpUrl(), getHttpsUrl())
    }

    override fun toString(): String {
        return "Service{" +
                "fullName='" + fullName + '\'' +
                ", httpUrl='" + httpUrl + '\'' +
                ", httpsUrl='" + httpsUrl + '\'' +
                '}'
    }
}
