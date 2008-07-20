<%@ include file="/WEB-INF/jsp/fragments/includes.jspf" %>
<%@ page import="org.shredzone.repowatch.web.util.Sequencer" %>
<% pageContext.setAttribute("sequence", new Sequencer("gridodd", "grideven")); %>

<fmt:message scope="request" var="title" key="search.title"/>
<c:import url="/WEB-INF/jsp/fragments/header.jspf"/>
<c:import url="/WEB-INF/jsp/fragments/searchmask.jspf"/>

<h2><fmt:message key="search.result"/></h2>
<c:import url="/WEB-INF/jsp/fragments/browser.jspf"/>
<table class="grid">
  <tr class="gridhead">
    <th><fmt:message key="search.release"/></th>
    <th><fmt:message key="search.package"/></th>
    <th><fmt:message key="search.summary"/></th>
  </tr>
  <c:forEach var="package" items="${packageList}">
    <tr class="${sequence.next}">
      <td><c:out value="${package.domain.name}"/> <c:out value="${package.domain.release}"/></td>
      <td><a href="<c:url value="/package/${package.domain.name}/${package.domain.release}/${package.name}.html"/>"><c:out value="${package.name}"/></a></td>
      <td><c:out value="${package.summary}"/></td>
    </tr>
  </c:forEach>
</table>

<%@ include file="/WEB-INF/jsp/fragments/footer.jspf" %>
