package e_indeks;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import static java.lang.Integer.parseInt;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;



public class EIndeksServer {
    
    public static ArrayList<student> studenti;
    public static ArrayList<admin> admini;
    public static ArrayList<predmeti> sviPredmeti;

    private ServerSocket ssocket;
    private int port;
    private ArrayList<EIndeksClient> clients;

    public ServerSocket getSsocket() {
        return ssocket;
    }

    public void setSsocket(ServerSocket ssocket) {
        this.ssocket = ssocket;
    }
    


    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    /**
     * Prihvata u petlji klijente i za svakog novog klijenta kreira novu nit. Iz
     * petlje se moze izaci tako sto se na tastaturi otkuca Exit.
     */
    public void acceptClients() {
        Socket client = null;
        Thread thr;
        while (true) {
            try {
                System.out.println("Waiting for new clients..");
                client = this.ssocket.accept();
            } catch (IOException ex) {
                Logger.getLogger(EIndeksServer.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (client != null) {
                //Povezao se novi klijent, kreiraj objekat klase ConnectedChatRoomClient
                //koji ce biti zaduzen za komunikaciju sa njim
                EIndeksClient clnt = new EIndeksClient(client, clients);
                //i dodaj ga na listu povezanih klijenata jer ce ti trebati kasnije
                clients.add(clnt);
                //kreiraj novu nit (konstruktoru prosledi klasu koja implementira Runnable interfejs)
                thr = new Thread(clnt);
                //..i startuj ga
                thr.start();
            } else {
                break;
            }
        }
    }

    public EIndeksServer(int port) {
        this.clients = new ArrayList<>();
        try {
            this.port = port;
            this.ssocket = new ServerSocket(port);
        } catch (IOException ex) {
            Logger.getLogger(EIndeksServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    

    public static void main(String[] args) throws FileNotFoundException, IOException {
        
        // UCITAVANJE KORISNIKA NA POCETKU
        BufferedReader reader = new BufferedReader(new FileReader("users.txt"));
        BufferedReader readerS = new BufferedReader(new FileReader("studenti.txt"));
        BufferedReader readerP = new BufferedReader(new FileReader("predmeti.txt"));
        studenti = new ArrayList<student>();
        admini = new ArrayList<admin>();
        sviPredmeti = new ArrayList<predmeti>();
        
        //prvo ucitati sve predmete radi poredjenja kasnije
        String predmet;
        while ((predmet = readerP.readLine()) != null) {
            if(!predmet.equals("")){
                int count =0;

                for(int i=0;i<predmet.length();i++){
                    if(predmet.charAt(i)==':'){
                        count++;
                    }
                }
                String[] kat = new String[count];
                int[] bodMin = new int[count];
                int[] bod = new int[count];

                String[] predmetPodaci = predmet.split(":", 0);
                String imePredmeta = predmetPodaci[0];

                for(int i=1;i<=count;i++){
                    String[] katPodaci = predmetPodaci[i].split(",",0);
                    kat[i-1]=katPodaci[0];
                    bodMin[i-1]=parseInt(katPodaci[1]);
                    bod[i-1]=parseInt(katPodaci[2]);
                    //System.out.println(katPodaci[0]);
                    //System.out.println(katPodaci[1]);
                    //System.out.println(bod[i-1]);
                }

                predmeti predmet1 = new predmeti(imePredmeta,kat,bod,bodMin);
                sviPredmeti.add(predmet1);
            }
        }
        //System.out.println(sviPredmeti.get(1).getBodovi()[2]);
        //ucitavanje korisnika - admin/student
        String korisnik;
        while ((korisnik = reader.readLine()) != null) {
            if(!korisnik.equals("")){
                String[] korisnikPodaci = korisnik.split(":", 0); //,0
                String username = korisnikPodaci[0];
                //System.out.println(username);
                String password = korisnikPodaci[1];
                String role = korisnikPodaci[2];
                if(role.equals("admin")){
                    //System.out.println("ADMIN OCITAN");
                    admin admin1 = new admin(username, password);
                    admini.add(admin1);
                }
                else if(role.equals("student")){
                    //System.out.println("STUDENT OCITAN");
                    String student = readerS.readLine();
                    while(student.equals(""))
                    {
                        student = readerS.readLine();
                    }    
                    int count =0;
                    int countPlus=0;
                    int countPlusPredmet=0;

                    for(int i=0;i<student.length();i++){
                        if(student.charAt(i)==':'){
                            count++;
                        }
                        if(student.charAt(i)=='-'){
                                countPlus++;
                        }
                    }
                    ArrayList<predmeti> predmetiStudenta = new ArrayList<predmeti>();
                    student student1 = new student(username, password);               

                    String[] studentPodaci = student.split(":", 0);
                    String ime = studentPodaci[0];
                    System.out.println(ime);
                    student1.setIme(ime);
                    String prezime  = studentPodaci[1];
                    student1.setPrezime(prezime);

                    String indeks = studentPodaci[2];
                    try{
                        student1.setIndeks(indeks);
                    }
                    catch(IllegalArgumentException a){
                        System.out.println("GRESKA");
                    }

                    String jmbg = studentPodaci[3];
                    try{
                        student1.setJmbg(jmbg);
                    }
                    catch(IllegalArgumentException a){
                        System.out.println("GRESKA");
                    }
                    //System.out.println(count);
                    /*System.out.println(student1.getIme());
                    System.out.println(student1.getPrezime());
                    System.out.println(student1.getIndeks());
                    System.out.println(student1.getJmbg());*/
                    studenti.add(student1);
                    System.out.println(studenti.size());
                    if(count>=4){
                        System.out.println("Ima predmete");
                        for(int i=4;i<=count;i++){
                            int osvBodovi[] = new int[countPlus+1];
                            String[] predStudPodaci =studentPodaci[i].split(",",0);
                            String predIme = predStudPodaci[0];
                            //System.out.println(predIme);
                            countPlusPredmet=0;
                            for(int z=0;z<predStudPodaci[1].length();z++){
                                if(predStudPodaci[1].charAt(z)=='-'){
                                    countPlusPredmet++;
                                }
                            }
                            //System.out.println(countPlusPredmet);
                            String[] sviBodovi=predStudPodaci[1].split("-",0);

                            for(int g=0;g<=countPlusPredmet;g++){
                               osvBodovi[g] = parseInt(sviBodovi[g]); 
                            }

                            for(predmeti predmetT : sviPredmeti)
                            {
                                //System.out.println(sviPredmeti.get(0).getBodovi()[0]);
                                if(predmetT.getImePredmeta().equals(predIme)){
                                    //System.out.println(predmetT.getImePredmeta());
                                    //System.out.println(predmetT.getBodovi()[0]);
                                    //System.out.println(predmetT.getBodovi()[1]);

                                    predmeti predmetStudentaT = new predmeti(predmetT.getImePredmeta(),predmetT.getKategorije(),predmetT.getBodovi(),predmetT.getBodovi_minimum());
                                    predmetStudentaT.setOsvojeni_bodovi(osvBodovi);
                                    //System.out.println(predmetStudentaT.getOsvojeni_bodovi()[0]);
                                    predmetiStudenta.add(predmetStudentaT);
                                }
                                /*else
                                {
                                    System.out.println("NEMA TAKVOG PREDMETA");
                                }*/
                            }
                        }
                        student1.setPredmeti(predmetiStudenta);
                    }
                        /*if(studenti.size()>1){
                            System.out.println(studenti.get(1).getPredmeti().get(0).getOsvojeni_bodovi()[0]);
                        }*/
                }
                else{
                    System.out.println("LOS UNOS");
                    break;
                }
            }
        }
        reader.close();
        //System.out.println(sviPredmeti.get(0).getImePredmeta());
        //System.out.println(studenti.get(0).getPredmeti().get(1).getBodovi()[2]);
        // KRAJ UCITAVANJA KORISNIKA
        EIndeksServer server = new EIndeksServer(6001);

        System.out.println("Server pokrenut, slusam na portu 6001");

        //Prihvataj klijente u beskonacnoj petlji
        server.acceptClients();

    }

}
