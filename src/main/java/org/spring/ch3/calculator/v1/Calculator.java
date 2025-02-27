package org.spring.ch3.calculator.v1;

import java.io.IOException;

public interface Calculator {
    int calcSum(String filePath) throws IOException;
}
