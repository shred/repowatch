<%--
  Repowatch -- A repository watcher
    (C) 2008 Richard "Shred" Körber
    http://repowatch.shredzone.org/
 -----------------------------------------------------------------------
  This program is free software: you can redistribute it and/or modify
  it under the terms of the GNU General Public License as published by
  the Free Software Foundation, either version 3 of the License, or
  any later version.
 
  This program is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU General Public License for more details.
 
  You should have received a copy of the GNU General Public License
  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 
  $Id: adminrepo.jsp 277 2009-03-03 00:05:58Z shred $
--%>
<%@ include file="/WEB-INF/jsp/fragments/includes.jspf" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<fmt:message scope="request" var="title" key="admin.title"/>
<c:import url="/WEB-INF/jsp/fragments/header.jspf"/>

<form:form commandName="repo">
  <table>
    <tr>
      <td><fmt:message key="admin.name"/>:</td>
      <td><form:input path="name" /></td>
      <td><form:errors path="name" /></td>
    </tr>
    <tr>
      <td><fmt:message key="admin.architecture"/>:</td>
      <td><form:input path="architecture" /></td>
      <td><form:errors path="architecture" /></td>
    </tr>
    <tr>
      <td><fmt:message key="admin.baseurl"/>:</td>
      <td><form:input path="baseUrl" /></td>
      <td><form:errors path="baseUrl" /></td>
    </tr>
    <tr>
      <td><fmt:message key="admin.repoviewurl"/>:</td>
      <td><form:input path="repoviewUrl" /></td>
      <td><form:errors path="repoviewUrl" /></td>
    </tr>
    <tr>
      <td>&nbsp;</td>
      <td><input type="submit" value="<fmt:message key="admin.submit"/>" /></td>
      <td><form:errors path="*" /></td>
    </tr>
  </table>
</form:form>


<c:import url="/WEB-INF/jsp/fragments/footer.jspf"/>
