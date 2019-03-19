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
<%@taglib prefix="acme" tagdir="/WEB-INF/tags"%>

<security:authorize access="hasRole('BROTHERHOOD')">

	<table class="displayStyle">

		<tr>
			<td><strong><spring:message
						code="history.inceptionRecord" />: </strong></td>
			<jstl:if test="${isInceptionRecord}">
			
				<td><jstl:out value="${inceptionRecord.title}"></jstl:out></td>
				<td><a
					href="inceptionRecord/display.do?inceptionRecordId=${inceptionRecord.id}">
						<spring:message code="history.inceptionRecord.display" />
				</a></td>

			</jstl:if>
			
			<jstl:if test="${!isInceptionRecord}">
				<td><spring:message code="history.empty.record" /></td>
				<td><a href="inceptionRecord/create.do"> <spring:message
							code="history.inceptionRecord.create" />
				</a></td>
			</jstl:if>
		</tr>

		<tr>
			<td><strong><spring:message
						code="history.first.miscellaneousRecord" />: </strong></td>
			<jstl:if test="${!miscellaneousRecords.isEmpty()}">
				<td><jstl:out value="${miscellaneousRecord.title}"></jstl:out></td>

				<td><a
					href="miscellaneousRecord/list.do?historyId=${history.id}"> <spring:message
							code="history.miscellaneousRecord.list" />
				</a></td>
			</jstl:if>
			<jstl:if test="${miscellaneousRecords.isEmpty()}">
				<td><spring:message code="history.empty.record" /></td>
				<td><a href="miscellaneousRecord/create.do"> <spring:message
							code="history.miscellaneousRecord.create" />
				</a></td>
			</jstl:if>
		</tr>

		<tr>
			<td><strong><spring:message
						code="history.first.periodRecord" />: </strong></td>
			<jstl:if test="${!periodRecords.isEmpty()}">
				<td><jstl:out value="${periodRecord.title}"></jstl:out></td>

				<td><a href="periodRecord/list.do?historyId=${history.id}">
						<spring:message code="history.periodRecord.list" />
				</a></td>
			</jstl:if>
			<jstl:if test="${periodRecords.isEmpty()}">
				<td><spring:message code="history.empty.record" /></td>
				<td><a href="periodRecord/create.do"> <spring:message
							code="history.periodRecord.create" />
				</a></td>
			</jstl:if>
		</tr>

		<tr>
			<td><strong><spring:message
						code="history.first.legalRecord" />: </strong></td>
			<jstl:if test="${!legalRecords.isEmpty()}">
				<td><jstl:out value="${legalRecord.title}"></jstl:out></td>

				<td><a href="legalRecord/list.do?historyId=${history.id}">
						<spring:message code="history.legalRecord.list" />
				</a></td>
			</jstl:if>

			<jstl:if test="${legalRecords.isEmpty()}">
				<td><spring:message code="history.empty.record" /></td>
				<td><a href="legalRecord/create.do"> <spring:message
							code="history.legalRecord.create" />
				</a></td>
			</jstl:if>
		</tr>

		<tr>
			<td><strong><spring:message
						code="history.first.linkRecord" />: </strong></td>
			<jstl:if test="${!linkRecords.isEmpty()}">
				<td><jstl:out value="${linkRecord.title}"></jstl:out></td>

				<td><a href="linkRecord/list.do?historyId=${history.id}"> <spring:message
							code="history.linkRecord.list" />
				</a></td>
			</jstl:if>

			<jstl:if test="${linkRecords.isEmpty()}">
				<td><spring:message code="history.empty.record" /></td>
				<td><a href="linkRecord/create.do"> <spring:message
							code="history.linkRecord.create" />
				</a></td>
			</jstl:if>

		</tr>




	</table>
</security:authorize>

<security:authorize access="isAnonymous()">

	<!-- INCEPTION RECORD -->
	<tr>
		<td><h3>
				<strong><spring:message code="history.inceptionRecord" />:
				</strong>
			</h3></td>

		<td>
			<table class="displayStyle">

				<tr>
					<td><strong><spring:message
								code="history.inceptionRecord.title" />: </strong></td>
					<td><jstl:out value="${inceptionRecord.title }"></jstl:out></td>
				</tr>

				<tr>
					<td><strong><spring:message
								code="history.inceptionRecord.description" /> :</strong></td>
					<td><jstl:out value="${inceptionRecord.description}"></jstl:out>
					</td>
				</tr>

				<tr>
					<td><strong><spring:message
								code="history.inceptionRecord.photos" />: </strong></td>
					<td><jstl:out value="${inceptionRecord.photos }"></jstl:out></td>
				</tr>
			</table> <!-- MISCELLANEOUS RECORDS -->
	<tr>
		<td><h3>
				<strong><spring:message code="history.miscellaneousRecords" />:
				</strong>
			</h3></td>
		<jstl:if test="${!miscellaneousRecords.isEmpty() }">
			<jstl:forEach var="mr" items="${miscellaneousRecords}">

				<table class="displayStyle">

					<tr>
						<td><strong> <spring:message
									code="history.miscellaneousRecord.title" />:
						</strong></td>
						<td><jstl:out value="${mr.title}"></jstl:out></td>
					</tr>

					<tr>
						<td><strong> <spring:message
									code="history.miscellaneousRecord.description" />:
						</strong></td>
						<td><jstl:out value="${mr.description}"></jstl:out></td>
					</tr>

				</table>

			</jstl:forEach>
		</jstl:if>

		<jstl:if test="${miscellaneousRecords.isEmpty()}">
			<table class="displayStyle">
				<tr>
					<td><strong> <spring:message
								code="history.empty.record" />
					</strong></td>

				</tr>

			</table>
		</jstl:if>

	</tr>

	<!-- LEGAL RECORDS -->
	<tr>
		<td><h3>
				<strong><spring:message code="history.legalRecords" />: </strong>
			</h3></td>

		<jstl:if test="${!legalRecords.isEmpty()}">
			<jstl:forEach var="x" items="${legalRecords}">

				<table class="displayStyle">

					<tr>
						<td><strong> <spring:message
									code="history.legalRecord.title" />:
						</strong></td>
						<td><jstl:out value="${x.title}"></jstl:out></td>
					</tr>

					<tr>
						<td><strong> <spring:message
									code="history.legalRecord.description" />:
						</strong></td>
						<td><jstl:out value="${x.description}"></jstl:out></td>
					</tr>

					<tr>
						<td><strong> <spring:message
									code="history.legalRecord.name" />:
						</strong></td>
						<td><jstl:out value="${x.name}"></jstl:out></td>
					</tr>

					<tr>
						<td><strong> <spring:message
									code="history.legalRecord.VAT" />:
						</strong></td>
						<td><jstl:out value="${x.VAT}"></jstl:out></td>
					</tr>

					<tr>
						<td><strong> <spring:message
									code="history.legalRecord.laws" />:
						</strong></td>
						<td><jstl:out value="${x.laws}"></jstl:out></td>
					</tr>

				</table>

			</jstl:forEach>
		</jstl:if>

		<jstl:if test="${legalRecords.isEmpty()}">
			<table class="displayStyle">
				<tr>
					<td><strong> <spring:message
								code="history.empty.record" />
					</strong></td>

				</tr>

			</table>
		</jstl:if>


	</tr>

	<!-- PERIOD RECORDS -->
	<tr>
		<td><h3>
				<strong><spring:message code="history.periodRecords" />: </strong>
			</h3></td>

		<jstl:if test="${!periodRecords.isEmpty() }">
			<jstl:forEach var="i" items="${periodRecords}">

				<table class="displayStyle">

					<tr>
						<td><strong> <spring:message
									code="history.periodRecords.title" />:
						</strong></td>
						<td><jstl:out value="${i.title}"></jstl:out></td>
					</tr>

					<tr>
						<td><strong> <spring:message
									code="history.periodRecords.description" />:
						</strong></td>
						<td><jstl:out value="${i.description}"></jstl:out></td>
					</tr>

					<tr>
						<td><strong> <spring:message
									code="history.periodRecords.start" />:
						</strong></td>
						<td><jstl:out value="${i.startYear}"></jstl:out></td>
					</tr>

					<tr>
						<td><strong> <spring:message
									code="history.periodRecords.end" />:
						</strong></td>
						<td><jstl:out value="${i.endYear}"></jstl:out></td>
					</tr>

					<tr>
						<td><strong> <spring:message
									code="history.periodRecords.photos" />:
						</strong></td>
						<td><jstl:out value="${i.photos}"></jstl:out></td>
					</tr>

				</table>

			</jstl:forEach>
		</jstl:if>

		<jstl:if test="${periodRecords.isEmpty()}">
			<table class="displayStyle">
				<tr>
					<td><strong> <spring:message
								code="history.empty.record" />
					</strong></td>

				</tr>

			</table>
		</jstl:if>

	</tr>


	<tr>
		<td><h3>
				<strong><spring:message code="history.linkRecords" />: </strong>
			</h3></td>

		<jstl:if test="${!linkRecords.isEmpty()}">
			<jstl:forEach var="j" items="${linkRecords}">

				<table class="displayStyle">

					<tr>
						<td><strong> <spring:message
									code="history.linkRecord.title" />:
						</strong></td>
						<td><jstl:out value="${j.title}"></jstl:out></td>
					</tr>

					<tr>
						<td><strong> <spring:message
									code="history.linkRecord.description" />:
						</strong></td>
						<td><jstl:out value="${j.description}"></jstl:out></td>
					</tr>

					<tr>
						<td><strong> <spring:message
									code="history.linkRecord.brotherhood" />:
						</strong></td>
						<td><jstl:out value="${j.linkedBrotherhood.title}"></jstl:out></td>
					</tr>


				</table>

			</jstl:forEach>
		</jstl:if>

		<jstl:if test="${linkRecords.isEmpty()}">
			<table class="displayStyle">
				<tr>
					<td><strong> <spring:message
								code="history.empty.record" />
					</strong></td>

				</tr>

			</table>
		</jstl:if>
	</tr>
</security:authorize>