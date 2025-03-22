package org.spring.ch5.transaction;

import java.sql.Connection;
import java.util.List;

public interface UserDao {
    void add(Connection connection, User user);

    User getById(Connection connection, String id);

    List<User> getAll(Connection connection);

    void deleteAll(Connection connection);

    int getCount(Connection connection);

    int update(Connection connection, User user);
}
