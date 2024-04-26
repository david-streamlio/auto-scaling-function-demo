package io.streamnative.simulators.strategy;

public interface ProcessingStrategy {

    public void process(String s, int numInstances, int launchDelay);

}
