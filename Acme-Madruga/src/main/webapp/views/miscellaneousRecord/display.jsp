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
						code="miscellaneousRecord.title" /> : </strong></td>
			<td><jstl:out value="${record.title}">

				</jstl:out></td>


		</tr>


		<tr>

			<td><strong><spring:message
						code="miscellaneousRecord.description" /> : </strong></td>
			<td><jstl:out value="${record.description}">

				</jstl:out></td>

		</tr>

		<tr>

			<td><input type="button" name="edit"
				value="<spring:message code="miscellaneousRecord.edit"	/>"
				onclick="redirect: location.href = 'miscellaneousRecord/edit.do?miscellaneousRecordId=${record.id}';" />

			</td>

		</tr>

		<tr>

			<td><input type="button" name="back"
				value="<spring:message code="miscellaneousRecord.cancel" />"
				onclick="window.history.back()" /></td>

		</tr>
	</table>
	
	
		
	
</security:authorize>