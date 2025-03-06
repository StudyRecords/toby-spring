package org.spring.ch4.jdbcTemplate;

import org.springframework.dao.DataAccessException;

public class DuplicateUserIdException extends DataAccessException {

    public DuplicateUserIdException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
