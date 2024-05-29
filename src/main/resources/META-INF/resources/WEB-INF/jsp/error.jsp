<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<html>
<head>
    <%@include file="common/head.jspf" %>
</head>
<body>
<%@include file="common/navigation.jspf" %>
<div class="container">
    <h1>Error!</h1>
<%--    TODO: Add details about the error--%>
    <a href="/" class="btn">Go to welcome-page</a>
</div>
<%@include file="common/scripts.jspf" %>
</body>
</html>