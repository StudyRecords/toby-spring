package org.spring.ch1.v8;

import org.spring.ch4.User;

/**
 * 의존관계 검색을 통해 매번 동일한 UserDao를 가져올 수 있다.
 */
public class UserDaoTestV8 {
    public static void main(String[] args) throws Exception {
        // v8. DaoFactory가 애플리케이션 컨텍스트가 필요로 하는 설정정보 역할을 수행함

        UserDaoV8 userDaoV8_1 = new UserDaoV8();
        UserDaoV8 userDaoV8_2 = new UserDaoV8();
        System.out.println("userDaoV8_1 = " + userDaoV8_1);
        System.out.println("userDaoV8_2 = " + userDaoV8_2);     // 둘이 같음 (동일성O, 싱글톤, 매번 동일한 객체 반환, 하나 만들어서 재활용)
        System.out.println("userDaoV8_1 == userDaoV8_2 : " + (userDaoV8_1 == userDaoV8_2));     // true


        User user = new User("lyouxsun", "이영선", "lll");
        userDaoV8_1.add(user);

        System.out.println(user.getId() + " 등록 성공");
        User user2 = userDaoV8_1.get(user.getId());
        System.out.println("user2 name = " + user2.getName());
        System.out.println("user2 password = " + user2.getPassword());
    }
}
