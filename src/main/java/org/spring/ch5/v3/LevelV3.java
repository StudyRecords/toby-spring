package org.spring.ch5.v3;

import java.util.Arrays;
import java.util.Comparator;

public enum LevelV3 {
    BASIC(0, 0),
    SILVER(50, 0),
    GOLD(50, 30);

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
        throw new IllegalArgumentException("GOLD 레벨은 upgrade가 불가능합니다");
    }

}
