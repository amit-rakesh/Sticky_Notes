<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">
<title>Edit File Page</title>
<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/resources/css/jquery.ketchup.css" />">
<script type="text/javascript" src="<c:url value="/resources/js/jquery-1.4.4.min.js"/>"></script>
<script type="text/javascript" src="<c:url value="/resources/js/jquery.ketchup.all.min.js"/>"></script>
<script type="text/javascript" src="<c:url value="/resources/js/jquery.validate.js"/>"></script>
<script type="text/javascript">
	window.onload= initPage;
	function initPage(){
		$('#default-behavior').ketchup();
	// Ketchup Validation Code ends
	}
</script>
<link href="<c:url value='/resources/favicon.ico'/>" rel="icon"> 
<link href="<c:url value='/resources/css/bootstrap.min.css' />" rel="stylesheet">
<link href="<c:url value='/resources/css/signin.css' />" rel="stylesheet">
<script src="<c:url value='/resources/js/ie-emulation-modes-warning.js' />"></script>
<link href="<c:url value='/resources/css/create.css' />" rel="stylesheet">
</head>
<body>
	<div class="panel panel-primary">
    	<div class="panel-heading">
        	<h3 class="panel-title"><spring:message code="label.sticky.notes.edit.file"/></h3>
        </div>
        <div class="panel-body">
			<form:form method="post" action="createsaveeditfile" modelAttribute="createFile" class="form-signin" role="form" id="default-behavior">
        		<form:input value="${filesForm.filesToList}" type="text" readonly="true" path="newName" id="inputEmail" class="form-control" placeholder="New File Name" data-validate="validate(required)" />
        		<form:textarea value="${filesForm.filesToList}" path="filecontents" id="inputEmail" class="form-control" placeholder="Write Data" data-validate="validate(required)" rows="8" cols="200" />
        		<button class="btn btn-lg btn-primary btn-block" type="submit"><spring:message code="label.sticky.notes.save.file"/></button>
			</form:form>
		</div>
	</div>
    <script src="<c:url value="/resources/js/ie10-viewport-bug-workaround.js" />"></script>
</body>
</html>