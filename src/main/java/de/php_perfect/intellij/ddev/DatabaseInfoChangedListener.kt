package de.php_perfect.intellij.ddev

import com.intellij.util.messages.Topic
import de.php_perfect.intellij.ddev.cmd.DatabaseInfo

interface DatabaseInfoChangedListener {
    fun onDatabaseInfoChanged(databaseInfo: DatabaseInfo?)

    companion object {
        val DATABASE_INFO_CHANGED_TOPIC: Topic<DatabaseInfoChangedListener> =
            Topic.create<DatabaseInfoChangedListener>(
                "DDEV Database Info Changed",
                DatabaseInfoChangedListener::class.java
            )
    }
}
