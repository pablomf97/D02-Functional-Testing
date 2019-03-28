<%--
 * Copyright (C) 2018 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>

<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<security:authorize access="isAuthenticated()" var="authenticated" >

<jstl:choose>
	<jstl:when test="${not empty socialProfiles}">
		<p>
			<spring:message code="actor.socialprofiles" />
		</p>
		<table class="displayStyle" style="width: 50%">
			<tr>
				<td><display:table pagesize="5" class="displaytag"
						name="socialProfiles"
						requestURI="socialProfile/actor/list.do"
						id="socialProfiles">

						<display:column titleKey="actor.socialprofile.network">
						<jstl:out value="${socialProfiles.name}"></jstl:out>
						</display:column>
						<display:column titleKey="actor.socialprofile.nick">
						<jstl:out value="${socialProfiles.nick}"></jstl:out>
						</display:column>
						<display:column titleKey="actor.socialprofile.link">
						<jstl:out value="${socialProfiles.linkProfile}"></jstl:out>
						</display:column>	
						
							<display:column titleKey="actor.edit.social">
								<input type="button" name="editSocial"
									value="<spring:message code="actor.edit.social" />"
									onclick="redirect: location.href = 'socialProfile/actor/edit.do?socialprofileID=${socialProfiles.id}';" />
							</display:column>
						
					</display:table></td>
			</tr>
		</table>
		<input type="button" name="editSocial"
			value="<spring:message code="actor.add.social" />"
			onclick="redirect: location.href = 'socialProfile/actor/create.do';" />
	</jstl:when>
	<jstl:otherwise>
		<spring:message code="actor.how" />
		<!-- para comprobar si funciona ** -->
		<input type="button" name="editSocial"
			value="<spring:message code="actor.add.social" />"
			onclick="redirect: location.href = 'socialProfile/actor/create.do';" />
	</jstl:otherwise>
</jstl:choose>
</security:authorize>