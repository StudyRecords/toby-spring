package org.spring.ch6.transaction.proxyFactoryBean;

import lombok.RequiredArgsConstructor;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

//@Service
@RequiredArgsConstructor
public class TransactionAdvice implements MethodInterceptor {
    private final PlatformTransactionManager transactionManager;

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());       // 트랜잭션 시작
        try {
            Object callback = invocation.proceed();             // 위임 : 타깃 오브젝트 메서드 호출
            transactionManager.commit(status);                  // 트랜잭션 커밋
            return callback;
        } catch (RuntimeException e) {                          // JDK 다이내믹 프록시와 달리 타깃 호출 시 예외가 포장되지 않고 그대로 전달된다.
            transactionManager.rollback(status);                // 예외 발생 시 트랜잭션 롤백
            throw e;
        }
    }
}
