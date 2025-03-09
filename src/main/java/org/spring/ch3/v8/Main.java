package org.spring.ch3.v8;

import org.spring.ch4.User;
import org.spring.ch3.dataSource.DDataSource;
import org.spring.ch3.dataSource.DataSource;

public class Main {
    public static void main(String[] args) {
        DataSource dataSource = new DDataSource();
        JdbcContext jdbcContext = new JdbcContext(dataSource);
        UserDaoV8 userDaoV8 = new UserDaoV8(jdbcContext);

        User user = new User("123L", "youngsun", "1234");
        userDaoV8.add(user);

        int cnt = userDaoV8.getCount();
        System.out.println("add user 작업 수행 이후 cnt = " + cnt);

        userDaoV8.deleteAll();
        cnt = userDaoV8.getCount();
        System.out.println("delete all 작업 수행 이후 cnt = " + cnt);

    }
}
