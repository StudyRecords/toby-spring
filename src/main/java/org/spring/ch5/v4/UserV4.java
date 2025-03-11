package org.spring.ch5.v4;

import java.util.Objects;

public class UserV4 {
    private String id;
    private String name;
    private String password;
    private LevelV4 levelV4;
    private int login;
    private int recommend;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserV4 userV4 = (UserV4) o;
        return getLogin() == userV4.getLogin() &&
                getRecommend() == userV4.getRecommend() &&
                Objects.equals(getId(), userV4.getId()) &&
                Objects.equals(getName(), userV4.getName()) &&
                Objects.equals(getPassword(), userV4.getPassword()) &&
                getLevel() == userV4.getLevel();
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

    public LevelV4 getLevel() {
        return levelV4;
    }

    public void setLevel(LevelV4 levelV4) {
        this.levelV4 = levelV4;
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

    public UserV4(String id, String name, String password,
                  LevelV4 level, int login, int recommend) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.levelV4 = level;
        this.login = login;
        this.recommend = recommend;
    }

    public UserV4() {
    }


    public void upgradeLevel() {
        LevelV4 nextLevelV4 = this.levelV4.nextLevel();
        if (nextLevelV4 == null) {
            throw new IllegalArgumentException("[User.upgradeLevel] " + this.levelV4 + "은 업그레이드 할 수 없습니다.");
        } else {
            this.levelV4 = nextLevelV4;
        }
    }
}
