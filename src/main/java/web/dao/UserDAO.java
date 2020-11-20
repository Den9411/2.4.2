package web.dao;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import web.models.User;

import java.util.List;

public interface UserDAO {
    public void addUser (User user);
    public void updateUser(long id, User user);
    public void removeUser(long id);
    public User getUserById(long id);
    public List<User> getAll();
    User getUserByUsername(String name) throws UsernameNotFoundException;

    UserDetails loadUserByUsername(String name);
}
