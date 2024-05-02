<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
    <title>Language-Generator Word View</title>
</head>
<body>
<div>
    <h1>Generated words:</h1>
    <table>
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
</div>
<script src="webjars/bootstrap/5.1.3/js/bootstrap.min.js"></script>
<script src="webjars/jquery/3.6.0/jquery.min.js"></script>
<script src="webjars/bootstrap-datepicker/1.9.0/js/bootstrap-datepicker.min.js"></script>
</body>
</html>