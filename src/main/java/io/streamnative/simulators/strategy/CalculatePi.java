package io.streamnative.simulators.strategy;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import io.streamnative.simulators.utils.CpuSamplingThread;

public class CalculatePi implements ProcessingStrategy {

    ExecutorService executor = Executors.newFixedThreadPool(10);

    @Override
    public void process(String s) {
        List<Callable<BigDecimal>> tasks = new ArrayList<>();

        for (int idx = 0; idx < 5; idx++) {
            tasks.add( () -> { return new PiCalculator(20000).calculatePi(); });
        }

        CpuSamplingThread samplingThread = new CpuSamplingThread();

        try {
            samplingThread.start();
            List<Future<BigDecimal>> futures = executor.invokeAll(tasks);
            futures.forEach( f -> {
                try {
                    System.out.println("Pi = " + f.get());
                } catch (final Exception ex) {
                    ex.printStackTrace();
                }
            });
        } catch (InterruptedException e) {
            System.err.println("Well that was unexpected. ");
            e.printStackTrace();
        } finally {
            samplingThread.halt();
            samplingThread.report();
        }
    }

    private static class PiCalculator {

        private final int precision;

        public PiCalculator(int precision) {
            this.precision = precision;
        }

        public BigDecimal calculatePi() {
            BigDecimal pi = BigDecimal.ZERO;
            BigDecimal two = BigDecimal.valueOf(2);

            for (int i = 0; i < precision; i++) {
                BigDecimal term = BigDecimal.ONE.divide(BigDecimal.valueOf(2 * i + 1), precision, BigDecimal.ROUND_HALF_UP);
                if (i % 2 == 0) {
                    pi = pi.add(term);
                } else {
                    pi = pi.subtract(term);
                }
            }

            return pi.multiply(two);
        }
    }
}
