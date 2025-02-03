package org.spring.ch1.v7;

import org.spring.User;
import org.spring.ch1.v6.DaoFactory;
import org.spring.ch1.v6.UserDaoV6;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * UserDao의 클라이언트 객체 : '기능 테스트' 라는 관심사를 갖는다.
 * ConnectionMaker 구현체 결정 및 생성, 실행 권한은 DaoFactory에게 위임했다.
 */
public class UserDaoTestV7 {
    public static void main(String[] args) throws Exception {

        // v6. DaoFactory가 애플리케이션 컨텍스트 역할을 수행함

        UserDaoV6 userDaoV6_2 = DaoFactory.userDaoV6();
        UserDaoV6 userDaoV6_1 = DaoFactory.userDaoV6();
        System.out.println("userDaoV6_1 = " + userDaoV6_1);
        System.out.println("userDaoV6_2 = " + userDaoV6_2);     // 둘이 다름 (동일성X, 매번 새로운 객체 생성)
        System.out.println("userDaoV6_1 == userDaoV6_2 : " + (userDaoV6_1 == userDaoV6_2));     // false


        // v7. 스프링의 애플리케이션 컨텍스트 적용,
        //     DaoFactory는 애플리케이션 컨텍스트에게 설정 정보를 제공함
        ApplicationContext context = new AnnotationConfigApplicationContext(DaoFactoryV7.class);
        UserDaoV7 userDaoV7 = context.getBean("userDaoV7", UserDaoV7.class);        // 빈 이름 (메서드 이름), 반환 타입
        UserDaoV7 userDaoV7_2 = context.getBean("userDaoV7", UserDaoV7.class);        // 빈 이름 (메서드 이름), 반환 타입

        System.out.println("userDaoV7 = " + userDaoV7);
        System.out.println("userDaoV7_2 = " + userDaoV7_2);     // 둘이 같음 (동일성O, 싱글톤, 매번 동일한 객체 반환, 하나 만들어서 재활용)
        System.out.println("userDaoV7 == userDaoV7_2 : " + (userDaoV7 == userDaoV7_2));     // true


        User user = new User("lyouxsun", "이영선", "lll");
        userDaoV7.add(user);

        System.out.println(user.getId() + " 등록 성공");
        User user2 = userDaoV7.get(user.getId());
        System.out.println("user2 name = " + user2.getName());
        System.out.println("user2 password = " + user2.getPassword());
    }
}
