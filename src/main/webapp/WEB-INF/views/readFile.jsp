<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<form:form modelAttribute="filesForm">
	<div class="panel panel-primary">
    	<div class="panel-heading">
        	<h3 class="panel-title"><spring:message code="label.sticky.notes.read.file"/></h3>
        </div>
        <div class="panel-body">
        	<form:textarea path="readFile" readonly="true" rows="15" cols="185"/>
        </div>
    </div>
</form:form>

<script>
	document.documentElement.style.overflow=document.body.style.overflow='hidden';
</script>