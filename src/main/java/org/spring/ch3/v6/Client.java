package org.spring.ch3.v6;

import org.spring.ch4.User;
import org.spring.ch3.dataSource.DDataSource;
import org.spring.ch3.dataSource.DataSource;
import org.spring.ch3.v6.strategy.AddStatementStrategy;
import org.spring.ch3.v6.strategy.DeleteAllStrategy;
import org.spring.ch3.v6.strategy.GetCountStrategy;
import org.spring.ch3.v6.strategy.StatementStrategy;

public class Client {
    public static void main(String[] args) {
        DataSource dataSource = new DDataSource();
        Context context = new Context(dataSource);

        User user = new User("123L", "youngsun", "1234");
        StatementStrategy strategy = new AddStatementStrategy(user);
        context.jdbcContextWithStatementStrategy(strategy);

        StatementStrategy getCountStrategy = new GetCountStrategy();
        int cnt = context.lookupJdbcContextWithStatementStrategy(getCountStrategy);
        System.out.println("add user 작업 수행 이후 cnt = " + cnt);

        StatementStrategy deleteAllStrategy = new DeleteAllStrategy();
        context.jdbcContextWithStatementStrategy(deleteAllStrategy);

        cnt = context.lookupJdbcContextWithStatementStrategy(getCountStrategy);
        System.out.println("delete all 작업 수행 이후 cnt = " + cnt);

    }
}
