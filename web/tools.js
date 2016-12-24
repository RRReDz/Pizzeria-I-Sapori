/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

function checkPrenotazione() {
    var error = document.getElementById("error");
    var elements = document.getElementsByName("num_pizza");
    var tot_pizze = 0;
    for(i = 0; i < elements.length; i++) 
        tot_pizze += parseInt(elements[i].value);
    if(tot_pizze === 0) {
        error.innerHTML = "ERRORE! Nessuna pizza selezionata!";
        return false;
    }
    var newDate = new Date();
    var date = document.getElementById("myDataOra").value;
    var myDate = new Date(date);
    var timezoneOffset = newDate.getTimezoneOffset() * 60 * 1000;
    var today = new Date(newDate.getTime() - timezoneOffset);
  
    if(Object.prototype.toString.call(myDate) !== "[object Date]" || myDate.getTime() <= today.getTime())
        return false;
    return true;
}

//SET PARAMETRO HIDDEN ACTION E ID DELL'ORDINE DA MANDARE A SERVLET PER SMISTARE RICHIESTA -> NUOVO
function setHiddenOrdine(tipoAzione, idOrdine) {
    document.getElementById('input_hidden').value = tipoAzione;
    document.getElementById('id_ordine_hidden').value = idOrdine;
}

function setHiddenCatalogo(tipoAzione, nomePizza) {
    document.getElementById('input_hidden').value = tipoAzione;
    document.getElementById('nome_pizza').value = nomePizza;
}

function goToInserimento() {
    location.href = "./inserimento.jsp";
}

function setDataOra() {
    var currentDate = new Date();
 
    // Find the current time zone's offset in milliseconds.
    var timezoneOffset = currentDate.getTimezoneOffset() * 60 * 1000;

    // Subtract the time zone offset from the current UTC date, and pass
    //  that into the Date constructor to get a date whose UTC date/time is
    //  adjusted by timezoneOffset for display purposes.
    var localDate = new Date(currentDate.getTime() - timezoneOffset);

    // Get that local date's ISO date string and remove the Z.
    var localDateISOString = localDate.toISOString().substring(0, 16);

    // Finally, set the input's value to that timezone-less string.
    document.getElementById('myDataOra').value = localDateISOString;
}

function setXMLHttpRequest() {
    var xhr = null;
    if (window.XMLHttpRequest) // browser standard con supporto nativo
        xhr = new XMLHttpRequest();
    else if (window.ActiveXObject) // browser MS Internet Explorer - ActiveX
        xhr = new ActiveXObject("Microsoft.XMLHTTP");
    return xhr;
}

var xhrObj = setXMLHttpRequest();
function getInfoPizza(){
    var nomePizza = document.getElementById("nomepizza").value;
    var url = "http://localhost:8080/Pizzeria/Controller?action=modifica_ajax&pizza=" + nomePizza;
    xhrObj.open("GET", url, true); // (indirizzo=url)
    xhrObj.onreadystatechange = updatePage; // indico funzione (updatePage)
    xhrObj.send();
}

function updatePage() {
    if(xhrObj.readyState === 4){
        var info = JSON.parse(xhrObj.responseText);

        var ingredienti = info[0].split(", ");
        var prezzo = info[1];

        var checkbox = document.getElementsByName("ingredienti");
        for(var i = 0; i < checkbox.length; i++){
            for(var j = 0; j < ingredienti.length; j++)
                if(checkbox[i].value === ingredienti[j])
                    checkbox[i].checked = true;
        }

        document.getElementById("pr").value = prezzo;
    }
}