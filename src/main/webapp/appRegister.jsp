<%-- 
    Document   : app_register
    Created on : 2020/10/30, 下午 05:08:15
    Author     : imsofa
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.8.0/jquery.min.js"></script>
        <title>JSP Page</title>

        <script type="text/javascript">
            
            function encode(str) {
                var buf = [];

                for (var i = str.length - 1; i >= 0; i--) {
                    buf.unshift(['&#', str[i].charCodeAt(), ';'].join(''));
                }
                return buf.join('');
            }
            function decode(str) {
                return str.replace(/&#(\d+);/g, function (match, dec) {
                    return String.fromCharCode(dec);
                });
            }
            function Submit() {
                var name = "<%= request.getParameter("name")%>";
                var email = "<%= request.getParameter("email")%>";
                var account = document.getElementById("account").value;
                var password = document.getElementById("password").value;
                var result = document.getElementById("success");

                $.ajax({
                    type: "POST",
                    dataType: 'text',
                    contentType: 'application/json; charset=utf-8',
                    url: 'root/authenticate/appRegister',
                    data: JSON.stringify({"name": name, "email": email, "account": account, "password": password}),
                    success: function (msg) {
                        var jsonmsg = JSON.parse(msg);
                        var registerStatus = jsonmsg.RegisterStatus;
                        var memberId = jsonmsg.MemberId;
                        if (registerStatus == "Success") {
                            result.innerHTML = "";
                            alert("AppRegister: " + registerStatus);
                            window.location.href = "appLogin.jsp";
                        } else {
                            result.innerHTML = registerStatus;
                        }
                    }
                });
            }
        </script>
    </head>
    <body>

        <form>
            <h1>Register</h1><br>
            Please enter your account:    <input type="text" id="account"><br>
            Please enter your password: <input type="text" id="password"><br>
            <input type = 'button'  onclick="Submit()" value="Register">
        </form><br>
        <p><div id="success"></div></p>
</body>
</html>
