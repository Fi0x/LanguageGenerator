<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<html>
<head>
    <%@include file="common/head.jspf" %>
</head>
<body>
<%@include file="common/navigation.jspf" %>
<div class="container">
    <h1>Loaded languages for user ${username}:</h1>
    <table class="table">
        <thead>
        <tr>
            <th>Language</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${languages}" var="language">
            <tr>
                <td>${language.name}</td>
                <td>
                    <a href="generate?language=${language.id}&amount=10" class="btn">Generate 10</a>
                </td>
                <td>
                    <a href="generate?language=${language.id}&amount=100" class="btn">Generate 100</a>
                </td>
                <td>
                    <a href="download?languageId=${language.id}" class="btn">Download</a>
                </td>
                <td>
                    <a href="language?languageId=${language.id}" class="btn">Edit</a>
                </td>
                <td>
                    <a href="delete-language?languageId=${language.id}" class="btn-danger">Delete</a>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
    <a href="language" class="btn-success">Create new Language</a>
    <a href="upload" class="btn-success">Upload language.json file</a>
</div>
<%@include file="common/scripts.jspf" %>
</body>
</html>