/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.imagedbservice.authenticate;

import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;

/**
 *
 * @author imsofa
 */
public class MemberDaoImpl implements MemberDao {

    @Override
    public int persistMember(EntityManager em, String name, String mail, String account, String password) {
        int memberId = -1;

        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            Member mem = new Member();
            mem.setName(Base64.getEncoder().encodeToString(name.getBytes("UTF-8")));
            mem.setEmail(mail);
            mem.setAccount(account);
            mem.setPassword(password);

            em.persist(mem);
            em.flush();
            transaction.commit();
            memberId = mem.getId();
        } catch (PersistenceException e) {
            e.printStackTrace();
            System.out.println("======================");
            System.out.println("Unsuccessfully: ConstraintViolationException.");
            System.out.println("======================");
        } catch (Exception e) {
            System.out.println("======================");
            System.out.println("Unsuccessfully: Have some porblem in persistence step.");
            System.out.println("======================");
            e.printStackTrace();
            if (transaction.isActive()) {
                transaction.rollback();
            }
        }
        return memberId;
    }

    @Override
    public int findMember(EntityManager em, String name, String email) {
        int memberId = -1;

        try {
            name = Base64.getEncoder().encodeToString(name.getBytes("UTF-8"));
            TypedQuery<Integer> query = em.createQuery("select i.id from Member i where (i.name = :name  AND i.email = :email)", Integer.class);
            query.setParameter("name", name);
            query.setParameter("email", email);
            memberId = query.getSingleResult();
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(MemberDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoResultException e) {
            System.out.println("======================================================================");
            System.out.println("NoResultException: No entity found for query Member");
            System.out.println("======================================================================");
        }
        return memberId;
    }

    @Override
    public int findMember(EntityManager em, int id, String account, String password) {
        int memberId = -1;

        try {
            TypedQuery<Integer> query = em.createQuery("select i.id from Member i where (i.id = :id AND "
                    + "i.account = :account AND i.password = :password) ", Integer.class);
            query.setParameter("id", id);
            query.setParameter("account", account);
            query.setParameter("password", password);
            memberId = query.getSingleResult();

        } catch (NoResultException e) {
            System.out.println("======================================================================");
            System.out.println("NoResultException: No entity found for query Member");
            System.out.println("======================================================================");
        }
        return memberId;
    }

    @Override
    public List loadMembers(EntityManager em) {
        TypedQuery<Tuple> query = em.createQuery("SELECT i.name, i.email FROM Member i", Tuple.class);
        List<Tuple> resultList = query.getResultList();

        System.out.println("======================");
        for (Tuple t : resultList) {
            String Name = t.get(0, String.class);
            String decodedName = null;
            try {
                decodedName = new String(Base64.getDecoder().decode(Name), "UTF-8");
            } catch (UnsupportedEncodingException ex) {
                Logger.getLogger(MemberDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
            String email = t.get(1, String.class);
            System.out.println("imgId: " + decodedName + ", imgName: " + email);
        }
        System.out.println("======================");

        return resultList;
    }

}
