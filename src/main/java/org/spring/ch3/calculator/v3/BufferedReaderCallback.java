package org.spring.ch3.calculator.v3;

import java.io.BufferedReader;
import java.io.IOException;

public interface BufferedReaderCallback {
    Integer calculateWithBR(BufferedReader br) throws IOException;
}
