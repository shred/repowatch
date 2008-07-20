<%@ include file="/WEB-INF/jsp/fragments/includes.jspf" %>

<fmt:message scope="request" var="title" key="search.title"/>
<c:import url="/WEB-INF/jsp/fragments/header.jspf"/>
<c:import url="/WEB-INF/jsp/fragments/searchmask.jspf"/>

<c:if test="${not empty message}">
  <div class="note"><fmt:message key="${message}"/></div>
</c:if>

<c:import url="/WEB-INF/jsp/fragments/footer.jspf"/>