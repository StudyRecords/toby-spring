package org.spring.ch3.calculator.v1;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.spring.ch3.calculator.Calculator;
import org.spring.ch3.calculator.v2.CalculatorV2;

import java.util.Objects;

class CalculatorV1Test {

    private Calculator calculator;

    @BeforeEach
    public void init() {
//        this.calculator = new CalculatorV1();
        this.calculator = new CalculatorV2();
    }


    @Test
    public void sumOfNumbers() throws Exception {
        String filePath = Objects.requireNonNull(
                getClass().getClassLoader().getResource("numbers.txt")
        ).getPath();
        int sum = calculator.calcSum(filePath);
        Assertions.assertEquals(sum, 55);
    }
}
