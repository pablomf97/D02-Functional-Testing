<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<jstl:choose>
	<jstl:when test="${isPrincipal && march.status == 'PENDING'}">
	
		<form:form action="march/accept.do" modelAttribute="march" id="form">

			<fieldset>
				<br>
				<form:hidden path="id" />
				
				<acme:textbox code="march.row" path="row"/><br/><br/>
				
				<acme:textbox code="march.column" path="col"/><br/>
			
			</fieldset><br/>
			
			<acme:submit code="march.accept" name="accept"/>&nbsp; 
			<acme:cancel code="march.cancel" url="march/member,brotherhood/list.do"/><br/>
		</form:form>
	</jstl:when>
	<jstl:otherwise>
		<p>
			<spring:message code="march.notAllowed" />
		</p>
	</jstl:otherwise>
</jstl:choose>
<script>
	function recommendedPos(){
		alert("The rcommended position for this member is row: ${recomRow} and column: ${recomCol}");
	}
	window.onload = recommendedPos;
</script>