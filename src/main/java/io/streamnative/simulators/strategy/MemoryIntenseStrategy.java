package io.streamnative.simulators.strategy;

import org.apache.commons.lang3.RandomStringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class MemoryIntenseStrategy implements ProcessingStrategy {

    ExecutorService executor = Executors.newFixedThreadPool(20);

    @Override
    public void process(String s) {

        List<Callable<String>> tasks = new ArrayList<>();

        for (int idx = 0; idx < 5; idx++) {
            tasks.add(() -> { return new RandomStringSorter(500000l, 1000).generateAndSort(); });
        }

        try {
            List<Future<String>> futures = executor.invokeAll(tasks);
            futures.forEach(f -> {
                try {
                    System.out.println(f.get());
                } catch (final Exception ex) {
                    ex.printStackTrace();
                }
            });
        } catch (final Exception ex) {

        }
    }

    private static class RandomStringSorter {
        private final long numberOfStrings;

        private final int stringLength;

        List<String> randomStrings = new ArrayList<String>();

        private RandomStringSorter(long numberOfStrings, int stringLength) {
            this.numberOfStrings = numberOfStrings;
            this.stringLength = stringLength;
        }

        public String generateAndSort() {
            for (long i =0; i < numberOfStrings; i++) {
                randomStrings.add(RandomStringUtils.randomAlphabetic(stringLength) +
                        RandomStringUtils.randomAlphabetic(stringLength));
            }

            Collections.sort(randomStrings);
            return "";
        }
    }
}
