package org.spring.ch6.transaction;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class MockUserDao implements UserDao {

    private List<User> users;
    @Getter
    private final List<User> updatedUsers = new ArrayList<>();

    public MockUserDao(List<User> users) {
        this.users = users;
    }

    @Override
    public List<User> getAll() {        // Stub 기능 제공
        return this.users;
    }

    @Override
    public void update(User user) {        // Mock 기능 제공
        updatedUsers.add(user);
    }

    @Override
    public void add(User user) {
        throw new UnsupportedOperationException();
    }

    @Override
    public User getById(String id) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void deleteAll() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getCount() {
        throw new UnsupportedOperationException();
    }
}
