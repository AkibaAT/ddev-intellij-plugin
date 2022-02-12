package de.php_perfect.intellij.ddev;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.startup.StartupActivity;
import de.php_perfect.intellij.ddev.config.DdevConfigurationProvider;
import org.jetbrains.annotations.NotNull;

public class PostStartupActivity implements StartupActivity, StartupActivity.DumbAware {
    @Override
    public void runActivity(@NotNull Project project) {
        DdevConfigurationProvider.getInstance(project).initialize();
    }
}
