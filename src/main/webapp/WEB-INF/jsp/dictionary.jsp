<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<html>
<%@include file="../common/head.jspf" %>
<body>
<%@include file="../common/navigation.jspf" %>
<div class="container">
    <h1>Dictionary for '${languageName}'</h1>
<%--TODO: Add a search function--%>
<%--TODO: Add a button and text field to add a new custom word--%>
    <table class="table">
        <thead>
        <tr>
            <th>Word</th>
            <th colspan="2">Options</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${savedWords}" var="word">
            <tr>
                <td>
                    <label>${word.letters}</label>
                </td>
                <td>
                    <a href="word?languageId=${word.languageId}&word=${word.letters}" class="btn">Translations</a>
                </td>
                <td>
                    <c:if test="${languageCreator == username}">
                        <a href="delete-word?languageId=${word.languageId}&wordNumber=${word.wordNumber}" class="btn-danger">Delete</a>
                    </c:if>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>
<%@include file="../common/scripts.jspf" %>
</body>
</html>
