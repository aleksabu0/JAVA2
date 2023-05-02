package e_indeks;


import java.util.ArrayList;
import java.util.regex.Pattern;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Aleksa
 */
public class student{
    private String indeks;
    private String ime;
    private String prezime;
    private String jmbg;
    private ArrayList<predmeti> predmeti;
    private String username;
    private String password;
    private boolean admin ;

    public student(String username, String password) {
        this.username = username;
        this.password = password;
        this.admin = false;
    }

    public student(String indeks, String ime, String prezime, String jmbg, String username, String password) {
        if(!isIndeksValid(indeks)){
            System.out.println("Indeks mora biti u formatu E/e - 1/2/3");
            throw new IllegalArgumentException();
        }
        if(!isJmbgValid(jmbg)){
            System.out.println("Los format za JMBG");
            throw new IllegalArgumentException();
        }
        this.indeks = indeks;
        this.ime = ime;
        this.prezime = prezime;
        this.jmbg = jmbg;
        this.username = username;
        this.password = password;
    }

    public String getIndeks() {
        return indeks;
    }

    public void setIndeks(String indeks) {
        if(!isIndeksValid(indeks)){
            System.out.println("Indeks mora biti u formatu E/e - 1/2/3 - 2000-2023");
            throw new IllegalArgumentException();
        }
        this.indeks = indeks;
    }

    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public String getPrezime() {
        return prezime;
    }

    public void setPrezime(String prezime) {
        this.prezime = prezime;
    }

    public String getJmbg() {
        return jmbg;
    }

    public void setJmbg(String jmbg) {
        if(!isJmbgValid(jmbg)){
            System.out.println("Los format za JMBG");
            throw new IllegalArgumentException();
        }
        this.jmbg = jmbg;
    }
    

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public ArrayList<predmeti> getPredmeti() {
        return predmeti;
    }

    public void setPredmeti(ArrayList<predmeti> predmeti) {
        this.predmeti = predmeti;
    }

    private boolean isIndeksValid(String indeks) {
        System.out.println(indeks);
        //return Pattern.matches("[0-9]", indeks);
        return Pattern.matches("[eE][1-3][\\-\\/]([2][0]([0-1][0-9]|[2][0-3]))", indeks);
    }

    private boolean isJmbgValid(String jmbg) {
        return Pattern.matches("([0][1-9]|[12][0-9]|3[01])([0][1-9]|[1][012])([0-9][0-9][0-9][0-9][0-9][0-9])([9][5-9][0-9]|[0][0][0-9])", jmbg);
    }
}
