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

<security:authorize access="hasRole('ADMINISTRATOR')">
	<jstl:choose>
		<jstl:when test="${not empty actors}">
			<p>
				<spring:message code="administrator.list.suspicious.actors" />
			</p>
			<!-- Tabla de actores sospechosos -->

			<table class="displayStyle">
				<tr>
					<th><spring:message
							code="administrator.list.suspicious.actors" /></th>
				</tr>

				<tr>
					<td><display:table pagesize="5" class="displaytag"
							name="actors"
							requestURI="actor/administrator/list-suspicious-actors.do"
							id="actors">

							<display:column property="userAccount.username"
								titleKey="actor.userAccount.username" sortable="true" />

							<display:column property="surname" titleKey="actor.surname"
								sortable="true" />

							<display:column property="name" titleKey="actor.name"
								sortable="true" />

							<display:column property="email" titleKey="actor.email" />

							<display:column property="userAccount.authorities"
								titleKey="actor.type" />

							<display:column>
								<a href="actor/display.do?actorID=${actors.id }"><spring:message
										code="actor.display" /></a>
							</display:column>

							<display:column>
								<jstl:choose>
									<jstl:when test="${!actors.userAccount.isBanned}">

										<input type="button" name="ban"
											value="<spring:message code="actor.ban" />"
											onclick="redirect: location.href = 'actor/administrator/ban.do?actorID=${actors.id}';" />

									</jstl:when>
									<jstl:otherwise>

										<input type="button" name="unban"
											value="<spring:message code="actor.unban" />"
											onclick="redirect: location.href = 'actor/administrator/unban.do?actorID=${actors.id}';" />

									</jstl:otherwise>
								</jstl:choose>
							</display:column>

						</display:table></td>
				</tr>
			</table>
		</jstl:when>
		<jstl:otherwise>
			<p>
				<spring:message code="empty" />
			</p>
		</jstl:otherwise>
	</jstl:choose>
</security:authorize>