/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package e_indeks_client;

import java.io.BufferedReader;
import java.io.IOException;
//import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

//import javax.swing.JComboBox;
//import javax.swing.JOptionPane;
/**
 * Ova klasa se koristi za prijem poruka od strane servera jer ce one stizati
 * asinhrono (ne znamo u kom trenutku ce se novi korisnik ukljuciti u Chat room,
 * kao ni kada ce nam poslati poruku)
 *
 */
/*
public class ReceiveMessageFromServer implements Runnable {

    EIndeksClientSide parent;
    BufferedReader br;

    public ReceiveMessageFromServer(EIndeksClientSide parent) {
        //parent ce nam trebati da bismo mogli iz ovog thread-a da menjamo sadrzaj 
        //komponenti u osnovnom GUI prozoru (npr da popunjavamo Combo Box sa listom
        //korisnika
        this.parent = parent;
        //BufferedReader koristimo za prijem poruka od servera, posto su sve
        //poruke u formi Stringa i linija teksta, BufferedReader je zgodniji nego
        //da citamo poruke iz InputStream objekta
        this.br = parent.getBr();
    }

    @Override
    public void run() {
        while (true) {
            String line;
            try {
                line = this.br.readLine();

                if (line.startsWith("Users: ")) {


                    String[] imena = line.split(":")[1].split(" ");

                    parent.getCbUsers().removeAllItems();

                    for (String ime : imena) {
                        if (!ime.equals("")) {
                            parent.getCbUsers().addItem(ime.trim());
                        }
                    }
                    parent.setTaReceivedMessages("Novi clan se prikljucio ili je postojeci napustio sobu! Tretnutni clanovi su: " + line.split(": ")[1].toString());

                } else {
                    //prikazi poruku koja je primljena u polju za prijem poruka
                    parent.setTaReceivedMessages(line);

                }
            } catch (IOException ex) {
                Logger.getLogger(ReceiveMessageFromServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}*/
