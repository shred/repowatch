<%@ include file="/WEB-INF/jsp/fragments/includes.jspf" %>
<%@ include file="/WEB-INF/jsp/fragments/header.jspf" %>
<%@ page import="org.shredzone.repowatch.web.util.Sequencer" %>
<% pageContext.setAttribute("sequence", new Sequencer("gridodd", "grideven")); %>

<h2><c:out value="${domain.name}"/> <c:out value="${domain.release}"/> <c:out value="${repository.name}"/> (<c:out value="${repository.architecture}"/>)</h2>
<table class="grid">
  <tr class="gridrow">
    <td class="gridlabel"><fmt:message key="vers.release"/></td>
    <td><c:out value="${domain.name}"/> <c:out value="${domain.release}"/></td>
  </tr>
  <tr class="gridrow">
    <td class="gridlabel"><fmt:message key="vers.repository"/></td>
    <td><c:out value="${repository.name}"/> (<c:out value="${repository.architecture}"/>)</td>
  </tr>
  <tr class="gridrow">
    <td class="gridlabel"><fmt:message key="vers.baseurl"/></td>
    <td><a href="<c:url value="${repository.baseUrl}"/>" class="ext"><c:out value="${repository.baseUrl}"/></a></td>
  </tr>
  <tr class="gridrow">
    <td class="gridlabel"><fmt:message key="vers.since"/></td>
    <td><fmt:formatDate value="${repository.firstScanned}" type="both" dateStyle="full"/></td>
  </tr>
  <tr class="gridrow">
    <td class="gridlabel"><fmt:message key="vers.lastscanned"/></td>
    <td><fmt:formatDate value="${repository.lastScanned}" type="both" dateStyle="full"/></td>
  </tr>
  <tr class="gridrow">
    <td class="gridlabel"><fmt:message key="vers.lastmodified"/></td>
    <td><fmt:formatDate value="${repository.lastModifiedDate}" type="both" dateStyle="full"/></td>
  </tr>
</table>

<h2><fmt:message key="vers.packages"/></h2>
<c:import url="/WEB-INF/jsp/fragments/browser.jspf"/>
<table class="grid">
  <tr class="gridhead">
    <th><fmt:message key="vers.name"/></th>
    <th><fmt:message key="vers.version"/></th>
    <th><fmt:message key="vers.summary"/></th>
  </tr>
  <c:forEach var="version" items="${versionList}">
    <tr class="${sequence.next}">
      <td><a href="<c:url value="/package/${domain.name}/${domain.release}/${version.package.name}.html"/>"><c:out value="${version.package.name}"/></a></td>
      <td><c:out value="${version.ver}"/>-<c:out value="${version.rel}"/></td>
      <td><c:out value="${version.package.summary}"/></td>
    </tr>
  </c:forEach>
</table>

<%@ include file="/WEB-INF/jsp/fragments/footer.jspf" %>
