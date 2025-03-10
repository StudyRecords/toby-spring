package org.spring.ch5.v3;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class UserV3Test {
    private UserV3 user;

    @BeforeEach
    public void setUp() {
        user = new UserV3();
    }

    @Test
    public void upgradeLevel() {
        Arrays.stream(LevelV3.values())
                .filter(level -> level.nextLevel() != null)
                .forEach(level -> {
                    user.setLevel(level);
                    user.upgradeLevel();
                    assertThat(user.getLevel()).isEqualTo(level.nextLevel());
                });
    }

    @Test
    public void cannotUpgradeLevel() {
        assertThatThrownBy(() -> {
            Arrays.stream(LevelV3.values())
                    .filter(level -> level.nextLevel() == null)
                    .forEach(level -> {
                        user.setLevel(level);
                        user.upgradeLevel();
                    });
        }).isInstanceOf(IllegalArgumentException.class);

    }
}
