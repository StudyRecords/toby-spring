package org.spring.ch3.calculator.v1;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.spring.ch3.calculator.v2.CalculatorV2;
import org.spring.ch3.calculator.v3.CalculatorV3;
import org.spring.ch3.calculator.v4.CalculatorV4;
import org.spring.ch3.calculator.v5.CalculatorV5;

class CalculatorTest {

    private String filePath;

    //    private Calculator calculator;
    @BeforeEach
    public void init() {
//        this.calculator = new CalculatorV1();
        this.filePath = getClass().getClassLoader().getResource("numbers.txt").getPath();
    }


    @Test
    public void sumOfNumbersV2() throws Exception {
        // given
        CalculatorV2 calculator = new CalculatorV2();

        // when & then
        int sum = calculator.calcSum(filePath);
        Assertions.assertEquals(sum, 55);
    }

    @Test
    public void sumOfNumbersV3() throws Exception {
        // given
        CalculatorV3 calculator = new CalculatorV3();

        // when & then
        int sum = calculator.calcSum(filePath);
        Assertions.assertEquals(sum, 55);

        // when & then
        int multiply = calculator.calcMultiply(filePath);
        Assertions.assertEquals(multiply, 3_628_800);
    }

    @Test
    public void sumOfNumbersV4() throws Exception {
        // given
        CalculatorV4 calculator = new CalculatorV4();

        // when & then
        int sum = calculator.calcSum(filePath);
        Assertions.assertEquals(sum, 55);

        // when & then
        int multiply = calculator.calcMultiply(filePath);
        Assertions.assertEquals(multiply, 3_628_800);
    }

    @Test
    public void sumOfNumbersV5() throws Exception {
        // given
        CalculatorV5 calculator = new CalculatorV5();

        // when & then
        int sum = calculator.calcSum(filePath);
        Assertions.assertEquals(sum, 55);

        // when & then
        int multiply = calculator.calcMultiply(filePath);
        Assertions.assertEquals(multiply, 3_628_800);


        // when & then
        String result = calculator.concatenate(filePath);
        Assertions.assertEquals(result, "12345678910");
    }
}

