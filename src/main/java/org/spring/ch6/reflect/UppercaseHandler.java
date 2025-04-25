package org.spring.ch6.reflect;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class UppercaseHandler implements InvocationHandler {

    // InvocationHandler의 invoke() 메서드가 타깃 오브젝트에게 위임할 때
    // 타겟 오브젝트가 필요하므로 미리 주입해둔다!
    Object target;

    public UppercaseHandler(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        String ret = (String) method.invoke(target, args);      // 타겟에 위임
        // 부가기능 제공
        if (ret instanceof String) {
            return ret.toUpperCase();
        }
        return ret;
    }
}
