package org.spring.ch1.v1;

import org.spring.User;

public class UserDaoTest {
    public static void main(String[] args) throws Exception {
        UserDaoV1 userDaoV1 = new UserDaoV1();
        User user = new User("lyouxsun", "이영선", "lll");
        userDaoV1.add(user);

        System.out.println(user.getId() + " 등록 성공");
        User user2 = userDaoV1.get(user.getId());
        System.out.println("user2 name = " + user2.getName());
        System.out.println("user2 password = " + user2.getPassword());
    }
}
