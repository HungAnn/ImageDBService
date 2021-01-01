<%-- 
    Document   : download
    Created on : 2020/10/16, 下午 04:38:15
    Author     : imsofa
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!--<!DOCTYPE html>
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
</html>-->



<!DOCTYPE html>

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.8.0/jquery.min.js"></script>
    <title>JSP Page</title>
</head>
<script type="text/javascript">
    var memberId = <%= session.getAttribute("memberId")%>
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
        $("#dform").on('submit', function (e) {
            var formMap = deparam($('#dform').serialize());
            var formData = JSON.stringify({'imgName': formMap.get('imgName').split('.')[0], 'type': formMap.get('imgName').split('.')[1]});

            $.ajax({
                type: "POST",
                url: "root/fileservice/download",
                contentType: 'application/json; charset=utf-8',
                data: formData,
                success: function (response, status, xhr)
                {
                    console.log(response);
                    console.log(xhr);
                    var disposition = xhr.getResponseHeader('Content-Disposition');
                    if (disposition && disposition.indexOf('attachment') !== -1) {
                        var filename = "";
                        var filenameRegex = /filename[^;=\n]*=((['"]).*?\2|[^;\n]*)/;
                        var matches = filenameRegex.exec(disposition);
                        if (matches != null && matches[1])
                            filename = matches[1].replace(/['"]/g, '');

                        $('#img').attr('alt', 'Something wrong in display stage.');

                        var type = xhr.getResponseHeader('Content-Type');
                        var blob = b64toBlob(response, type);

                        if (typeof window.navigator.msSaveBlob !== 'undefined') {
                            // IE workaround for "HTML7007: One or more blob URLs were revoked by closing the blob for which they were created. These URLs will no longer resolve as the data backing the URL has been freed."
                            window.navigator.msSaveBlob(blob, filename);
                        } else {
                            var URL = window.URL || window.webkitURL;
                            var downloadUrl = URL.createObjectURL(blob);

                            if (filename) {
                                // use HTML5 a[download] attribute to specify filename
                                var a = document.createElement("a");
                                // safari doesn't support this yet
                                if (typeof a.download === 'undefined') {
                                    window.location = downloadUrl;
                                } else {
                                    a.href = downloadUrl;
                                    a.download = filename;
                                    document.body.appendChild(a);
                                    a.click();
                                }
                            } else {
                                window.location = downloadUrl;
                            }

                            setTimeout(function () {
                                URL.revokeObjectURL(downloadUrl);
                            }, 100); // cleanup
                        }
                    } else if (xhr.getResponseHeader('Content-Type') === 'application/json') {
                        $('#img').attr('alt', response['DownloadStatus']);
                    }
                    $('#img').attr('src', 'data:image/png;base64,' + response);
                }
//                error: function (e)
//                {
//                    console.log(e);
//                }
            });
            e.preventDefault(); // avoid to execute the actual submit of the form.
        });
    });
</script>
<body>

    <form id="dform">
        <label for="fname">Download image. Image name:</label>
        <input type="text" id="imgName" name="imgName">
        <input type="submit" value="Download">
    </form><br>
    <input type="button" value="Root" name="CreateCourse" onclick=" location.href = 'fileservice.jsp'"/><br>
    <img  id="img">
    <p><div id="success"></div></p>
</body>

