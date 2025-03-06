package org.spring.ch3.jdbcTemplate;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.spring.User;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserDao {
    private static final Log log = LogFactory.getLog(UserDao.class);
    private final JdbcTemplate jdbcTemplate;

    public UserDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void deleteAll() {
        String command = "delete from users";
        jdbcTemplate.update(command);
    }

    public void add(User user) {
        String command = "insert into users(id, name, password) values (?, ?, ?)";
        jdbcTemplate.update(command, user.getId(), user.getName(), user.getPassword());
    }

    // queryForInt() 메서드는 스프링 3.x 까지 존재했음. 스프링 4.x 부터 deprecated 되어 queryForObject() 로 대체됨
    public Integer getCount() {
        return jdbcTemplate.queryForObject("select count(*) from users", Integer.class);
    }

    public Optional<User> getById(String id) {
        String query = "select * from users where id = ?";

        RowMapper<User> rowMapper = (rs, rowNum) -> new User(
                rs.getString("id"),
                rs.getString("name"),
                rs.getString("password")
        );

        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(query, rowMapper, id));
        } catch (EmptyResultDataAccessException e) {
            log.info("[getById] 조회된 행의 개수가 0개");
            return Optional.empty();
        } catch (IncorrectResultSizeDataAccessException e) {
            log.info("[getById] 조회된 행의 개수가 2개 이상");
            throw new IncorrectResultSizeDataAccessException(1);
        }
    }
}
