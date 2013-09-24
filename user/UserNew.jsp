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
<title>New User Form</title>
</head>
<body>

<h1>New User</h1> 
<s:actionerror/>

<s:form action="Manager.action">
	<s:submit value="Back" />
</s:form>

<s:form action="User_create.action">
 	  	<s:textfield name="login" label="Login" />
 	  	<s:password name="password" label="Password" />
 	  	<s:textfield name="first" label="First" />
 	  	<s:textfield name="last" label="Last" />

 	  	<s:submit value="Create"  />
</s:form>	



</body>
</html>