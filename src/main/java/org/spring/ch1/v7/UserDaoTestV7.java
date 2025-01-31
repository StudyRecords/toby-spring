package org.spring.ch1.v7;

import org.spring.User;
import org.spring.ch1.v6.DaoFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * UserDao의 클라이언트 객체 : '기능 테스트' 라는 관심사를 갖는다.
 * ConnectionMaker 구현체 결정 및 생성, 실행 권한은 DaoFactory에게 위임했다.
 */
public class UserDaoTestV7 {
    public static void main(String[] args) throws Exception {
        ApplicationContext context = new AnnotationConfigApplicationContext(DaoFactoryV7.class);
        UserDaoV7 userDao = context.getBean("userDao", UserDaoV7.class);        // 빈 이름 (메서드 이름), 반환 타입

        User user = new User("lyouxsun", "이영선", "lll");
        userDao.add(user);

        System.out.println(user.getId() + " 등록 성공");
        User user2 = userDao.get(user.getId());
        System.out.println("user2 name = " + user2.getName());
        System.out.println("user2 password = " + user2.getPassword());
    }
}
