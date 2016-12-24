<%-- 
    Document   : index.jsp
    Created on : 9-dic-2015, 16.42.14
    Author     : Riccardo Rossi, Joana Minja, Simone Ricciardi e Saimir Pasho
--%>

<%@include file="header.html" %>
<%@page session="false" %>
<script>
$(document).ready(function() { //Quando il DOM è "pronto" esegue la funzione
    var url = 'http://localhost:8080/Pizzeria/Controller?action=catalogo';
    $.get(url, function(responseJson) {    // Execute Ajax GET request on URL of "someservlet" and execute the following function with Ajax response JSON...
        //var $ul = $("<ul>").appendTo($("#catalogo")); // Create HTML <ul> element and append it to HTML DOM element with ID "somediv".
        $.each(responseJson, function(index, item) { // Iterate over the JSON array.
            var pizza = item.split(";");
            $("<div>").html("<div id='pizza'>" + pizza[0] + "</div><div id='prezzo'>" + pizza[2] + "&#8364;</div><div id='ingr'>" + pizza[1] + "</div>").appendTo($("#catalogo")); // Create HTML <li> element, set its text content with currently iterated item and append it to the <ul>.
        });
    });
});
</script>
    <form id="login" name="form" action="<%=request.getContextPath()%>/Controller" method="post">
      <fieldset>
        <legend>Accedi</legend>
          <%    
                String param = request.getParameter("login");
                if(param != null) {
                    if(param.equals("error"))
                        out.print("ERRORE DATI INSERITI!");
                    else if(param.equals("error_session"))
                        out.print("AUTENTICAZIONE NON EFFETTUATA!");
                }
                
          %>
          <input type="hidden" name="action" value="login">
          <input type="text" name="user" placeholder="Username" pattern = "([A-Z]{0,}[a-z]{0,}[0-9]{0,}){1,}" required ="required" />
          <input type="password" name="psw" placeholder="Password" required="required" /><br>           
          <input type="submit" name="submit" value="Login" /> 
      </fieldset>
    </form>
    <div id ="catalogo"><p align="center">
        <h1>Catalogo pizze</h1>
    </div>
 <%@include  file="footer.html" %>
