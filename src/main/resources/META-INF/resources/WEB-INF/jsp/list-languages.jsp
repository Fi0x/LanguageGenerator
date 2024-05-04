<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<html>
<head>
    <link href="webjars/bootstrap/5.1.3/css/bootstrap.min.css" rel="stylesheet">
    <title>Language-Generator Language View</title>
</head>
<body>
<%@include file="common/navigation.jspf" %>
<div class="container">
    <h1>Loaded languages:</h1>
    <table class="table">
        <thead>
        <tr>
            <th>Language</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${languages}" var="language">
            <tr>
                <td>${language}</td>
                <td>
                    <a href="generate?language=${language}&amount=10" class="btn">Generate</a>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
    <a href="add-language" class="btn-success">Add Language</a>
</div>
<script src="webjars/bootstrap/5.1.3/js/bootstrap.min.js"></script>
<script src="webjars/jquery/3.6.0/jquery.min.js"></script>
</body>
</html>