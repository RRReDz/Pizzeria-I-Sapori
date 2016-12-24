<%-- 
    Document   : index.jsp
    Created on : 9-dic-2015, 16.42.14
    Author     : Riccardo Rossi, Joana Minja, Simone Ricciardi e Saimir Pasho
--%>

<%@page session="true" %>
<% if(session == null || session.getAttribute("username") == null)
        response.sendRedirect("./index.jsp?param=error_session");
    else {
%>
<%@include  file="header.html" %>
<div id="uscita">
    <a href="<%=request.getContextPath()%>/Controller?action=esci">
      <img src="images/uscita.png" alt="esci" title="Esci"></a>
</div>
<p id="benvenuto">Benvenuto/a <strong>${sessionScope.username}</strong></p>
        <div id="autenticato">
          <div id="prenotazione">
              <a href="<%=request.getContextPath()%>/Controller?action=prenotazione">
                <img src="images/prenota.png" alt="prenota" title="Prenota pizze"></a> <br/>
                Prenota pizze
          </div>
          <div id="ordini">
              <a href="<%=request.getContextPath()%>/Controller?action=visualizza_ordine">
              <img src="images/ordini.png" alt="ordini" title="Visualizza gli ordini"></a> <br/>
              Visualizza gli ordini
          </div>
          <% if(session.getAttribute("ruolo").equals("amministratore")) { 
              %>
              <div id="modifica">
                  <a href="<%=request.getContextPath()%>/Controller?action=modifica_catalogo">
                  <img src="images/modifica.png" alt="modifica" title="Modifica il catalogo"></a> <br/>
                  Modifica il catalogo
              </div>
          <% } %>
        </div>
<%@include  file="footer.html" %>
<%}%>