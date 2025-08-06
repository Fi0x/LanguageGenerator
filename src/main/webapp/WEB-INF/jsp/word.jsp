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
        <tbody id="translationTableBody">
        <c:forEach items="${translations}" var="singleTranslation" varStatus="loop">
            <tr id="translationRow${loop.index}">
                <td>
                    <label>${singleTranslation.word}</label>
                </td>
                <td>
                    <a href="dictionary?languageId=${singleTranslation.languageId}"
                       class="btn-hidden-light">${singleTranslation.languageName}</a>
                </td>
                <td>
                    <a href="word?languageId=${singleTranslation.languageId}&word=${singleTranslation.word}"
                       class="btn">Translations</a>
                </td>
                <td>
                    <c:if test="${username == originalLanguageData.username}">
                        <a onclick="deleteFromDb(${singleTranslation.languageId}, ${singleTranslation.wordNumber}, ${word.languageId}, ${word.wordNumber}, ${loop.index}, this)"
                           class="btn btn-danger">Delete Translation</a>
                    </c:if>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
    <c:if test="${(originalLanguageData.name.equals('English') || username == originalLanguageData.username) && languages.size() > 1}">
        <label>
            <select id="languageSelectionDropDown" class="selection">
                <c:forEach items="${languages}" var="selectableLanguage">
                    <c:if test="${selectableLanguage.id != originalLanguageData.id}">
                        <option value="${selectableLanguage.id}">${selectableLanguage.name}</option>
                    </c:if>
                </c:forEach>
            </select>
        </label>
        <label>
            <input id="translationWord" type="text"/>
        </label>
        <a onclick="saveInDb(${word.languageId}, `${word.letters}`, this, ${word.wordNumber})" class="btn btn-success">Add Translation</a>
    </c:if>
</div>
<%@include file="../common/scripts.jspf" %>
<script src="${pageContext.request.contextPath}/js/dictionary-functions.js"></script>
<script>
    let baseUrl = `${pageContext.request.contextPath}/api`;
</script>
<script>
    onload = function () {
        loadNavBar();
    }
</script>
</body>
</html>
