<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<html>
<%@include file="common/head.jspf" %>
<body>
<div class="container">
    <h1>Register</h1>
    <%--@elvariable id="userDto" type="io.fi0x.languagegenerator.logic.dto.UserDto"--%>
    <form:form method="post" modelAttribute="userDto" action="/register">
        Username: <form:input type="text" path="username"/>
        <%--@elvariable id="registerError" type="java.util.List"--%>
        Password: <form:input type="password" path="password"/> <a class="error">${registerError}</a>
        Repeat password: <form:input type="password" path="matchingPassword"/>
        <input type="submit" class="btn-success">
    </form:form>
    <a href="custom-login" class="btn">Login with an existing account</a>
</div>
<%@include file="common/scripts.jspf" %>
</body>
</html>