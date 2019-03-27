<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<!-- Listing grid -->

<display:table pagesize="5" class="displaytag" name="segments"
	requestURI="segment/list.do" id="row">

	<!-- Attributes-->

	<display:column titleKey="segment.expectedTimeOrigin" sortable="true">
		<jstl:out value="${row.expectedTimeOrigin }"></jstl:out>
	</display:column>

	<display:column titleKey="segment.expectedTimeDestination" sortable="true">
		<jstl:out value="${row.expectedTimeDestination }"></jstl:out>
	</display:column>

	<display:column titleKey="segment.origin">
		<jstl:out value="${row.originLatitude }, ${row.originLongitude } "></jstl:out>
	</display:column>

	<display:column titleKey="segment.destination">
		<jstl:out value="${row.destinationLatitude }, ${row.destinationLongitude } "></jstl:out>
	</display:column>
	
	<display:column titleKey="segment.parade" sortable="true">
		<jstl:out value="${row.parade.title }"></jstl:out>
	</display:column>

	<!-- Action links -->

	<display:column>
		<a href="segment/display.do?segmentId=${row.id}"> <spring:message
				code="segment.display" />
		</a>
	</display:column>

	<security:authorize access="hasRole('BROTHERHOOD')">
		<display:column>
			<jstl:if test="${row.isEditable == true}">
				<a href="segment/edit.do?segmentId=${row.id}"> <spring:message
						code="segment.edit" />
				</a>
			</jstl:if>

		</display:column>
	</security:authorize>

</display:table>
<security:authorize access="hasRole('BROTHERHOOD')">
	<p>
		<a href="segment/create.do?paradeId=${pId }"><spring:message
				code="segment.create" /></a>
	</p>
</security:authorize>