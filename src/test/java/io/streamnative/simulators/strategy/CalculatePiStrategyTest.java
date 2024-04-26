package io.streamnative.simulators.strategy;

import org.junit.Test;

public class CalculatePiStrategyTest {

    @Test
    public final void simpleTest() {
        CalculatePi calc = new CalculatePi();
        calc.process("", 1, 1);
    }
}
