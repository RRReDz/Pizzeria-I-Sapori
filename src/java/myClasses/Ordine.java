/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myClasses;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author Riccardo Rossi
 */
public class Ordine {
    private String username, data, ora;
    private ArrayList<Pizze> ordinePizze;

    public Ordine(String username, String data, String ora, ArrayList<Pizze> ordinePizze) {
        this.username = username;
        this.data = data;
        this.ora = ora;
        this.ordinePizze = ordinePizze;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getOra() {
        return ora;
    }

    public void setOra(String ora) {
        this.ora = ora;
    }

    public ArrayList<Pizze> getOrdinePizze() {
        return ordinePizze;
    }

    public void setOrdinePizze(ArrayList<Pizze> ordinePizze) {
        this.ordinePizze = ordinePizze;
    }

    @Override
    public String toString() {
        return "Ordine{" + "username=" + username + ", data=" + data + ", ora=" + ora + ", ordinePizze=" + ordinePizze + '}';
    }
      
}
