<%-- 
    Document   : upload_success
    Created on : 2020/10/13, 上午 11:23:52
    Author     : imsofa
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <form method="post" action="root/fileservice/download" enctype="multipart/form-data">
            <label for="fname">Download image. Image name:</label>
            <input type="text" id="fname" name="fname">
            <input type="submit" value="Submit">
        </form>

        <form method="post" action="root/fileservice/analysis" enctype="multipart/form-data">
            <label for="fname">Go analysis. File name:</label>
            <input type="text" id="fname" name="fname">
            <input type="submit" value="Submit">
        </form><br>

        <input type="button" value="Root" name="root" onclick="location.href='fileservice.jsp'"/>
    </body>
</html>
