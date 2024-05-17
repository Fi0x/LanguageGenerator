<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<html>
<head>
    <link href="webjars/bootstrap/5.1.3/css/bootstrap.min.css" rel="stylesheet">
    <title>Language Generator</title>
</head>
<body>
<%@include file="common/navigation.jspf" %>
<div class="container">
    <h1>Edit Language Details</h1>
    <form:form method="post" modelAttribute="languageData">
        <form:input type="hidden" path="id"/>
        Name: <form:input type="text" path="name"/>
        <form:input type="hidden" path="username"/>
<%--        TODO: make this visible and changeable. Currently produces null values!--%>
        <form:input type="hidden" path="isPublic" />
        Min-Word-Length: <form:input type="number" path="minWordLength"/>
        Max-Word-Length: <form:input type="number" path="maxWordLength"/>
        <a>Separate individual letter-combinations by ','</a>
        Possible Vocals: <form:input type="text" path="vocals"/>
        Possible Consonants: <form:input type="text" path="consonants"/>
        Possible Vocal-Consonant-Combinations: <form:input type="text" path="vocalConsonant"/>
        Possible Consonant-Vocal-Combinations: <form:input type="text" path="consonantVocals"/>
        Forbidden Combinations: <form:input type="text" path="forbiddenCombinations"/>
        <input type="submit" class="btn-success">
    </form:form>
</div>
<script src="webjars/bootstrap/5.1.3/js/bootstrap.min.js"></script>
<script src="webjars/jquery/3.6.0/jquery.min.js"></script>
</body>
</html>