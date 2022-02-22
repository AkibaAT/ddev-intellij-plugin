package de.php_perfect.intellij.ddev.actions;

import com.intellij.openapi.project.Project;
import de.php_perfect.intellij.ddev.cmd.DdevRunner;
import de.php_perfect.intellij.ddev.state.DdevStateManager;
import de.php_perfect.intellij.ddev.state.State;
import org.jetbrains.annotations.NotNull;

public final class DdevConfigAction extends DdevRunAction {
    @Override
    protected void run(@NotNull Project project) {
        DdevRunner.getInstance().config(project);
    }

    @Override
    protected boolean isActive(@NotNull Project project) {
        final State state = DdevStateManager.getInstance(project).getState();

        return state.isInstalled() && !state.isConfigured();
    }
}
