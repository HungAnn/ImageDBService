/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.imagedbservice.authenticate;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author user
 */
@Path("/authenticate")
public class AuthenticateServiceImpl implements AuthenticateService {

    MemberDaoImpl memberDaoImplDaoImpl = null;

    public AuthenticateServiceImpl() throws ClassNotFoundException {
        memberDaoImplDaoImpl = new MemberDaoImpl();
        Class.forName("com.mysql.cj.jdbc.Driver");
    }

    @Override
    @POST
    @Path("/googleLogin")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public String GoogleLogin(String idtokenJson, @Context HttpServletRequest req) throws JSONException {
        HttpTransport transport = null;
        GoogleIdToken idToken = null;
        String GoogleLoginStatus = "Not a member yet.";
        int memberId = -1;
        String idtokenString = new JSONObject(idtokenJson).getString("idtoken");

        EntityManagerFactory factory = Persistence.createEntityManagerFactory("com.mycompany_ImageDBService_war_1.0-SNAPSHOTPU");
        EntityManager em = factory.createEntityManager();
        try {
            transport = GoogleNetHttpTransport.newTrustedTransport();
        } catch (GeneralSecurityException ex) {
            Logger.getLogger(AuthenticateServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(AuthenticateServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        JacksonFactory jsonFactory = JacksonFactory.getDefaultInstance();

        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, jsonFactory)
                .setAudience(Collections.singletonList("302474309018-hti64pabkoq9666favrrb5oqf3cnj3o2.apps.googleusercontent.com"))// Or, if multiple clients access the backend:
                .build();
        try {
            idToken = verifier.verify(idtokenString);
        } catch (GeneralSecurityException ex) {
            Logger.getLogger(AuthenticateServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(AuthenticateServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (idToken != null) {
            System.out.println(idToken);
            Payload payload = idToken.getPayload();
            String email = payload.getEmail().split("@")[0];  // Get profile information from payload
            boolean emailVerified = Boolean.valueOf(payload.getEmailVerified());
            String name = (String) payload.get("name");
            try {
                memberId = memberDaoImplDaoImpl.findMember(em, name, email);
                if (memberId > 0) {
                    GoogleLoginStatus = "Success";
                    HttpSession session = req.getSession(true);
                    session.setAttribute("memberId", memberId);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (em != null) {
                    em.close();
                }
                if (factory != null) {
                    factory.close();
                }
            }
        } else {
            GoogleLoginStatus = "Unsuccessfully: Invalid Google account";
        }

        return new JSONObject().put("GoogleLoginStatus", GoogleLoginStatus).toString();
    }

    @POST
    @Path("/appRegister")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    @Override
    public Response AppRegister(Member member, @Context HttpServletRequest req) {
        String registerStatus = "Unsuccessfully: Have some problem in persistMember";
        int memberId = -1;
        String name = member.getName();
        String email = member.getEmail();
        String account = member.getAccount();
        String password = member.getPassword();

        EntityManagerFactory factory = Persistence.createEntityManagerFactory("com.mycompany_ImageDBService_war_1.0-SNAPSHOTPU");
        EntityManager em = factory.createEntityManager();
        try {
            memberId = memberDaoImplDaoImpl.persistMember(em, name, email, account, password);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (em != null) {
                em.close();
            }
            if (factory != null) {
                factory.close();
            }
        }
        if (memberId > 0) {
            registerStatus = "Success";
            HttpSession session = req.getSession(true);
            session.setAttribute("memberId", memberId);
        } else {
            registerStatus = "Unsuccessfully: Have some porblem in persistence step.";
        }
        return Response.ok(new JSONObject().put("RegisterStatus", registerStatus).put("MemberId", memberId).toString(), MediaType.APPLICATION_JSON)
                .build();
    }

    @Override
    @POST
    @Path("/appLogin")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response AppLogin(Member member, @Context HttpServletRequest req) {
        int memberId = -1;
        String loginStatus = "Get wrong account or password";
        String account = member.getAccount();
        String password = member.getPassword();
        HttpSession session = req.getSession(true);
        int id = (int) session.getAttribute("memberId");

        EntityManagerFactory factory = Persistence.createEntityManagerFactory("com.mycompany_ImageDBService_war_1.0-SNAPSHOTPU");
        EntityManager em = factory.createEntityManager();
        try {
            memberId = memberDaoImplDaoImpl.findMember(em, id, account, password);
            if (memberId > 0) {
                loginStatus = "Success";
                session.setAttribute("memberId", memberId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (em != null) {
                em.close();
            }
            if (factory != null) {
                factory.close();
            }
        }

        return Response.ok(new JSONObject().put("LoginStatus", loginStatus).toString(), MediaType.APPLICATION_JSON)
                .build();
    }
//    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
//    public String AppLogin(@FormParam("account") String account, @FormParam("password") String password) {
//        boolean isMember = false;
//        EntityManagerFactory factory = Persistence.createEntityManagerFactory("com.mycompany_GoogleAuthenticate_war_1.0-SNAPSHOTPU");
//        EntityManager em = factory.createEntityManager();
//        try{
//            isMember = memberDaoImplDaoImpl.findMember(em, account, password, true);
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            if (em != null) {
//                em.close();
//            }
//            if (factory != null) {
//                factory.close();
//            }
//        }
//        System.out.println("======================");
//        System.out.println(account+password);
//        System.out.println("======================");
//        
//        return isMember;
//    }

}
