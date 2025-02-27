package org.spring.ch3.calculator.v2;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.spring.ch3.calculator.Calculator;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * v2. 초난감 calculatorV1에 예외 처리
 */
public class CalculatorV2 implements Calculator {
    private static final Log log = LogFactory.getLog(CalculatorV2.class);

    public int calcSum(String filePath) throws IOException {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(filePath));
            int sum = 0;
            String line = null;
            while ((line = br.readLine()) != null) {
                sum += Integer.valueOf(line);
            }
            return sum;
        } catch (IOException e) {
            log.info("[calcSum] exception message = " + e.getMessage());
            throw e;
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    log.info("[calcSum] exception message = " + e.getMessage());
                }
            }

        }
    }
}
