package io.streamnative.simulators;

import io.streamnative.simulators.strategy.MemoryIntenseStrategy;
import org.apache.pulsar.client.api.Schema;
import org.apache.pulsar.common.functions.ConsumerConfig;
import org.apache.pulsar.common.functions.FunctionConfig;
import org.apache.pulsar.functions.LocalRunner;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class FixedWorkSimulatorLocalRunner {

    private final static String IN = "persistent://public/default/data-in";
    private final static String OUT = "persistent://public/default/data-out";

    public static void main(String[] args) throws Exception {

        LocalRunner localRunner =
                LocalRunner.builder()
                        .brokerServiceUrl("pulsar://192.168.1.121:6650")
                        .functionConfig(getFunctionConfig())
                        .build();

        localRunner.start(false);
//        Thread.sleep(60 * 1000);
//        localRunner.stop();
    }

    private static FunctionConfig getFunctionConfig() {

        Map<String, ConsumerConfig> inputSpecs = new HashMap<String, ConsumerConfig>();
        inputSpecs.put(IN, ConsumerConfig.builder().schemaType(
                Schema.STRING.getSchemaInfo().getType().toString()).build());


        Map<String, Object> userConfigs = new HashMap<>();
        userConfigs.put("strategyClassName", MemoryIntenseStrategy.class.getName());

        return FunctionConfig.builder()
                .className(FixedWorkSimulator.class.getName())
                .cleanupSubscription(true)
                .inputs(Collections.singleton(IN))
                .inputSpecs(inputSpecs)
                .output(OUT)
                .runtime(FunctionConfig.Runtime.JAVA)
                .userConfig(userConfigs)
                .build();
    }

}
