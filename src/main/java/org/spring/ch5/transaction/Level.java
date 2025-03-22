package org.spring.ch5.transaction;

import java.util.Arrays;
import java.util.Comparator;

import static org.spring.ch5.transaction.UserService.MIN_LOGIN_FOR_SILVER;
import static org.spring.ch5.transaction.UserService.MIN_RECOMMEND_FOR_GOLD;

public enum Level {
//    BASIC(0, 0),
//    SILVER(MIN_LOGIN_FOR_SILVER, 0),
//    GOLD(MIN_LOGIN_FOR_SILVER, MIN_RECOMMEND_FOR_GOLD);

    BASIC(0, 0),
    SILVER(MIN_LOGIN_FOR_SILVER, 0),
    GOLD(MIN_LOGIN_FOR_SILVER, MIN_RECOMMEND_FOR_GOLD);

//    private static final int MIN_LOGIN_FOR_SILVER = 50;
//    private static final int MIN_RECOMMEND_FOR_GOLD = 30;

    private final int loginNum;
    private final int recommendNum;

    Level(int loginNum, int recommendNum) {
        this.loginNum = loginNum;
        this.recommendNum = recommendNum;
    }

    public static Level createLevel(int loginNum, int recommendNum) {
        return Arrays.stream(values())
                .sorted(Comparator.comparing(Level::getLoginNum)
                        .thenComparing(Level::getRecommendNum)
                        .reversed())
                .filter(level -> level.getLoginNum() <= loginNum && level.getRecommendNum() <= recommendNum)
                .findFirst()
                .orElse(BASIC);
    }

    public int getLoginNum() {
        return loginNum;
    }

    public int getRecommendNum() {
        return recommendNum;
    }

    public Level nextLevel() {
        if (this == BASIC) {
            return SILVER;
        }
        if (this == SILVER) {
            return GOLD;
        }
        return null;
    }

}
