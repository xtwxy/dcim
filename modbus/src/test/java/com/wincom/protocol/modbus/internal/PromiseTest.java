package com.wincom.protocol.modbus.internal;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.junit.Test;

/**
 *
 * @author master
 */
public class PromiseTest {

    @Test
    public void test() {
        Runnable runnable = () -> {
            System.out.println("Executing in "
                    + Thread.currentThread().getName());
        };

        ExecutorService executor = Executors.newSingleThreadExecutor();
        
        CompletableFuture<Void> cf
                = CompletableFuture.runAsync(runnable, executor);

        cf.thenRun(() -> System.out.println("I'm done."))
                .thenRun(() -> System.out.println("Once again, done."));

        executor.shutdown();
    }
}
