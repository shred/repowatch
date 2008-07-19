<%@ include file="/WEB-INF/jsp/fragments/includes.jspf" %>
<%@ include file="/WEB-INF/jsp/fragments/header.jspf" %>
<%@ page import="org.shredzone.repowatch.web.util.Sequencer" %>
<% pageContext.setAttribute("sequence", new Sequencer("gridodd", "grideven")); %>

<h2><c:out value="${package.name}"/></h2>
<table class="grid">
  <tr class="gridrow">
    <td class="gridlabel"><fmt:message key="pack.name"/></td>
    <td class="gridstrong"><c:out value="${package.name}"/></td>
  </tr>
  <c:if test="${not empty domain}">
    <tr class="gridrow">
      <td class="gridlabel"><fmt:message key="pack.release"/></td>
      <td><c:out value="${domain.name}"/> <c:out value="${domain.release}"/></td>
    </tr>
  </c:if>
  <tr class="gridrow">
    <td class="gridlabel"><fmt:message key="pack.summary"/></td>
    <td><c:out value="${package.summary}"/></td>
  </tr>
  <tr class="gridrow">
    <td class="gridlabel"><fmt:message key="pack.description"/></td>
    <td><c:out value="${package.description}"/></td>
  </tr>
  <tr class="gridrow">
    <td class="gridlabel"><fmt:message key="pack.group"/></td>
    <td><c:out value="${package.packGroup}"/></td>
  </tr>
  <c:if test="${not empty package.homeUrl}">
    <tr class="gridrow">
      <td class="gridlabel"><fmt:message key="pack.homepage"/></td>
      <td><c:out value="${package.homeUrl}"/></td>
    </tr>
  </c:if>
</table>

<h2><fmt:message key="pack.repositories"/></h2>
<table class="grid">
  <tr class="gridhead">
    <th><fmt:message key="pack.release"/></th>
    <th><fmt:message key="pack.repository"/></th>
    <th><fmt:message key="pack.version"/></th>
    <th><fmt:message key="pack.dateadded"/></th>
    <th><fmt:message key="pack.dateupdated"/></th>
  </tr>
  <c:forEach var="version" items="${versionList}">
    <tr class="${sequence.next}">
      <td><c:out value="${version.repository.domain.name}"/> <c:out value="${version.repository.domain.release}"/></td>
      <td><a href="<c:url value="/repo/${version.repository.domain.name}/${version.repository.domain.release}/${version.repository.name}/${version.repository.architecture}.html"/>"><c:out value="${version.repository.name}"/> (<c:out value="${version.repository.architecture}"/>)</a></td>
      <td><c:out value="${version.ver}"/>-<c:out value="${version.rel}"/></td>
      <td><fmt:formatDate value="${version.firstSeen}" type="both" dateStyle="short"/></td>
      <td><fmt:formatDate value="${version.fileDate}" type="both" dateStyle="short"/></td>
    </tr>
  </c:forEach>
  <c:forEach var="version" items="${alternativeList}">
    <tr class="${sequence.next}">
      <td><a href="<c:url value="/package/${version.repository.domain.name}/${version.repository.domain.release}/${package.name}.html"/>"><c:out value="${version.repository.domain.name}"/> <c:out value="${version.repository.domain.release}"/></a></td>
      <td><a href="<c:url value="/repo/${version.repository.domain.name}/${version.repository.domain.release}/${version.repository.name}/${version.repository.architecture}.html"/>"><c:out value="${version.repository.name}"/> (<c:out value="${version.repository.architecture}"/>)</a></td>
      <td><c:out value="${version.ver}"/>-<c:out value="${version.rel}"/></td>
      <td><fmt:formatDate value="${version.firstSeen}" type="both" dateStyle="short"/></td>
      <td><fmt:formatDate value="${version.fileDate}" type="both" dateStyle="short"/></td>
    </tr>
  </c:forEach>
</table>

<%@ include file="/WEB-INF/jsp/fragments/footer.jspf" %>
