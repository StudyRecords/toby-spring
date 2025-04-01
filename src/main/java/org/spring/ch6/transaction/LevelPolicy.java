package org.spring.ch6.transaction;

public interface LevelPolicy {
    boolean canUpgradeLevel(User user);

    void upgradeLevel(User user);
}
