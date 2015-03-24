<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<script type="text/javascript">
	function logout() {
		var message ='<spring:message code="confirmation.logout"/>';
		var confirmmsg = confirm(message);
		if(confirmmsg) {
			var filesForm = document.forms['filesForm'];
			filesForm.method = "post";
			filesForm.action = "logout";
			filesForm.submit();
		}
 	}
	
	function help() {
		window.open("https://www.dropbox.com/help");
	}
</script>

<form:form modelAttribute="filesForm">
	<div id="GlobalLinksTop">
		<div>
		    <ul>
		    	<li><a href="?sitelocale=en"><img src="/stickynotes/resources/images/usa_flag.gif" alt="USA"></a> | <a href="?sitelocale=fr"><img src="/stickynotes/resources/images/france_flag.jpg" alt="France"></a></li>
		    	<li><spring:message code="label.sticky.notes.welcome"/> ${filesForm.loggedInUser}</li>
				<li><a href="#" onclick="javascript:logout();"><spring:message code="label.sticky.notes.logout"/></a></li>
				<li><a href="#" onclick="javascript:help();"><spring:message code="label.sticky.notes.help"/></a></li>
			</ul>
		</div>
	</div>
</form:form>