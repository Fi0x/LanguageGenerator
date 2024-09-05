<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<html>
<%@include file="../common/head.jspf" %>
<body>
<%@include file="../common/navigation.jspf" %>
<div class="container">
    <h1>Dictionary for '${languageName}'</h1>
    <label class="search-label">
        <input type="text" id="searchText" onkeyup="searchFunction()" class="search-input" placeholder="Search...">
    </label>
    <table id="searchableTable" class="table sortable">
        <thead>
        <tr>
            <th class="clickable">Word</th>
            <c:if test="${!languageName.equals('English')}">
                <th class="clickable">English Translation</th>
            </c:if>
            <th colspan="2" class="clickable">Options</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${savedWords}" var="word">
            <tr>
                <td>
                    <label>${word.letters}</label>
                </td>
                <c:if test="${!languageName.equals('English')}">
                    <td>
                        <label>${englishTranslations.get(word.wordNumber)}</label>
                    </td>
                </c:if>
                <td>
                    <a href="word?languageId=${word.languageId}&word=${word.letters}" class="btn">Translations</a>
                </td>
                <td>
                    <c:if test="${languageCreator == username}">
                        <a href="delete-word?languageId=${word.languageId}&wordNumber=${word.wordNumber}"
                           class="btn-danger">Delete</a>
                    </c:if>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
    <form:form method="post" action="word">
        <input type="hidden" name="listIndex" value="-1">
        <label>
            <input type="text" name="word"/>
        </label>
        <input type="submit" class="btn-success" value="Add Word"/>
    </form:form>
</div>
<%@include file="../common/scripts.jspf" %>
<script src="${pageContext.request.contextPath}/js/functions.js"></script>
</body>
</html>