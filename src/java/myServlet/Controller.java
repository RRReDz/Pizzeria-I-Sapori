/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myServlet;

import myBeans.DBConnectionBean;
import com.google.gson.Gson; // Import JSON (GitHub)
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import myClasses.Ordine;
import myClasses.Pizze;

/**
 *
 * @author Riccardo Rossi
 */
public class Controller extends HttpServlet {

    private static String url = "";
    private static String user = "";
    private static String pwd = "";
    private static DBConnectionBean conn;

    /* ---------------- Metodi per l'accesso al database ------------------ */
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        //Get parametri configurazione da servlet
        url = config.getInitParameter("DBurl");
        user = config.getInitParameter("DBuser");
        pwd = config.getInitParameter("DBpwd");

        try {
            DriverManager.registerDriver(new org.apache.derby.jdbc.ClientDriver());
        } catch (SQLException e) {
            throw new ServletException(e);
        }

        //Set JavaBean
        conn = new DBConnectionBean();
        conn.setUrl(url);
        conn.setUser(user);
        conn.setPwd(pwd);
    }

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();
        String username = request.getParameter("user");
        String password = request.getParameter("psw");
        String action = request.getParameter("action");
        HttpSession s = request.getSession();

        /* Debug controller */
        System.out.println("---------- DEBUG PARAMETRI----------");
        System.out.println("- USERNAME: " + username);
        System.out.println("- PASSWORD: " + password);
        System.out.println("- ACTION: " + action);
        System.out.println("------------------------------------");

        System.out.println("---------- DEBUG SESSIONE----------");
        System.out.println("- SESSIONE NUOVA?: " + s.isNew());
        System.out.println("- USER IN SESSIONE: " + s.getAttribute("username"));
        System.out.println("- ID SESSIONE: " + s.getId());
        System.out.println("- DATA CREAZIONE: " + new Date(s.getCreationTime()));
        System.out.println("- MAX INACTIVE TIME INTERVAL (SECONDS): " + s.getMaxInactiveInterval());
        System.out.println("------------------------------------");

        RequestDispatcher rd;

        try {
            //----------------------------------- CASO LOGIN --------------------------------
            if (action.equals("login")) {
                //Fase di autenticazione semplice
                if (conn.getCheckLogin(username, password)) {
                    s.setAttribute("username", username);
                    s.setAttribute("connbean", conn);
                    s.setAttribute("ruolo", conn.getRuolo(username));
                    rd = getServletContext().getRequestDispatcher("/autenticated.jsp");
                    rd.forward(request, response);
                } else {
                    s.invalidate();
                    rd = getServletContext().getRequestDispatcher("/index.jsp?login=error");
                    rd.forward(request, response);
                }
            } else if (action.equals("home")) {
                rd = getServletContext().getRequestDispatcher("/autenticated.jsp");
                rd.forward(request, response);
            } 
            else if (action.equals("catalogo")) {
                // Creazione oggetto Gson (JSON) e conversione ArrayList 
                String json = new Gson().toJson(conn.getCatalogo("utente"));
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write(json);
            } else if (action.equals("prenotazione")) {
                rd = getServletContext().getRequestDispatcher("/prenotazione.jsp");
                rd.forward(request, response);
            } else if (action.equals("visualizza_ordine")) {
                rd = getServletContext().getRequestDispatcher("/ordine.jsp");
                rd.forward(request, response);
            } else if (action.equals("conferma_ordine")) {
                String[] nome = request.getParameterValues("nome");
                String[] prezzo = request.getParameterValues("prezzo");
                String[] quantita = request.getParameterValues("num_pizza");
                String dataora = request.getParameter("dataora");
                ArrayList<Pizze> ordinePizze = new ArrayList<>();
                
                for (int i = 0; i < nome.length; i++) {
                    if(Integer.parseInt(quantita[i]) > 0) {
                        Pizze pizze = new Pizze(nome[i], Double.parseDouble(prezzo[i]), Integer.parseInt(quantita[i]));
                        ordinePizze.add(pizze);
                    }
                }
                
                String[] dataora_splitted = dataora.split("T");
                
                Ordine ordine = new Ordine((String)s.getAttribute("username"), dataora_splitted[0], dataora_splitted[1], ordinePizze);
                conn.insertOrdine(ordine);
                rd = getServletContext().getRequestDispatcher("/ordine.jsp");
                rd.forward(request, response);
                
            } else if (action.equals("conferma_consegna")) {
                int idOrdine = Integer.parseInt(request.getParameter("id_ordine"));
                conn.confermaConsegna(idOrdine);
                rd = getServletContext().getRequestDispatcher("/ordine.jsp");
                rd.forward(request, response);
                
            } else if (action.equals("cancella_ordine")) {
                int idOrdine = Integer.parseInt(request.getParameter("id_ordine"));
                conn.cancellaOrdine(idOrdine);
                rd = getServletContext().getRequestDispatcher("/ordine.jsp");
                rd.forward(request, response);
            } else if (action.equals("modifica_catalogo")){
                rd = getServletContext().getRequestDispatcher("/catalogoAdmin.jsp");
                rd.forward(request, response);
            } else if (action.equals("modifica_pizza")){
                rd = getServletContext().getRequestDispatcher("/modifica.jsp");
                rd.forward(request, response);                
            } else if (action.equals("modifica_ajax")){
                String nome = request.getParameter("pizza");
                // Creazione oggetto Gson (JSON) e conversione ArrayList 
                String json = new Gson().toJson(conn.getInfoPizza(nome));
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write(json);
            } else if (action.equals("cancella_pizza")){
                conn.cancellaPizza(request.getParameter("nome_pizza"));
                rd = getServletContext().getRequestDispatcher("/catalogoAdmin.jsp");
                rd.forward(request, response);
            } else if(action.equals("riattiva_pizza")){
                conn.riattivaPizza(request.getParameter("nome_pizza"));
                rd = getServletContext().getRequestDispatcher("/catalogoAdmin.jsp");
                rd.forward(request, response);
            } else if(action.equals("inserisci_pizza")){
                String nome = request.getParameter("nome");
                String[] ingredienti = request.getParameterValues("ingredienti");
                String prezzo = request.getParameter("prezzo");
                String tipo = request.getParameter("tipo");
                String ing = "";
                for(String a : ingredienti)
                    ing += (a + ", "); 
                ing = ing.substring(0, ing.length()-2);
                Pizze p = new Pizze(nome, Double.parseDouble(prezzo), ing);
                conn.inserisciPizza(p, tipo);
                rd = getServletContext().getRequestDispatcher("/catalogoAdmin.jsp");
                rd.forward(request, response);
            } else if (action.equals("esci")) {
                s.invalidate();
                rd = getServletContext().getRequestDispatcher("/index.jsp");
                rd.forward(request, response);
            } else if (action.equals("kivylogin")) {
                out.write("" + conn.getCheckLogin(username, password));
            } else if (action.equals("kivy_ordini")) {
                String json = new Gson().toJson(conn.getOrdini(username, conn.getRuolo(username)));
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write(json);
            } else if(action.equals("kivy_cancella_ordine")) {
                conn.cancellaOrdine(Integer.parseInt(request.getParameter("idordine")));
            } else if(action.equals("kivy_conferma_ordine")) {
                conn.confermaConsegna(Integer.parseInt(request.getParameter("idordine")));
            } else if(action.equals("kivy_inserisci_ordine")) {
                String[] nome = request.getParameterValues("nome");
                String[] prezzo = request.getParameterValues("prezzo");
                String[] quantita = request.getParameterValues("num_pizza");
                String data = request.getParameter("data");
                String ora = request.getParameter("ora");
                
                ArrayList<Pizze> ordinePizze = new ArrayList<>();
                for (int i = 0; i < nome.length; i++) {
                    if(Integer.parseInt(quantita[i]) > 0) {
                        Pizze pizze = new Pizze(nome[i], Double.parseDouble(prezzo[i]), Integer.parseInt(quantita[i]));
                        ordinePizze.add(pizze);
                    }
                }
                
                Ordine ordine = new Ordine(username, data, ora, ordinePizze);
                conn.insertOrdine(ordine);
            } 
            

        } catch (SQLException sqle) {
            throw new ServletException(sqle);
        } finally {
            out.close();
        }

    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
