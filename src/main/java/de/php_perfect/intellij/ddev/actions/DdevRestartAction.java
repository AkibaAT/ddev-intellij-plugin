package de.php_perfect.intellij.ddev.actions;

import com.intellij.openapi.project.Project;
import de.php_perfect.intellij.ddev.cmd.Description;
import de.php_perfect.intellij.ddev.config.DdevConfigurationProvider;
import org.jetbrains.annotations.NotNull;

public class DdevRestartAction extends DdevRunAction {
    public DdevRestartAction() {
        super("restart");
    }

    protected boolean isActive(@NotNull Project project) {
        final DdevConfigurationProvider ddevConfigurationProvider = DdevConfigurationProvider.getInstance(project);

        if (!ddevConfigurationProvider.isInstalled()) {
            return false;
        }

        Description status = ddevConfigurationProvider.getStatus();

        if (status == null) {
            return false;
        }

        return status.getStatus() == Description.Status.RUNNING || status.getStatus() == Description.Status.UNDEFINED;
    }
}
