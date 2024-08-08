<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<html>
<%@include file="../common/head.jspf" %>
<body>
<%@include file="../common/navigation.jspf" %>
<div class="container">
    <h1>Dictionary for '${languageName}'</h1>
    <%--TODO: Make all tables sortable by a desired column--%>
    <%--TODO: Add a button and text field to add a new custom word--%>
    <label class="search-label">
        <input type="text" id="searchText" onkeyup="searchFunction()" class="search-input" placeholder="Search...">
    </label>
    <table id="searchableTable" class="table">
        <thead>
        <tr>
            <th>Word</th>
            <th colspan="2">Options</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${savedWords}" var="word">
            <tr>
                <td>
                    <label>${word.letters}</label>
                </td>
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
</div>
<%@include file="../common/scripts.jspf" %>
<script>
    function searchFunction() {
        var input, filter, table, rows, td, i, txtValue;
        input = document.getElementById("searchText");
        filter = input.value.toUpperCase();
        table = document.getElementById("searchableTable");
        rows = table.getElementsByTagName("tr");

        for (i = 0; i < rows.length; i++) {
            td = rows[i].getElementsByTagName("td")[0];
            if (td) {
                txtValue = td.textContent || td.innerText;
                if (txtValue.toUpperCase().indexOf(filter) > -1) {
                    rows[i].style.display = "";
                } else {
                    rows[i].style.display = "none";
                }
            }
        }
    }
</script>
</body>
</html>
