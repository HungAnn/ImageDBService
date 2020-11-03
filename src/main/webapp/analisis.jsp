<%-- 
    Document   : analisis
    Created on : 2020/10/16, 下午 04:39:36
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
        <form method="post" action="root/fileservice/analysis" enctype="multipart/form-data">
            <label for="fname">Go analysis. File name:</label>
            <input type="text" id="fname" name="fname">
            <input type="submit" value="Submit">
        </form><br>
        <input type="button" value="Root" name="CreateCourse" onclick="location.href='fileservice.jsp'"/>
    </body>
</html>
