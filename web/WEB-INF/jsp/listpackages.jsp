<%@ include file="/WEB-INF/jsp/fragments/includes.jspf" %>
<%@ include file="/WEB-INF/jsp/fragments/header.jspf" %>
<%@ page import="org.shredzone.repowatch.web.util.Sequencer" %>
<% pageContext.setAttribute("sequence", new Sequencer("gridodd", "grideven")); %>

<h2><fmt:message key="allp.result"/></h2>
<c:import url="/WEB-INF/jsp/fragments/browser.jspf"/>
<table class="grid">
  <tr class="gridhead">
    <th><fmt:message key="allp.package"/></th>
    <th><fmt:message key="allp.summary"/></th>
  </tr>
  <c:forEach var="package" items="${packageList}">
    <tr class="${sequence.next}">
      <td><a href="<c:url value="/package/${package[0]}.html"/>"><c:out value="${package[0]}"/></a></td>
      <td><c:out value="${package[1]}"/></td>
    </tr>
  </c:forEach>
</table>

<%@ include file="/WEB-INF/jsp/fragments/footer.jspf" %>
