package org.spring.ch5.v4;

public interface LevelPolicy {
    boolean canUpgradeLevel(UserV4 userV4);

    void upgradeLevel(UserV4 userV4);
}
