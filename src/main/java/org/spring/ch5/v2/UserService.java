package org.spring.ch5.v2;

import java.util.List;

import static org.spring.ch5.v2.Level.*;

public class UserService {
    private final UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public void upgradeLevels() {
        List<User> users = userDao.getAll();
        for (User user : users) {
            if (canUpgradeLevel(user)) {
                upgradeLevel(user);
            }
        }
    }

    private boolean canUpgradeLevel(User user) {
        Level level = user.getLevel();
        return switch (level) {
            case BASIC -> user.getLogin() >= 50;
            case SILVER -> user.getRecommend() >= 30;
            case GOLD -> false;
            default -> throw new IllegalArgumentException("[UserService.canUpgradeLevel] 존재하지 않는 Level입니다.");
        };
    }

    private void upgradeLevel(User user) {
        Level nowLevel = user.getLevel();
        switch (nowLevel) {
            case BASIC -> user.setLevel(SILVER);
            case SILVER -> user.setLevel(GOLD);
        };
        userDao.update(user);
    }

    public void add(User user) {
        userDao.add(user);
    }
}
