<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<html>
<%@include file="common/head.jspf" %>
<body>
<%@include file="common/navigation.jspf" %>
<div class="container">
    <h1>Error ${errorCode}!</h1>
    <table>
        <tbody>
        <tr>
            <td>
                ${errorMessage}
            </td>
        </tr>
        <tr>
            <td>
                <a href="/" class="btn">Go to welcome-page</a>
            </td>
        </tr>
        </tbody>
    </table>
</div>
<%@include file="common/scripts.jspf" %>
</body>
</html>