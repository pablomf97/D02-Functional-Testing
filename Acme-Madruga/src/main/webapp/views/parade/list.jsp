<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<!-- Listing grid -->

<style>
<!--
.tableColorGreen {
	background-color: chartreuse;
}

.tableColorOrange {
	background-color: orange;
}

.tableColorGrey {
	background-color: lightgrey;
}

.tableColorDefault {
	background-color: white;
}
-->
</style>

<display:table pagesize="5" class="displaytag" name="parades"
	requestURI="parade/member,brotherhood/list.do" id="row">

	<jstl:choose>
		<jstl:when test="${row.status == 'ACCEPTED'}">
			<jstl:set var="bgcolor" value="tableColorGreen" />
		</jstl:when>

		<jstl:when test="${row.status == 'REJECTED'}">
			<jstl:set var="bgcolor" value="tableColorOrange" />
		</jstl:when>

		<jstl:when test="${row.status == 'SUBMITTED'}">
			<jstl:set var="bgcolor" value="tableColorGrey" />
		</jstl:when>

		<jstl:otherwise>
			<jstl:set var="bgcolor" value="tableColorDefault" />
		</jstl:otherwise>
	</jstl:choose>

	<jstl:choose>
		<jstl:when test="${not row.isDraft}">

			<!-- Attributes-->

			<display:column titleKey="parade.title" sortable="true">
				<jstl:out value="${row.title }"></jstl:out>
			</display:column>

			<display:column titleKey="parade.ticker" sortable="true">
				<jstl:out value="${row.ticker }"></jstl:out>
			</display:column>

			<display:column class="${bgcolor}" titleKey="parade.status"
				sortable="true">
				<jstl:if test="${row.status == 'SUBMITTED' }">
					<span class="SUBMITTED"> <jstl:out value="${ row.status }" /></span>
				</jstl:if>
				<jstl:if test="${row.status == 'ACCEPTED' }">
					<span class="ACCEPTED"> <jstl:out value="${ row.status }" /></span>
				</jstl:if>
				<jstl:if test="${row.status == 'REJECTED' }">
					<span class="REJECTED"> <jstl:out value="${ row.status }" /></span>
				</jstl:if>
				<br />
			</display:column>

			<display:column titleKey="parade.description">
				<jstl:out value="${row.description }"></jstl:out>
			</display:column>

			<display:column titleKey="parade.organisedMoment" sortable="true">
				<jstl:out value="${row.organisedMoment }"></jstl:out>
			</display:column>
			
			<display:column titleKey="parade.brotherhood" sortable="true">
				<jstl:out value="${row.brotherhood.title }"></jstl:out>
			</display:column>

			<!-- Action links -->

			<display:column>
				<a href="parade/display.do?paradeId=${row.id}"> <spring:message
						code="parade.display" />
				</a>
			</display:column>
			
			<display:column>
				<jstl:if test="${row.isDraft == true}">
					<a href="parade/edit.do?paradeId=${row.id}"> <spring:message
							code="parade.edit" />
					</a>
				</jstl:if>

			</display:column>

			<display:column titleKey="parade.path">
				<a href="segment/list.do?paradeId=${row.id}"> <spring:message
						code="parade.display" />
				</a>
			</display:column>
		</jstl:when>

		<jstl:when test="${row.isDraft && permission}">

			<!-- Attributes-->

			<display:column titleKey="parade.title" sortable="true">
				<jstl:out value="${row.title }"></jstl:out>
			</display:column>

			<display:column titleKey="parade.ticker" sortable="true">
				<jstl:out value="${row.ticker }"></jstl:out>
			</display:column>

			<display:column class="${bgcolor}" titleKey="parade.status"
				sortable="true">
				<jstl:if test="${row.status == 'SUBMITTED' }">
					<span class="SUBMITTED"> <jstl:out value="${ row.status }" /></span>
				</jstl:if>
				<jstl:if test="${row.status == 'ACCEPTED' }">
					<span class="ACCEPTED"> <jstl:out value="${ row.status }" /></span>
				</jstl:if>
				<jstl:if test="${row.status == 'REJECTED' }">
					<span class="REJECTED"> <jstl:out value="${ row.status }" /></span>
				</jstl:if>
				<br />
			</display:column>

			<display:column titleKey="parade.description">
				<jstl:out value="${row.description }"></jstl:out>
			</display:column>

			<display:column titleKey="parade.organisedMoment" sortable="true">
				<jstl:out value="${row.organisedMoment }"></jstl:out>
			</display:column>
			
			<display:column titleKey="parade.brotherhood" sortable="true">
				<jstl:out value="${row.brotherhood.title }"></jstl:out>
			</display:column>

			<!-- Action links -->

			<display:column>
				<a href="parade/display.do?paradeId=${row.id}"> <spring:message
						code="parade.display" />
				</a>
			</display:column>

			<display:column>
				<jstl:if test="${row.isDraft == true}">
					<a href="parade/edit.do?paradeId=${row.id}"> <spring:message
							code="parade.edit" />
					</a>
				</jstl:if>

			</display:column>


			<display:column titleKey="parade.path">
				<a href="segment/list.do?paradeId=${row.id}"> <spring:message
						code="parade.display" />
				</a>
			</display:column>
		</jstl:when>

	</jstl:choose>
</display:table>

<security:authorize access="hasRole('BROTHERHOOD')">
	<p>
		<a href="parade/create.do"><spring:message code="parade.create" /></a>
	</p>
</security:authorize>
