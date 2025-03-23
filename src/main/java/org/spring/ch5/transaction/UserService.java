package org.spring.ch5.transaction;

import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;


public class UserService {

    public static final int MIN_LOGIN_FOR_SILVER = 50;
    public static final int MIN_RECOMMEND_FOR_GOLD = 30;

    protected UserDao userDao;
    protected DataSource dataSource;

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }


    public void upgradeLevels() throws SQLException {
        TransactionSynchronizationManager.initSynchronization();        // 트랜잭션 동기화 관리자를 이용해 동기화 작업 초기화
        Connection connection = DataSourceUtils.getConnection(dataSource);        // DB 커넥션을 생성 & 동기화 동시에 수행
        connection.setAutoCommit(false);                                               // 트랜잭션 시작

        List<User> users = userDao.getAll();

        try {
            for (User user : users) {
                if (canUpgradeLevel(user)) {
                    upgradeLevel(user);
                }
            }
            connection.commit();
        } catch (Exception e) {
            connection.rollback();
            throw e;
        } finally {
//            connection.close();
            DataSourceUtils.releaseConnection(connection, dataSource);
            TransactionSynchronizationManager.clearSynchronization();
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

    protected void upgradeLevel(User user) throws SQLException {
        user.upgradeLevel();
        userDao.update(user);
    }

    public void add(User user) throws SQLException {
        userDao.add(user);
    }
}
