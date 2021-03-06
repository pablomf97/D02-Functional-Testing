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
	<jstl:when test="${(isPrincipal) &&  segment.id == 0}">
		<form:form action="segment/edit.do" modelAttribute="segment" id="form">
			<fieldset>
				<br>
				<form:hidden path="id" />
				<form:hidden path="parade" value="${par.id }" />

				<acme:textbox code="segment.expectedTimeOrigin"
					path="expectedTimeOrigin" />
				<br>

				<acme:textbox code="segment.expectedTimeDestination"
					path="expectedTimeDestination" />

				<h3>Origin</h3>
				<acme:textbox code="segment.longitude" path="originLongitude"
					size="50%" />
				&nbsp; &nbsp;
				<acme:textbox code="segment.latitude" path="originLatitude"
					size="50%" />

				<h3>Destination</h3>
				<acme:textbox code="segment.longitude" path="destinationLongitude"
					size="50%" />
				&nbsp; &nbsp;
				<acme:textbox code="segment.latitude" path="destinationLatitude"
					size="50%" />
				<br>

			</fieldset>
			<br />

			<acme:submit code="segment.save" name="save" />&nbsp;

		<jstl:if test="${segment.id != 0}">
				<acme:delete code="segment.delete" name="delete"
					confirmation="segment.confirm.delete" />&nbsp; 
			<acme:cancel code="segment.cancel"
					url="segment/list.do?paradeId=${segment.parade.id }" />
				<br />
				<br />
			</jstl:if>
			<jstl:if test="${segment.id == 0}">
				<acme:cancel code="segment.cancel"
					url="segment/list.do?paradeId=${par.id }" />
				<br />
				<br />
			</jstl:if>
		</form:form>
	</jstl:when>
	<jstl:when test="${(isPrincipal) &&  (segment.isEditable)}">
		<form:form action="segment/edit.do" modelAttribute="segment" id="form">
			<fieldset>
				<br>
				<form:hidden path="id" />
				<form:hidden path="parade" value="${par.id }" />

				<acme:textbox code="segment.expectedTimeOrigin"
					path="expectedTimeOrigin" />
				<br>
				<acme:textbox code="segment.expectedTimeDestination"
					path="expectedTimeDestination" />

				<h3>Destination</h3>
				<acme:textbox code="segment.latitude" path="destinationLatitude"
					size="50%" />
				<br>&nbsp; &nbsp;
				<acme:textbox code="segment.longitude" path="destinationLongitude"
					size="50%" />

			</fieldset>
			<br />

			<acme:submit code="segment.save" name="save" />&nbsp;

    <jstl:if test="${segment.id != 0}">
				<acme:delete code="segment.delete" name="delete"
					confirmation="segment.confirm.delete" />&nbsp; 
      <acme:cancel code="segment.cancel"
					url="segment/list.do?paradeId=${segment.parade.id }" />
				<br />
				<br />
			</jstl:if>
			<jstl:if test="${segment.id == 0}">
				<acme:cancel code="segment.cancel"
					url="segment/list.do?paradeId=${par.id }" />
				<br />
				<br />
			</jstl:if>
		</form:form>
	</jstl:when>
	<jstl:otherwise>
		<p>
			<spring:message code="segment.notAllowed" />
		</p>
	</jstl:otherwise>
</jstl:choose>