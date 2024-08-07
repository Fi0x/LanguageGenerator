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
            <th colspan="3">Word</th>
        </tr>
        </thead>
        <tbody>
        <%--@elvariable id="words" type="java.util.List"--%>
        <c:forEach items="${words}" var="singleWord" varStatus="status">
            <tr>
                <form:form method="post" action="word">
                    <input type="hidden" name="listIndex" value="${singleWord.listIndex}"/>
                    <input type="hidden" name="languageId" value="${singleWord.languageId}"/>
                    <td>
                        <label>
                            <input onchange="updateSaveState('${singleWord.word}', word.value, ${singleWord.savedInDb}, ${singleWord.listIndex})"
                                   type="text" name="word" value="${singleWord.word}"/>
                        </label>
                    </td>
                    <td>
                            <%--TODO: Add button to show translation page for this word (Should also save the word before showing the page, if it is not yet saved)--%>
                    </td>
                    <c:if test="${languageCreator == username}">
                        <td>
                            <label id="savedLabel${singleWord.listIndex}"
                                   style="visibility: ${singleWord.savedInDb ? "visible" : "hidden"}">Saved</label>
                            <input id="saveButton${singleWord.listIndex}"
                                   style="visibility: ${singleWord.savedInDb ? "hidden" : "visible"}" type="submit"
                                   class="btn-success" value="Save"/>
                        </td>
                    </c:if>
                </form:form>
            </tr>
        </c:forEach>
        </tbody>
    </table>
    <a href="generate" class="btn">Generate new words in this language</a>
</div>
<%@include file="../common/scripts.jspf" %>
<script>
    function updateSaveState(originalWord, text, isAlreadySaved, listIndex) {
        if (originalWord !== text || !isAlreadySaved) {
            document.getElementById("savedLabel" + listIndex).style.visibility = 'hidden';
            document.getElementById("saveButton" + listIndex).style.visibility = 'visible';
        }
    }
</script>
</body>
</html>