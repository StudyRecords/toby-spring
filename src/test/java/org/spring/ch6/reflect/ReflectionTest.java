package org.spring.ch6.reflect;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.junit.jupiter.api.Test;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

public class ReflectionTest {

    // ======================== JDK 다이내믹 프록시 생성 ========================

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
    public void simpleProxy() {
        Hello hello = new HelloTarget();
        assertThat(hello.sayHello("lyouxsun")).isEqualTo("Hello lyouxsun");
        assertThat(hello.sayHi("lyouxsun")).isEqualTo("Hi lyouxsun");
        assertThat(hello.sayThankYou("lyouxsun")).isEqualTo("Thank You lyouxsun");

        Hello proxiedHello = new HelloProxy(hello);
        assertThat(proxiedHello.sayHello("lyouxsun")).isEqualTo("HELLO LYOUXSUN");
        assertThat(proxiedHello.sayHi("lyouxsun")).isEqualTo("HI LYOUXSUN");
        assertThat(proxiedHello.sayThankYou("lyouxsun")).isEqualTo("THANK YOU LYOUXSUN");
    }

    @Test
    public void dynamicProxy() {
        Hello proxiedHello = (Hello) Proxy.newProxyInstance(
                getClass().getClassLoader(),
                new Class[]{Hello.class},
                new UppercaseHandler(new HelloTarget())
        );
        assertThat(proxiedHello.sayHello("lyouxsun")).isEqualTo("HELLO LYOUXSUN");
        assertThat(proxiedHello.sayHi("lyouxsun")).isEqualTo("HI LYOUXSUN");
        assertThat(proxiedHello.sayThankYou("lyouxsun")).isEqualTo("THANK YOU LYOUXSUN");
    }

    // ======================== 스프링 ProxyFactoryBean을 이용한 다이내믹 프록시 테스트 ========================
    @Test
    public void simpleSpringProxy() {
        // given
        ProxyFactoryBean proxyFactoryBean = new ProxyFactoryBean();
        proxyFactoryBean.setTarget(new HelloTarget());          // 타깃 설정
        proxyFactoryBean.addAdvice(new UppercaseAdvice());      // 부가기능을 담은 어드바이스를 추가

        // when
        Hello proxiedHello = (Hello) proxyFactoryBean.getObject();

        // then
        assertThat(proxiedHello.sayHello("lyouxsun")).isEqualTo("HELLO LYOUXSUN");
        assertThat(proxiedHello.sayHi("lyouxsun")).isEqualTo("HI LYOUXSUN");
        assertThat(proxiedHello.sayThankYou("LYOuxSUN")).isEqualTo("THANK YOU LYOUXSUN");
    }

    static class UppercaseAdvice implements MethodInterceptor {

        @Override
        public Object invoke(MethodInvocation invocation) throws Throwable {
            String result = (String) invocation.proceed();              // 위임부분 : MethodInvocation은 메서드 정보와 타깃 오브젝트를 알고 있으므로
            // 메서드 실행 시 타깃 오브젝트를 전달할 필요가 없다.
            return Objects.requireNonNull(result).toUpperCase();        // 부가기능 적용
        }
    }

    // 포인트컷을 통해 부가기능 적용할 메서드 선정코드 구현
    @Test
    public void pointcutAdvisor() {
        // given
        ProxyFactoryBean proxyFactoryBean = new ProxyFactoryBean();
        proxyFactoryBean.setTarget(new HelloTarget());

        NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut();
        pointcut.setMappedName("sayH*");

        // 포인트컷과 어드바이스를 Advisor로 묶어서 한번에 추가해줘야 한다.
        proxyFactoryBean.addAdvisor(new DefaultPointcutAdvisor(pointcut, new UppercaseAdvice()));

        Hello proxiedHello = (Hello) proxyFactoryBean.getObject();

        // then
        assertThat(proxiedHello.sayHello("lyouxsun")).isEqualTo("HELLO LYOUXSUN");
        assertThat(proxiedHello.sayHi("lyouxsun")).isEqualTo("HI LYOUXSUN");
        assertThat(proxiedHello.sayThankYou("lyouxsun")).isEqualTo("Thank You lyouxsun");

    }

}
