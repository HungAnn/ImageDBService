<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
        <title>Jersey 2.x : File Upload</title>
    </head>
    <script type="text/javascript">
        var memberId = <%= session.getAttribute("memberId") %>
        console.log("upload: " + memberId);
    </script>
    <body>
        <center>
            <b>Upload Image</b>

            <div style="width: 400px; border: 1px solid blue; padding: 20px; text-align: center;">
                <form method="post" action="root/fileservice/upload" enctype="multipart/form-data">
                    <table align="center" border="1" bordercolor="black" cellpadding="0" cellspacing="0">
                        <tr>
                            <td>Select File 1 :</td>

                            <td><input type="file" name="uploadFile" size="100" /></td>
                            
                        </tr>

                        <tr>

                            <td><input type="submit" value="Upload File" /></td>

                            <td><input type="reset" value="Reset" /></td>
                        </tr>
                    </table>
                </form>
            </div>
            <input type="button" value="Root" name="CreateCourse" onclick="location.href = 'fileservice.jsp'"/>
        </center>

    </body>
</html>
