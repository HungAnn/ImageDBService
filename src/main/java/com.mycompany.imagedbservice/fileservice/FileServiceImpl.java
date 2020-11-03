/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.imagedbservice.fileservice;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.media.multipart.file.FileDataBodyPart;
import org.json.JSONObject;

/**
 *
 * @author imsofa
 */
@Path("/fileservice")
public class FileServiceImpl implements FileService {

    private ImageDaoImpl imageDaoImpl = null;

    public FileServiceImpl() throws ClassNotFoundException {
        imageDaoImpl = new ImageDaoImpl();
        Class.forName("com.mysql.cj.jdbc.Driver");
    }

    @POST
    @Path("/download")
//    @Consumes(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces({"image/png", "image/jpg", "image/gif"})
//    public Response downloadImage(Image img, @Context HttpServletRequest req) {
    public Response downloadImage(@FormDataParam("fname") String fullName, @Context HttpServletRequest req) {
        byte[] Img = null;
        String[] findResult = null;
        ResponseBuilder responseBuilder = null;
        byte[] decodedBytes = null;
        String downloadStatus = "";
        HttpSession session = req.getSession(true);
        int memberId = (int) session.getAttribute("memberId");
//        String fullName = img.getImgName();
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("com.mycompany_ImageDBService_war_1.0-SNAPSHOTPU");
        EntityManager em = factory.createEntityManager();

        String[] base_ext = separateName(fullName);
        String imgName = base_ext[0];
        try {
            findResult = imageDaoImpl.findImg(em, memberId, imgName);
            System.out.println("======================");
            System.out.println(memberId+" "+imgName);
            System.out.println(findResult);
            System.out.println("======================");
            downloadStatus = findResult[0];
            if (downloadStatus == "Success") {
                String encodImg = findResult[1];
                decodedBytes = Base64.getDecoder().decode(encodImg);
                responseBuilder = Response.ok(decodedBytes);
                responseBuilder.header("Content-Disposition", "attachment; filename=\"" + fullName + "\"");
                return responseBuilder.build();
            } else {
                return Response.ok(new JSONObject().put("DownloadStatus", downloadStatus).put("MemberId", memberId).toString(), MediaType.APPLICATION_JSON).build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Response.ok(new JSONObject().put("DownloadStatus", downloadStatus).put("MemberId", memberId).toString(), MediaType.APPLICATION_JSON).build();
        } finally {
            if (em != null) {
                em.close();
            }
            if (factory != null) {
                factory.close();
            }
        }
    }

    @POST
    @Path("/upload")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response uploadImage(
            @FormDataParam("uploadFile") InputStream fileInputStream,
            @FormDataParam("uploadFile") FormDataContentDisposition fileFormDataContentDisposition,
            @Context HttpServletRequest req) {

        try {
            HttpSession session = req.getSession(true);
            int memberId = (int) session.getAttribute("memberId");
            String persistStatus = writeToDBServer(fileInputStream, fileFormDataContentDisposition, memberId);
            if (persistStatus == "Success") {
                return Response.temporaryRedirect(URI.create("http://localhost:8080/ImageDBService/upload_success.jsp")).build();
            } else {
                return Response.ok(persistStatus).build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Response.ok("Have some problem.").build();
        }
    }

    @POST
    @Path("/analysis")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response analysisImage(@FormDataParam("fname") String Imgname) {
        String httpURL = "http://127.0.0.1:5000";
        ClientConfig clientConfig = null;
        Client client = null;
        WebTarget webTarget = null;
        Invocation.Builder invocationBuilder = null;
        Response response = null;
        FileDataBodyPart fileDataBodyPart = null;
        FormDataMultiPart formDataMultiPart = null;
        int responseCode;
        String responseMessageFromServer = null;
        String responseString = null;

        try {
            clientConfig = new ClientConfig();  // invoke service after setting necessary parameters
            clientConfig.register(MultiPartFeature.class
            );
            client = ClientBuilder.newClient(clientConfig);
            webTarget = client.target(httpURL);

            formDataMultiPart = new FormDataMultiPart();
            fileDataBodyPart = new FileDataBodyPart("uploadFile", new File(UPLOAD_FILE_SERVER + Imgname), MediaType.APPLICATION_OCTET_STREAM_TYPE);
            formDataMultiPart.bodyPart(fileDataBodyPart);   //???
            formDataMultiPart.field("image_name", Imgname);

            invocationBuilder = webTarget.request();    // invoke service
            response = invocationBuilder.post(Entity.entity(formDataMultiPart, MediaType.MULTIPART_FORM_DATA));
            responseCode = response.getStatus();    // get response code
            System.out.println("Response code: " + responseCode);

            if (response.getStatus() != 200) {
                throw new RuntimeException("Failed with HTTP error code : " + responseCode);
            }
            responseMessageFromServer = response.getStatusInfo().getReasonPhrase(); // get response message
            responseString = response.readEntity(String.class);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {   // release resources, if any
                fileDataBodyPart.cleanup();
                formDataMultiPart.cleanup();
                formDataMultiPart.close();
                response.close();
                client.close();
            } catch (IOException ex) {
                Logger.getLogger(FileServiceImpl.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
        }
        return Response.ok(responseString).build();
    }

    private String writeToDBServer(InputStream inputStream, FormDataContentDisposition fullName, int memberId) throws IOException {
        ByteArrayOutputStream outputStream = null;
        String[] base_ext = separateName(fullName.getFileName());
        String imgName = base_ext[0];
        String imgType = base_ext[1];
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("com.mycompany_ImageDBService_war_1.0-SNAPSHOTPU");
        EntityManager em = factory.createEntityManager();
        String persistStatus = null;

        try {
            outputStream = new ByteArrayOutputStream();
            int read = 0;
            byte[] bytes = new byte[1024];
            while ((read = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
            }
            String encodeImg = Base64.getEncoder().encodeToString(outputStream.toByteArray());
            persistStatus = imageDaoImpl.persistImg(em, imgName, imgType, encodeImg, memberId);
//            System.out.println("===================");
////            System.out.println(outputStream.toByteArray().length);
//            System.out.println(encodeImg.length());
//            System.out.println("===================");
            outputStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            outputStream.close(); //release resource, if any
            if (em != null) {
                em.close();
            }
            if (factory != null) {
                factory.close();
            }
            return persistStatus;
        }
    }

    private String[] separateName(String fullname) {
        String base = fullname.substring(0, fullname.lastIndexOf(".")).toLowerCase();
        String extension = fullname.substring(fullname.lastIndexOf(".") + 1, fullname.length()).toLowerCase();
        String[] base_extension = {base, extension};

        return base_extension;
    }
}
