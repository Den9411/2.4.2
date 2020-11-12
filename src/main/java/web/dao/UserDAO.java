package web.dao;

import web.models.User;

import java.util.List;

public interface UserDAO {
    public void addUser (User user);
    public void updateUser(long id, User user);
    public void removeUser(long id);
    public User getUserById(long id);
    public List<User> getAll();
}
