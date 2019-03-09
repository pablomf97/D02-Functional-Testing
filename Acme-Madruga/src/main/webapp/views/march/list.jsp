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

	<display:table pagesize="5" class="displaytag" name="marchs"
		requestURI="march/member,brotherhood/list.do" id="row">

	<security:authorize access="hasRole('MEMBER')">
	
		<jstl:choose>
			<jstl:when test="${row.status == 'APPROVED'}">
				<jstl:set var="bgcolor" value="tableColorGreen" />
			</jstl:when>

			<jstl:when test="${row.status == 'REJECTED'}">
				<jstl:set var="bgcolor" value="tableColorOrange" />
			</jstl:when>

			<jstl:when test="${row.status == 'PENDING'}">
				<jstl:set var="bgcolor" value="tableColorGrey" />
			</jstl:when>

			<jstl:otherwise>
				<jstl:set var="bgcolor" value="tableColorDefault" />
			</jstl:otherwise>
		</jstl:choose>
			
		<!-- Attributes-->
		
		<display:column titleKey="march.member" sortable="true" >
		
			<jstl:out value="${row.member.name}"></jstl:out>
		</display:column>
				
		<display:column titleKey="march.procession" sortable="true" >
			<a href="procession/display.do?processionId=${row.procession.id}">
				<jstl:out value="${row.procession.title }"></jstl:out>
			</a>
		</display:column>
				
		<display:column titleKey="march.status" sortable="true" class="${bgcolor}">
			<jstl:out value="${row.status }"></jstl:out>
		</display:column>
		
		<jstl:if test="${status == 'ACCEPTED' }">
				
		<display:column titleKey="march.row" sortable="true" >
			<jstl:out value="${row.row }"></jstl:out>
		</display:column>
				
		<display:column titleKey="march.column" sortable="true" >
			<jstl:out value="${row.col }"></jstl:out>
		</display:column>
		
		</jstl:if>
	
		<!-- Action links -->
	
		<display:column>
			<a href="march/display.do?marchId=${row.id}"> <spring:message
						code="march.display" />
			</a>		
		</display:column>
		
		<display:column>
				<jstl:if test="${row.status == 'PENDING'}">
					<a id="delete" href="march/delete.do?marchId=${row.id}"> <spring:message code="march.delete" /></a>
				</jstl:if>

		</display:column>
		
	</security:authorize>
	
	<security:authorize access="hasRole('BROTHERHOOD')">
	
	<jstl:choose>
			<jstl:when test="${row.status == 'APPROVED'}">
				<jstl:set var="bgcolor" value="tableColorGreen" />
			</jstl:when>

			<jstl:when test="${row.status == 'REJECTED'}">
				<jstl:set var="bgcolor" value="tableColorOrange" />
			</jstl:when>

			<jstl:when test="${row.status == 'PENDING'}">
				<jstl:set var="bgcolor" value="tableColorGrey" />
			</jstl:when>

			<jstl:otherwise>
				<jstl:set var="bgcolor" value="tableColorDefault" />
			</jstl:otherwise>
		</jstl:choose>
			
		<!-- Attributes-->
		
		<display:column titleKey="march.member" sortable="true" >
			<jstl:out value="${row.member.name}"></jstl:out>
		</display:column>
				
		<display:column titleKey="march.procession" sortable="true" >
			<jstl:out value="${row.procession.title }"></jstl:out>
		</display:column>
				
		<display:column titleKey="march.status" sortable="true" class="${bgcolor}">
			<jstl:out value="${row.status }"></jstl:out>
		</display:column>
		
		<jstl:if test="${status == 'ACCEPTED' }">
				
		<display:column titleKey="march.row" sortable="true" >
			<jstl:out value="${row.row }"></jstl:out>
		</display:column>
				
		<display:column titleKey="march.column" sortable="true" >
			<jstl:out value="${row.col }"></jstl:out>
		</display:column>
		
		</jstl:if>
	
		<!-- Action links -->
	
		<display:column>
			<a href="march/display.do?marchId=${row.id}"> <spring:message
						code="march.display" />
			</a>		
		</display:column>
		
		<display:column>
			<jstl:if test="${row.status == 'PENDING'}">
				<a href="march/accept.do?marchId=${row.id}">
					<spring:message code="march.accept" />
				</a>
			</jstl:if>
		</display:column>
			
		<display:column>
			<jstl:if test="${row.status == 'PENDING'}">
				<a href="march/rejectv.do?marchId=${row.id}">
					<spring:message code="march.reject" />
				</a>
			</jstl:if>			
		</display:column>
	</security:authorize>
	</display:table>
	<security:authorize access="hasRole('MEMBER')">
	<p>
		<a href="march/create.do"><spring:message
				code="march.create" /></a>
	</p>
</security:authorize>