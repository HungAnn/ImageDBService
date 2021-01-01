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
        <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.8.0/jquery.min.js"></script>
        <title>JSP Page</title>
    </head>
    <script type="text/javascript">
        function deparam(query) {
            var pairs, i, keyValuePair, key, value;
            var map = new Map();
            // remove leading question mark if its there
            if (query.slice(0, 1) === '?') {
                query = query.slice(1);
            }
            if (query !== '') {
                pairs = query.split('&');
                for (i = 0; i < pairs.length; i += 1) {
                    keyValuePair = pairs[i].split('=');
                    key = decodeURIComponent(keyValuePair[0]);
                    value = (keyValuePair.length > 1) ? decodeURIComponent(keyValuePair[1]) : undefined;
                    map.set(key, value);
                }
            }
            return map;
        }

        function b64toBlob(b64Data, contentType = '', sliceSize = 512) {
            const byteCharacters = atob(b64Data);
            const byteArrays = [];

            for (let offset = 0; offset < byteCharacters.length; offset += sliceSize) {
                const slice = byteCharacters.slice(offset, offset + sliceSize);

                const byteNumbers = new Array(slice.length);
                for (let i = 0; i < slice.length; i++) {
                    byteNumbers[i] = slice.charCodeAt(i);
                }
                const byteArray = new Uint8Array(byteNumbers);
                byteArrays.push(byteArray);
            }
            const blob = new Blob(byteArrays, {type: contentType});

            return blob;
        }

        $(function () {
            $("#analysisform").on('submit', function (e) {
                var formMap = deparam($('#analysisform').serialize());
                var formData = JSON.stringify({'imgName': formMap.get('imgName').split('.')[0], 'type': formMap.get('imgName').split('.')[1]});

                $.ajax({
                    type: "POST",
                    url: "root/fileservice/analysis",
                    contentType: 'application/json; charset=utf-8',
                    data: formData,
                    success: function (response, status, xhr)
                    {
                        console.log(response);
                        console.log(xhr);
                        if (response['DownloadStatus'] === 'Success' && response['AnalysisStatus'] === '200') {
                            var responseHtml = (response['responseString']).replaceAll("\n", "<br/>");
                            $('#img').attr('src', 'data:image/png;base64,' + response['encodImg']);
                            $('#responseString').html(responseHtml);
                        }
                    },
                    error: function (XMLHttpRequest, textStatus) {
                        $('#responseString').html(XMLHttpRequest['statusText'] + ': No entity found for query Image');
                    }
                });
                e.preventDefault(); // avoid to execute the actual submit of the form.
            });
        })
    </script>
    <body>
        <form id="analysisform">
            <label for="fname">Go analysis. File name:</label>
            <input type="text" id="imgName" name="imgName">
            <input type="submit" value="Submit">
        </form><br>
        <input type="button" value="Root" name="CreateCourse" onclick=" location.href = 'fileservice.jsp'"/>
        <div style="border: 1px solid blue;">
            <div style="border: 1px solid green;">
                <img  id="img">
            </div>
            <div  style="border: 1px solid red;" id="responseString"> </div>
        </div>
        <!--        <form method="post" action="root/fileservice/analysis" enctype="multipart/form-data">
                    <label for="fname">Go analysis. File name:</label>
                    <input type="text" id="fname" name="fname">
                    <input type="submit" value="Submit">
                </form><br>
                <input type="button" value="Root" name="CreateCourse" onclick="location.href = 'fileservice.jsp'"/>-->
    </body>
</html>
