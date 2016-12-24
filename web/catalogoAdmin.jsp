<%-- 
    Document   : index.jsp
    Created on : 9-dic-2015, 16.42.14
    Author     : Riccardo Rossi, Joana Minja, Simone Ricciardi e Saimir Pasho
--%>

<%@page session="true" %>
<% if(session == null || session.getAttribute("username") == null)
        response.sendRedirect("./index.jsp");
    else {
%>
<%@page import="java.util.ArrayList"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include  file="header.html" %>
<!-- Dichiaro uso del bean -->
<jsp:useBean id="connbean" scope="session" class="myBeans.DBConnectionBean" />
<div id="home">
    <a href="<%=request.getContextPath()%>/Controller?action=home">
    <img src="images/home.png" alt="home" title="Vai alla Home Page"></a>
</div>
<p id="benvenuto">Benvenuto/a <strong>${sessionScope.username}</strong></p>

<form id="form_catalogo" action="./Controller" method="post">
    <input type="hidden" name="action" id="input_hidden" >
    <input type="hidden" name="nome_pizza" id="nome_pizza">
    
    <input type="button" value="Inserisci Pizza" onclick = "javascript: goToInserimento()">
    <%  
       ArrayList<String> catalogo = connbean.getCatalogo((String)session.getAttribute("ruolo"));
       for(String pizza : catalogo){
           String[] pizza_splitted = pizza.split(";");%>
           
           <div class="pizza">
               <span><%= pizza_splitted[0] %></span>
               <span><%= pizza_splitted[1] %></span>
               <span><%= pizza_splitted[2] %>&#8364;</span><br><br>
               <% if(pizza_splitted[3].equals("true")){ %>
                    <input type = "submit" value="Modifica" onclick = "javascript: setHiddenCatalogo('modifica_pizza','<%= pizza_splitted[0] %>')">
                    <input type = "submit" value="Cancella" onclick = "javascript: setHiddenCatalogo('cancella_pizza','<%= pizza_splitted[0] %>')">
               <%}else{%>
                    <input type = "submit" value="Riattiva" onclick = "javascript: setHiddenCatalogo('riattiva_pizza','<%= pizza_splitted[0] %>')">
               <% } %>
               
           </div>           
           
    <%       
       }
    %>
</form>
<%@include  file="footer.html" %>
<%}%>