package org.spring.ch5.transaction;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.List;


@RequiredArgsConstructor
public class UserService {

    public static final int MIN_LOGIN_FOR_SILVER = 50;
    public static final int MIN_RECOMMEND_FOR_GOLD = 30;

    @Value("${spring.mail.username}")
    private String username;

    protected final UserDao userDao;
    protected final PlatformTransactionManager transactionManager;
    protected final MailSender mailSender;


    public void upgradeLevels() {
        // 1. JDBC 트랜잭션 추상 오브젝트 생성
        //    JDBC 기반의 로컬 트랜잭션을 처리하기 위해 PlatformTransactionManager의 구현체 중 하나인 DataSourceTransactionManager 생성
//        PlatformTransactionManager transactionManager = new DataSourceTransactionManager(dataSource);

        // 2. 트랜잭션 시작
        //    DefaultTransactionDefinition : 트랜잭션에 대한 속성을 담고 있음 (파라미터를 통해 옵션 설정 가능)
        //    TransactionStatus : 생성된 트랜잭션의 상태 정보를 담고 있는 변수
        TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());

        try {
            List<User> users = userDao.getAll();
            for (User user : users) {
                if (canUpgradeLevel(user)) {
                    upgradeLevel(user);
                }
            }
            transactionManager.commit(status);
        } catch (RuntimeException e) {
            // commit, rollback 메서드에서 작업을 마무리한 후 트랜잭션 관련 리소스를 알아서 clean up 해준다.
            transactionManager.rollback(status);
            throw e;
        }
    }

    protected boolean canUpgradeLevel(User user) {
        Level level = user.getLevel();
        return switch (level) {
            case BASIC -> user.getLogin() >= 50;
            case SILVER -> user.getRecommend() >= 30;
            case GOLD -> false;
            default -> throw new IllegalArgumentException("[UserService.canUpgradeLevel] 존재하지 않는 Level입니다.");
        };
    }

    protected void upgradeLevel(User user) {
        user.upgradeLevel();
        userDao.update(user);
        sendUpgradeMail(user);
    }

    public void add(User user) {
        userDao.add(user);
    }

    private void sendUpgradeMail(User user) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setFrom(username);
        message.setSubject("Upgrade 안내");
        message.setText("사용자님의 등급이 " + user.getLevel().name() + "로 업그레이드 되었습니다!");

        mailSender.send(message);
    }

}
