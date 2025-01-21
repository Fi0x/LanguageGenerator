<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html>
<%@include file="../common/head.jspf" %>
<body>
<%@include file="../common/navigation.jspf" %>
<div class="container">
    <h1>Translations for '${word.letters}' from language '${originalLanguageData.name}'</h1>
    <table class="table sortable">
        <thead>
        <tr>
            <th>Word</th>
            <th>Language</th>
            <th colspan="2">Options</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${translations}" var="singleTranslation" varStatus="status">
            <tr>
                <td>
                    <label>${singleTranslation.word}</label>
                </td>
                <td>
                    <a href="dictionary?languageId=${singleTranslation.languageId}" class="btn-hidden-light">${singleTranslation.languageName}</a>
                </td>
                <td>
                    <a href="word?languageId=${singleTranslation.languageId}&word=${singleTranslation.word}" class="btn">Translations</a>
                </td>
                <td>
                    <c:if test="${username == originalLanguageData.username}">
                        <a href="delete-translation?languageId1=${singleTranslation.languageId}&wordNumber1=${singleTranslation.wordNumber}&languageId2=${word.languageId}&wordNumber2=${word.wordNumber}"
                           class="btn-danger">Delete Translation</a>
                    </c:if>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
    <c:if test="${(originalLanguageData.name.equals('English') || username == originalLanguageData.username) && languages.size() > 1}">
        <form:form method="post" action="translation">
            <input type="hidden" name="languageId" value="${originalLanguageData.id}">
            <input type="hidden" name="word" value="${word.letters}">
            <label>
                <select name="translationLanguageId" class="selection">
                    <c:forEach items="${languages}" var="selectableLanguage">
                        <c:if test="${selectableLanguage.id != originalLanguageData.id}">
                            <option value="${selectableLanguage.id}">${selectableLanguage.name}</option>
                        </c:if>
                    </c:forEach>
                </select>
            </label>
            <label>
                    <%--TODO: When the user types, provide reccommendations of existing words from the selected language--%>
                <input type="text" name="translationWord"/>
            </label>
            <input type="submit" class="btn-success" value="Add Translation"/>
        </form:form>
    </c:if>
</div>
<%@include file="../common/scripts.jspf" %>
<script>
    onload = function () {
        loadNavBar();
    }
</script>
</body>
</html>
