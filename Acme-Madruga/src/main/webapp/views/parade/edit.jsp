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
	<jstl:when test="${(isPrincipal || parade.id == 0) &&  not empty actorBrother.zone}">
		<jstl:choose>
			<jstl:when test="${parade.id == 0 || parade.isDraft == true}">
				<form:form action="parade/edit.do" modelAttribute="parade" id="form">
					<fieldset>
					<br>
					<form:hidden path="id" />
					
					<acme:textbox code="parade.title" path="title"/><br><br>
					
					<acme:textbox code="parade.organisedMoment" path="organisedMoment"/><br><br>
					
					<acme:textbox code="parade.maxCols" path="maxCols" placeholder="parade.maxCols.placeholder" size="50%"/><br><br>
					
					<acme:textbox code="parade.description" path="description"/><br /><br />
					
				<form:label path="platforms">
					<spring:message code="parade.platform" />
				</form:label><br>
				<form:select multiple="true" path="platforms" items="${platforms}" itemLabel="title" />				
				<br><br>
				</fieldset>
				<br />
							
				<jstl:if test="${parade.isDraft == true || parade.id == 0}">
					<acme:submit code="parade.save" name="save"/>&nbsp;
					<acme:submit code="parade.save.final" name="saveFinal"/>&nbsp; 
				</jstl:if>
				<jstl:if test="${parade.id != 0}">
					<acme:delete code="parade.delete" name="delete" confirmation="parade.confirm.delete"/>&nbsp; 
				</jstl:if>
				<acme:cancel code="parade.cancel" url="parade/member,brotherhood/list.do"/><br/><br/>
				
				</form:form>
			</jstl:when>
			<jstl:otherwise>
				<h3>
					<spring:message code="parade.nopermission" />
				</h3>
			</jstl:otherwise>
		</jstl:choose>
	</jstl:when>
	<jstl:when test="${empty actorBrother.zone }">
		<p>
			<spring:message code="parade.noArea" />
		</p>
	</jstl:when>
	<jstl:otherwise>
		<p>
			<spring:message code="parade.notAllowed" />
		</p>
	</jstl:otherwise>
</jstl:choose>