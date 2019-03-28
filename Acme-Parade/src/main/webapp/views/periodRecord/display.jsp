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

			<td><strong><spring:message code="periodRecord.title" />
					: </strong></td>
			<td><jstl:out value="${periodRecord.title}">

				</jstl:out></td>


		</tr>


		<tr>

			<td><strong><spring:message
						code="periodRecord.description" /> : </strong></td>
			<td><jstl:out value="${periodRecord.description}">

				</jstl:out></td>

		</tr>
		<tr>

			<td><strong><spring:message
						code="periodRecord.startYear" /> : </strong></td>
			<td><jstl:out value="${periodRecord.startYear}">

				</jstl:out></td>

		</tr>
		<tr>

			<td><strong><spring:message code="periodRecord.endYear" />
					: </strong></td>
			<td><jstl:out value="${periodRecord.endYear}">

				</jstl:out></td>

		</tr>
		<tr>
			<display:table name="photos" id="row">
				<td><strong><spring:message code="periodRecord.photos" /></strong></td>
				<display:column>
					<img class="picture" src="${row}" />
				</display:column>
			</display:table>

		</tr>

		<tr>

			<td><input type="button" name="edit"
				value="<spring:message code="periodRecord.edit"	/>"
				onclick="redirect: location.href = 'periodRecord/edit.do?periodRecordId=${periodRecord.id}';" />

			</td>

		</tr>


		<tr>

			<td><input type="button" name="back"
				value="<spring:message code="periodRecord.cancel" />"
				onclick="window.history.back()" /></td>

		</tr>
	</table>








</security:authorize>