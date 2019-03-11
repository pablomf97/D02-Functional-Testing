<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>


<jstl:choose>
	<jstl:when test="${march.id ==0}">
		<form:form modelAttribute="march" action="march/edit.do"
			id="form">
			<fieldset>
				<br>
				<form:hidden path="id" />
				<form:label path="procession">
					<spring:message code="march.procession" />:
				</form:label>
				<form:select multiple="true" path="procession" items="${toApply}" itemLabel="title"  style="width:200px;"/><br/><br/>					
			</fieldset>
			<br>
			<acme:submit code="march.save" name="save"/>&nbsp; 
				<jstl:if test="${march.id != 0}">
					<acme:delete code="march.delete" name="delete" confirmation="march.confirm.delete"/>&nbsp;
				</jstl:if>
			<acme:cancel code="march.cancel" url="march/member,brotherhood/list.do"/><br />			
		</form:form>
	</jstl:when>
	<jstl:otherwise>
		<p>
			<spring:message code="march.notAllowed" />
		</p>
	</jstl:otherwise>
</jstl:choose>