<%--
 * action-1.jsp
 *
 * Copyright (C) 2019 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>

<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<security:authorize access="hasRole('ADMINISTRATOR')">

	<jstl:choose>
		<jstl:when test="${err}">
			<p>
				<jstl:out value="${errMsg.getMessage()}" />
			</p>
		</jstl:when>
		<jstl:otherwise>

			<table class="displayStyle">

				<tr>
					<td><spring:message code="system.name" />:</td>
					<td><jstl:out value="${sysConfig.systemName}" /></td>
				</tr>
				<tr>
					<td><spring:message code="system.bannerURL" />:</td>
					<td><a href="${sysConfig.banner}"><jstl:out
								value="${sysConfig.banner}" /></a></td>
				</tr>

				<tr>
					<td><spring:message code="system.resultscached" />:</td>
					<td><jstl:out value="${sysConfig.timeResultsCached}" /></td>
				</tr>

				<tr>
					<td><spring:message code="system.resultspersearch" />:</td>
					<td><jstl:out value="${sysConfig.maxResults}" /></td>
				</tr>

				<tr>
					<td><spring:message code="system.countrycode" />:</td>
					<td><jstl:out value="${sysConfig.countryCode}" /></td>
				</tr>
			</table>

			<div style="width: 20%; float: left; position: static">
				<table class="displayStyle">
					<tr>
						<td><display:table pagesize="5" class="displaytag"
								name="messagePriority"
								requestURI="sysconfig/administrator/display.do"
								id="messagePriority">

								<display:column titleKey="system.message.priority"
									sortable="true">
									<jstl:out value="${messagePriority}" />
								</display:column>

							</display:table></td>
				</table>
			</div>

			<div style="width: 20%; float: left">
				<table class="displayStyle" style="text-align: center;">
					<tr>
						<td><display:table pagesize="5" class="displaytag"
								name="spamWords" requestURI="sysconfig/administrator/display.do"
								id="spamWords">

								<display:column titleKey="system.spamwords" sortable="true">
									<jstl:out value="${spamWords}" />
								</display:column>

							</display:table></td>
					</tr>
				</table>
			</div>

			<div style="width: 20%; float: left; position: static">
				<table class="displayStyle">
					<tr>
						<td><display:table pagesize="5" class="displaytag"
								name="negativeWords"
								requestURI="sysconfig/administrator/display.do"
								id="negativeWords">

								<display:column titleKey="system.negativewords" sortable="true">
									<jstl:out value="${negativeWords}" />
								</display:column>

							</display:table></td>
					</tr>
				</table>
			</div>

			<div style="width: 20%; float: left; position: static">
				<table class="displayStyle">
					<tr>
						<td><display:table pagesize="5" class="displaytag"
								name="positiveWords"
								requestURI="sysconfig/administrator/display.do"
								id="positiveWords">

								<display:column titleKey="system.positivewords" sortable="true">
									<jstl:out value="${positiveWords}" />
								</display:column>

							</display:table></td>
				</table>
			</div>

			<div style="width: 20%; float: left; position: static">
				<table class="displayStyle">
					<tr>
						<td><display:table pagesize="5" class="displaytag"
								name="welcomeMessage"
								requestURI="sysconfig/administrator/display.do"
								id="welcomeMessage">

								<display:column titleKey="system.welcomemessage" sortable="true">
									<jstl:out value="${welcomeMessage}" />
								</display:column>

							</display:table></td>
				</table>
			</div>
			
			<div style="width: 20%; float: left; position: static">
				<table class="displayStyle">
					<tr>
						<td><display:table pagesize="5" class="displaytag"
								name="breachNotification"
								requestURI="sysconfig/administrator/display.do"
								id="breachNotification">

								<display:column titleKey="system.breachNotification" sortable="true">
									<jstl:out value="${breachNotification}" />
								</display:column>

							</display:table></td>
				</table>
			</div>

			<input type="button" name="edit"
				value='<spring:message code="system.edit"/>'
				onclick="redirect: location.href = 'sysconfig/administrator/edit.do?systemconfigurationID=${sysConfig.id}';" />

		</jstl:otherwise>
	</jstl:choose>


</security:authorize>
