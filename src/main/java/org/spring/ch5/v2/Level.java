package org.spring.ch5.v2;

import java.util.Arrays;
import java.util.Comparator;

public enum Level {
    BASIC(0, 0),
    SILVER(50, 0),
    GOLD(50, 30);

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
        throw new IllegalArgumentException("GOLD 레벨은 upgrade가 불가능합니다");
    }

}
