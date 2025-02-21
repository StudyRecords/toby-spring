package org.spring.ch3.v7;

import org.spring.User;
import org.spring.ch3.dataSource.DDataSource;
import org.spring.ch3.dataSource.DataSource;
import org.spring.ch3.v7.strategy.GetCountStrategy;
import org.spring.ch3.v7.strategy.StatementStrategy;

public class Client {
    public static void main(String[] args) {
        DataSource dataSource = new DDataSource();
        StatementStrategy getCountStrategy = new GetCountStrategy();
        Context context = new Context(dataSource);

        User user = new User("123L", "youngsun", "1234");
        context.add(user);

        int cnt = context.lookupJdbcContextWithStatementStrategy(getCountStrategy);
        System.out.println("add user 작업 수행 이후 cnt = " + cnt);

        context.deleteAll();
        cnt = context.lookupJdbcContextWithStatementStrategy(getCountStrategy);
        System.out.println("delete all 작업 수행 이후 cnt = " + cnt);

    }
}
