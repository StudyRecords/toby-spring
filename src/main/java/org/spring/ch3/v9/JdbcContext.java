package org.spring.ch3.v9;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.spring.ch3.dataSource.DataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * Context 역할 수행
 */
public class JdbcContext {
    private static final Log log = LogFactory.getLog(JdbcContext.class);

    /**
     * 생성자 주입 시
     */
    private final DataSource dataSource;

    public JdbcContext(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * 수정자 주입 시
     */
//    private DataSource dataSource;
//    public JdbcContext() {
//    }
//    public void setDataSource(DataSource dataSource) {
//        this.dataSource = dataSource;
//    }


    public void workWithStatementStrategy(StatementStrategy strategy) {
        Connection connection = null;
        PreparedStatement pstmt = null;

        try {
            connection = dataSource.getConnection();
            pstmt = strategy.makePreparedStatement(connection);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            log.info("[deleteAll] SQLException = " + e.getMessage());
        } catch (ClassNotFoundException e) {
            log.info("[deleteAll] ClassNotFoundException = " + e.getMessage());
        } finally {
            closePreparedStatement(pstmt);
            closeConnection(connection);
        }
    }

    public int lookupWorkWithStatementStrategy(StatementStrategy strategy) {
        Connection connection = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int cnt = 0;

        try {
            connection = dataSource.getConnection();
            pstmt = strategy.makePreparedStatement(connection);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                // ResultSet의 초기 상태는 첫 번째 행을 가리키고 있지 않음. 첫 번째 행 이전에 위치해 있음.
                cnt = rs.getInt(1);
            } else {
                throw new SQLException("반환된 결과가 없습니다.");
            }
        } catch (SQLException e) {
            log.info("[getCount] SQLException = " + e.getMessage());
        } catch (ClassNotFoundException e) {
            log.info("[getCount] ClassNotFoundException = " + e.getMessage());
        } finally {
            // close는 자원이 만들어진 순서의 반대로 하는 것이 원칙이다!!!
            closeResultSet(rs);
            closePreparedStatement(pstmt);
            closeConnection(connection);
        }
        return cnt;
    }


    private void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                log.info("[deleteAll] connection close error");
            }
        }
    }

    private void closePreparedStatement(PreparedStatement pstmt) {
        if (pstmt != null) {
            try {
                pstmt.close();
            } catch (SQLException e) {
                log.info("[deleteAll] prepareStatement close error");
            }
        }
    }

    private void closeResultSet(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                log.info("[deleteAll] resultSet close error");
            }
        }
    }

}
