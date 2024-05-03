<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<html>
<head>
    <link href="webjars/bootstrap/5.1.3/css/bootstrap.min.css" rel="stylesheet">
    <title>Language Details</title>
</head>
<body>
<nav class="navbar navbar-expand-md navbar-light bg-light mb-3 p-1">
    <a class="navbar-brand m-1" href="https://courses.in28minutes.com">in28minutes</a>
    <div class="collapse navbar-collapse">
        <ul class="navbar-nav">
            <li class="nav-item"><a class="nav-link" href="/">Home</a></li>
            <li class="nav-item"><a class="nav-link" href="/list-todos">Todos</a></li>
        </ul>
    </div>
    <ul class="navbar-nav">
        <li class="nav-item"><a class="nav-link" href="/logout">Logout</a></li>
    </ul>
</nav>
<div class="container">
    <h1>Edit Language Details</h1>
    <form method="post">
        Name: <input type="text" name="languageName">
        <input type="submit" class="btn-success">
    </form>
</div>
<script src="webjars/bootstrap/5.1.3/js/bootstrap.min.js"></script>
<script src="webjars/jquery/3.6.0/jquery.min.js"></script>
</body>
</html>