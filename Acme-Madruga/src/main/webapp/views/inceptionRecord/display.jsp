<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<security:authorize access="hasRole('BROTHERHOOD')">

	<table class="displayStyle">
		<tr>

			<td><strong><spring:message
						code="inceptionRecord.title" /> : </strong></td>
			<td><jstl:out value="${inceptionRecord.title}">

				</jstl:out></td>


		</tr>


		<tr>

			<td><strong><spring:message
						code="inceptionRecord.description" /> : </strong></td>
			<td><jstl:out value="${inceptionRecord.description}">

				</jstl:out></td>

		</tr>
	
			<tr>
			<display:table name="photos" id="row">
				<td><strong><spring:message code="inceptionRecord.photos" /></strong></td>
				<display:column>
					<img class="picture" src="${row}" />
				</display:column>
			</display:table>

		</tr>
	<jstl:if test="${inceptionRecord.id != 0}">
		<tr>

			<td><input type="button" name="edit"
				value="<spring:message code="inceptionRecord.edit"	/>"
				onclick="redirect: location.href = 'inceptionRecord/edit.do?inceptionRecordId=${inceptionRecord.id}';" />

			</td>

		</tr>
		</jstl:if>
		<jstl:if test="${ inceptionRecord.id == 0}">
			<a href="inceptionRecord/create.do"> <spring:message
			code="periodRecord.create" /></a>
		</jstl:if>

		<tr>

			<td><input type="button" name="back"
				value="<spring:message code="inceptionRecord.cancel" />"
				onclick="window.history.back()" /></td>

		</tr>
	</table>
		
	




</security:authorize>