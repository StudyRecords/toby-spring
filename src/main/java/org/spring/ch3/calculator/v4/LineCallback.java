package org.spring.ch3.calculator.v4;

import java.io.IOException;

public interface LineCallback {
    Integer calculateWithLine(String line, Integer value) throws IOException;
}
