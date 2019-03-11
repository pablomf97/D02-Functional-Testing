<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<!-- Listing grid -->


<display:table pagesize="5" class="displaytag" name="brotherhoods"
	requestURI="brotherhood/list.do" id="brotherhood">
	<!-- Attributes-->

	<jstl:if test="${isMember}">
		<display:column>
			<jstl:if test="${!enrolledBrotherhoods.contains(brotherhood)}">
				<a
					href="enrolment/member/request.do?brotherhoodID=${brotherhood.id}">
					<spring:message code="brotherhood.request" />
				</a>
			</jstl:if>
		</display:column>
	</jstl:if>

	<display:column titleKey="brotherhood.title" sortable="true">
		<jstl:out value="${brotherhood.title}" />
	</display:column>

	<display:column titleKey="brotherhood.establishmentDate">
		<jstl:out value="${brotherhood.establishmentDate}" />
	</display:column>

	<display:column titleKey="brotherhood.zone">
		<jstl:out value="${brotherhood.zone.name}" />
	</display:column>
	
	<display:column>
	<a href="brotherhood/members/list.do?brotherhoodId=${brotherhood.id}">
		<spring:message code="brotherhood.members" />
	</a>
	</display:column>
	
	<display:column>
	<a href="platform/list.do?brotherhoodId=${brotherhood.id}">
		<spring:message code="brotherhood.floats" />
	</a>
	</display:column>
</display:table>

