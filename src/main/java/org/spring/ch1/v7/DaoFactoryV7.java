package org.spring.ch1.v7;

import org.spring.ch1.v4.ConnectionMaker;
import org.spring.ch1.v4.NConnectionMaker;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * ConnectionMaker 구현체 결정 및 생성, 실행 권한을 갖는다. (제거 권한 get)
 */
@Configuration
public class DaoFactoryV7 {

    @Bean
    public static UserDaoV7 userDaoV6() {
        return new UserDaoV7(connectionMaker());
    }

    // 이런 것도 가능
//    public static TeamDao userDaoV6(){
//        return new TeamDao(connectionMaker());
//    }

//    public static MessageDao userDaoV6(){
//        return new MessageDao(connectionMaker());
//    }


    private static ConnectionMaker connectionMaker() {
        return new NConnectionMaker();
    }
}
