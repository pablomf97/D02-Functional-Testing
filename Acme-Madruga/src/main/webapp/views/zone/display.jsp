<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<security:authorize access="hasRole('ADMINISTRATOR')">

	<table class="displayStyle">
		<tr>

			<td><strong><spring:message
						code="zone.name" /> : </strong></td>
			<td><jstl:out value="${zone.name}">

				</jstl:out></td>


		</tr>


	
			<tr>
			<display:table name="pictures" id="row">
				<td><strong><spring:message code="zone.pictures" /></strong></td>
				<display:column>
					<img class="picture" src="${row}" />
				</display:column>
			</display:table>

		</tr>


		<tr>

			<td><input type="button" name="back"
				value="<spring:message code="zone.cancel" />"
				onclick="window.history.back()" /></td>

		</tr>
	</table>
		
	




</security:authorize>