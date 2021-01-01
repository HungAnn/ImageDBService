/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.imagedbservice.fileservice;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

/**
 *
 * @author imsofa
 */
public interface FileService {

    String CATCH_PATH = "/home/imsofa/catch/";

    public Response downloadImage(Image img, @Context HttpServletRequest req);
//    public Response downloadImage(@FormDataParam("fname") String fullName, @Context HttpServletRequest req);

    public Response uploadImage(@FormDataParam("uploadFile") InputStream fileInputStream,
            @FormDataParam("uploadFile") FormDataContentDisposition fileFormDataContentDisposition,
            @Context HttpServletRequest req);

//    public Response analysisImage(@FormDataParam("fname") String fullName, @Context HttpServletRequest req);
    public Response analysisImage(Image img, @Context HttpServletRequest req);
}
