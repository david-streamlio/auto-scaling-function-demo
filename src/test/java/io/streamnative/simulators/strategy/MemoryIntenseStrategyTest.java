package io.streamnative.simulators.strategy;

import io.streamnative.simulators.strategy.MemoryIntenseStrategy;
import org.junit.Test;

public class MemoryIntenseStrategyTest {

    @Test
    public final void simpleTest() {
        MemoryIntenseStrategy strategy = new MemoryIntenseStrategy();
        strategy.process("");
    }
}
