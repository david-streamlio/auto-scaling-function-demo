package io.streamnative.simulators.utils;

import javax.management.ObjectName;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.List;

public class CpuSamplingThread extends Thread {

    private List<Double> cpuSamples = new ArrayList<>();

    private boolean keepRunning = true;

    @Override
    public void run() {
        while (keepRunning) {
            recordCPU();
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void halt() {
        this.keepRunning = false;
    }

    private void recordCPU() {
        try {
            double cpuLoad = (Double) ManagementFactory.getPlatformMBeanServer()
                    .getAttribute(new ObjectName("java.lang:type=OperatingSystem"), "CpuLoad");

            cpuSamples.add(cpuLoad);

        } catch (final Exception mfEx) {
            System.err.println("Couldn't retrieve CPU usage data due to " + mfEx.getMessage());
        }
    }

    public void report() {
        System.out.println(String.format("Average CPU usage: %3.4f", cpuSamples.stream()
                .mapToDouble(d -> d).average().orElse(0.0)));
    }

}
