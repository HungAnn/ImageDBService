/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.imagedbservice.fileservice;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Tuple;

/**
 *
 * @author imsofa
 */
public interface ImageDao {
    public String persistImg(EntityManager em, String imgName, String type, String picByte, int memberId);
    public String[] findImg(EntityManager em, int memberId, String imgName);
    public List loadImgs(EntityManager em);
}
