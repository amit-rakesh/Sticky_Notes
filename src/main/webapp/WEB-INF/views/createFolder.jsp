<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<form:form modelAttribute="filesForm" action="createFolder" method="post">

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
        	<h3 class="panel-title"><spring:message code="label.sticky.notes.create.folder"/></h3>
        </div>
        <div class="panel-body">
        	<spring:message code="label.sticky.notes.create.folder.name"/> <form:input path="folderNameToCreate" placeholder="New Folder"/><br><br>
        	<button type="submit" class="btn btn-sm btn-primary"><spring:message code="label.sticky.notes.create.folder"/></button>
        </div>
    </div>
</form:form>

<script>
	document.documentElement.style.overflow=document.body.style.overflow='hidden';
</script>