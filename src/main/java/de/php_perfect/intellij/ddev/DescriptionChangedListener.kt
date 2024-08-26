package de.php_perfect.intellij.ddev

import com.intellij.util.messages.Topic
import de.php_perfect.intellij.ddev.cmd.Description

interface DescriptionChangedListener {
    fun onDescriptionChanged(description: Description?)

    companion object {
        val DESCRIPTION_CHANGED: Topic<DescriptionChangedListener> = Topic.create<DescriptionChangedListener>(
            "DDEV description changed",
            DescriptionChangedListener::class.java
        )
    }
}
