<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<style>
<!--
.tableColorGreen {
	background-color: chartreuse;
}

.tableColorOrange {
	background-color: orange;
}

.tableColorGrey {
	background-color: grey;
}

.tableColorDefault {
	background-color: white;
}
-->
</style>

<!-- Listing grid -->

<jstl:if test="${permission}">

	<display:table pagesize="5" class="displaytag" name="parades"
		requestURI="parade/chapter/list.do" id="parade">

		<security:authorize access="hasRole('CHAPTER')">

			<jstl:choose>
				<jstl:when test="${parade.status == 'ACCEPTED'}">
					<jstl:set var="bgcolor" value="tableColorGreen" />
				</jstl:when>

				<jstl:when test="${parade.status == 'REJECTED'}">
					<jstl:set var="bgcolor" value="tableColorOrange" />
				</jstl:when>

				<jstl:when test="${parade.status == 'SUBMITTED'}">
					<jstl:set var="bgcolor" value="tableColorGrey" />
				</jstl:when>

				<jstl:otherwise>
					<jstl:set var="bgcolor" value="tableColorDefault" />
				</jstl:otherwise>
			</jstl:choose>

			<!-- Attributes-->

			<display:column titleKey="parade.brotherhood" sortable="true">
				<jstl:out value="${parade.brotherhood.name}" />
			</display:column>

			<display:column titleKey="parade.name" sortable="true">
				<jstl:out value="${parade.title}" />
			</display:column>

			<display:column titleKey="parade.status" sortable="true"
				class="${bgcolor}">
				<jstl:out value="${parade.status}" />
			</display:column>

			<!-- Action links -->

			<display:column>
				<a href="parade/display.do?paradeId=${parade.id}"> <spring:message
						code="parade.display" />
				</a>
			</display:column>

			<display:column>
				<jstl:if test="${parade.status == 'SUBMITTED'}">
					<a href="parade/accept.do?paradeId=${parade.id}"><spring:message
							code="parade.accept" /></a>
				</jstl:if>
			</display:column>

			<display:column>
				<jstl:if test="${parade.status == 'SUBMITTED'}">
					<a href="parade/rejectv.do?paradeId=${parade.id}"><spring:message
							code="parade.reject" /></a>
				</jstl:if>
			</display:column>
		</security:authorize>
	</display:table>
</jstl:if>