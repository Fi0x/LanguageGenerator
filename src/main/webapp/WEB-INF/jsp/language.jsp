<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<html>
<%@include file="../common/head.jspf" %>
<body>
<%@include file="../common/navigation.jspf" %>
<div class="container">
    <h1>Edit Language Details</h1>
    <%--@elvariable id="languageData" type="io.fi0x.languagegenerator.logic.dto.LanguageData"--%>
    <form:form method="post" modelAttribute="languageData">
        <form:input type="hidden" path="id"/>
        <form:input type="hidden" path="username"/>
        <table class="table">
            <thead>
            <tr class="underlined-row header-row">
                <th colspan="2">Required settings</th>
                <th colspan="2">Optional settings</th>
            </tr>
            </thead>
            <tbody>
<%--TODO: Add an option, to create an existing real language. This disables all the generation options and creates a placeholder language--%>
            <tr>
                <td>Name*:</td>
                <td><form:input type="text" path="name"/></td>
                <td>Min Characters before special Chars:</td>
                <td><form:input type="number" path="charsBeforeSpecial"/></td>
            </tr>
            <tr>
                <td>Public Language*:</td>
                <td><form:checkbox path="visible"/></td>
                <td>Min Characters after special Chars:</td>
                <td><form:input type="number" path="charsAfterSpecial"/></td>
            </tr>
            <tr>
                <td>Min-Word-Length*:</td>
                <td><form:input type="number" path="minWordLength"/></td>
                <td>Min-Amount of special Chars per word:</td>
                <td><form:input type="number" path="minSpecialChars"/></td>
            </tr>
            <tr>
                <td>Max-Word-Length*:</td>
                <td><form:input type="number" path="maxWordLength"/></td>
                <td>Max-Amount of special chars per word:</td>
                <td><form:input type="number" path="maxSpecialChars"/></td>
            </tr>
            <tr>
                <td colspan="2"></td>
                <td>Chance for special characters:</td>
                <td><form:input type="number" path="specialCharacterChance"/></td>
            </tr>
            <tr>
                <th colspan="4">Letter combinations*, separated by ','.</th>
            </tr>
            <tr>
                <td>Possible Vocals:</td>
                <td><form:input type="text" path="vocals"/></td>
                <td>Special Character Combinations:</td>
                <td><form:input type="text" path="specialCharacters"/></td>
            </tr>
            <tr>
                <td>Possible Consonants:</td>
                <td><form:input type="text" path="consonants"/></td>
                <td>Forbidden Combinations:</td>
                <td><form:input type="text" path="forbiddenCombinations"/></td>
            </tr>
            <tr>
                <td>Possible Vocal-Consonant-Combinations:</td>
                <td><form:input type="text" path="vocalConsonant"/></td>
                <td>Word Beginnings:</td>
                <td><form:input type="text" path="startingCombinations"/></td>
            </tr>
            <tr>
                <td>Possible Consonant-Vocal-Combinations:</td>
                <td><form:input type="text" path="consonantVocals"/></td>
                <td>Word Endings:</td>
                <td><form:input type="text" path="endingCombinations"/></td>
            </tr>
            <tr>
                <td colspan="4">
                    <input type="submit" class="btn-success" value="Save">
                </td>
            </tr>
            </tbody>
        </table>
    </form:form>

    <a>* Not all, but at least 1 allowed letter combination needs to contain entries.<br>
        Special characters, forbidden, starting and ending combinations can be empty.<br>
        The combinations are used to build words in the following order:
        consonant-vocal > consonant > vocal-consonant > vocal.
        This order loops until the correct word-length is created.<br>
        While looping, the forbidden combinations are constantly checked, to avoid any entries in there.<br>
        If a combination list does not contain enough entries to avoid a forbidden combination,
        the next combination list will be used instead.<br>
        If entries are present in the starting combinations, those will be used before looping through the other combinations.<br>
        If ending combinations exist, those will be added to the end of each created word,
        but they will not exceed the maximum word length for the language.<br>
        Special characters will be added after the word is completed and has not used the maximum word length for this language.<br>
    </a>
</div>
<%@include file="../common/scripts.jspf" %>
</body>
</html>