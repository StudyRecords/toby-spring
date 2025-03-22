package org.spring.ch5.transaction;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

public class UserService {

    public static final int MIN_LOGIN_FOR_SILVER = 50;
    public static final int MIN_RECOMMEND_FOR_GOLD = 30;

    private UserDao userDao;

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    public void upgradeLevels() throws SQLException {
        Connection connection = DriverManager.getConnection("jdbc:h2:tcp://localhost/~/toby", "sa", "");
        List<User> users = userDao.getAll(connection);

        connection.setAutoCommit(false);
        try {
            for (User user : users) {
                if (canUpgradeLevel(user)) {
                    upgradeLevel(connection, user);
                }
            }
            connection.commit();
        } catch (Exception e) {
            connection.rollback();
            throw e;
        } finally {
            connection.close();
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

    protected void upgradeLevel(Connection connection, User user) throws SQLException {
        user.upgradeLevel();
        userDao.update(connection, user);
    }

    public void add(User user) throws SQLException {
        Connection connection = DriverManager.getConnection("jdbc:h2:tcp://localhost/~/toby", "sa", "");
        userDao.add(connection, user);
    }
}
