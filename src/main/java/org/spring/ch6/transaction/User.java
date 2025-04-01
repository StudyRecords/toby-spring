package org.spring.ch6.transaction;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class User {
    private String id;
    private String name;
    private String password;
    private Level level;
    private int login;
    private int recommend;
    private String email;


    public void upgradeLevel() {
        Level nextLevel = this.level.nextLevel();
        if (nextLevel == null) {
            throw new IllegalArgumentException("[User.upgradeLevel] " + this.level + "은 업그레이드 할 수 없습니다.");
        } else {
            this.level = nextLevel;
        }
    }

}
