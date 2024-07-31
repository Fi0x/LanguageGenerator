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
<%--                            <input onchange="updateSaveState(singleWord, word.value)" type="text" name="word" value="${singleWord.word}"/>--%>
                            <input onchange="${singleWord.savedInDb = false}" type="text" name="word" value="${singleWord.word}"/>
                        </label>
                    </td>
                    <td>
                            <%--TODO: Add button to show translation page for this word (Should also save the word before showing the page, if it is not yet saved)--%>
                    </td>
                    <c:if test="${languageCreator == username}">
                        <td>
                            <c:choose>
                                <%--                            TODO: Also show the save-button, when the word was changed--%>
                                <c:when test="${singleWord.savedInDb}">
                                    <label>Saved</label>
                                </c:when>
                                <c:otherwise>
                                    <input type="submit" class="btn-success" value="Save"/>
                                </c:otherwise>
                            </c:choose>
                                <%--                        TODO: Show visual indicator, if save was successful--%>
                                <%--                        TODO: Only enable this button, if word does not already exist in db--%>
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
</body>
</html>