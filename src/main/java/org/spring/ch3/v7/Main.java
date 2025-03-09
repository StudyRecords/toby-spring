package org.spring.ch3.v7;

import org.spring.ch4.User;
import org.spring.ch3.dataSource.DDataSource;
import org.spring.ch3.dataSource.DataSource;
import org.spring.ch3.v7.strategy.GetCountStrategy;
import org.spring.ch3.v7.strategy.StatementStrategy;

public class Main {
    public static void main(String[] args) {
        DataSource dataSource = new DDataSource();
        StatementStrategy getCountStrategy = new GetCountStrategy();
        UserDaoV7 userDaoV7 = new UserDaoV7(dataSource);

        User user = new User("123L", "youngsun", "1234");
        userDaoV7.add(user);

        int cnt = userDaoV7.lookupJdbcContextWithStatementStrategy(getCountStrategy);
        System.out.println("add user 작업 수행 이후 cnt = " + cnt);

        userDaoV7.deleteAll();
        cnt = userDaoV7.lookupJdbcContextWithStatementStrategy(getCountStrategy);
        System.out.println("delete all 작업 수행 이후 cnt = " + cnt);

    }
}
