/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.imagedbservice.authenticate;

import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author imsofa
 */
public interface MemberDao {
    public int persistMember(EntityManager em, String name, String email,  String account, String password);
    public int findMember(EntityManager em, String name, String email);
    public int findMember(EntityManager em, int id, String account, String password);
    public List loadMembers(EntityManager em);
}
