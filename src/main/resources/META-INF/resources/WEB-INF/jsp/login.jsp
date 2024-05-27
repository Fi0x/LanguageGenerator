<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<html>
<head>
    <link href="webjars/bootstrap/5.1.3/css/bootstrap.min.css" rel="stylesheet">
    <title>Language Generator</title>
</head>
<body>
<div class="container">
    <h1>Login</h1>
<%--    TODO: Use correct dto to send to spring verification--%>
    <form:form method="post" modelAttribute="userDto" action="/register">
        Username: <form:input type="text" path="username"/>
        Password: <form:input type="password" path="password"/>
        <input type="submit" class="btn-success">
    </form:form>
    <a href="register" class="btn">Register a new account</a>
</div>
<script src="webjars/bootstrap/5.1.3/js/bootstrap.min.js"></script>
<script src="webjars/jquery/3.6.0/jquery.min.js"></script>
</body>
</html>