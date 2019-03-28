<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<div>
	<jstl:if test="${not empty messageCode}">
		<h4>
			<spring:message code="${messageCode}" />
		</h4>
	</jstl:if>
	<display:table name="messageboxes" id="messagebox" requestURI="${requestURI}"
		pagesize="10">
		<display:column titleKey="messagebox.name">
			<jstl:out value="${messagebox.name}"></jstl:out>
		</display:column>

		<display:column titleKey="message.display">
			<button
				onClick="window.location.href='messagebox/content.do?Id=${messagebox.id}'">
				<spring:message code="messagebox.seeM" />
			</button>
		</display:column>

		<display:column titleKey="messagebox.edit">
			<jstl:if test="${messagebox.isPredefined == false}">
				<button
					onClick="window.location.href='messagebox/edit.do?Id=${messagebox.id}'">
					<spring:message code="messagebox.edit" />
				</button>
			</jstl:if>
		</display:column>
		<display:column titleKey="messagebox.delete">
			<jstl:if test="${messagebox.isPredefined == false}">
				<button
					onClick="window.location.href='messagebox/delete.do?Id=${messagebox.id}'">
					<spring:message code="messagebox.delete" />
				</button>
			</jstl:if>
		</display:column>

	</display:table>
</div>
