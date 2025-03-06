package org.spring.ch4.independentDao;

import org.spring.User;

import java.util.List;

public interface UserDao {
    void add(User user);

    User getById(String id);

    List<User> getAll();

    void deleteAll();

    int getCount();
}
