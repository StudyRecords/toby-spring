package org.spring.ch1.v8;

import org.spring.User;
import org.spring.ch1.v4.ConnectionMaker;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * v8. 의존관계 검색 (Dependency Lookup) : 오브젝트를 생성, 결정하는 것은 외부 컨테이너가 담당하지만
 * 오브젝트를 직접 가져오는 건 스스로 요청을 통해서 가져온다.
 */
public class UserDaoV8 {

    private final ConnectionMaker connectionMaker;

    public UserDaoV8() {
        // 의존관계 검색(DL) 방식 : 직접 컨테이너에서 빈을 검색해서 가져온다!
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(DaoFactoryV8.class);
        this.connectionMaker = applicationContext.getBean("connectionMaker", ConnectionMaker.class);
    }

    public void add(User user) throws Exception {
        Connection connection = connectionMaker.makeConnection();
        PreparedStatement pstmt = connection.prepareStatement("insert into users (id, name, password) values(?,?,?)");
        pstmt.setString(1, user.getId());
        pstmt.setString(2, user.getName());
        pstmt.setString(3, user.getPassword());
        pstmt.executeUpdate();

        pstmt.close();
        connection.close();
    }

    public User get(String id) throws Exception {
        Connection connection = connectionMaker.makeConnection();
        PreparedStatement pstmt = connection.prepareStatement("select * from users where id = ?");
        pstmt.setString(1, id);

        ResultSet rs = pstmt.executeQuery();
        rs.next();
        User user = new User();
        user.setId(rs.getString("id"));
        user.setName(rs.getString("name"));
        user.setPassword(rs.getString("password"));

        rs.close();
        pstmt.close();
        connection.close();

        return user;
    }
}


