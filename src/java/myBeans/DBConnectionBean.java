/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myBeans;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import myClasses.Ordine;
import myClasses.Pizze;

/**
 *
 * @author Riccardo Rossi
 */
public class DBConnectionBean {

    private String url;
    private String user;
    private String pwd;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    } 
    
    public boolean getCheckLogin(String username, String password) throws SQLException {
        boolean out = false;

        Connection conn = DriverManager.getConnection(url, user, pwd);
        Statement st = conn.createStatement();
        ResultSet login = st.executeQuery("SELECT PASSWORD FROM UTENTE WHERE USERNAME LIKE '" + username + "'");

        //Se esiste l'utente e la password è corretta
        if(login.next() && login.getString("PASSWORD").equals(password)) {
            out = true;
        }
        login.close(); st.close(); conn.close();
        
        return out;
    }

    /**
     * Ricava il nome, gli ingredienti e il prezzo di tutte le pizze in catalogo
     *
     * @param query da servlet
     * @return Array di stringhe contenente i tre parametri per ciascuna pizza
     * @throws SQLException
     */
    public ArrayList<String> getCatalogo(String ruolo) throws SQLException {
        ArrayList<String> out = new ArrayList<String>();
        
        /* Connessione al database */
        Connection conn = DriverManager.getConnection(url, user, pwd);
        Statement st = conn.createStatement();
        
        /* Ricavo le informazioni delle pizze del catalogo */
        String query = "";
        if(ruolo.equals("amministratore"))
            query = "SELECT * FROM PIZZA";
        else
            query = "SELECT * FROM PIZZA WHERE STATO = TRUE";
        
        ResultSet pizze = st.executeQuery(query);
        while(pizze.next()) {
            out.add(pizze.getString("NOME") + ";" + pizze.getString("INGREDIENTI") + ";" + pizze.getString("PREZZO") +
                    ";" + pizze.getString("STATO"));
        }
        pizze.close(); st.close(); conn.close();

        return out;
    }
    
    public ArrayList<String> getOrdini(String username, String ruolo) throws SQLException{    
        ArrayList<String> out = new ArrayList<>();
        
        // Connessione al database
        System.out.println("URL: " + url);
        Connection conn = DriverManager.getConnection(url, user, pwd);
        Statement st = conn.createStatement();
        Statement st2 = conn.createStatement();        
        
        // Ricavo informazioni globali ordine
        String query_ordine = "";
        String query_pizze = "";
        if(ruolo.equals("utente")){
            query_ordine = "SELECT UTENTE.USERNAME, O.ID, O.NUMERO_ORDINE, O.PREZZO, O.STATO, O.DATA_P, O.ORA_P "
                                              + "FROM ORDINE O JOIN UTENTE ON O.IDUTENTE = UTENTE.ID "
                                              + "WHERE UTENTE.USERNAME LIKE '" + username + "' "
                                              + "ORDER BY O.STATO DESC, O.NUMERO_ORDINE DESC";
            
            query_pizze = "SELECT D_O.IDORDINE, P.NOME, D_O.QUANTITA " +
                                            "FROM ORDINE O JOIN DETTAGLIO_ORDINE D_O ON O.ID = D_O.IDORDINE " +
                                            "JOIN PIZZA P ON P.ID = D_O.IDPIZZA " +
                                            "JOIN UTENTE U ON U.ID = O.IDUTENTE " +
                                            "WHERE U.USERNAME LIKE '" + username + "' " +
                                            "ORDER BY D_O.IDORDINE ASC";
        }
        else{
            query_ordine = "SELECT UTENTE.USERNAME, O.ID, O.NUMERO_ORDINE, O.PREZZO, O.STATO, O.DATA_P, O.ORA_P "
                                              + "FROM ORDINE O JOIN UTENTE ON O.IDUTENTE = UTENTE.ID "                                              
                                              + "ORDER BY O.STATO DESC, O.NUMERO_ORDINE DESC";
            
            query_pizze = "SELECT D_O.IDORDINE, P.NOME, D_O.QUANTITA " +
                                            "FROM ORDINE O JOIN DETTAGLIO_ORDINE D_O ON O.ID = D_O.IDORDINE " +
                                            "JOIN PIZZA P ON P.ID = D_O.IDPIZZA " +
                                            "JOIN UTENTE U ON U.ID = O.IDUTENTE " +                                            
                                            "ORDER BY D_O.IDORDINE ASC";
        }
            
        ResultSet ordine_glob = st.executeQuery(query_ordine);
        
        ArrayList<String[]> listOrdineGlob = new ArrayList<>();
        while(ordine_glob.next())
            listOrdineGlob.add(new String[] {ordine_glob.getString("ID"),
                                            ordine_glob.getString("USERNAME"),
                                            ordine_glob.getString("NUMERO_ORDINE"), 
                                            ordine_glob.getString("PREZZO"), 
                                            ordine_glob.getString("STATO"),
                                            ordine_glob.getString("DATA_P"),
                                            ordine_glob.getString("ORA_P")});
            
        // Ricavo dettagli ordine in base al nome utente 
        ResultSet ordini = st2.executeQuery(query_pizze);
        
        ArrayList<String> pizzeList = new ArrayList<>();
        while(ordini.next()) {
            pizzeList.add(ordini.getString("IDORDINE") + ";" + ordini.getString("NOME") +
                        ";" + ordini.getString("QUANTITA"));
        }
        
        //Composizione stringa risultato 
        for(String[] str_ordine : listOrdineGlob) {
            String ordineFinal = str_ordine[0] + ";" + str_ordine[1] + ";" + str_ordine[2] + ";" +
                    str_ordine[3] + ";" + str_ordine[4] + ";" + str_ordine[5] + ";" + str_ordine[6];
            for(String dettOrdine : pizzeList) {
                String[] dettOrdineSplitted = dettOrdine.split(";");
                if(str_ordine[0].equals(dettOrdineSplitted[0])) {
                    ordineFinal += ";" + dettOrdineSplitted[1] +
                            ";" + dettOrdineSplitted[2];
                }
            }
            out.add(ordineFinal);
        }
        
        ordini.close(); st.close(); conn.close();
        return out;
    }
    
    public void insertOrdine(Ordine ordine) throws SQLException {
        
        // Connessione al database 
        Connection conn = DriverManager.getConnection(url, user, pwd);
        Statement st = conn.createStatement();
        ResultSet rs;
        
        //ricavo id username tramite username
        int usernameId = 0;
        rs = st.executeQuery("SELECT U.ID FROM UTENTE U WHERE U.USERNAME = '" + ordine.getUsername() + "'");
        if(rs.next())
            usernameId = rs.getInt("ID");
        
        //numero ordine da ricavare e id ordine da ricavare   
        int numeroOrdine = 0;
        rs = st.executeQuery("SELECT MAX(O.NUMERO_ORDINE) AS O_NUM FROM ORDINE O");
        if(rs.next()) {
            numeroOrdine = rs.getInt("O_NUM");
            //Da incrementare entrambi
            numeroOrdine++;
        }
        
        //Calcolo prezzo totale e set id delle pizze nell'ordine
        double prezzoTotale = 0.0;
        for(Pizze p : ordine.getOrdinePizze()) {
            prezzoTotale += (p.getPrezzo() * p.getQuantita());
            rs = st.executeQuery("SELECT ID FROM PIZZA WHERE NOME LIKE '" + p.getNome() + "'");
            if(rs.next())
                p.setId(rs.getInt("ID"));
            
            System.out.println(p);
        }
        
        st.executeUpdate("INSERT INTO ORDINE (NUMERO_ORDINE, IDUTENTE, PREZZO, DATA_P, ORA_P) "
                        + "VALUES ("+numeroOrdine+", "+usernameId+", "+prezzoTotale+", "+
                          "'"+ordine.getData()+"', '"+ordine.getOra()+"')");
        
        rs = st.executeQuery("SELECT ID FROM ORDINE WHERE NUMERO_ORDINE = " + numeroOrdine);
        int ordineId = 0;
        if(rs.next())
            ordineId = rs.getInt("ID");
        
        for(Pizze p : ordine.getOrdinePizze()) {
            st.executeUpdate("INSERT INTO DETTAGLIO_ORDINE (IDORDINE, IDPIZZA, QUANTITA) "
                           + " VALUES ("+ordineId+", "+p.getId()+", "+p.getQuantita()+")");
        }
        
        rs.close(); st.close(); conn.close();
    }
    
    public void confermaConsegna(int idOrdine) throws SQLException {
        // Connessione al database 
        Connection conn = DriverManager.getConnection(url, user, pwd);
        Statement st = conn.createStatement();
        st.executeUpdate("UPDATE ORDINE SET STATO='CONSEGNATO' WHERE ID = "+idOrdine);
    }
    
    public void cancellaOrdine(int idOrdine) throws SQLException {
        // Connessione al database 
        Connection conn = DriverManager.getConnection(url, user, pwd);
        Statement st = conn.createStatement();
        st.executeUpdate("DELETE FROM ORDINE WHERE ID = "+idOrdine);
    }

    public String getRuolo(String username) throws SQLException {
        Connection conn = DriverManager.getConnection(url, user, pwd);
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery("SELECT RUOLO FROM UTENTE WHERE USERNAME LIKE '" + username + "'");
        if(rs.next())
            return rs.getString("RUOLO");        
        return null;
    }
    
    public void cancellaPizza(String nomePizza) throws SQLException{
        Connection conn = DriverManager.getConnection(url, user, pwd);
        Statement st = conn.createStatement();
        st.executeUpdate("UPDATE PIZZA SET STATO=FALSE WHERE NOME LIKE '" + nomePizza + "'");
    }
    
    public void riattivaPizza(String nomePizza) throws SQLException{
        Connection conn = DriverManager.getConnection(url, user, pwd);
        Statement st = conn.createStatement();
        st.executeUpdate("UPDATE PIZZA SET STATO=TRUE WHERE NOME LIKE '" + nomePizza + "'");
    }
    
    public void inserisciPizza(Pizze p, String tipo) throws SQLException {
        Connection conn = DriverManager.getConnection(url, user, pwd);
        Statement st = conn.createStatement();
        //Controllo sul tipo
        String query = "";
        if(tipo.equals("inserisci")){
            query = "INSERT INTO PIZZA (NOME, INGREDIENTI, PREZZO) "
                    + "VALUES ('" + p.getNome() + "', '" + p.getIngredienti() + "', " + p.getPrezzo() + ")";
        }
        else if(tipo.equals("modifica")){
            query = "UPDATE PIZZA SET NOME = '" + p.getNome() + "', INGREDIENTI = '" + p.getIngredienti() + "', PREZZO = "
                    + p.getPrezzo() + "WHERE NOME LIKE '" + p.getNome() + "'";
        }
        st.executeUpdate(query);
    }
    
    public ArrayList<String> getInfoPizza(String nomePizza) throws SQLException {
        Connection conn = DriverManager.getConnection(url, user, pwd);
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery("SELECT INGREDIENTI, PREZZO FROM PIZZA WHERE NOME LIKE '" + nomePizza + "'");
        ArrayList<String> result = new ArrayList<>();
        if(rs.next()) {
            result.add(rs.getString("INGREDIENTI"));
            result.add(rs.getString("PREZZO"));
        }
        return result;
    }

}