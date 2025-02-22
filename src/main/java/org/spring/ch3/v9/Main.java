package org.spring.ch3.v9;

import org.spring.User;
import org.spring.ch3.dataSource.DDataSource;
import org.spring.ch3.dataSource.DataSource;

public class Main {
    public static void main(String[] args) {
        DataSource dataSource = new DDataSource();
        UserDaoV9 userDaoV9 = new UserDaoV9(dataSource);

        User user = new User("123L", "youngsun", "1234");
        userDaoV9.add(user);

        int cnt = userDaoV9.getCount();
        System.out.println("add user 작업 수행 이후 cnt = " + cnt);

        userDaoV9.deleteAll();
        cnt = userDaoV9.getCount();
        System.out.println("delete all 작업 수행 이후 cnt = " + cnt);

    }
}
