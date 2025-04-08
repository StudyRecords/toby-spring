package org.spring.ch6.transaction.userService;

import lombok.AllArgsConstructor;
import lombok.Setter;
import org.spring.ch6.transaction.User;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

@AllArgsConstructor
public class UserServiceTx implements UserService {

    private final PlatformTransactionManager transactionManager;
    private final UserService userService;

    @Override
    public void upgradeLevels() {
        // 1. JDBC 트랜잭션 추상 오브젝트 생성
        //    JDBC 기반의 로컬 트랜잭션을 처리하기 위해 PlatformTransactionManager의 구현체 중 하나인 DataSourceTransactionManager 생성
//        PlatformTransactionManager transactionManager = new DataSourceTransactionManager(dataSource);

        // 2. 트랜잭션 시작
        //    DefaultTransactionDefinition : 트랜잭션에 대한 속성을 담고 있음 (파라미터를 통해 옵션 설정 가능)
        //    TransactionStatus : 생성된 트랜잭션의 상태 정보를 담고 있는 변수

        TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());

        try {
            userService.upgradeLevels();
            transactionManager.commit(status);
        } catch (RuntimeException e) {
            // commit, rollback 메서드에서 작업을 마무리한 후 트랜잭션 관련 리소스를 알아서 clean up 해준다.
            transactionManager.rollback(status);
            throw e;
        }
    }

    @Override
    public void add(User user) {
        this.userService.add(user);
    }

}
