package org.spring.ch6.transaction.factoryBean;

import lombok.Getter;
import lombok.Setter;
import org.spring.ch6.transaction.refelction.TransactionHandler;
import org.spring.ch6.transaction.userService.UserService;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import java.lang.reflect.Proxy;

@Getter
public class TxProxyFactoryBean implements FactoryBean<Object> {
    @Setter
    private Object target;            // UserService 타입 외의 다른 오브젝트를 위한 프록시를 만들 때에도 재사용 가능
    private final PlatformTransactionManager transactionManager;
    private final String pattern;
    private final Class<?> serviceInterface;      // 다이내믹 프록시를 생성할 때 필요. UserService 외의 인터페이스를 가진 타깃에도 적용할 수 있다.
    // 어디서? 어떻게 사용돼??


    public TxProxyFactoryBean(Object target, PlatformTransactionManager transactionManager, String pattern, Class<?> serviceInterface) {
        this.target = target;
        this.transactionManager = transactionManager;
        this.pattern = pattern;
        this.serviceInterface = serviceInterface;
    }

    // FactoryBean 인터페이스 구현 메서드
    @Override
    public Object getObject() throws Exception {        // DI 받은 정보를 이용해서 TransactionHandler를 사용하는 다이내믹 프록시를 생성
        TransactionHandler transactionHandler = new TransactionHandler(target, transactionManager, pattern);
        return Proxy.newProxyInstance(
                getClass().getClassLoader(),
                new Class[]{UserService.class},
                transactionHandler
        );
    }

    @Override
    public Class<?> getObjectType() {
        return serviceInterface;        // 팩토리 빈이 생성하는 오브젝트의 타입은 DI 받은 인터페이스 타입에 따라 달라진다.
                                        // 따라서 다양한 타입의 프록시 오브젝트 생성에 재사용할 수 있다.
    }

    @Override
    public boolean isSingleton() {
        return false;       // 싱글톤 빈이 아니다 => (x)
                            // getObject()가 매번 같은 오브젝트를 리턴하지 않는다. => (O)
    }
}
