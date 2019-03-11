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
	<jstl:when test="${(isPrincipal || procession.id == 0) &&  not empty actorBrother.zone}">
		<jstl:choose>
			<jstl:when test="${procession.id == 0 || procession.isDraft == true}">
				<form:form action="procession/edit.do" modelAttribute="procession" id="form">
					<fieldset>
					<br>
					<form:hidden path="id" />
					
					<acme:textbox code="procession.title" path="title"/><br><br>
					
					<acme:textbox code="procession.organisedMoment" path="organisedMoment"/><br><br>
					
					<acme:textbox code="procession.maxCols" path="maxCols" placeholder="procession.maxCols.placeholder" size="50%"/><br><br>
					
					<acme:textbox code="procession.description" path="description"/><br /><br />
					
				<form:label path="platforms">
					<spring:message code="procession.platform" />
				</form:label><br>
				<form:select multiple="true" path="platforms" items="${platforms}" itemLabel="title" />				
				<br><br>
				</fieldset>
				<br />
							
				<jstl:if test="${procession.isDraft == true || procession.id == 0}">
					<acme:submit code="procession.save" name="save"/>&nbsp;
					<acme:submit code="procession.save.final" name="saveFinal"/>&nbsp; 
				</jstl:if>
				<jstl:if test="${procession.id != 0}">
					<acme:delete code="procession.delete" name="delete" confirmation="procession.confirm.delete"/>&nbsp; 
				</jstl:if>
				<acme:cancel code="procession.cancel" url="procession/member,brotherhod/list.do"/><br/><br/>
				
				</form:form>
			</jstl:when>
			<jstl:otherwise>
				<h3>
					<spring:message code="procession.nopermission" />
				</h3>
			</jstl:otherwise>
		</jstl:choose>
	</jstl:when>
	<jstl:when test="${empty actorBrother.zone }">
		<p>
			<spring:message code="procession.noArea" />
		</p>
	</jstl:when>
	<jstl:otherwise>
		<p>
			<spring:message code="procession.notAllowed" />
		</p>
	</jstl:otherwise>
</jstl:choose>