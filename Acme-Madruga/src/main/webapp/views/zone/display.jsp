<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<security:authorize access="isAnonymous()">

	<table class="displayStyle">
		<tr>

			<td><strong><spring:message
						code="zone.name" /> : </strong></td>
			<td><jstl:out value="${zone.name}">

				</jstl:out></td>


		</tr>


		<tr>

			<td><strong><spring:message
						code="zone.pictures" /> : </strong></td>
			<td><jstl:out value="${record.pictures}">

				</jstl:out></td>

		</tr>

	</table>


	<acme:cancel code="zone.cancel" url="chapter/list.do" />
</security:authorize>