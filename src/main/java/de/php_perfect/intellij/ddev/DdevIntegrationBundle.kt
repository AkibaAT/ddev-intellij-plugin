package de.php_perfect.intellij.ddev

import com.intellij.DynamicBundle
import org.jetbrains.annotations.Nls
import org.jetbrains.annotations.NonNls
import org.jetbrains.annotations.PropertyKey
import java.util.function.Supplier

@NonNls
private const val BUNDLE = "messages.DdevIntegrationBundle"

object DdevIntegrationBundle : DynamicBundle(BUNDLE) {
    @NonNls
    private const val BUNDLE = "messages.DdevIntegrationBundle"

    @Nls
    fun message(
        @PropertyKey(resourceBundle = BUNDLE) key: String,
        vararg params: Any
    ): String = getMessage(key, *params)

    fun messagePointer(
        @PropertyKey(resourceBundle = BUNDLE) key: String,
        vararg params: Any
    ): Supplier<@Nls String> = getLazyMessage(key, *params)

    fun getName(): String = BUNDLE
}
