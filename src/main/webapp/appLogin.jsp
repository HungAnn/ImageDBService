<%-- 
    Document   : app_login
    Created on : 2020/10/30, 下午 03:11:42
    Author     : imsofa
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.8.0/jquery.min.js"></script>
        <title>JSP Page</title>
    </head>
    <script type="text/javascript">
        <%
            Object memberId = session.getAttribute("memberId");
        %>
        var memberId = <%= memberId%>;
        
        function login() {
            var account = document.getElementById("account").value;
            var password = document.getElementById("password").value;

            $.ajax({
                type: "POST",
                url: "root/authenticate/appLogin",
                dataType: 'text',
                contentType: 'application/json; charset=utf-8',
                data: JSON.stringify({"account": account, "password": password}),
                success: function (msg) {
                    var jsonmsg = JSON.parse(msg);
                    var loginStatus = jsonmsg.LoginStatus;
                    var result = document.getElementById("success");
                    result.innerHTML = loginStatus;
                    if (loginStatus == "Success") {
                        document.getElementById("form1").style.visibility = "hidden";
                        document.getElementById("signout").style.visibility = "visible";
                        document.getElementById("DBService").style.visibility = "visible";
                    }
                },
                error: function (msg) {
                    var result = document.getElementById("success");
                    result.innerHTML = "Have some problem in transmission stage";
                }
            });
        }
        function signOut() {
            window.location.href = "index.jsp";
        }
    </script>
    <body>
        <form id="form1" method="post">
            <h1>Login</h1><br>
            Please enter your account:    <input type="text" id="account" name="account"><br>
            Please enter your password: <input type="text" id="password" name="password"><br>
            <input type = 'button'  onclick="login()" value="Login" >
        </form><br>
        <p><div id="success"></div></p>
    <a  id="DBService" href="fileservice.jsp" style="visibility:hidden">Go to my web service.</a><br>
    <a id="signout" href="#" onclick="signOut();" style="visibility:hidden">Sign out</a>
</body>
</html>
