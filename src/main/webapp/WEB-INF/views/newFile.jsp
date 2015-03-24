<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<form:form modelAttribute="filesForm" action="uploadNewFile" method="post">

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
        	<h3 class="panel-title"><spring:message code="label.sticky.notes.new.file"/></h3>
        </div>
        <div class="panel-body">
        	<spring:message code="label.sticky.notes.enter.file.name"/> <form:input path="newFileName" placeholder="newFile"/>.txt<br><br>
        	<form:textarea path="readFile" rows="10" cols="185" placeholder="Start writing file content..."/><br><br>
        	<button type="submit" class="btn btn-sm btn-primary"><spring:message code="label.sticky.notes.save.file"/></button>
        </div>
    </div>
</form:form>

<script>
	document.documentElement.style.overflow=document.body.style.overflow='hidden';
</script>