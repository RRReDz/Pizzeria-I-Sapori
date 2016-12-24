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
<%@include file ="header.html" %>
<jsp:useBean id="connbean" scope="session" class="myBeans.DBConnectionBean" />
<script>
    window.onload = setDataOra;
</script>
    <div id="home">
      <a href="<%=request.getContextPath()%>/Controller?action=home">
        <img src="images/home.png" alt="home" title="Vai alla Home Page"></a>
    </div>
    <p id="benvenuto">Benvenuto/a <strong>${sessionScope.username}</strong></p>
    <h2>Scegli la tua pizza</h2>
    <article>
        <form id="form" name="form" action="./Controller" method="post">
          <fieldset>
            <input type="hidden" name="action" value="conferma_ordine">
            <% 
                ArrayList<String> listaPizze = connbean.getCatalogo((String)session.getAttribute("ruolo"));
                String[] pizza;

                for(String infoPizza : listaPizze) {
                    pizza = infoPizza.split(";");
            %>
            <div class="prenotazione_pizze">
                <p id="pizza"><%= pizza[0]%></p><input type = "hidden" name="nome" value="<%= pizza[0]%>"/><br> <!-- Nome pizza -->
                <p id="prezzo"><%= pizza[2]%> &#8364;</p><input type = "hidden" name="prezzo" value="<%= pizza[2]%>"/><br> <!-- Prezzo -->
                <p id="ingr"><%= pizza[1]%></p><br> <!-- Ingredienti -->
                <span id="quantita">Quantità:</span><input id="myPizza1" name="num_pizza" type="number" min="0" value="0" required="required"><br><br>
            </div>              
            <%
                }    
            %>
            </fieldset>
            <p> Inserire data e ora di prenotazione: <input id="myDataOra" name="dataora" type="datetime-local" required="required"> </p><br/>
            <input type="submit" name="submit" value="Ordina" onclick="javascript: return checkPrenotazione()" />
            <p id="error"></p>
        </form>
    </article>
<%@include file ="footer.html" %>
<%}%>