<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

	<!-- Listing grid -->

	<display:table pagesize="5" class="displaytag" name="processions"
		requestURI="procession/member,brotherhood/list.do" id="row">

<security:authorize access="hasRole('BROTHERHOOD')">
	
		<!-- Attributes-->
				
		<display:column titleKey="procession.title" sortable="true" >
			<jstl:out value="${row.title }"></jstl:out>
		</display:column>
				
		<display:column titleKey="procession.ticker" sortable="true" >
			<jstl:out value="${row.ticker }"></jstl:out>
		</display:column>
				
		<display:column titleKey="procession.description" >
			<jstl:out value="${row.description }"></jstl:out>
		</display:column>
				
		<display:column titleKey="procession.organisedMoment" sortable="true" >
			<jstl:out value="${row.organisedMoment }"></jstl:out>
		</display:column>
	
		<!-- Action links -->
	
		<display:column>
			<a href="procession/display.do?processionId=${row.id}"> <spring:message
						code="procession.display" />
			</a>		
		</display:column>
		
		<display:column>
				<jstl:if test="${row.isDraft == true}">
					<a href="procession/edit.do?processionId=${row.id}"> <spring:message
							code="procession.edit" />
					</a>
				</jstl:if>

		</display:column>
		
	</security:authorize>
	
	<security:authorize access="hasRole('MEMBER')">
	
	<jstl:choose>
			
		<jstl:when test="${row.isDraft == false}">
					
			<!-- Attributes-->
			
			<display:column titleKey="procession.title" sortable="true" >
				<jstl:out value="${row.title }"></jstl:out>
			</display:column>
					
			<display:column titleKey="procession.ticker" sortable="true" >
				<jstl:out value="${row.ticker }"></jstl:out>
			</display:column>
					
			<display:column titleKey="procession.description" >
				<jstl:out value="${row.description }"></jstl:out>
			</display:column>
					
			<display:column titleKey="procession.organisedMoment" sortable="true" >
				<jstl:out value="${row.organisedMoment }"></jstl:out>
			</display:column>
		
			<!-- Action links -->
		
			<display:column>
				<a href="procession/display.do?processionId=${row.id}"> <spring:message
							code="procession.display" />
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
		<a href="procession/create.do"><spring:message
				code="procession.create" /></a>
	</p>
</security:authorize>