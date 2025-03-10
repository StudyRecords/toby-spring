package org.spring.ch5.v3;

import java.util.Objects;

public class UserV3 {
    private String id;
    private String name;
    private String password;
    private LevelV3 levelV3;
    private int login;
    private int recommend;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserV3 userV3 = (UserV3) o;
        return getLogin() == userV3.getLogin() &&
                getRecommend() == userV3.getRecommend() &&
                Objects.equals(getId(), userV3.getId()) &&
                Objects.equals(getName(), userV3.getName()) &&
                Objects.equals(getPassword(), userV3.getPassword()) &&
                getLevel() == userV3.getLevel();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getPassword(), getLevel(), getLogin(), getRecommend());
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LevelV3 getLevel() {
        return levelV3;
    }

    public void setLevel(LevelV3 levelV3) {
        this.levelV3 = levelV3;
    }

    public int getLogin() {
        return login;
    }

    public void setLogin(int login) {
        this.login = login;
    }

    public int getRecommend() {
        return recommend;
    }

    public void setRecommend(int recommend) {
        this.recommend = recommend;
    }

    public UserV3(String id, String name, String password,
                  LevelV3 level, int login, int recommend) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.levelV3 = level;
        this.login = login;
        this.recommend = recommend;
    }

    public UserV3() {
    }


    public void upgradeLevel() {
        LevelV3 nextLevelV3 = this.levelV3.nextLevel();
        if (nextLevelV3 == null) {
            throw new IllegalArgumentException("[User.upgradeLevel] " + this.levelV3 + "은 업그레이드 할 수 없습니다.");
        } else {
            this.levelV3 = nextLevelV3;
        }
    }
}
