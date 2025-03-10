package org.spring.ch5.v3;

import java.util.List;

public interface UserDaoV3 {
    void add(UserV3 userV3);

    UserV3 getById(String id);

    List<UserV3> getAll();

    void deleteAll();

    int getCount();

    int update(UserV3 userV3);
}
