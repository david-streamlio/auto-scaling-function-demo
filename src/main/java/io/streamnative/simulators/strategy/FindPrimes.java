package io.streamnative.simulators.strategy;

import io.streamnative.simulators.utils.CpuSamplingThread;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class FindPrimes implements ProcessingStrategy {

    ExecutorService executor = Executors.newFixedThreadPool(20);

    @Override
    public void process(String s, int numInstances, int launchDelay) {

        CpuSamplingThread samplingThread = new CpuSamplingThread();
        samplingThread.start();
        List<Future<String>> futures = new ArrayList<>();

        for (int idx = 0; idx < numInstances; idx++) {
            futures.add(executor.submit(() -> { return new PrimeFinder(getMaxLong()).findPrimes(); }));
            try {
                Thread.sleep(launchDelay * 1000);
            } catch (InterruptedException iEx) {
                System.err.println(iEx);
            }
        }

        try {
            futures.forEach( f -> {
                try {
                    System.out.println(f.get());
                } catch (final Exception ex) {
                    ex.printStackTrace();
                }
            });
        } finally {
            samplingThread.halt();
            samplingThread.report();
        }
    }

    private static long getMaxLong() {
        Random rnd = new Random();

        return 400000L + (rnd.nextInt(9) * 10000)
                + (rnd.nextInt(9) * 1000)
                + (rnd.nextInt(9) * 100)
                + (rnd.nextInt(9) * 10)
                + rnd.nextInt(9);
    }

    private static class PrimeFinder {

        private final long maxLong;

        public PrimeFinder(long maxLong) {
            this.maxLong = maxLong;
        }

        public String findPrimes() {
            long count = 0, max = 0;
            for (long i=3; i<= maxLong; i++) {
                boolean isPrime = true;
                for (long j=2; j<=i/2 && isPrime; j++) {
                    isPrime = i % j > 0;
                }
                if (isPrime) {
                    count++;
                    max = i;
                }
            }

            return String.format("Number of primes: %d, largest prime number: %d", count, max);
        }
    }
}
