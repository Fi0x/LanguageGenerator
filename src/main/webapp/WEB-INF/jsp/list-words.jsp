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
        </tr>
        </thead>
        <tbody>
        <%--@elvariable id="words" type="java.util.List"--%>
        <c:forEach items="${words}" var="word">
            <tr>
                <td>${word.word}</td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
    <a href="generate" class="btn">Generate new words in this language</a>
</div>
<%@include file="../common/scripts.jspf" %>
</body>
</html>