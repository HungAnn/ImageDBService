/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.imagedbservice.fileservice;

import com.mycompany.imagedbservice.authenticate.Member;
import java.util.List;
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
public class ImageDaoImpl implements ImageDao {

    public ImageDaoImpl() {
    }

    @Override
    public String persistImg(EntityManager em, String imgName, String type, String picByte, int memberId) {
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            Member member = em.find(Member.class, memberId);
            Image img = new Image();
            img.setImgName(imgName);
            img.setType(type);
            img.setPicByte(picByte);

            em.persist(img);
            img.setMember(member);
            em.flush();
            transaction.commit();
            return "Success";
        } catch (PersistenceException e) {
            e.printStackTrace();
            return "Unsuccessfully: Some problem in persistence stage.";
        } finally {
            if (transaction.isActive()) {
                transaction.rollback();
            }
        }
    }

    @Override
    public String[] findImg(EntityManager em, int memberId, String imgName) {
        String resString[] = new String[2];
        String findedImg = null;
        String findedStatus = "UnSuccess: No entity found or Permission denied, please make sure your input.";

        TypedQuery<String> query = em.createQuery("SELECT i.picByte FROM Image i WHERE"
                + " (i.imgName = :imgName AND i.member.id = :memberId)", String.class);
        query.setParameter("memberId", memberId);
        query.setParameter("imgName", imgName);
        try {
            findedImg = query.getSingleResult();
            findedStatus = "Success";
        } catch (NoResultException e) {
            System.out.println("======================================================================");
            System.out.println("NoResultException: No entity found for query Image");
            System.out.println("======================================================================");
        }
        resString[1] = findedImg;
        resString[0] = findedStatus;
        return resString;
    }

    public List findImgs(EntityManager em, int memberId) {
        String findedStatus = "UnSuccess: No entity found or Permission denied, please make sure your input.";
        TypedQuery<Tuple> query = em.createQuery("SELECT i.imgName, i.type FROM Image i WHERE "
                + "(i.member.id = :memberId)", Tuple.class);
        
        query.setParameter("memberId", memberId);
        List<Tuple> resultList = query.getResultList();

        System.out.println("======================");
        for (Tuple t : resultList) {
            String imgName = t.get(0, String.class);
            String type = t.get(1, String.class);
            System.out.println("imgName: " + imgName + ", type: " + type);
        }
        System.out.println("======================");
        
        return resultList;
    }
}
