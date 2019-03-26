<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<!-- Listing grid -->

<display:table pagesize="5" class="displaytag" name="parades"
	requestURI="parade/member,brotherhood/list.do" id="row">

	<security:authorize access="hasRole('BROTHERHOOD')">

		<!-- Attributes-->

		<display:column titleKey="parade.title" sortable="true">
			<jstl:out value="${row.title }"></jstl:out>
		</display:column>

		<display:column titleKey="parade.ticker" sortable="true">
			<jstl:out value="${row.ticker }"></jstl:out>
		</display:column>
		<display:column titleKey="parade.status" sortable="true">
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

	</security:authorize>
	
	<display:column titleKey="parade.path" >
		<a href="segment/list.do?paradeId=${row.id}"> <spring:message
				code="parade.display" />
		</a>
	</display:column>

	<security:authorize access="hasRole('MEMBER')">

		<jstl:choose>

			<jstl:when test="${row.isDraft == false}">

				<!-- Attributes-->

				<display:column titleKey="parade.title" sortable="true">
					<jstl:out value="${row.title }"></jstl:out>
				</display:column>

				<display:column titleKey="parade.ticker" sortable="true">
					<jstl:out value="${row.ticker }"></jstl:out>
				</display:column>

				<display:column titleKey="parade.status" sortable="true">
						<jstl:out value="${ row.status }" />
					<br />
				</display:column>

				<display:column titleKey="parade.description">
					<jstl:out value="${row.description }"></jstl:out>
				</display:column>

				<display:column titleKey="parade.organisedMoment"
					sortable="true">
					<jstl:out value="${row.organisedMoment }"></jstl:out>
				</display:column>

				<!-- Action links -->

				<display:column>
					<a href="parade/display.do?paradeId=${row.id}"> <spring:message
							code="parade.display" />
					</a>
				</display:column>

			</jstl:when>
			<jstl:otherwise>
				<p>
					<spring:message code="march.create" />
				</p>
			</jstl:otherwise>

		</jstl:choose>

	</security:authorize>
</display:table>
<security:authorize access="hasRole('BROTHERHOOD')">
	<p>
		<a href="parade/create.do"><spring:message
				code="parade.create" /></a>
	</p>
</security:authorize>