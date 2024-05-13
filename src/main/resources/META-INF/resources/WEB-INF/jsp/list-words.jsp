<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<html>
<head>
    <link href="webjars/bootstrap/5.1.3/css/bootstrap.min.css" rel="stylesheet">
    <title>Language-Generator Word View</title>
</head>
<body>
<%@include file="common/navigation.jspf" %>
<div class="container">
    <h1>Generated words:</h1>
    <table class="table">
        <thead>
        <tr>
            <th>Word</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${words}" var="word">
            <tr>
                <td>${word.word}</td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
<%--    TODO: Also add the option, to generate the new words additionally, instead of replacing the old ones. And add an option to decide how many words should get generated--%>
    <a href="generate?language=${param.language}&amount=10" class="btn">Generate new words in this language</a>
</div>
<script src="webjars/bootstrap/5.1.3/js/bootstrap.min.js"></script>
<script src="webjars/jquery/3.6.0/jquery.min.js"></script>
</body>
</html>