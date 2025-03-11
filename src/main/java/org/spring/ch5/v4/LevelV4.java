package org.spring.ch5.v4;

import java.util.Arrays;
import java.util.Comparator;

import static org.spring.ch5.v4.UserServiceV4.MIN_LOGIN_FOR_SILVER;
import static org.spring.ch5.v4.UserServiceV4.MIN_RECOMMEND_FOR_GOLD;


public enum LevelV4 {
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

    LevelV4(int loginNum, int recommendNum) {
        this.loginNum = loginNum;
        this.recommendNum = recommendNum;
    }

    public static LevelV4 createLevel(int loginNum, int recommendNum) {
        return Arrays.stream(values())
                .sorted(Comparator.comparing(LevelV4::getLoginNum)
                        .thenComparing(LevelV4::getRecommendNum)
                        .reversed())
                .filter(levelV4 -> levelV4.getLoginNum() <= loginNum && levelV4.getRecommendNum() <= recommendNum)
                .findFirst()
                .orElse(BASIC);
    }

    public int getLoginNum() {
        return loginNum;
    }

    public int getRecommendNum() {
        return recommendNum;
    }

    public LevelV4 nextLevel() {
        if (this == BASIC) {
            return SILVER;
        }
        if (this == SILVER) {
            return GOLD;
        }
        return null;
    }

}
