package de.php_perfect.intellij.ddev.version;

import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import de.php_perfect.intellij.ddev.cmd.Versions;
import de.php_perfect.intellij.ddev.notification.DdevNotifier;
import de.php_perfect.intellij.ddev.state.DdevStateManager;
import de.php_perfect.intellij.ddev.state.State;
import de.php_perfect.intellij.ddev.version.util.VersionCompare;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class VersionCheckerImpl implements VersionChecker {
    private final @NotNull Project project;

    public VersionCheckerImpl(@NotNull Project project) {
        this.project = project;
    }

    @Override
    public void checkDdevVersion() {
        this.checkDdevVersion(false);
    }

    @Override
    public void checkDdevVersion(boolean confirmNewestVersion) {
        State state = DdevStateManager.getInstance(this.project).getState();
        String currentVersion = this.getCurrentVersion(state);

        if (currentVersion == null) {
            if (state.isConfigured()) {
                DdevNotifier.getInstance(project).asyncNotifyInstallDdev();
            }
            return;
        }

        ProgressManager.getInstance().run(new Task.Backgroundable(project, "Checking DDEV version", true) {
            @Override
            public void run(@NotNull ProgressIndicator progressIndicator) {
                final LatestRelease latestRelease = ReleaseClient.getInstance().loadCurrentVersion(progressIndicator);
                progressIndicator.checkCanceled();

                if (latestRelease == null || latestRelease.getTagName() == null) {
                    return;
                }

                final String latestVersion = latestRelease.getTagName();

                if (VersionCompare.needsUpdate(currentVersion, latestVersion)) {
                    DdevNotifier.getInstance(project).asyncNotifyNewVersionAvailable(currentVersion, latestVersion);
                } else if (confirmNewestVersion) {
                    DdevNotifier.getInstance(project).asyncNotifyAlreadyLatestVersion();
                }
            }
        });
    }

    private @Nullable String getCurrentVersion(State state) {
        if (!state.isInstalled()) {
            return null;
        }

        Versions versions = state.getVersions();

        if (versions == null) {
            return null;
        }

        return versions.getDdevVersion();
    }
}
