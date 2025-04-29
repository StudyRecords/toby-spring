package org.spring.ch6.expressionPointcut;

import org.junit.jupiter.api.Test;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;

import static org.assertj.core.api.Assertions.assertThat;

public class ExpressionPointcutTest {
    @Test
    public void methodSignaturePointcut() throws SecurityException, NoSuchMethodException {
        AspectJExpressionPointcut expressionPointcut = new AspectJExpressionPointcut();
        expressionPointcut.setExpression("execution(public int org.spring.ch6.expressionPointcut.Target.minus(int,int) throws java.lang.RuntimeException)");

        assertThat(expressionPointcut.getClassFilter().matches(Target.class)
                && expressionPointcut.getMethodMatcher().matches(Target.class.getMethod("minus", int.class, int.class), Target.class)).isTrue();
    }
}
