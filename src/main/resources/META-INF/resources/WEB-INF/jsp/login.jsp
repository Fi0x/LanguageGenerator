<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<html>
<%@include file="common/head.jspf" %>
<body>
<div class="container">
    <h1>Login</h1>
    <%--@elvariable id="loginDto" type="io.fi0x.languagegenerator.logic.dto.LoginDto"--%>
    <form:form method="post" modelAttribute="loginDto" action="/login">
        Username: <form:input type="text" path="username"/>
        Password: <form:input type="password" path="password"/>
        <input type="submit" class="btn-success">
    </form:form>
    <a href="register" class="btn">Register a new account</a>
</div>
<%@include file="common/scripts.jspf" %>
</body>
</html>