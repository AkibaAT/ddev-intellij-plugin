package de.php_perfect.intellij.ddev.errorReporting

import com.intellij.openapi.application.ApplicationInfo
import io.sentry.Hint
import io.sentry.Sentry
import io.sentry.Sentry.OptionsConfiguration
import io.sentry.SentryEvent
import io.sentry.SentryOptions
import io.sentry.SentryOptions.BeforeSendCallback
import io.sentry.protocol.OperatingSystem
import io.sentry.protocol.SentryRuntime

object SentrySdkInitializer {
    private const val DSN = "https://92fd27b7c1fe48a98b040b3a1b603570@o1261149.ingest.sentry.io/6438173"

    @JvmStatic
    fun init() {
        Sentry.init(OptionsConfiguration { options: SentryOptions? ->
            options!!.setDsn(SentrySdkInitializer.DSN)
            options.setEnableUncaughtExceptionHandler(false)
            options.setBeforeSend(BeforeSendCallback { event: SentryEvent?, hint: Hint? ->
                event!!.setServerName(null)
                event.setEnvironment(null)
                event.setLevel(null)

                event.getContexts().setRuntime(SentrySdkInitializer.buildRuntimeContext())
                event.getContexts().setOperatingSystem(SentrySdkInitializer.buildOperatingSystemContext())

                event.setExtra("jdk.vendor", System.getProperty("java.vendor"))
                event.setExtra("jdk.version", System.getProperty("java.version"))
                event
            })
        })
    }

    private fun buildRuntimeContext(): SentryRuntime {
        val runtime = SentryRuntime()

        runtime.setName(ApplicationInfo.getInstance().getVersionName())
        runtime.setVersion(ApplicationInfo.getInstance().getFullVersion())
        runtime.setRawDescription(ApplicationInfo.getInstance().getBuild().asString())

        return runtime
    }

    private fun buildOperatingSystemContext(): OperatingSystem {
        val os = OperatingSystem()

        os.setName(System.getProperty("os.name"))
        os.setVersion(System.getProperty("os.version"))

        return os
    }
}
