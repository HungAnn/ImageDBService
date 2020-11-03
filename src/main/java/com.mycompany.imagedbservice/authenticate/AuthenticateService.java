/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.imagedbservice.authenticate;

import java.io.InputStream;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.FormParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

/**
 *
 * @author user
 */
public interface AuthenticateService {
    public Response AppRegister(Member member, @Context HttpServletRequest req);
    public Response AppLogin(Member member, @Context HttpServletRequest req);
    public String GoogleLogin(String idtokenString, @Context HttpServletRequest req);
}
