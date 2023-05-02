package e_indeks;


import java.util.Arrays;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Aleksa
 */
public class predmeti {
    private String imePredmeta;
    private int ocena;
    private boolean polozeno;
    private int[] osvojeni_bodovi;
    private String[] kategorije;
    private int[] bodovi;
    private int[] bodovi_minimum;
    private boolean admin = false;

    public predmeti(String imePredmeta, String[] kategorije, int[] bodovi, int[] bodovi_minimum) {
        if(kategorije.length!=bodovi.length || kategorije.length!=bodovi_minimum.length){
            System.out.println("Uneti bodove po jednom za svaku od kategorija");
            throw new IllegalArgumentException();
        }
        if(Arrays.stream(bodovi).sum()!=100){
            System.out.println("Ukupan broj bodova mora biti 100");
            throw new IllegalArgumentException();
        }
        for(int i=0;i<bodovi.length;i++){
            if(bodovi[i]<=bodovi_minimum[i]){
                System.out.println("Minimalan broj bodova mora biti manji od ukupnog");
                throw new IllegalArgumentException();
            }
        }
        //int kat_len = kategorije.length;
        this.imePredmeta = imePredmeta;
        this.kategorije = kategorije;
        this.bodovi = bodovi;
        this.bodovi_minimum = bodovi_minimum;
    }

    public int[] getOsvojeni_bodovi() {
        return osvojeni_bodovi;
    }

    public void setOsvojeni_bodovi(int[] osvojeni_bodovi) {
        for(int i=0;i<bodovi.length;i++){
            if(osvojeni_bodovi[i]>bodovi[i]){
                System.out.println("Osvojeni bodovi moraju biti manji od maksimalnog broja bodova");
                throw new IllegalArgumentException();
            }
        }
        this.osvojeni_bodovi = osvojeni_bodovi;
    }

    public String getImePredmeta() {
        return imePredmeta;
    }

    public int getOcena() {
        return ocena;
    }

    public boolean isPolozeno() {
        return polozeno;
    }

    public String[] getKategorije() {
        return kategorije;
    }

    public int[] getBodovi() {
        return bodovi;
    }

    public int[] getBodovi_minimum() {
        return bodovi_minimum;
    }

    
    
    public void checkIfPolozeno(){
        for(int i=0;i<bodovi.length;i++){
            if(osvojeni_bodovi[i]>=bodovi_minimum[i]){}
            else{
                polozeno = false;
                System.out.println("Nije polozeno");
                ocena = 5;
                return;
            }
        }
        if(Arrays.stream(osvojeni_bodovi).sum()>51){
            polozeno = true;
            System.out.println("Polozeno");
            ocena = (Arrays.stream(osvojeni_bodovi).sum()-1)/10+1;
        }
        else{
            polozeno = false;
            ocena = 5;
            System.out.println("Nije polozeno");
        }
    }
   
}
