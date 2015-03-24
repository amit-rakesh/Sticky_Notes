<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script type="text/javascript">
	function getAllFiles() {
		var filesForm = document.forms['filesForm'];
		filesForm.method = "get";
		filesForm.action = "getFiles";
		filesForm.submit();
	}
	
	function uploadFile() {
		var filesForm = document.forms['filesForm'];
		filesForm.method = "get";
		filesForm.action = "upload";
		filesForm.submit();
	}
	
	function newFile() {
		var filesForm = document.forms['filesForm'];
		filesForm.method = "get";
		filesForm.action = "newFile";
		filesForm.submit();
	}
	
	function createFolder() {
		var filesForm = document.forms['filesForm'];
		filesForm.method = "get";
		filesForm.action = "newFolder";
		filesForm.submit();
	}
</script>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Menu</title>
</head>
<body>
	<form:form modelAttribute="filesForm">
		<nav class="navbar navbar-inverse">
			<div class="container">
				<div class="navbar-collapse collapse">
					<ul class="nav navbar-nav">
						<li><a href="javascript:getAllFiles();"><img src="/stickynotes/resources/images/backButton1.png" alt="back" height="25px" width="25px"></a></li>
						<li><a href="javascript:getAllFiles();"><spring:message code="label.sticky.notes.getAllFiles"/></a></li>
						<li><a href="javascript:uploadFile();"><spring:message code="label.sticky.notes.uploadFile"/></a></li>
						<li><a href="javascript:newFile();"><spring:message code="label.sticky.notes.newFile"/></a></li>
						<li><a href="javascript:createFolder();"><spring:message code="label.sticky.notes.create.folder"/></a></li>
					</ul>
				</div>
			</div>
		</nav>
	</form:form>
</body>
</html>