<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>

<script type="text/javascript">
	function setHiddenValue() {
		var selected = document.getElementById("selectedFolderName");
		document.getElementById("selectedFolder").value = selected.options[selected.selectedIndex].value;	
	}
</script>

<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title><spring:message code="label.sticky.notes.upload.file"/></title>
</head>
<body>
	<form:form method="POST" enctype="multipart/form-data" action="/savefile" modelAttribute="filesForm">
		<input type="hidden" id="selectedFolder" name="selectedFolder" value="">
	
		<c:if test="${resultError ne null && not empty resultError}">
	        <table id="RnoMsg" class="RnoMsgError">
	        	<tr>
	        		<td>
	        			<h2><spring:message code="${resultError}"/></h2>
	        		</td>
	        	</tr>
	        </table>
		</c:if>
		
		<div class="panel panel-primary">
	    	<div class="panel-heading">
	        	<h3 class="panel-title"><spring:message code="label.sticky.notes.upload.file"/></h3>
	        </div>
	        <div class="panel-body">
	        
	        	<table>
	        		<tr>
	        			<td>
	        				<strong><spring:message code="label.sticky.notes.select.folder"/></strong>
	        			</td>
	        			<td>
	        				<form:select path="" id="selectedFolderName" onchange="javascript:setHiddenValue();">
								<form:option value="" label="--Select--" />
								<c:forEach var="data" items="${filesForm.filesToList}" varStatus="loop">    
									<c:if test="${!data.file}">
										<form:option value="${data.name}" label="${data.name}" />
									</c:if>
								</c:forEach>
							</form:select>
	        			</td>
	        		</tr>
	        		
	        		<tr>
						<td colspan="2">
							&nbsp;&nbsp;&nbsp;
						</td>
					</tr>
				
					<tr>
						<td>
							<strong><spring:message code="label.sticky.notes.file.to.upload"/></strong>
						</td>
						<td>
							<input type="file" name="file">
						</td>
					</tr>
					
					<tr>
						<td colspan="2">
							&nbsp;&nbsp;&nbsp;
						</td>
					</tr>
					
					<tr>
						<td colspan="2">
							<button type="submit" class="btn btn-sm btn-primary"><spring:message code="label.sticky.notes.upload.file"/></button>
						</td>
					</tr>
				</table>
	        </div>
	    </div>
	</form:form>
</body>
</html>