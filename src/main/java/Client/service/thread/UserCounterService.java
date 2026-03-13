package Client.service.thread;

import Client.model.User;
import Client.collection.CustomUserCollection;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;

public class UserCounterService {

    private final int threadCount;

    public UserCounterService() {
        this.threadCount = Runtime.getRuntime().availableProcessors();
    }

    public UserCounterService(int threadCount) {
        this.threadCount = threadCount;
    }

    public int countOccurrencesParallel(CustomUserCollection collection, User target) {
        if (collection == null || collection.isEmpty() || target == null) {
            return 0;
        }

        List<User> users = collection.toList();

        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        List<Future<Integer>> futures = new ArrayList<>();

        int chunkSize = (int) Math.ceil(users.size() / (double) threadCount);

        for (int i = 0; i < threadCount; i++) {
            int start = i * chunkSize;
            int end = Math.min(start + chunkSize, users.size());

            if (start >= end) break;

            List<User> subList = users.subList(start, end);
            Callable<Integer> task = new CountTask(subList, target);
            futures.add(executor.submit(task));
        }

        AtomicInteger total = new AtomicInteger(0);
        for (Future<Integer> future : futures) {
            try {
                total.addAndGet(future.get());
            } catch (Exception e) {
                System.err.println("❌ Ошибка при подсчете: " + e.getMessage());
            }
        }

        executor.shutdown();
        try {
            if (!executor.awaitTermination(1, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }

        return total.get();
    }

    public int countByPredicateParallel(CustomUserCollection collection, Predicate<User> predicate) {
        if (collection == null || collection.isEmpty() || predicate == null) {
            return 0;
        }

        return (int) collection.parallelStream()
                .filter(predicate)
                .count();
    }

    private static class CountTask implements Callable<Integer> {
        private final List<User> users;
        private final User target;

        public CountTask(List<User> users, User target) {
            this.users = users;
            this.target = target;
        }

        @Override
        public Integer call() {
            int count = 0;
            for (User user : users) {
                if (user.equals(target)) {
                    count++;
                }
            }
            return count;
        }
    }
}