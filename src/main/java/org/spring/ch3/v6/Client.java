package org.spring.ch3.v6;

import org.spring.ch3.dataSource.DDataSource;
import org.spring.ch3.dataSource.DataSource;
import org.spring.ch3.v6.strategy.DeleteAllStrategy;
import org.spring.ch3.v6.strategy.GetCountStrategy;
import org.spring.ch3.v6.strategy.StatementStrategy;

public class Client {
    public static void main(String[] args) {
        DataSource dataSource = new DDataSource();
        StatementStrategy deleteAllStrategy = new DeleteAllStrategy();
        StatementStrategy getCountStrategy = new GetCountStrategy();
        Context context = new Context(dataSource);
        context.jdbcContextWithStatementStrategy(deleteAllStrategy);
        int count = context.lookupJdbcContextWithStatementStrategy(getCountStrategy);
        System.out.println("count = " + count);
    }
}
