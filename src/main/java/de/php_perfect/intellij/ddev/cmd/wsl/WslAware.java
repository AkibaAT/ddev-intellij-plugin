package de.php_perfect.intellij.ddev.cmd.wsl;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.execution.wsl.WSLCommandLineOptions;
import com.intellij.execution.wsl.WSLDistribution;
import com.intellij.execution.wsl.WslPath;
import org.jetbrains.annotations.NotNull;

public class WslAware {
    public static <T extends GeneralCommandLine> T patchCommandLine(T commandLine) {
        WSLDistribution distribution = WslPath.getDistributionByWindowsUncPath(commandLine.getWorkDirectory().getPath());

        if (distribution == null) {
            return commandLine;
        }

        try {
            return applyWslPatch(commandLine, distribution);
        } catch (ExecutionException ignored) {
            return commandLine;
        }
    }

    @NotNull
    private static <T extends GeneralCommandLine> T applyWslPatch(T generalCommandLine, WSLDistribution distribution) throws ExecutionException {
        WSLCommandLineOptions options = new WSLCommandLineOptions()
                .setExecuteCommandInLoginShell(true)
                .setShellPath(distribution.getShellPath());

        return distribution.patchCommandLine(generalCommandLine, null, options);
    }
}
