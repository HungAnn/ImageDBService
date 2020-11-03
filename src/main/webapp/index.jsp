<html lang="en">
    <head>
        <meta name="google-signin-scope" content="profile email">
        <meta name="google-signin-client_id" content="368378304196-7425nuf6veshm6q8h7o2ue5g0u2jtdpu.apps.googleusercontent.com">
        <script src="https://apis.google.com/js/platform.js" async defer></script>
        <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.8.0/jquery.min.js"></script>
        <script type="text/javascript">
            var name = null;
            var email = null;
            var isMember = null;
            function onSignIn(googleUser) {
                var profile = googleUser.getBasicProfile();
                var imagurl = profile.getImageUrl();
                name = profile.getName();
                email = profile.getEmail().split("@")[0];
                var idtoken = googleUser.getAuthResponse().id_token;
//                document.getElementById("id_token").innerHTML = idtoken;
                document.getElementById("myImg").src = imagurl;
                document.getElementById("name").innerHTML = "Hello! " + name;
                document.getElementById("myP").style.visibility = "hidden";
                document.getElementById("status").style.visibility = "visible";

                $.ajax({
                    type: "POST",
                    dataType: 'text',
                    contentType: 'application/json; charset=utf-8',
                    url: 'root/authenticate/googleLogin',
                    data: JSON.stringify({"idtoken": idtoken}),
                    success: function (msg) {
                        var jsonmsg = JSON.parse(msg);
                        var GoogleLoginStatus = jsonmsg.GoogleLoginStatus;
                        alert("GoogleLogin" + GoogleLoginStatus);
                        if(GoogleLoginStatus == "Success"){
                            var redirectLink = "appLogin.jsp";
                            var redirectText = "Continue with Google login App.";
                        }else{
                            var redirectLink = "appRegister.jsp?name=" + name+"&email="+email;
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
        <a href="#" onclick="signOut();">Sign out</a>
    </body>
</html>


