package org.spring.ch5.transaction;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.spring.ch4.jdbcTemplate.DuplicateUserIdException;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

//@Repository
public class UserDaoJdbc implements UserDao {
    private static final Log log = LogFactory.getLog(UserDaoJdbc.class);
    private final JdbcTemplate jdbcTemplate;

    public UserDaoJdbc(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void deleteAll(Connection connection) {
        String command = "delete from users";
        jdbcTemplate.update(command);
    }

    @Override
    public void add(Connection connection, User user) throws DuplicateUserIdException {
        try {
            String command = "insert into users(id, name, password, level, login, recommend) values (?, ?, ?, ?, ?, ?)";
            jdbcTemplate.update(command, user.getId(), user.getName(), user.getPassword(),
                    user.getLevel().name(), user.getLogin(), user.getRecommend());
        } catch (DataAccessException e) {       // 언체크 예외 (RuntimeException)
            if ("23505".equals(((SQLException) e.getCause()).getSQLState())) {
                throw new DuplicateUserIdException(e.getMessage(), e.getCause());       // 언체크 예외
            } else {
                throw e;
            }
        }
    }

    @Override
    public List<User> getAll(Connection connection) {
        String query = "select * from users";
        List<User> users = new ArrayList<>();

        try (PreparedStatement ps = connection.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                User user = new User();
                user.setId(rs.getString("id"));
                user.setName(rs.getString("name"));
                user.setPassword(rs.getString("password"));
                user.setLevel(Level.valueOf(rs.getString("level")));
                user.setLogin(rs.getInt("login"));
                user.setRecommend(rs.getInt("recommend"));
                users.add(user);
            }

        } catch (SQLException e) {
            log.error("[getAll] 데이터 액세스 중 오류 발생 : " + e.getMessage());
            throw new RuntimeException(e);
        }

        return users;
    }

    // queryForInt() 메서드는 스프링 3.x 까지 존재했음. 스프링 4.x 부터 deprecated 되어 queryForObject() 로 대체됨
    @Override
    public int getCount(Connection connection) {
        Integer count = jdbcTemplate.queryForObject("select count(*) from users", Integer.class);
        return count != null ? count : 0;
    }

    @Override
    public User getById(Connection connection, String id) {
        String query = "select * from users where id = ?";

        RowMapper<User> rowMapper = getRowMapper();

        try {
            List<User> users = jdbcTemplate.query(query, rowMapper, id);
            if (users.isEmpty()) {
                log.info("[getById] 조회된 행이 없음 (id: " + id + ")");
                throw new EmptyResultDataAccessException("조회된 행이 없음", 1);
            } else if (users.size() > 1) {
                log.info("[getById] 예상보다 많은 행이 조회됨 (id: " + id + ", 개수: " + users.size() + ")");
                throw new IncorrectResultSizeDataAccessException(1, users.size());
            }
            return users.get(0);
        } catch (DataAccessException e) {
            log.error("[getById] 데이터 액세스 중 오류 발생 (id: " + id + ")");
            throw e;
        }
    }

    @Override
    public int update(Connection connection, User user) {
        String command = "update users set name = ?, password = ?, level = ?, login = ?, recommend = ? where id = ?;";
        try (PreparedStatement ps = connection.prepareStatement(command)) {
            ps.setString(1, user.getName());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getLevel().name());
            ps.setInt(4, user.getLogin());
            ps.setInt(5, user.getRecommend());
            ps.setString(6, user.getId());

            return ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private RowMapper<User> getRowMapper() {
        return (rs, rowNum) -> new User(
                rs.getString("id"),
                rs.getString("name"),
                rs.getString("password"),
                Level.valueOf(rs.getString("level")),
                rs.getInt("login"),
                rs.getInt("recommend")
        );
    }

}
