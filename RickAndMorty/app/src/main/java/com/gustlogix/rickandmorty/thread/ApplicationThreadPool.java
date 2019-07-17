package com.gustlogix.rickandmorty.thread;

import android.os.Handler;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ApplicationThreadPool {
    static ExecutorService executorService = Executors.newFixedThreadPool(10);

    public static <T> void execute(final Task<T> task, final TaskCallback<T> callback) {
        final Handler callerThreadHandler = new Handler();
        executorService.execute(new Runnable() {

            @Override
            public void run() {
                try {
                    final T result = task.execute();
                    if (callback != null) {
                        callerThreadHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                callback.onResult(result);
                            }
                        });
                    }
                } catch (final Exception e) {
                    if (callback != null) {

                        callerThreadHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                callback.onError(e);
                            }
                        });
                    }
                }
            }

        });
    }

}
