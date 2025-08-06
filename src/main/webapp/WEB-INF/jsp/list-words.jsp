<%@ page import="java.util.List" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<html>
<%@include file="../common/head.jspf" %>
<body>
<%@include file="../common/navigation.jspf" %>
<div class="container">
    <%--@elvariable id="languageName" type="java.lang.String"--%>
    <h1>Generated words with '${languageName}'</h1>
    <table class="table">
        <thead>
        <tr>
            <th>Word</th>
            <th colspan="2">Options</th>
        </tr>
        </thead>
        <tbody>
        <%--@elvariable id="words" type="java.util.List"--%>
        <c:forEach items="${words}" var="singleWord" varStatus="loop">
            <tr>
                <input type="hidden" name="listIndex" value="${singleWord.listIndex}"/>
                <input type="hidden" name="languageId" value="${singleWord.languageId}"/>
                <td>
                    <label>
                        <input id="wordText${loop.index}"
                               onchange="updateSaveState('${singleWord.word}', word.value, ${singleWord.savedInDb}, ${loop.index})"
                               type="text" name="word" value="${singleWord.word}"/>
                    </label>
                </td>
                <td>
                    <a href="word?languageId=${singleWord.languageId}&word=${singleWord.word}"
                       class="btn">Translations</a>
                </td>
                <td>
                    <c:if test="${languageCreator == username}">
                        <a id="saveButton${loop.index}" class="btn btn-success"
                           style="visibility: ${singleWord.savedInDb ? 'hidden' : 'visible'}"
                           onclick="saveInDb(${loop.index}, ${singleWord.languageId}, this)">Save</a>
                        <a id="deleteButton${loop.index}" class="btn btn-danger"
                           onclick="deleteFromDb(${loop.index}, ${singleWord.languageId}, this)"
                           style="visibility: ${singleWord.savedInDb ? "visible" : "hidden"}">Delete</a>
                    </c:if>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
    <a href="generate" class="btn">Generate new words in this language</a>
</div>
<%@include file="../common/scripts.jspf" %>
<script src="${pageContext.request.contextPath}/js/functions.js"></script>
<script src="${pageContext.request.contextPath}/js/word-functions.js"></script>
<script>
    let baseUrl = `${pageContext.request.contextPath}/api`;
    let wordIdMapping = [
        <c:forEach items="${words}" var="singleWord" varStatus="loop">
        {
            text: `${singleWord.word}`,
            dbNumber: ${singleWord.savedInDb ? singleWord.wordNumber : -1}
        }${loop.last ? '' : ','}
        </c:forEach>
    ];
</script>
<script>
    onload = function () {
        loadNavBar();
    }
</script>
</body>
</html>