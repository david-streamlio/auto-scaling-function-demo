package io.streamnative.simulators;

import io.streamnative.simulators.strategy.ProcessingStrategy;
import org.apache.pulsar.functions.api.Context;
import org.apache.pulsar.functions.api.Function;

public class FixedWorkSimulator implements Function<String, Void> {

    private ProcessingStrategy strategy;

    @Override
    public Void process(String input, Context context) throws Exception {
        strategy.process(input);
        return null;
    }

    @Override
    public void initialize(Context context) throws Exception {
        Function.super.initialize(context);
        String clazz = context.getUserConfigValueOrDefault("strategyClassName",
                "io.streamnative.simulators.strategy.FindPrimes").toString();

        context.getLogger().info(String.format("Using %s strategy", clazz));
        strategy = (ProcessingStrategy) Class.forName(clazz).newInstance();
    }

    @Override
    public void close() throws Exception {
        Function.super.close();
    }
}
