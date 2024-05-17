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
    <form:form method="post" modelAttribute="userDto" action="/register">
        Username: <form:input type="text" path="username"/>
        Password: <form:input type="password" path="password"/>
        Repeat password: <form:input type="password" path="matchingPassword"/>
        <input type="submit" class="btn-success">
<%--        TODO: Add a button to go to the login page instead--%>
    </form:form>
</div>
<script src="webjars/bootstrap/5.1.3/js/bootstrap.min.js"></script>
<script src="webjars/jquery/3.6.0/jquery.min.js"></script>
</body>
</html>