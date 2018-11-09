package dao;

import domain.User;

public interface UserDao {
    int findUsername(String username);

    void save(User user);

    boolean activeUser(String code);
}
