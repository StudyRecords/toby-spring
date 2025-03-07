package org.spring.ch5.v1;

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

    public static Level createLevel(int loginNum, int recommendNum){
        for (Level level : values()) {
            if (level.loginNum <= loginNum && level.recommendNum <= recommendNum){
                return level;
            }
        }
        return BASIC;
    }

}
