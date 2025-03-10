package org.spring.ch5.v1;

import java.util.List;

public class UserService {
    private final UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    // TODO. Level을 변경하는 비즈니스 로직을 User 객체 안에 넣는 건 어때?
    public void upgradeLevels() {
        List<User> users = userDao.getAll();
        for (User user : users) {
            boolean change = false;
            if (user.getLevel() == Level.BASIC && user.getLogin() >= 50) {
                user.setLevel(Level.SILVER);
                change = true;
            }
            if (user.getLevel() == Level.SILVER && user.getRecommend() >= 30) {
                user.setLevel(Level.GOLD);
                change = true;
            }
            if (change) {
                userDao.update(user);
            }
        }
    }
}
