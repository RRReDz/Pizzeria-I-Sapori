<%-- 
    Document   : index.jsp
    Created on : 9-dic-2015, 16.42.14
    Author     : Riccardo Rossi, Joana Minja, Simone Ricciardi e Saimir Pasho
--%>

<%@page session="true" %>
<% 
    if(session == null || session.getAttribute("username") == null)
        response.sendRedirect("./index.jsp");
    else {
%>
<%@page import="java.util.ArrayList"%>
<%@page contentType="text/html" pageEncoding="UTF-8" session="true"%>
<%@include file="header.html" %>
<!-- Dichiaro uso del bean -->
<jsp:useBean id="connbean" scope="session" class="myBeans.DBConnectionBean" />

<div id="home">
    <a href="<%=request.getContextPath()%>/Controller?action=home">
      <img src="images/home.png" alt="home" title="Vai alla Home Page"></a>
</div>
<p id="benvenuto">Benvenuto/a <strong>${sessionScope.username}</strong></p>

<form id="form_ordine" action="./Controller" method="post">
    <input id="input_hidden" type="hidden" name="action">
    <input id="id_ordine_hidden" type="hidden" name="id_ordine">
    <% ArrayList<String> ordini = connbean.getOrdini((String)session.getAttribute("username"), (String)session.getAttribute("ruolo"));
       for(String ordine : ordini){
           /* Legenda:
              0 -> ID ordine
              1 -> Username 
              2 -> Numero ordine
              3 -> Prezzo totale ordine
              4 -> Stato ordine
              5 -> Data consegna
              6 -> Ora consegna
              per i che parte da 7
              i -> Nome pizza (i)
              (i+1) -> Quantità pizza (i)
           */
           String[] ordini_splitted = ordine.split(";");
    %>      
            <fieldset>
              <span>Username</span>: <%= ordini_splitted[1] %> <br><br>
              <span>Ordine numero</span>: <%= ordini_splitted[2] %><br><br>
              <span>Prezzo totale</span>: <%= ordini_splitted[3] %> &#8364;<br><br>
              <span>Stato</span>: <%= ordini_splitted[4] %><br><br>
              <span>Data consegna</span>: <%= ordini_splitted[5] %><br>
              <span>Ora consegna</span>: <%= ordini_splitted[6] %> <br><br>
                <% for(int i = 7; i < ordini_splitted.length; i += 2) { %>
                <span>Pizza</span>: <%= ordini_splitted[i] %><br>
                   <span>Quantità</span>: <%= ordini_splitted[i + 1] %>
                   <br><br>
                   <% } if(ordini_splitted[4].equals("DA CONSEGNARE")) { %>
                        <button type="submit" id="b_conferma" onclick="javascript: setHiddenOrdine('conferma_consegna', '<%= ordini_splitted[0]%>')">Conferma consegna</button>
                        <button type="submit" id="b_cancella" onclick="javascript: setHiddenOrdine('cancella_ordine', '<%= ordini_splitted[0]%>')">Cancella ordine</button>
                   <% } %>
            </fieldset>
            <br><br>
    <%}
    %>
</form>

<%@include  file="footer.html" %>
<%}%>