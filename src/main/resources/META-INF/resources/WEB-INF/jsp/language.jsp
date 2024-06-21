<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<html>
<%@include file="../common/head.jspf" %>
<body>
<%@include file="../common/navigation.jspf" %>
<div class="container">
    <h1>Edit Language Details</h1>
<%--    TODO: Update fields to contain new 3 lists--%>
    <%--@elvariable id="languageData" type="io.fi0x.languagegenerator.logic.dto.LanguageData"--%>
    <form:form method="post" modelAttribute="languageData">
        <form:input type="hidden" path="id"/>
        <form:input type="hidden" path="username"/>
        <table>
            <tbody>
            <tr>
                <td>Name:</td>
                <td><form:input type="text" path="name"/></td>
            </tr>
            <tr>
                <td>Public Language:</td>
                <td><form:checkbox path="visible"/></td>
            </tr>
            <tr>
                <td>Min-Word-Length:</td>
                <td><form:input type="number" path="minWordLength"/></td>
            </tr>
            <tr>
                <td>Max-Word-Length:</td>
                <td><form:input type="number" path="maxWordLength"/></td>
            </tr>
            <tr>
                <td>Separate individual letter-combinations by ','</td>
            </tr>
            <tr>
                <td>Possible Vocals:</td>
                <td><form:input type="text" path="vocals"/></td>
            </tr>
            <tr>
                <td>Possible Consonants:</td>
                <td><form:input type="text" path="consonants"/></td>
            </tr>
            <tr>
                <td>Possible Vocal-Consonant-Combinations:</td>
                <td><form:input type="text" path="vocalConsonant"/></td>
            </tr>
            <tr>
                <td>Possible Consonant-Vocal-Combinations:</td>
                <td><form:input type="text" path="consonantVocals"/></td>
            </tr>
            <tr>
                <td>Forbidden Combinations:</td>
                <td><form:input type="text" path="forbiddenCombinations"/></td>
            </tr>
            <tr>
                <td>
                    <input type="submit" class="btn-success" value="Save">
                </td>
            </tr>
            </tbody>
        </table>
    </form:form>
</div>
<%@include file="../common/scripts.jspf" %>
</body>
</html>