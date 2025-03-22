package org.spring.ch5.transaction;

public interface LevelPolicy {
    boolean canUpgradeLevel(User user);

    void upgradeLevel(User user);
}
