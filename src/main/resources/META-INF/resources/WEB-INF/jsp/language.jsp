<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<html>
<head>
    <%@include file="common/head.jspf" %>
</head>
<body>
<%@include file="common/navigation.jspf" %>
<div class="container">
    <h1>Edit Language Details</h1>
    <%--@elvariable id="languageData" type="io.fi0x.languagegenerator.logic.dto.LanguageData"--%>
    <form:form method="post" modelAttribute="languageData">
        <form:input type="hidden" path="id"/>
        <form:input type="hidden" path="username"/>
        <table>
            <tr>
                Name: <form:input type="text" path="name"/>
            </tr>
            <tr>
                    <%--        TODO: Fix the initial state of the checkbox to represent the language data--%>
                Public Language: <form:checkbox path="isPublic" checked="${languageData.isPublic}"/>
            </tr>
            <tr>
                Min-Word-Length: <form:input type="number" path="minWordLength"/>
            </tr>
            <tr>
                Max-Word-Length: <form:input type="number" path="maxWordLength"/>
            </tr>
            <tr>
                <a>Separate individual letter-combinations by ','</a>
            </tr>
            <tr>
                Possible Vocals: <form:input type="text" path="vocals"/>
            </tr>
            <tr>
                Possible Consonants: <form:input type="text" path="consonants"/>
            </tr>
            <tr>
                Possible Vocal-Consonant-Combinations: <form:input type="text" path="vocalConsonant"/>
            </tr>
            <tr>
                Possible Consonant-Vocal-Combinations: <form:input type="text" path="consonantVocals"/>
            </tr>
            <tr>
                Forbidden Combinations: <form:input type="text" path="forbiddenCombinations"/>
            </tr>
            <tr>
                <input type="submit" class="btn-success">
            </tr>
        </table>
    </form:form>
</div>
<%@include file="common/scripts.jspf" %>
</body>
</html>