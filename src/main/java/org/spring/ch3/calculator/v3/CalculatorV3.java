package org.spring.ch3.calculator.v3;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.spring.ch3.calculator.v1.Calculator;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * v3. 템플릿/콜백 패턴 적용 : 중복되는 흐름을 템플릿으로, 변해야 하는 계산로직을 콜백으로 분리
 */
public class CalculatorV3 implements Calculator {
    private static final Log log = LogFactory.getLog(CalculatorV3.class);

    public int calcSum(String filePath) throws IOException {
        BufferedReaderCallback plusCallback = br -> {
            int sum = 0;
            String line = null;
            while ((line = br.readLine()) != null) {
                sum += Integer.valueOf(line);
            }
            return sum;
        };
        return fileReadTemplate(filePath, plusCallback);
    }

    public int calcMultiply(String filePath) throws IOException {
        BufferedReaderCallback multiplyCallback = br -> {
            int multiply = 1;
            String line = null;
            while ((line = br.readLine()) != null) {
                multiply *= Integer.valueOf(line);
            }
            return multiply;
        };
        return fileReadTemplate(filePath, multiplyCallback);

    }

    private int fileReadTemplate(String filePath, BufferedReaderCallback callback) throws IOException {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(filePath));
            return callback.calculateWithBR(br);
        } catch (IOException e) {
            log.info("[fileReadTemplate] exception message = " + e.getMessage());
            throw e;
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    log.info("[fileReadTemplate] br.close() exception message = " + e.getMessage());
                }
            }
        }
    }
}
