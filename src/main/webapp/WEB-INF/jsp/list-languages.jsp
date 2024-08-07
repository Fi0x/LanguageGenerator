<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<html>
<%@include file="../common/head.jspf" %>
<body>
<%@include file="../common/navigation.jspf" %>
<div class="container">
    <%--@elvariable id="username" type="java.lang.String"--%>
    <h1>Loaded languages for user ${username}:</h1>
    <table class="table">
        <thead>
        <tr class="underlined-row">
            <th>Language</th>
            <th colspan="2">Generate words</th>
            <th>Creator</th>
            <th>.json-Download</th>
            <th colspan="2">Options for creator</th>
        </tr>
        </thead>
        <tbody>
        <%--@elvariable id="languages" type="java.util.List"--%>
        <c:forEach items="${languages}" var="language">
            <tr class="underlined-row">
                <td>
                        ${language.name}
                </td>
                <td>
                    <a href="generate?language=${language.id}&amount=50" class="btn">Generate new words</a>
                </td>
                <td>
<%--                    TODO: Replace with link to dictionary of this language--%>
                    <a href="generate?language=${language.id}&amount=2" class="btn">Dictionary</a>
                </td>
                <td>
                        ${language.username}
                </td>
                <td>
                    <a href="download?languageId=${language.id}" class="btn">Download</a>
                </td>
                <c:if test="${language.username == username}">
                    <td>
                        <a href="language?languageId=${language.id}" class="btn-edit">Edit</a>
                    </td>
                    <td>
                        <a href="delete-language?languageId=${language.id}" class="btn-danger">Delete</a>
                    </td>
                </c:if>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>
<%@include file="../common/scripts.jspf" %>
</body>
</html>