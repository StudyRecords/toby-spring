package org.spring.ch5.v1;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.spring.ch4.User;
import org.spring.ch4.jdbcTemplate.DuplicateUserIdException;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;

@Repository
public class UserDaoJdbc implements UserDao {
    private static final Log log = LogFactory.getLog(UserDaoJdbc.class);
    private final JdbcTemplate jdbcTemplate;

    public UserDaoJdbc(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void deleteAll() {
        String command = "delete from users";
        jdbcTemplate.update(command);
    }

    @Override
    public void add(User user) throws DuplicateUserIdException {
        try {
            String command = "insert into users(id, name, password) values (?, ?, ?)";
            jdbcTemplate.update(command, user.getId(), user.getName(), user.getPassword());
        } catch (DataAccessException e) {       // 언체크 예외 (RuntimeException)
            if ("23505".equals(((SQLException) e.getCause()).getSQLState())) {
                throw new DuplicateUserIdException(e.getMessage(), e.getCause());       // 언체크 예외
            } else {
                throw e;
            }
        }
    }

    @Override
    public List<User> getAll() {
        String query = "select * from users";
        RowMapper<User> rowMapper = (rs, rowNum) -> new User(
                rs.getString("id"),
                rs.getString("name"),
                rs.getString("password")
        );
        try {
            return jdbcTemplate.query(query, rowMapper);
        } catch (DataAccessException e) {
            log.error("[getAll] 데이터 액세스 중 오류 발생 : " + e.getMessage());
            throw e;
        }
    }

    // queryForInt() 메서드는 스프링 3.x 까지 존재했음. 스프링 4.x 부터 deprecated 되어 queryForObject() 로 대체됨
    @Override
    public int getCount() {
        Integer count = jdbcTemplate.queryForObject("select count(*) from users", Integer.class);
        return count != null ? count : 0;
    }

    @Override
    public User getById(String id) {
        String query = "select * from users where id = ?";

        RowMapper<User> rowMapper = (rs, rowNum) -> new User(
                rs.getString("id"),
                rs.getString("name"),
                rs.getString("password")
        );

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
}
