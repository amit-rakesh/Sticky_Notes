<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>

<script type="text/javascript">
	function deleteFile(pathFile) {
		var message ='<spring:message code="confirmation.delete"/>';
		var confirmmsg = confirm(message);
		if(confirmmsg){
			var filesForm = document.forms['filesForm'];
			filesForm.method = "post";
			filesForm.action = "delete?pathToDelete="+pathFile;
			filesForm.submit();
		}
	}
	
	function downloadFile(pathFile, fileNameToDownload) {
		var filesForm = document.forms['filesForm'];
		filesForm.method = "post";
		filesForm.action = "download?pathToDownload="+pathFile+"&fileNameToDownload="+fileNameToDownload;
		filesForm.submit();
	}
	
	function readFile(pathFile, fileNameToRead) {
		var filesForm = document.forms['filesForm'];
		filesForm.method = "post";
		filesForm.action = "read?pathToRead="+pathFile+"&fileNameToRead="+fileNameToRead;
		filesForm.submit();
	}
	
	function sharePath(pathToShare) {
		var filesForm = document.forms['filesForm'];
		filesForm.method = "post";
		filesForm.action = "shareUrl?sharePathUrl="+pathToShare;
		filesForm.submit();
	}
	
	function editFile(pathFile, fileNameToRead) {
		var filesForm = document.forms['filesForm'];
		filesForm.method = "post";
		filesForm.action = "readfile?pathToRead="+pathFile+"&filename="+fileNameToRead;
		filesForm.submit();
	}
	
	/*(function(document) {
		'use strict';

		var LightTableFilter = (function(Arr) {

			var _input;

			function _onInputEvent(e) {
				_input = e.target;
				var tables = document.getElementsByClassName(_input.getAttribute('data-table'));
				Arr.forEach.call(tables, function(table) {
					Arr.forEach.call(table.tBodies, function(tbody) {
						Arr.forEach.call(tbody.rows, _filter);
					});
				});
			}

			function _filter(row) {
				var text = row.textContent.toLowerCase(), val = _input.value.toLowerCase();
				row.style.display = text.indexOf(val) === -1 ? 'none' : 'table-row';
			}

			return {
				init: function() {
					var inputs = document.getElementsByClassName('light-table-filter');
					Arr.forEach.call(inputs, function(input) {
						input.oninput = _onInputEvent;
					});
				}
			};
		})(Array.prototype);

		document.addEventListener('readystatechange', function() {
			if (document.readyState === 'complete') {
				LightTableFilter.init();
			}
		});

	})(document);*/
	
</script>

<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title><spring:message code="label.welcome.to.sticky.notes"/></title>
</head>
<body>
	<form:form modelAttribute="filesForm">
	
		<c:if test="${resultInfo ne null && not empty resultInfo}">
	        <div class="alert alert-info" role="alert">
	        	<div align="center">
		        	<strong style="color: #3366CC;">
		        		<spring:message code="${resultInfo}"/>
		        	</strong>
		        </div>
	        </div>
		</c:if>
		
		<c:if test="${resultError ne null && not empty resultError}">
			<div class="alert alert-danger" role="alert">
				<div align="center">
					<strong style="color: red;">
						<span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true"></span>
						<spring:message code="${resultError}"/>
					</strong>
				</div>
			</div>
		</c:if>
		
		<c:if test="${sharePathForm ne null && not empty sharePathForm}">
	        <div class="alert alert-info" role="alert">
	        	<div align="center">
		        	<a href="${sharePathForm}">${sharePathForm}</a>
		        </div>
	        </div>
		</c:if>
		
		<div class="panel panel-primary">
	    	<div class="panel-heading">
	        	<h3 class="panel-title"><spring:message code="label.sticky.notes.getAllFiles"/></h3>
	        </div>
	        <div class="panel-body">
			<div class="table-responsive" style="overflow-y: auto; height: 300px;">
				<table class="table table-striped table-bordered table-hover" id="dataTables-example">
					<thead>
	                   <tr style="background-color: #404040; color: #F8F8F8;">
	                       <th><spring:message code="label.sticky.notes.folders"/></th>
	                       <th><spring:message code="label.sticky.notes.date.modified"/></th>
	                       <th><spring:message code="label.sticky.notes.type"/></th>
	                       <th><spring:message code="label.sticky.notes.size"/></th>
	                       <th colspan="5"><spring:message code="label.sticky.notes.operations"/></th>
	                   </tr>
					</thead>
	               
					<tbody>
						<c:forEach var="dataFolder" items="${filesForm.filesToList}" varStatus="loop">    
							<c:if test="${!dataFolder.file}">
								<tr style="background-color: #787878; color: #F8F8F8;">
									<td>
										${dataFolder.name}
									</td>
									<td>
										&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
									</td>
									<td>
										${dataFolder.type}
									</td>
									<td>
										&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
									</td>
									<td>
										<button type="button" class="btn btn-sm btn-danger" onclick="javascript:deleteFile('${dataFolder.path}');"><spring:message code="label.sticky.notes.delete"/></button>
									</td>
									<td colspan="4">
										<button type="button" class="btn btn-sm btn-info" onclick="javascript:sharePath('${dataFolder.path}');"><spring:message code="label.sticky.notes.share"/></button>
									</td>
								</tr>
								
								<c:forEach var="nested" items="${dataFolder.nestedFolders}" varStatus="dataLoop">
									<tr>
										<td>
											${nested.name}
										</td>
										<td>
											${nested.lastModified}
										</td>
										<td>
											${nested.type}
										</td>
										<td>
											${nested.fileSize}
										</td>
										<td>
											<button type="button" class="btn btn-sm btn-primary" onclick="javascript:readFile('${nested.path}', '${nested.name}');"><spring:message code="label.sticky.notes.read"/></button>
										</td>
										<td>
											<button type="button" class="btn btn-sm btn-info" onclick="javascript:sharePath('${nested.path}');"><spring:message code="label.sticky.notes.share"/></button>
										</td>
										<td>
											<button type="button" class="btn btn-sm btn-warning" onclick="javascript:downloadFile('${nested.path}', '${nested.name}');"><spring:message code="label.sticky.notes.download"/></button>
										</td>
										<td>
											<button type="button" class="btn btn-sm btn-default" onclick="javascript:editFile('${nested.path}', '${nested.name}');"><spring:message code="label.sticky.notes.edit.button"/></button>
										</td>
										<td>
											<button type="button" class="btn btn-sm btn-danger" onclick="javascript:deleteFile('${nested.path}');"><spring:message code="label.sticky.notes.delete"/></button>
										</td>
									</tr>
								</c:forEach>
							</c:if>
						</c:forEach>
					</tbody>
					
					<thead>
	                   <tr style="background-color: #404040; color: #F8F8F8;">
	                       <th><spring:message code="label.sticky.notes.files"/></th>
	                       <th><spring:message code="label.sticky.notes.date.modified"/></th>
	                       <th><spring:message code="label.sticky.notes.type"/></th>
	                       <th><spring:message code="label.sticky.notes.size"/></th>
	                       <th colspan="5"><spring:message code="label.sticky.notes.operations"/></th>
	                   </tr>
					</thead>
					
					<tbody>
						<c:choose>
						    <c:when test="${filesForm.filesToList ne null && !filesForm.filesToList.isEmpty()}">
						        <c:forEach var="dataFile" items="${filesForm.filesToList}" varStatus="fileLoop">    
									<c:if test="${dataFile.file}">
										<tr>
											<td>
												${dataFile.name}
											</td>
											<td>
												${dataFile.lastModified}
											</td>
											<td>
												${dataFile.type}
											</td>
											<td>
												${dataFile.fileSize}
											</td>
											<td>
												<button type="button" class="btn btn-sm btn-primary" onclick="javascript:readFile('${dataFile.path}', '${dataFile.name}');"><spring:message code="label.sticky.notes.read"/></button>
											</td>
											<td>
												<button type="button" class="btn btn-sm btn-info" onclick="javascript:sharePath('${dataFile.path}');"><spring:message code="label.sticky.notes.share"/></button>
											</td>
											<td>
												<button type="button" class="btn btn-sm btn-warning" onclick="javascript:downloadFile('${dataFile.path}', '${dataFile.name}');"><spring:message code="label.sticky.notes.download"/></button>
											</td>
											<td>
												<button type="button" class="btn btn-sm btn-default" onclick="javascript:editFile('${dataFile.path}', '${dataFile.name}');"><spring:message code="label.sticky.notes.edit.button"/></button>
											</td>
											<td>
												<button type="button" class="btn btn-sm btn-danger" onclick="javascript:deleteFile('${dataFile.path}');"><spring:message code="label.sticky.notes.delete"/></button>
											</td>
										</tr>
									</c:if>
								</c:forEach>
						    </c:when>
						    <c:otherwise>
						        <tr style="color: red;">
									<td colspan="9">
										<spring:message code="label.sticky.notes.no.files"/> 
									</td>								</tr>					    	</c:otherwise>
						</c:choose>
					</tbody>
				</table>
			</div>
		</div>
	    </div>
		<INPUT type="hidden" value="" name="path" id="path">
	</form:form>
	
	<script>
		document.documentElement.style.overflow=document.body.style.overflow='hidden';
    </script>
        
</body>
</html>