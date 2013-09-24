<%@ page import="csc309.A4.User" %>
<%@ page import="csc309.A4.UserController" %>

<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<META HTTP-EQUIV="CACHE-CONTROL" CONTENT="NO-CACHE">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Welcome <s:property value="#session.logged.first" /> <s:property value="#session.logged.last" /></title>
</head>
<body>
<h1>Manager</h1>
<h2 class="style2">Welcome <s:property value="#session.logged.first" /> <s:property value="#session.logged.last" /></h2>
<s:actionerror/>
			
<s:form action="User_list.action">
	<s:submit value="User List" />
</s:form>

<s:form action="User_new.action">
	<s:submit value="Create New User"  />
</s:form>	

	<form action="User_logout.action">	
		<input type="submit" value='Logout' />
	</form>
	
</body>
</html>