<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
    <%@include file="common/head.jspf" %>
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
<%@include file="common/scripts.jspf" %>
</body>
</html>