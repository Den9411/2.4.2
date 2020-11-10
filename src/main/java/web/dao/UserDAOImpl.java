package web.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import web.models.User;

import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class UserDAOImpl implements UserDAO {

    private SessionFactory sessionFactory;

    @Autowired
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void addUser(User user) {
        Session session = this.sessionFactory.getCurrentSession();
        session.persist(user);
        System.out.println("user " + user.getName() + " сохранен");
    }

    @Override
    public void updateUser(int id, User user) {
        User updatedUser = getUserById(id);
        updatedUser.setName(user.getName());
        updatedUser.setLastName(user.getLastName());
        updatedUser.setEmail(user.getEmail());
        Session session = sessionFactory.getCurrentSession();
        session.update(updatedUser);
        System.out.println("user " + updatedUser.getName() + " обновлён");
    }

    @Override
    public void removeUser(long id) {
        Session session = this.sessionFactory.getCurrentSession();
        User user = (User) session.load(User.class, id);

        if (user!=null) {
            session.delete(user);
        }
        System.out.println("user " + user.getName() + " удалён");
    }

    @Override
    public User getUserById(int id) {
        String hql = "FROM User U WHERE U.id = " + id;
        Query query = sessionFactory.getCurrentSession().createQuery(hql);
        List<User> users = query.list();
        return users.get(0);
    }

    @Override
    public List<User> getAll() {
        TypedQuery<User> query=sessionFactory.getCurrentSession().createQuery("from User");
        return query.getResultList();
    }
}
