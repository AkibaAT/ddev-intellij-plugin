package de.php_perfect.intellij.ddev.cmd;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface Ddev {
    @Nullable String findBinary(@NotNull Project project) throws CommandFailedException;

    @NotNull Versions version(@NotNull Project project) throws CommandFailedException;

    @NotNull Description describe(@NotNull Project project) throws CommandFailedException;

    static Ddev getInstance() {
        return ApplicationManager.getApplication().getService(Ddev.class);
    }
}
