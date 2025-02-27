package org.spring.ch3.calculator.v5;

import java.io.IOException;

public interface LineCallbackV5<T> {
    T calculateWithLine(String line, T value) throws IOException;
}
