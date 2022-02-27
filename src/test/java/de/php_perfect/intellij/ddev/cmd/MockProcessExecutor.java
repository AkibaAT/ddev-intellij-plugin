package de.php_perfect.intellij.ddev.cmd;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.execution.process.ProcessOutput;
import com.intellij.openapi.util.SystemInfo;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public final class MockProcessExecutor implements ProcessExecutor {
    private final Map<String, ProcessOutput> processList = new HashMap<>();

    public MockProcessExecutor() {
        if (SystemInfo.isWindows) {
            this.addProcessOutput("where ddev", new ProcessOutput(1));
        } else {
            this.addProcessOutput("which ddev", new ProcessOutput(1));
        }
    }

    public void addProcessOutput(@NotNull String command, @NotNull ProcessOutput processOutput) {
        this.processList.put(command, processOutput);
    }

    @Override
    public @NotNull ProcessOutput executeCommandLine(GeneralCommandLine commandLine, int timeout) throws ExecutionException {
        String commandLineString = commandLine.getCommandLineString();

        if (!this.processList.containsKey(commandLineString)) {
            throw new ExecutionException(String.format("[TEST] Command '%s' was not expected", commandLineString));
        }

        ProcessOutput processOutput = this.processList.get(commandLineString);
        this.processList.remove(commandLineString);
        return processOutput;
    }
}
