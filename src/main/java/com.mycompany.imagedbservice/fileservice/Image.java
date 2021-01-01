/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.imagedbservice.fileservice;

import com.mycompany.imagedbservice.authenticate.Member;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author imsofa
 */
@Entity
@Table(name = "T_IMAGE")
public class Image {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "IMGNAME")
    private String imgName;
    @Column(name = "TYPE")
    private String type;
    @Column(name = "picByte")
    //    @Column(name = "picByte", length = 500,000)
    private String picByte;
    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    public Image(){
        
        
    }
    public Image(Integer id, String imgName, String type, String picByte, Member member) {
        this.id = id;
        this.imgName = imgName;
        this.type = type;
        this.picByte = picByte;
        this.member = member;
    }
    
    public void setImgName(String imgName) {
        this.imgName = imgName;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public String getImgName() {
        return imgName;
    }

    public Member getMember() {
        return member;
    }

    @Override
    public String toString() {
        return "ImageDB{" + "id=" + id + ", name=" + imgName + ", type=" + type + ", picByte=" + picByte + '}';
    }

    public Integer getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getPicByte() {
        return picByte;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setPicByte(String picByte) {
        this.picByte = picByte;
    }
    
    public String getFullName(){
        System.out.println(this.getImgName()+"."+this.getType());
        return this.getImgName()+"."+this.getType();
    }

}
