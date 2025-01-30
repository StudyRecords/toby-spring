package org.spring.ch1.v5;

import org.spring.User;
import org.spring.ch1.v1.UserDaoV1;
import org.spring.ch1.v4.DConnectionMaker;
import org.spring.ch1.v4.UserDaoV4;

/**
 * UserDao의 클라이언트 객체
 * 이 객체가 ConnectionMaker 구현체를 결정해준다.
 */
public class UserDaoTest {
    public static void main(String[] args) throws Exception {
        DConnectionMaker connectionMaker = new DConnectionMaker();
        UserDaoV5 userDao = new UserDaoV5(connectionMaker);
        User user = new User("lyouxsun", "이영선", "lll");
        userDao.add(user);

        System.out.println(user.getId() + " 등록 성공");
        User user2 = userDao.get(user.getId());
        System.out.println("user2 name = " + user2.getName());
        System.out.println("user2 password = " + user2.getPassword());
    }
}
