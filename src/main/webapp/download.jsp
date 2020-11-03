<%-- 
    Document   : download
    Created on : 2020/10/16, 下午 04:38:15
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
        </form><br>
        <input type="button" value="Root" name="CreateCourse" onclick=" location.href='fileservice.jsp'"/>
    </body>
</html>



<!--<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.8.0/jquery.min.js"></script>
        <title>JSP Page</title>
    </head>
    <script type="text/javascript">
        var memberId = <%= session.getAttribute("memberId") %>
        console.log("download: " + memberId);
        function Submit() {
                var imgName = document.getElementById("imgName").value;
                var result = document.getElementById("success");
                console.log(imgName);
                $.ajax({
                    type: "POST",
                    dataType: "image/png",
                    contentType: 'application/json; charset=utf-8',
                    url: "root/fileservice/download",
                    data: JSON.stringify({"imgName": imgName}),
                    complete: function (msg) {
//                        var jsonmsg = JSON.parse(msg);
//                        var downloadStatus = jsonmsg.DownloadStatus;
//                        if (downloadStatus == "Success") {
//                            result.innerHTML = downloadStatus;
                        alert("DownloadStatus " + "downloadStatus");
//                            window.location.href = "appLogin.jsp";
//                        } else {
//                            result.innerHTML = downloadStatus;
//                        }
                    }
                });
            }
    </script>
    <body>
        
        <form>
            <label for="fname">Download image. Image name:</label>
            <input type="text" id="imgName" name="imgName">
            <input type="submit" onclick="Submit()" value="Download">
        </form><br>
        <input type="button" value="Root" name="CreateCourse" onclick=" location.href='fileservice.jsp'"/><br>
        <p><div id="success"></div></p>
    </body>
</html>-->
