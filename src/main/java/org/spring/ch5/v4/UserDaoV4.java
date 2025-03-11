package org.spring.ch5.v4;

import java.util.List;

public interface UserDaoV4 {
    void add(UserV4 userV4);

    UserV4 getById(String id);

    List<UserV4> getAll();

    void deleteAll();

    int getCount();

    int update(UserV4 userV4);
}
