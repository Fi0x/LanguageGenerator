package io.fi0x.languagegenerator.logic;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

@Slf4j
public class Parallelization
{
    public static <T> CompletableFuture<T> runAndGetFuture(T callable)
    {
        CompletableFuture<T> future = new CompletableFuture<>();
        CompletableFuture.runAsync(() -> future.complete(callable), ForkJoinPool.commonPool());

        return future;
    }

    public static <T> CompletableFuture<T> runAndGetFuture(Callable<T> callable) {
        CompletableFuture<T> future = new CompletableFuture<>();
        CompletableFuture.runAsync(() -> {
            try {
                future.complete(callable.call());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        return future;
    }

    public static <T> T getWithoutException(Future<T> future) {
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            log.debug("Exception thrown when getting result of future", e);
        }
        return null;
    }

    public static <T> T getWithoutExecutionException(Future<T> future) throws InterruptedException
    {
        try {
            return future.get();
        }  catch (ExecutionException e) {
            log.debug("Exception thrown when getting result of future", e);
        }
        return null;
    }
}
