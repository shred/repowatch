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
 
  $Id: adminconfirmdelete.jsp 270 2009-02-25 23:06:14Z shred $
--%>
<%@ include file="/WEB-INF/jsp/fragments/includes.jspf" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<fmt:message scope="request" var="title" key="admin.title"/>
<c:import url="/WEB-INF/jsp/fragments/header.jspf"/>

<form:form>
  <table>
    <tr>
      <td><fmt:message key="admin.confirm"/>:</td>
      <td><form:checkbox path="confirmed" /></td>
      <td><form:errors path="name" /></td>
    </tr>
    <tr>
      <td>&nbsp;</td>
      <td colspan="2"><input type="submit" value="<fmt:message key="admin.submit"/>" /></td>
    </tr>
  </table>
</form:form>

<c:import url="/WEB-INF/jsp/fragments/footer.jspf"/>
