package org.spring.ch1.dbConnection;

import org.spring.User;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class UserDaoConnectionCountingTest {
    public static void main(String[] args) throws Exception {

        // DL (Dependency Lookup) 방식으로 UserDao를 가져온다.
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(CountingDaoFactory.class);
        UserDao userDao = applicationContext.getBean("userDao", UserDao.class);

        userDao.add(new User("id", "이영선", "password"));
        User user = userDao.get("id");
        System.out.println("user.getName() = " + user.getName());
        System.out.println("user.getPassword() = " + user.getPassword());

        CountingConnectionMaker connectionMaker = applicationContext.getBean("connectionMaker", CountingConnectionMaker.class);
        System.out.println("Connection counter : "+connectionMaker.getCount());
    }
}
