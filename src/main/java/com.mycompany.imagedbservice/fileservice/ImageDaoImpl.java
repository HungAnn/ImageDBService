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
            return "Unsuccessfully: ConstraintViolationException: image name already exist.";
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
        String selectStatus = "UnSuccess: No entity found or Permission denied";
        
        TypedQuery<String> query = em.createQuery("SELECT i.picByte FROM Image i WHERE (i.imgName = :imgName AND i.member.id = :memberId)", String.class);
        query.setParameter("memberId", memberId);
        query.setParameter("imgName", imgName);
        try {
            findedImg = query.getSingleResult();
            selectStatus = "Success";
        } catch (NoResultException e) {
            System.out.println("======================================================================");
            System.out.println("NoResultException: No entity found for query Image");
            System.out.println("======================================================================");
        } catch (Exception e) {
            e.printStackTrace();
        }
        resString[1] = findedImg;
        resString[0] = selectStatus;
        return resString;
    }

    public List loadImgs(EntityManager em) {
        TypedQuery<Tuple> query = em.createQuery("SELECT i.imgName, i.type FROM Image i", Tuple.class);
        List<Tuple> resultList = query.getResultList();

        System.out.println("======================");
        for (Tuple t : resultList) {
            String imgName = t.get(0, String.class);
            String type = t.get(1, String.class);
            System.out.println("imgId: " + imgName + ", imgName: " + type);
        }
        System.out.println("======================");
//        TypedQuery<Image> query = em.createQuery("select i from Image i", Image.class);
//        List<Image> resultList = query.getResultList();
//
//        System.out.println("======================");
//        for (Image t : resultList) {
//            System.out.println(t.toString());
//        }
//        System.out.println("======================");
        return resultList;
    }
}
