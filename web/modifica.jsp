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
<%@page contentType="text/html" pageEncoding="UTF-8" session="true"%>
<%@include file="header.html" %>
<script>window.onload = getInfoPizza;</script>
<script>
    $(document).ready(function () {
        $('#confirm').click(function() {
          $('#error').html("");
          checked = $("input[type=checkbox]:checked").length;

          if(!checked) {
            $('#error').html("Devi selezionare almeno un ingrediente!");
            return false;
          }

        });
    });
</script>
    <div id="home">
          <a href="<%=request.getContextPath()%>/Controller?action=home">
            <img src="images/home.png" alt="home" title="Vai alla Home Page"></a>
        </div>
<p id="benvenuto">Benvenuto/a <strong>${sessionScope.username}</strong></p>

    <h2> Modifica pizza </h2>
    <form id="form_modifica" method="post" action="./Controller">
      <fieldset>
        <input type="hidden" name="action" value="inserisci_pizza"/>
        <input type="hidden" name="tipo" value="modifica" />
        <p>Nome: <input id = "nomepizza" type="text" name="nome" required="required" value="<%= request.getParameter("nome_pizza")%>"><br></p>
        <div id="ingredienti">
            <p>Ingredienti:<br>
            <div class="ingredienti"><label><input name="ingredienti" type="checkbox" value="Pomodoro">Pomodoro</label><br>
              <label><input name="ingredienti" type="checkbox" value="Mozzarella">Mozzarella</label><br>
              <label><input name="ingredienti" type="checkbox" value="Mozzarella di Bufala">Mozzarella di Bufala</label><br>
              <label><input name="ingredienti" type="checkbox" value="Basilico">Basilico</label></div>
              <div class="ingredienti"><label><input name="ingredienti" type="checkbox" value="Prosciutto">Prosciutto</label><br>
              <label><input name="ingredienti" type="checkbox" value="Patatine">Patatine</label><br>
              <label><input name="ingredienti" type="checkbox" value="Wurstel">Wurstel</label><br>
              <label><input name="ingredienti" type="checkbox" value="Salsiccia">Salsiccia</label></div>
              <div class="ingredienti"><label><input name="ingredienti" type="checkbox" value="Funghi">Funghi</label><br>
              <label><input name="ingredienti" type="checkbox" value="Tonno">Tonno</label><br>
              <label><input name="ingredienti" type="checkbox" value="Carciofi">Carciofi</label><br>
              <label><input name="ingredienti" type="checkbox" value="Capperi">Capperi</label></div>
              <div class="ingredienti"><label><input name="ingredienti" type="checkbox" value="Frutti di Mare">Frutti di Mare</label><br>
              <label><input name="ingredienti" type="checkbox" value="Rucola">Rucola</label><br>
              <label><input name="ingredienti" type="checkbox" value="Aglio">Aglio</label><br>
              <label><input name="ingredienti" type="checkbox" value="Origano">Origano</label></div>
              <div class="ingredienti"><label><input name="ingredienti" type="checkbox" value="Olive">Olive</label><br>
              <label><input name="ingredienti" type="checkbox" value="Acciughe">Acciughe</label><br>
              <label><input name="ingredienti" type="checkbox" value="Taleggio">Taleggio</label><br>
              <label><input name="ingredienti" type="checkbox" value="Grana">Grana</label></div>
            </div>
        <p>Prezzo:<input id="pr" name="prezzo" type="number" min="1.00" required="required" step="any"> &#8364;</p>
        <input id="confirm" type="Submit" name="ok" value="Conferma">
        <div id="error" style="color:red"></div>
      </fieldset>
    </form>
<%@include  file="footer.html" %>
<%}%>