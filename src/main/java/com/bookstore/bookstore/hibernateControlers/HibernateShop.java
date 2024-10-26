package com.bookstore.bookstore.hibernateControlers;

import com.bookstore.bookstore.model.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import java.util.*;

public class HibernateShop extends GenericHibernate {
    public HibernateShop(EntityManagerFactory entityManagerFactory) {
        super(entityManagerFactory);
    }


    //Select User by credentials

    public <T> T getUserByCredentials(String login, String password) {
        EntityManager em = getEntityManager();
        try {
            Manager manager = getManagerByCredentials(login, password);
            return manager == null ? (T) getCustomerByCredentials(login, password) : (T) manager;
        } catch (NoResultException e) {
            return null;
        } finally {
            if (em != null) em.close();
        }
    }

    public Manager getManagerByCredentials(String login, String password) {
        EntityManager em = getEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Manager> query = cb.createQuery(Manager.class);
            Root<Manager> root = query.from(Manager.class);
            query.select(root).where(cb.and(cb.like(root.get("login"), login), cb.like(root.get("password"), password)));
            Query q;

            q = em.createQuery(query);
            return (Manager) q.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            if (em != null) em.close();
        }
    }

    public Customer getCustomerByCredentials(String login, String password) {
        EntityManager em = getEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Customer> query = cb.createQuery(Customer.class);
            Root<Customer> root = query.from(Customer.class);
            query.select(root).where(cb.and(cb.like(root.get("login"), login), cb.like(root.get("password"), password)));
            Query q;

            q = em.createQuery(query);
            return (Customer) q.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            if (em != null) em.close();
        }
    }

    public void deleteComment(int id) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            var comment = em.find(Comment.class, id);
            Product product = em.find(Product.class, comment.getParentProduct().getId());
            product.getComments().remove(comment);
            em.merge(product);

            User user = getUserByCredentials(comment.getCommentOwner().getLogin(), comment.getCommentOwner().getPassword());
            user.getMyComments().remove(comment);
            em.merge(user);

            comment.getReplies().clear();
            em.remove(comment);

            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Product> loadAvailableProducts() {
        EntityManager em = getEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Product> query = cb.createQuery(Product.class);
            Root<Product> root = query.from(Product.class);
            query.select(root).where(cb.isNull(root.get("cart")));
            Query q;

            q = em.createQuery(query);
            return q.getResultList();
        } catch (NoResultException e) {
            return null;
        } finally {
            if (em != null) em.close();
        }
    }

    public void createCart(List<Product> products, User user) {
        //This is a custom method for creating cart. It is more complicated, because cart must jave  abuyer assigned,
        //products must be assigned to cart
        //All must happen within the same transaction
        try {
            entityManager = getEntityManager();
            entityManager.getTransaction().begin();
            //First get the customer
            Customer buyer = entityManager.find(Customer.class, user.getId());
            //Then we go through the objects that user wants to buy
            Cart cart = new Cart(new ArrayList<>(), buyer);

            Set<String> uniqueTitles = new HashSet<>(); // Use a set to store unique product titles

            List<Manager> managers = getAllManagers();
            int randomIndex = (int) (Math.random() * managers.size());
            Manager randomManager = managers.get(randomIndex);

            for (Product p : products) {
                Product product = entityManager.find(Product.class, p.getId());
                //Assign cart to product and product to cart. So that data would be properly stored in db
                product.setCart(cart);
                cart.getProductList().add(product);

                // Add the product title to the set of unique titles
                uniqueTitles.add(product.getTitle());
            }
            // Set the number of items in the cart to the size of the unique titles set
            cart.setNrOfItems(uniqueTitles.size());
            buyer.getMyCarts().add(cart);
            cart.setManager(randomManager);
            //Update buyer, we have cascade all, so this new cart will be inserted and ids assigned
            entityManager.merge(buyer);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (entityManager != null) entityManager.close();
        }
    }

    public List<Manager> getAllManagers() {
        EntityManager entityManager = null;
        try {
            entityManager = getEntityManager();
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

            // Create CriteriaQuery for Manager entities
            CriteriaQuery<Manager> criteriaQuery = criteriaBuilder.createQuery(Manager.class);
            Root<Manager> root = criteriaQuery.from(Manager.class);
            criteriaQuery.select(root);

            // Execute query and return result list
            return entityManager.createQuery(criteriaQuery).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList(); // Return an empty list in case of an exception
        } finally {
            if (entityManager != null) entityManager.close();
        }
    }

    public List<Warehouse> loadAllWarehouses() {
        EntityManager em = getEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Warehouse> query = cb.createQuery(Warehouse.class);
            Root<Warehouse> root = query.from(Warehouse.class);
            query.select(root);
            return em.createQuery(query).getResultList();
        } catch (Exception e) {
            // Handle exceptions appropriately
            e.printStackTrace();
            return null;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
}

