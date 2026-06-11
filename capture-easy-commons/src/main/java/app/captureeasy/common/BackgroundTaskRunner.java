package app.captureeasy.common;

import javax.swing.*;
import java.util.concurrent.Callable;
import java.util.function.Consumer;

public class BackgroundTaskRunner {
    public static <T> void runInBackground(Callable<T> task, Consumer<T> onSuccess, Consumer<Throwable> onError) {
        SwingWorker<T, Void> worker = new SwingWorker<T,Void>() {
            @Override
            protected T doInBackground() throws Exception {
                return task.call();
            }

            @Override
            protected void done() {
                try {
                    if (onSuccess != null) onSuccess.accept(get());
                } catch (Exception e) {
                    if (onError != null) onError.accept(e);
                }
            }
        };
        worker.execute();
    }
}