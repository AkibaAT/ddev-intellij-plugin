package de.php_perfect.intellij.ddev.version

import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.util.io.HttpRequests
import java.io.IOException

class GithubClient : ReleaseClient {
    override fun loadCurrentVersion(indicator: ProgressIndicator): LatestRelease? {
        val requestBuilder =
            HttpRequests.request(GithubClient.Companion.RELEASE_URL).accept("application/json").redirectLimit(2)
        indicator.checkCanceled()

        try {
            GithubClient.Companion.LOG.info("Loading latest DDEV release meta data from GitHub")
            return createParser().fromJson<LatestRelease?>(
                requestBuilder.readString(indicator),
                LatestRelease::class.java
            )
        } catch (e: IOException) {
            GithubClient.Companion.LOG.error(e)
            return null
        }
    }

    private fun createParser(): Gson {
        return GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create()
    }

    companion object {
        private const val RELEASE_URL = "https://github.com/ddev/ddev/releases/latest"

        private val LOG = Logger.getInstance(GithubClient::class.java)
    }
}
