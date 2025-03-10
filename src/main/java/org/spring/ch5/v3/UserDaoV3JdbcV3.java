package org.spring.ch5.v3;

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

@Repository
public class UserDaoV3JdbcV3 implements UserDaoV3 {
    private static final Log log = LogFactory.getLog(UserDaoV3JdbcV3.class);
    private final JdbcTemplate jdbcTemplate;

    public UserDaoV3JdbcV3(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void deleteAll() {
        String command = "delete from users";
        jdbcTemplate.update(command);
    }

    @Override
    public void add(UserV3 userV3) throws DuplicateUserIdException {
        try {
            String command = "insert into users(id, name, password, level, login, recommend) values (?, ?, ?, ?, ?, ?)";
            jdbcTemplate.update(command, userV3.getId(), userV3.getName(), userV3.getPassword(),
                    userV3.getLevel().name(), userV3.getLogin(), userV3.getRecommend());
        } catch (DataAccessException e) {       // 언체크 예외 (RuntimeException)
            if ("23505".equals(((SQLException) e.getCause()).getSQLState())) {
                throw new DuplicateUserIdException(e.getMessage(), e.getCause());       // 언체크 예외
            } else {
                throw e;
            }
        }
    }

    @Override
    public List<UserV3> getAll() {
        String query = "select * from users";
        RowMapper<UserV3> rowMapper = getRowMapper();
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
    public UserV3 getById(String id) {
        String query = "select * from users where id = ?";

        RowMapper<UserV3> rowMapper = getRowMapper();

        try {
            List<UserV3> userV3s = jdbcTemplate.query(query, rowMapper, id);
            if (userV3s.isEmpty()) {
                log.info("[getById] 조회된 행이 없음 (id: " + id + ")");
                throw new EmptyResultDataAccessException("조회된 행이 없음", 1);
            } else if (userV3s.size() > 1) {
                log.info("[getById] 예상보다 많은 행이 조회됨 (id: " + id + ", 개수: " + userV3s.size() + ")");
                throw new IncorrectResultSizeDataAccessException(1, userV3s.size());
            }
            return userV3s.get(0);
        } catch (DataAccessException e) {
            log.error("[getById] 데이터 액세스 중 오류 발생 (id: " + id + ")");
            throw e;
        }
    }

    @Override
    public int update(UserV3 userV3) {
        String command = "update users set name = ?, password = ?, level = ?, login = ?, recommend = ? where id = ?;";
        return jdbcTemplate.update(
                command,
                userV3.getName(),
                userV3.getPassword(),
                userV3.getLevel().name(),
                userV3.getLogin(),
                userV3.getRecommend(),
                userV3.getId()
        );
    }

    private RowMapper<UserV3> getRowMapper() {
        return (rs, rowNum) -> new UserV3(
                rs.getString("id"),
                rs.getString("name"),
                rs.getString("password"),
                LevelV3.valueOf(rs.getString("level")),
                rs.getInt("login"),
                rs.getInt("recommend")
        );
    }

}
