package org.spring.ch3.v4;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.spring.ch3.dataSource.DataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;


/**
 * v4. 템플릿 메서드 패턴을 통해 코드 분리
 */
public class UserDaoGetCount extends UserDaoV4 {

    private static final Log log = LogFactory.getLog(UserDaoGetCount.class);

    public UserDaoGetCount(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    protected PreparedStatement makeStatement(Connection connection) throws SQLException {
        return connection.prepareStatement("select count(*) from users");
    }
}

