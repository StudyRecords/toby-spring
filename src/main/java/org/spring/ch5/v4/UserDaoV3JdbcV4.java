package org.spring.ch5.v4;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.spring.ch4.jdbcTemplate.DuplicateUserIdException;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;

//@Repository
public class UserDaoV3JdbcV4 implements UserDaoV4 {
    private static final Log log = LogFactory.getLog(UserDaoV3JdbcV4.class);
    private final JdbcTemplate jdbcTemplate;

    public UserDaoV3JdbcV4(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void deleteAll() {
        String command = "delete from users";
        jdbcTemplate.update(command);
    }

    @Override
    public void add(UserV4 userV4) throws DuplicateUserIdException {
        try {
            String command = "insert into users(id, name, password, level, login, recommend) values (?, ?, ?, ?, ?, ?)";
            jdbcTemplate.update(command, userV4.getId(), userV4.getName(), userV4.getPassword(),
                    userV4.getLevel().name(), userV4.getLogin(), userV4.getRecommend());
        } catch (DataAccessException e) {       // 언체크 예외 (RuntimeException)
            if ("23505".equals(((SQLException) e.getCause()).getSQLState())) {
                throw new DuplicateUserIdException(e.getMessage(), e.getCause());       // 언체크 예외
            } else {
                throw e;
            }
        }
    }

    @Override
    public List<UserV4> getAll() {
        String query = "select * from users";
        RowMapper<UserV4> rowMapper = getRowMapper();
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
    public UserV4 getById(String id) {
        String query = "select * from users where id = ?";

        RowMapper<UserV4> rowMapper = getRowMapper();

        try {
            List<UserV4> userV4s = jdbcTemplate.query(query, rowMapper, id);
            if (userV4s.isEmpty()) {
                log.info("[getById] 조회된 행이 없음 (id: " + id + ")");
                throw new EmptyResultDataAccessException("조회된 행이 없음", 1);
            } else if (userV4s.size() > 1) {
                log.info("[getById] 예상보다 많은 행이 조회됨 (id: " + id + ", 개수: " + userV4s.size() + ")");
                throw new IncorrectResultSizeDataAccessException(1, userV4s.size());
            }
            return userV4s.get(0);
        } catch (DataAccessException e) {
            log.error("[getById] 데이터 액세스 중 오류 발생 (id: " + id + ")");
            throw e;
        }
    }

    @Override
    public int update(UserV4 userV4) {
        String command = "update users set name = ?, password = ?, level = ?, login = ?, recommend = ? where id = ?;";
        return jdbcTemplate.update(
                command,
                userV4.getName(),
                userV4.getPassword(),
                userV4.getLevel().name(),
                userV4.getLogin(),
                userV4.getRecommend(),
                userV4.getId()
        );
    }

    private RowMapper<UserV4> getRowMapper() {
        return (rs, rowNum) -> new UserV4(
                rs.getString("id"),
                rs.getString("name"),
                rs.getString("password"),
                LevelV4.valueOf(rs.getString("level")),
                rs.getInt("login"),
                rs.getInt("recommend")
        );
    }

}
