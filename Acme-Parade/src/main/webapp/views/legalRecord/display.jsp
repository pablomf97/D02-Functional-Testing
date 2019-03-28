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

			<td><strong><spring:message code="legalRecord.title" />
					: </strong></td>
			<td><jstl:out value="${legalRecord.title}">

				</jstl:out></td>


		</tr>


		<tr>

			<td><strong><spring:message
						code="legalRecord.description" /> : </strong></td>
			<td><jstl:out value="${legalRecord.description}">

				</jstl:out></td>

		</tr>
		<tr>

			<td><strong><spring:message code="legalRecord.name" />
					: </strong></td>
			<td><jstl:out value="${legalRecord.name}">

				</jstl:out></td>

		</tr>
		<tr>

			<td><strong><spring:message code="legalRecord.VAT" />
					: </strong></td>
			<td><jstl:out value="${legalRecord.VAT}">

				</jstl:out></td>

		</tr>
		<tr>

			<td><strong><spring:message code="legalRecord.laws" />:</strong></td>

			<td>
			<ul>
			<jstl:forEach var="laws" items="${legalRecord.laws}">
					<li><jstl:out value="${laws }" /></li>
				</jstl:forEach>
				</ul>
				</td>
		</tr>


		<tr>

			<td><input type="button" name="edit"
				value="<spring:message code="legalRecord.edit"	/>"
				onclick="redirect: location.href = 'legalRecord/edit.do?legalRecordId=${legalRecord.id}';" />

			</td>

		</tr>


		<tr>

			<td><input type="button" name="back"
				value="<spring:message code="legalRecord.cancel" />"
				onclick="window.history.back()" /></td>

		</tr>
	</table>






</security:authorize>