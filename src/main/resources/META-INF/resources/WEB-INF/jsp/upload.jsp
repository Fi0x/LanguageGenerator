<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
    <link href="webjars/bootstrap/5.1.3/css/bootstrap.min.css" rel="stylesheet">
    <title>Language Details</title>
</head>
<body>
<%@include file="common/navigation.jspf" %>
<div class="container">
    <h1>Upload a language-file</h1>
    <form method="post" enctype="multipart/form-data" action="/upload">
        <table>
            <tr>
                <td>File to upload:</td>
                <td>
                    <input type="file" name="languageFile"/>
                </td>
            </tr>
            <tr>
                <td></td>
                <td>
                    <input type="submit" class="btn-success"/>
                </td>
            </tr>
        </table>
    </form>
</div>
<script src="webjars/bootstrap/5.1.3/js/bootstrap.min.js"></script>
<script src="webjars/jquery/3.6.0/jquery.min.js"></script>
</body>
</html>