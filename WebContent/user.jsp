<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>User</title>
</head>
<body>
<table>
<tr><th>Full Name</th><th>810 Number</th><th>Major</th></tr>
<tr><td>${User.fullName }</td><td>${User.identifier }</td><td>${User.major }</td></tr>
</table>
</body>
</html>