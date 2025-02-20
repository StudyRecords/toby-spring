package org.spring.ch3.dataSource;

import java.sql.Connection;
import java.sql.SQLException;

public interface DataSource {
    Connection getConnection() throws ClassNotFoundException, SQLException;
}
