package org.spring.ch5.v1;

import java.util.Objects;

public class User {
    private String id;
    private String name;
    private String password;
    private Level level;
    private int login;
    private int recommend;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return getLogin() == user.getLogin() &&
                getRecommend() == user.getRecommend() &&
                Objects.equals(getId(), user.getId()) &&
                Objects.equals(getName(), user.getName()) &&
                Objects.equals(getPassword(), user.getPassword()) &&
                getLevel() == user.getLevel();
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

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
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

    public User(String id, String name, String password,
                Level level, int login, int recommend) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.level = level;
        this.login = login;
        this.recommend = recommend;
    }

    public User() {
    }
}
