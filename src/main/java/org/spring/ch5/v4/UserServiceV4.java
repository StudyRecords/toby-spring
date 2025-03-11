package org.spring.ch5.v4;

import java.util.List;

public class UserServiceV4 {

    public static final int MIN_LOGIN_FOR_SILVER = 50;
    public static final int MIN_RECOMMEND_FOR_GOLD = 30;

    private final UserDaoV4 userDaoV4;

    public UserServiceV4(UserDaoV4 userDaoV4) {
        this.userDaoV4 = userDaoV4;
    }

    public void upgradeLevels() {
        List<UserV4> userV4s = userDaoV4.getAll();
        for (UserV4 userV4 : userV4s) {
            if (canUpgradeLevel(userV4)) {
                upgradeLevel(userV4);
            }
        }
    }

    private boolean canUpgradeLevel(UserV4 userV4) {
        LevelV4 levelV4 = userV4.getLevel();
        return switch (levelV4) {
            case BASIC -> userV4.getLogin() >= 50;
            case SILVER -> userV4.getRecommend() >= 30;
            case GOLD -> false;
            default -> throw new IllegalArgumentException("[UserService.canUpgradeLevel] 존재하지 않는 Level입니다.");
        };
    }

    private void upgradeLevel(UserV4 userV4) {
        userV4.upgradeLevel();
        userDaoV4.update(userV4);
    }

    public void add(UserV4 userV4) {
        userDaoV4.add(userV4);
    }
}
