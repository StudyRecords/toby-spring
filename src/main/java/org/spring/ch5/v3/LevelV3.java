package org.spring.ch5.v3;

import java.util.Arrays;
import java.util.Comparator;

import static org.spring.ch5.v3.UserServiceV3.MIN_LOGIN_FOR_SILVER;
import static org.spring.ch5.v3.UserServiceV3.MIN_RECOMMEND_FOR_GOLD;

public enum LevelV3 {

//    public static final int MIN_LOGIN_FOR_SILVER = 50;
//    public static final int MIN_RECOMMEND_FOR_GOLD = 30;

//    BASIC(0, 0),
//    SILVER(MIN_LOGIN_FOR_SILVER, 0),
//    GOLD(MIN_LOGIN_FOR_SILVER, MIN_RECOMMEND_FOR_GOLD);

    BASIC(0, 0),
    SILVER(MIN_LOGIN_FOR_SILVER, 0),
    GOLD(MIN_LOGIN_FOR_SILVER, MIN_RECOMMEND_FOR_GOLD);

    private final int loginNum;
    private final int recommendNum;

    LevelV3(int loginNum, int recommendNum) {
        this.loginNum = loginNum;
        this.recommendNum = recommendNum;
    }

    public static LevelV3 createLevel(int loginNum, int recommendNum) {
        return Arrays.stream(values())
                .sorted(Comparator.comparing(LevelV3::getLoginNum)
                        .thenComparing(LevelV3::getRecommendNum)
                        .reversed())
                .filter(levelV3 -> levelV3.getLoginNum() <= loginNum && levelV3.getRecommendNum() <= recommendNum)
                .findFirst()
                .orElse(BASIC);
    }

    public int getLoginNum() {
        return loginNum;
    }

    public int getRecommendNum() {
        return recommendNum;
    }

    public LevelV3 nextLevel() {
        if (this == BASIC) {
            return SILVER;
        }
        if (this == SILVER) {
            return GOLD;
        }
        return null;
    }

}
