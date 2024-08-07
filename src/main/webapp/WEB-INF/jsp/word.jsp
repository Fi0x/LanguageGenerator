<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html>
<%@include file="../common/head.jspf" %>
<body>
<%@include file="../common/navigation.jspf" %>
<div class="container">
    <h1>Translations for '${word.letters}' from language '${originalLanguageData.name}'</h1>
    <table class="table">
        <thead>
        <tr>
            <th>Word</th>
            <th>Language</th>
            <th>Options</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${translations}" var="singleTranslation" varStatus="status">
            <tr>
                <td>
                    <label>${singleTranslation.word}</label>
                </td>
                <td>
                    <label>${singleTranslation.languageName}</label>
                </td>
                <td>
                    <c:if test="${username == originalLanguageData.username}">
                        <a href="delete-translation?languageId1=${singleTranslation.languageId}&wordNumber1=${singleTranslation.wordNumber}&languageId2=${word.languageId}&wordNumber2=${word.wordNumber}">Delete
                            Translation</a>
                    </c:if>
                </td>
            </tr>
        </c:forEach>
        <form:form method="post" action="">
            <label>
                    <%--TODO: Make this into a drop-down and pre-fill it with existing languages and their names--%>
                <input type="number" name="translationLanguageId"/>
            </label>
            <label>
                    <%--TODO: When the user types, provide reccommendations of existing words from the selected language--%>
                <input type="text" name="translationWord"/>
            </label>
            <input type="submit" class="btn-success" value="Add Translation"/>
        </form:form>
        <%--TODO: Add a button and text-fields, to add a new translation (one text field could also be a drop-down of existing languages)--%>
        <%--TODO: Only show this option, if the user is the owner of the original word's language--%>
        </tbody>
    </table>
</div>
<%@include file="../common/scripts.jspf" %>
</body>
</html>
