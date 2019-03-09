<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>


<jstl:choose>
	<jstl:when test="${(isPrincipal || platform.id ==0) && not empty actorBrother.zone}">
		<form:form modelAttribute="platform" action="platform/edit.do"
			id="form">
			<fieldset>
				<br>
				<form:hidden path="id" />
				
				<acme:textbox code="platform.title" path="title"/><br/><br/>
						
				<acme:textbox code="platform.pictures" path="pictures"/><br/><br />
				
				<acme:textarea code="platform.description" path="description"/><br /><br />
			</fieldset>
			
			<acme:submit code="platform.save" name="save"/>&nbsp; 
			
			<jstl:if test="${platform.id != 0}">
				<acme:delete code="platform.delete" name="delete" confirmation="platform.confirm.delete"/>&nbsp; 
			</jstl:if>
			
			<acme:cancel code="platform.cancel" url="platform/list.do"/><br />
		
		</form:form>
	</jstl:when>
	<jstl:when test="${empty actorBrother.zone }">
		<p>
			<spring:message code="platform.noArea" />
		</p>
	</jstl:when>
	<jstl:otherwise>
		<p>
			<spring:message code="platform.notAllowed" />
		</p>
	</jstl:otherwise>
</jstl:choose>