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
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<security:authorize access="hasRole('SPONSOR')">
	<jstl:if test="${possible}">
		<jstl:if test="${sponsorship.id == 0}">
			<form:form action="sponsorship/edit.do" modelAttribute="sponsorship">

				<spring:message code="sponsorship.banner" />:
				<br />
				<form:textarea code="sponsorship.banner" path="banner" />
				<br>
				<br>

				<spring:message code="sponsorship.target" />:
				<br />
				<form:textarea code="sponsorship.target" path="target" />
				<br>
				<br>

				<form:label path="creditCard">
					<spring:message code="sponsorship.creditCard.number" />:
				</form:label>
				<form:select path="creditCard">
					<form:option label="-----" value="0" />
					<form:options items="${validCreditCards}" itemLabel="number"
						itemValue="id" />
				</form:select>
				<form:errors cssClass="error" path="creditCard" />
				<br />
				<br />



				<form:label path="parade">
					<spring:message code="sponsorship.parade.title" />:
				</form:label>
				<form:select path="parade">
					<form:option label="-----" value="0" />
					<form:options items="${acceptedParades}" itemLabel="title"
						itemValue="id" />
				</form:select>
				<form:errors cssClass="error" path="parade" />
				<br />
				<br />

				<acme:submit code="sponsorship.save" name="save" />&nbsp;
			
			<acme:cancel code="sponsorship.cancel" url="sponsorship/list.do" />
			</form:form>
		</jstl:if>

		<jstl:if test="${sponsorship.id != 0}">
			<form:form action="sponsorship/edit.do" modelAttribute="sponsorship">

				<form:hidden path="id" />

				<spring:message code="sponsorship.banner" />:
				<br />
				<form:textarea code="sponsorship.banner" path="banner" />
				<br>
				<br>

				<spring:message code="sponsorship.target" />:
				<br />
				<form:textarea code="sponsorship.target" path="target" />
				<br>
				<br>

				<acme:submit code="sponsorship.save" name="save" />&nbsp;
				
				<acme:delete name="delete" confirmation="sponsorship.confirm.delete"
					code="sponsorship.delete" />

				<acme:cancel code="sponsorship.cancel" url="sponsorship/list.do" />

			</form:form>
		</jstl:if>
	</jstl:if>


	<jstl:if test="${!possible}">
		<h3>
			<spring:message code="no.permission" />
		</h3>
	</jstl:if>
</security:authorize>