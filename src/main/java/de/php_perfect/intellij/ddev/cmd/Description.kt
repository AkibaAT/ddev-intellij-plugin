package de.php_perfect.intellij.ddev.cmd

import com.google.gson.annotations.SerializedName
import java.util.HashMap
import java.util.Objects

class Description(
    name: String?,
    phpVersion: String?,
    status: Status?,
    mailHogHttpsUrl: String?,
    mailHogHttpUrl: String?,
    mailpitHttpsUrl: String?,
    mailpitHttpUrl: String?,
    services: MutableMap<String?, Service?>?,
    databaseInfo: DatabaseInfo?,
    primaryUrl: String?
) {
    enum class Status {
        @SerializedName("running")
        RUNNING,

        @SerializedName("starting")
        STARTING,

        @SerializedName("stopped")
        STOPPED,

        @SerializedName("project directory missing")
        DIR_MISSING,

        @SerializedName(".ddev/config.yaml missing")
        CONFIG_MISSING,

        @SerializedName("paused")
        PAUSED,

        @SerializedName("unhealthy")
        UNHEALTHY,
    }

    private val name: String?

    private val phpVersion: String?

    private val status: Status?

    @SerializedName("mailhog_https_url")
    private val mailHogHttpsUrl: String?

    @SerializedName("mailhog_url")
    private val mailHogHttpUrl: String?

    private val mailpitHttpsUrl: String?

    private val mailpitHttpUrl: String?


    private val services: MutableMap<String?, Service?>?

    @SerializedName("dbinfo")
    private val databaseInfo: DatabaseInfo?

    @SerializedName("primary_url")
    private val primaryUrl: String?

    constructor(
        name: String?,
        phpVersion: String?,
        status: Status?,
        mailHogHttpsUrl: String?,
        mailHogHttpUrl: String?,
        mailpitHttpsUrl: String?,
        mailpitHttpUrl: String?,
        databaseInfo: DatabaseInfo?,
        primaryUrl: String?
    ) : this(
        name,
        phpVersion,
        status,
        mailHogHttpsUrl,
        mailHogHttpUrl,
        mailpitHttpsUrl,
        mailpitHttpUrl,
        HashMap<String?, Service?>(),
        databaseInfo,
        primaryUrl
    )

    init {
        this.name = name
        this.phpVersion = phpVersion
        this.status = status
        this.mailHogHttpsUrl = mailHogHttpsUrl
        this.mailHogHttpUrl = mailHogHttpUrl
        this.mailpitHttpsUrl = mailpitHttpsUrl
        this.mailpitHttpUrl = mailpitHttpUrl
        this.services = services
        this.databaseInfo = databaseInfo
        this.primaryUrl = primaryUrl
    }

    fun getName(): String? {
        return name
    }

    fun getPhpVersion(): String? {
        return this.phpVersion
    }

    fun getStatus(): Status? {
        return this.status
    }

    fun getMailHogHttpsUrl(): String? {
        return this.mailHogHttpsUrl
    }

    fun getMailHogHttpUrl(): String? {
        return this.mailHogHttpUrl
    }

    fun getMailpitHttpsUrl(): String? {
        return mailpitHttpsUrl
    }

    fun getMailpitHttpUrl(): String? {
        return mailpitHttpUrl
    }

    fun getServices(): MutableMap<String?, Service?> {
        if (this.services == null) {
            return HashMap<String?, Service?>()
        }

        val serviceMap = HashMap<String?, Service?>(this.services)

        if (this.getMailHogHttpsUrl() != null || this.getMailHogHttpUrl() != null) {
            serviceMap.put(
                "mailhog",
                Service("ddev-" + this.getName() + "-mailhog", this.getMailHogHttpsUrl(), this.getMailHogHttpUrl())
            )
        }

        if (this.getMailpitHttpsUrl() != null || this.getMailpitHttpUrl() != null) {
            serviceMap.put(
                "mailpit",
                Service("ddev-" + this.getName() + "-mailpit", this.getMailpitHttpsUrl(), this.getMailpitHttpsUrl())
            )
        }

        return serviceMap
    }

    fun getDatabaseInfo(): DatabaseInfo? {
        return databaseInfo
    }

    fun getPrimaryUrl(): String? {
        return primaryUrl
    }

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val that = o as Description
        return getName() == that.getName() && getPhpVersion() == that.getPhpVersion() && getStatus() == that.getStatus() && getMailHogHttpsUrl() == that.getMailHogHttpsUrl() && getMailHogHttpUrl() == that.getMailHogHttpUrl() && getMailpitHttpsUrl() == that.getMailpitHttpsUrl() && getMailpitHttpUrl() == that.getMailpitHttpUrl() && getServices() == that.getServices() && getDatabaseInfo() == that.getDatabaseInfo() && getPrimaryUrl() == that.getPrimaryUrl()
    }

    override fun hashCode(): Int {
        return Objects.hash(
            getName(),
            getPhpVersion(),
            getStatus(),
            getMailHogHttpsUrl(),
            getMailHogHttpUrl(),
            getMailpitHttpsUrl(),
            getMailpitHttpUrl(),
            getServices(),
            getDatabaseInfo(),
            getPrimaryUrl()
        )
    }

    override fun toString(): String {
        return "Description{" +
                "name='" + name + '\'' +
                ", phpVersion='" + phpVersion + '\'' +
                ", status=" + status +
                ", mailHogHttpsUrl='" + mailHogHttpsUrl + '\'' +
                ", mailHogHttpUrl='" + mailHogHttpUrl + '\'' +
                ", mailpitHttpsUrl='" + mailpitHttpsUrl + '\'' +
                ", mailpitHttpUrl='" + mailpitHttpUrl + '\'' +
                ", services=" + services +
                ", databaseInfo=" + databaseInfo +
                ", primaryUrl='" + primaryUrl + '\'' +
                '}'
    }
}
