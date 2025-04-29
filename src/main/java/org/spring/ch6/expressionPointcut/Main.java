package org.spring.ch6.expressionPointcut;

public class Main {
    public static void main(String[] args) throws NoSuchMethodException {
        System.out.println(Target.class.getMethod("minus", int.class, int.class));
    }
}
