package de.php_perfect.intellij.ddev.cmd;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.RunContentExecutor;
import com.intellij.execution.process.ColoredProcessHandler;
import com.intellij.execution.process.ProcessHandler;
import com.intellij.execution.process.ProcessTerminatedListener;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.ModalityState;
import com.intellij.openapi.components.Service;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Disposer;
import de.php_perfect.intellij.ddev.DdevIntegrationBundle;
import de.php_perfect.intellij.ddev.state.DdevStateManager;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Level;
import java.util.logging.Logger;

@Service(Service.Level.PROJECT)
public final class DdevRunnerImpl implements DdevRunner, Disposable {

    private final @NotNull Project project;

    public DdevRunnerImpl(@NotNull Project project) {
        this.project = project;
    }

    @Override
    public void runDdev(String ddevAction) {
        String title = DdevIntegrationBundle.message("ddev.run", this.ucFirst(ddevAction));
        ApplicationManager.getApplication().invokeLater(() -> {
            try {
                this.run(title, ddevAction);
            } catch (Throwable th) {
                Logger.getGlobal().log(Level.FINEST, "An error occurred", th);
            }
        }, ModalityState.NON_MODAL);
    }

    private void run(String title, String action) throws ExecutionException {
        final ProcessHandler process = this.createProcessHandler(action);
        final RunContentExecutor runContentExecutor = new RunContentExecutor(this.project, process).withTitle(title).withActivateToolWindow(true).withAfterCompletion(() -> DdevStateManager.getInstance(this.project).updateState()).withStop(process::destroyProcess, () -> !process.isProcessTerminated());
        Disposer.register(this, runContentExecutor);
        runContentExecutor.run();
    }

    private @NotNull String ucFirst(String string) {
        return string.substring(0, 1).toUpperCase() + string.substring(1);
    }

    private @NotNull ProcessHandler createProcessHandler(String ddevAction) throws ExecutionException {
        final ProcessHandler handler = new ColoredProcessHandler(this.createCommandLine(ddevAction));
        ProcessTerminatedListener.attach(handler);
        return handler;
    }

    private @NotNull WslAwareCommandLine createCommandLine(String ddevAction) {
        return new WslAwareCommandLine(this.project.getBasePath(), "ddev", ddevAction);
    }

    @Override
    public void dispose() {
    }
}
