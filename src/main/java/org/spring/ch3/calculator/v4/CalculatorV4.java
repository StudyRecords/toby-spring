package org.spring.ch3.calculator.v4;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.spring.ch3.calculator.v1.Calculator;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * v4. 템플릿/콜백 패턴 추가 발견 : 변하는 코드의 경계를 재정의
 */
public class CalculatorV4 implements Calculator {
    private static final Log log = LogFactory.getLog(CalculatorV4.class);

    public int calcSum(String filePath) throws IOException {
        LineCallback plusCallback = (line, value) -> {
            return value + Integer.parseInt(line);
        };
        return lineReadTemplate(filePath, plusCallback, 0);
    }

    public int calcMultiply(String filePath) throws IOException {
        LineCallback multiplyCallback = (line, value) -> {
            return value * Integer.parseInt(line);
        };
        return lineReadTemplate(filePath, multiplyCallback, 1);
    }

    private int lineReadTemplate(String filePath, LineCallback callback, Integer initValue) throws IOException {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(filePath));
            Integer value = initValue;
            String line = null;
            while ((line = br.readLine()) != null) {
                value = callback.calculateWithLine(line, value);
            }
            return value;
        } catch (IOException e) {
            log.info("[lineReadTemplate] exception message = " + e.getMessage());
            throw e;
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    log.info("[lineReadTemplate] br.close() exception message = " + e.getMessage());
                }
            }
        }
    }
}
