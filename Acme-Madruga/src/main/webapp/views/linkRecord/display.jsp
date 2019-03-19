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

			<td><strong><spring:message code="linkRecord.title" />
					: </strong></td>
			<td><jstl:out value="${linkRecord.title}">

				</jstl:out></td>


		</tr>


		<tr>

			<td><strong><spring:message
						code="linkRecord.description" /> : </strong></td>
			<td><jstl:out value="${linkRecord.description}">

				</jstl:out></td>

		</tr>

		<tr>

			<td><strong><spring:message
						code="linkRecord.brotherhood" /> : </strong></td>
			<td><a
				href="brotherhood/display.do?id=${linkRecord.linkedBrotherhood.id}">
					<jstl:out value="${linkRecord.linkedBrotherhood.title}">

					</jstl:out>
			</a></td>

		</tr>
	</table>

	<input type="button" name="edit"
		value="<spring:message code="linkRecord.edit"	/>"
		onclick="redirect: location.href = 'linkRecord/edit.do?linkRecordId=${linkRecord.id}';" />

	<input type="button" name="back"
				value="<spring:message code="linkRecord.cancel" />"
				onclick="window.history.back()" />
</security:authorize>