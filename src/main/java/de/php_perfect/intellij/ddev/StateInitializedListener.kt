package de.php_perfect.intellij.ddev

import com.intellij.util.messages.Topic
import de.php_perfect.intellij.ddev.state.State

interface StateInitializedListener {
    fun onStateInitialized(state: State)

    companion object {
        val STATE_INITIALIZED: Topic<StateInitializedListener> =
            Topic.create<StateInitializedListener>("DDEV state initialized", StateInitializedListener::class.java)
    }
}
