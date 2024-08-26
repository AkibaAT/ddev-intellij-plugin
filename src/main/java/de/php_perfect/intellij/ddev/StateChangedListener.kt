package de.php_perfect.intellij.ddev

import com.intellij.util.messages.Topic
import de.php_perfect.intellij.ddev.state.State

interface StateChangedListener {
    fun onDdevChanged(state: State)

    companion object {
        val DDEV_CHANGED: Topic<StateChangedListener> =
            Topic.create<StateChangedListener>("DDEV state changed", StateChangedListener::class.java)
    }
}
