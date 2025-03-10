package org.spring.ch5.v3;

import java.util.List;

public class UserServiceV3 {

    public static final int MIN_LOGIN_FOR_SILVER = 50;
    public static final int MIN_RECOMMEND_FOR_GOLD = 30;

    private final UserDaoV3 userDaoV3;

    public UserServiceV3(UserDaoV3 userDaoV3) {
        this.userDaoV3 = userDaoV3;
    }

    public void upgradeLevels() {
        List<UserV3> userV3s = userDaoV3.getAll();
        for (UserV3 userV3 : userV3s) {
            if (canUpgradeLevel(userV3)) {
                upgradeLevel(userV3);
            }
        }
    }

    private boolean canUpgradeLevel(UserV3 userV3) {
        LevelV3 levelV3 = userV3.getLevel();
        return switch (levelV3) {
            case BASIC -> userV3.getLogin() >= 50;
            case SILVER -> userV3.getRecommend() >= 30;
            case GOLD -> false;
            default -> throw new IllegalArgumentException("[UserService.canUpgradeLevel] 존재하지 않는 Level입니다.");
        };
    }

    private void upgradeLevel(UserV3 userV3) {
        userV3.upgradeLevel();
        userDaoV3.update(userV3);
    }

    public void add(UserV3 userV3) {
        userDaoV3.add(userV3);
    }
}
