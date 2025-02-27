package org.spring.ch3.calculator.v5;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * v5. 제네릭을 통해 파라미터/리턴 데이터의 타입 범용성 높이기
 */
public class CalculatorV5 {
    private static final Log log = LogFactory.getLog(CalculatorV5.class);

    public int calcSum(String filePath) throws IOException {
        LineCallbackV5<Integer> plusCallback = (line, value) -> {
            return value + Integer.parseInt(line);
        };
        return lineReadTemplateWithGenerics(filePath, plusCallback, 0);
    }

    public int calcMultiply(String filePath) throws IOException {
        LineCallbackV5<Integer> multiplyCallback = (line, value) -> {
            return value * Integer.parseInt(line);
        };
        return lineReadTemplateWithGenerics(filePath, multiplyCallback, 1);
    }

    public String concatenate(String filePath) throws IOException {
        LineCallbackV5<String> multiplyCallback = (line, value) -> value + line;
        return lineReadTemplateWithGenerics(filePath, multiplyCallback, "");
    }

    private <T> T lineReadTemplateWithGenerics(String filePath, LineCallbackV5<T> callback, T initValue) throws IOException {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(filePath));
            T value = initValue;
            String line = null;
            while ((line = br.readLine()) != null) {
                value = callback.calculateWithLine(line, value);
            }
            return value;
        } catch (IOException e) {
            log.info("[lineReadTemplateWithGenerics] exception message = " + e.getMessage());
            throw e;
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    log.info("[lineReadTemplateWithGenerics] br.close() exception message = " + e.getMessage());
                }
            }
        }
    }
}
