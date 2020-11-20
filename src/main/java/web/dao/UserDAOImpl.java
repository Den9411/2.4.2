package web.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import web.models.Role;
import web.models.User;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import java.sql.SQLOutput;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class UserDAOImpl implements UserDAO {

    private EntityManagerFactory emf;

    @Autowired
    public void setEntityManagerFactory(EntityManagerFactory emf) {
        this.emf = emf;
    }

    @Override
    public void addUser(User user) {
        EntityManager entityManager = emf.createEntityManager();
        EntityTransaction entityTransaction = entityManager.getTransaction();
        try {
            entityTransaction.begin();
            entityManager.persist(user);
            entityManager.getTransaction().commit();
            System.out.println("user " + user.getName() + " сохранен");
            entityTransaction.commit();
        } catch (Exception ex) {
            entityTransaction.rollback();
            System.out.println("Updateuser error: " + ex);
        } finally {
            if (entityManager != null && entityManager.isOpen())
                entityManager.close();
        }
    }

    @Override
    public void updateUser(long id, User user) {
        EntityManager entityManager = emf.createEntityManager();
        EntityTransaction entityTransaction = entityManager.getTransaction();
        try {
            entityTransaction.begin();
            User updateUser = entityManager.find(User.class, id);
            if (updateUser != null) {
                updateUser.setName(user.getName());
                updateUser.setLastName(user.getLastName());
                updateUser.setEmail(user.getEmail());
            }
            entityTransaction.commit();
        } catch (Exception ex) {
            entityTransaction.rollback();
            System.out.println("Updateuser error: " + ex);
        } finally {
            if (entityManager != null && entityManager.isOpen())
                entityManager.close();
        }
    }

    @Override
    public void removeUser(long id) {
        EntityManager entityManager = emf.createEntityManager();
        EntityTransaction entityTransaction = entityManager.getTransaction();
        try {
            entityTransaction.begin();
            User user = entityManager.find(User.class, id);
            entityManager.remove(user);
            entityTransaction.commit();
        } catch (Exception ex) {
            entityTransaction.rollback();
            System.out.println("RemoveUser error: " + ex);
        } finally {
            if (entityManager != null && entityManager.isOpen())
                entityManager.close();
        }
    }

    @Override
    public User getUserById(long id) {
        EntityManager entityManager = emf.createEntityManager();
        EntityTransaction entityTransaction = entityManager.getTransaction();
        try {
            entityTransaction.begin();
            User user = entityManager.find(User.class, id);
            entityTransaction.commit();
            return user;
        } catch (Exception ex) {
            entityTransaction.rollback();
            System.out.println("GetUserByID error: " + ex);
            return null;
        } finally {
            if (entityManager != null && entityManager.isOpen())
                entityManager.close();
        }
    }

    @Override
    public List<User> getAll() {
        EntityManager entityManager = emf.createEntityManager();
        EntityTransaction entityTransaction = entityManager.getTransaction();
        entityTransaction.begin();
        List<User> users = (List<User>) entityManager.createQuery("SELECT u FROM User u").getResultList();
        entityManager.getTransaction().commit();
        entityManager.close();
        return users;
    }

    @Transactional
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {

        EntityManager entityManager = emf.createEntityManager();
        EntityTransaction entityTransaction = entityManager.getTransaction();
        entityTransaction.begin();
//        User user = (User) entityManager.createQuery("FROM User where name = " + name).getResultList().get(0);
        String hql = "select u from User u where u.name = :paramName";
        Query query = (Query) entityManager.createQuery(hql);
        query.setParameter("paramName", name);
        User user = (User)query.list().get(0);
        entityManager.getTransaction().commit();

//        System.out.println(user);

        if(user==null) {
            entityManager.close();
            throw new UsernameNotFoundException(String.format("User '%s' not found", name));
        } else {
            entityManager.close();
            return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
                    mapRolesToAuth(user.getRoles()));
        }
    }

    @Transactional
    public User getUserByUsername(String name) throws UsernameNotFoundException {
        EntityManager entityManager = emf.createEntityManager();
        EntityTransaction entityTransaction = entityManager.getTransaction();
        entityTransaction.begin();
//        User user = (User) entityManager.createQuery("FROM User where name = " + name).getResultList().get(0);
        String hql = "select u from User u where u.name = :paramName";
        Query query = (Query) entityManager.createQuery(hql);
        query.setParameter("paramName", name);
        User user = (User)query.list().get(0);
        entityManager.getTransaction().commit();
        entityManager.close();
        return user;

    }

    private Collection<? extends GrantedAuthority> mapRolesToAuth(Collection<Role> roles) {
        return roles.stream().map(r -> new SimpleGrantedAuthority(r.getRole())).collect(Collectors.toList());
    }
}
