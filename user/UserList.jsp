<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="csc309.A4.User" %>
 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<META HTTP-EQUIV="CACHE-CONTROL" CONTENT="NO-CACHE">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Users List</title>
</head>
<body>

<h1>Users</h1> 
<s:actionerror/>

<s:form action="Manager.action">
	<s:submit value="Back" />
</s:form>
	

<table border='1'>

		
	<tr><td>id</td><td>login</td><td>salt</td><td>first</td><td>last</td><td>admin</td></tr>
	<s:iterator value="users">
		<tr>
			<td><s:property value="id" /></td>
			<td><s:property value="login" /></td>
			<td><s:property value="salt" /></td>
			<td><s:property value="first" /></td>
			<td><s:property value="last" /></td>
			<td><s:property value="admin" /></td>
		</tr>
	</s:iterator>
</table>
	<s:form action="managerHis.action">
		<s:submit value="View history" />
	</s:form>

</body>
</html>