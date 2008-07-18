<%@ include file="/WEB-INF/jsp/fragments/includes.jspf" %>
<%@ include file="/WEB-INF/jsp/fragments/header.jspf" %>
<%@ page import="org.shredzone.repowatch.web.util.Sequencer" %>
<% pageContext.setAttribute("sequence", new Sequencer("gridodd", "grideven")); %>

<h2><fmt:message key="repos.repositories"/></h2>
<table class="grid">
  <tr class="gridhead">
    <th><fmt:message key="repos.release"/></th>
    <th><fmt:message key="repos.repository"/></th>
    <th><fmt:message key="repos.packages"/></th>
    <th><fmt:message key="repos.lastupdated"/></th>
    <th>&nbsp;</th>
  </tr>
  <c:forEach var="repo" items="${repositoryList}">
    <tr class="${sequence.next}">
      <td><c:out value="${repo.domain.name}"/> <c:out value="${repo.domain.release}"/></td>
      <td><c:out value="${repo.name}"/> (<c:out value="${repo.architecture}"/>)</td>
      <td style="text-align:right"><fmt:formatNumber value="${counterMap[repo]}"/></td>
      <td style="text-align:center"><fmt:formatDate value="${repo.lastModifiedDate}" type="both" dateStyle="full"/></td>
      <td><a href="<c:url value="/repo/${repo.domain.name}/${repo.domain.release}/${repo.name}/${repo.architecture}.html"/>"><fmt:message key="repos.listpackages"/></a>
        | <a href="<c:url value="/changes/${repo.domain.name}/${repo.domain.release}/${repo.name}/${repo.architecture}.html"/>"><fmt:message key="repos.listchanges"/></a>
        | <a href="<c:url value="/rss/${repo.domain.name}/${repo.domain.release}/${repo.name}/${repo.architecture}.rss"/>" class="rss">RSS</a></td>
    </tr>
  </c:forEach>
</table>

<%@ include file="/WEB-INF/jsp/fragments/footer.jspf" %>
