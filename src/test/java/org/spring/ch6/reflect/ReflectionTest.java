package org.spring.ch6.reflect;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;

public class ReflectionTest {
    @Test
    public void invokeMethod() throws Exception {
        String name = "lyouxsun";
        assertThat(name.length()).isEqualTo(8);

        Method lengthMethod = String.class.getMethod("length");
        assertThat((Integer) lengthMethod.invoke(name)).isEqualTo(8);

        assertThat(name.charAt(0)).isEqualTo('l');

        Method charAtMethod = String.class.getMethod("charAt", int.class);
        assertThat((Character) charAtMethod.invoke(name, 0)).isEqualTo('l');
    }

    @Test
    public void simpleProxy(){
        Hello hello = new HelloTarget();
        assertThat(hello.sayHello("lyouxsun")).isEqualTo("Hello lyouxsun");
        assertThat(hello.sayHi("lyouxsun")).isEqualTo("Hi lyouxsun");
        assertThat(hello.sayThankYou("lyouxsun")).isEqualTo("Thank You lyouxsun");

        Hello proxiedHello = new HelloProxy(hello);
        assertThat(proxiedHello.sayHello("lyouxsun")).isEqualTo("HELLO LYOUXSUN");
        assertThat(proxiedHello.sayHi("lyouxsun")).isEqualTo("HI LYOUXSUN");
        assertThat(proxiedHello.sayThankYou("lyouxsun")).isEqualTo("THANK YOU LYOUXSUN");
    }
}
