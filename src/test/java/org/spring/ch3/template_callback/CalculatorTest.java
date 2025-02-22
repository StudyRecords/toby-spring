package org.spring.ch3.template_callback;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Objects;

class CalculatorTest {

    @Test
    public void sumOfNumbers() throws Exception {
        Calculator calculator = new Calculator();
        String filePath = Objects.requireNonNull(
                getClass().getClassLoader().getResource("numbers.txt")
        ).getPath();
        int sum = calculator.calcSum(filePath);
        Assertions.assertEquals(sum, 55);
    }
}
