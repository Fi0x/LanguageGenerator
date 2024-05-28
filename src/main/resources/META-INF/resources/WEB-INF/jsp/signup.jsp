<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<html>
<head>
    <link href="webjars/bootstrap/5.1.3/css/bootstrap.min.css" rel="stylesheet">
    <title>Language Generator</title>
</head>
<body>
<div class="container">
    <h1>Register</h1>
    <%--@elvariable id="userDto" type="io.fi0x.languagegenerator.logic.dto.UserDto"--%>
    <form:form method="post" modelAttribute="userDto" action="/register">
        Username: <form:input type="text" path="username"/>
        Password: <form:input type="password" path="password"/> <a class="error">${registerError}</a>
        Repeat password: <form:input type="password" path="matchingPassword"/>
        <input type="submit" class="btn-success">
    </form:form>
    <a href="custom-login" class="btn">Login with an existing account</a>
</div>
<script src="webjars/bootstrap/5.1.3/js/bootstrap.min.js"></script>
<script src="webjars/jquery/3.6.0/jquery.min.js"></script>
</body>
</html>