<html lang="en">
    <head>
        <meta name="google-signin-scope" content="profile email">
        <meta name="google-signin-client_id" content="302474309018-hti64pabkoq9666favrrb5oqf3cnj3o2.apps.googleusercontent.com">
        <script src="https://apis.google.com/js/platform.js" async defer></script>
        <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.8.0/jquery.min.js"></script>
        <script type="text/javascript">
            <%
                session.invalidate();
            %>
            var name = null;
            var email = null;
            var isMember = null;
            
            $(document).ready(function () {
                var auth2 = gapi.auth2.getAuthInstance();
                auth2.signOut();
                location.reload();
            });
            
            function onSignIn(googleUser) {
                var profile = googleUser.getBasicProfile();
                var imagurl = profile.getImageUrl();
                name = profile.getName();
                email = profile.getEmail().split("@")[0];
                var idtoken = googleUser.getAuthResponse().id_token;
                document.getElementById("myImg").src = imagurl;
                document.getElementById("name").innerHTML = "Hello! " + name;
                document.getElementById("myP").style.visibility = "hidden";
                document.getElementById("status").style.visibility = "visible";
                document.getElementById("signoutB").style.visibility = "visible";

                $.ajax({
                    type: "POST",
                    dataType: 'text',
                    contentType: 'application/json; charset=utf-8',
                    url: 'root/authenticate/googleLogin',
                    data: JSON.stringify({"idtoken": idtoken}),
                    success: function (msg) {
                        var jsonmsg = JSON.parse(msg);
                        var GoogleLoginStatus = jsonmsg.GoogleLoginStatus;
                        alert(GoogleLoginStatus);
                        console.log(msg);
                        if (GoogleLoginStatus == "Success") {
                            var redirectLink = "appLogin.jsp";
                            var redirectText = "Continue with Google login App.";
                        } else {
                            var redirectLink = "appRegister.jsp?name=" + name + "&email=" + email;
                            var redirectText = "You are not a member yet, please register.";
                        }
                        document.getElementById("status").href = redirectLink;
                        document.getElementById("status").innerHTML = redirectText;
                    }
                });
            }
            function signOut() {
                var auth2 = gapi.auth2.getAuthInstance();
                auth2.signOut().then(function () {
                    console.log('User signed out.');
                });
                location.reload();
                var name = null;
                var email = null;
                var isMember = null;
            }
        </script>
    </head>
    <body>
        <div class="g-signin2" data-onsuccess="onSignIn" id="myP"></div>
        <img id="myImg"><br>
        <p id="name"></p>
        <!--<p id="id_token"></p>-->
        <a  id="status" href="#" style="visibility:hidden"></a><br>
        <a id="signoutB" href="#" style="visibility:hidden" onclick="signOut();">Sign out</a>
    </body>
</html>


