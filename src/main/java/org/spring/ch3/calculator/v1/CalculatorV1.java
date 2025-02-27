package org.spring.ch3.calculator.v1;

import org.spring.ch3.calculator.Calculator;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * v1. 초난감 calculator
 *    - 아직 예외 처리가 안 되어 있음
 */
public class CalculatorV1 implements Calculator {
    public int calcSum(String filePath) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(filePath));
        int sum = 0;
        String line = null;
        while ((line = br.readLine()) != null) {
            sum += Integer.valueOf(line);
        }
        br.close();
        return sum;
    }
}
