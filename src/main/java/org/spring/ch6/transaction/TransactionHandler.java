package org.spring.ch6.transaction;

import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class TransactionHandler implements InvocationHandler {

    // DI
    private final Object target;                // Object로 함으로써 모든 오브젝트에 부가기능 적용이 가능해짐
    private final PlatformTransactionManager transactionManager;
    private final String pattern;     // 타깃 오브젝트의 모든 메서드에 무조건 트랜잭션이 적용되지 않도록 트랜잭션을 적용할 메서드 이름의 패턴을 DI 받는다.
    // pattern을 "get"으로 주면 get으로 시작하는 모든 메서드에 트랜잭션이 적용된다.

    public TransactionHandler(Object target, PlatformTransactionManager transactionManager, String pattern) {
        this.target = target;
        this.transactionManager = transactionManager;
        this.pattern = pattern;         // pattern으로 시작하는 메서드에만 부가기능을 제공하겠다.
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (method.getName().startsWith(pattern)) {
            return invokeInTransaction(method, args);       // 부가기능 실행 후 타깃 오브젝트 호출
        }
        return method.invoke(target, args);     // 부가기능 없이 타깃 오브젝트 호출
    }

    private Object invokeInTransaction(Method method, Object[] args) throws Throwable {
        TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());       // 트랜잭션 시작
        try {
            Object ret = method.invoke(target, args);       // 위임 : 타깃 오브젝트 메서드 호출
            transactionManager.commit(status);              // 트랜잭션 커밋
            return ret;
        } catch (InvocationTargetException e) {             // 리플렉션 메서드인 Method.invoke() 에서는 타깃 오브젝트에서 예외 발생 시 InvocationTargetException로 한 번 포장한 후 예외를 던진다.
            transactionManager.rollback(status);            // 예외 발생 시 트랜잭션 롤백
            throw e.getTargetException();                   // 중첩되어 있는 예외 안에서 진짜 예외를 가져오는 과정
        }
    }
}
