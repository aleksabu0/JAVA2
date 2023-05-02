package e_indeks;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
//import java.io.InputStream;
import java.io.InputStreamReader;
//import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import static java.lang.Integer.parseInt;
import java.net.Socket;
import java.net.SocketException;
//import java.net.SocketException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;



public class EIndeksClient implements Runnable {
    boolean logged = false;
    boolean firstLogLine = false;
    //atributi koji se koriste za komunikaciju sa klijentom
    private Socket socket;
    private String username;
    private String password;
    private String admin;
    private BufferedReader br;
    private PrintWriter pw;
    private ArrayList<EIndeksClient> allClients;

    //getters and setters
    public String getUserName() {
        return username;
    }

    public void setUserName(String username) {
        this.username = username;
    }

    //Konstruktor klase, prima kao argument socket kao vezu sa uspostavljenim klijentom
    public EIndeksClient(Socket socket, ArrayList<EIndeksClient> allClients) {
        this.socket = socket;
        this.allClients = allClients;

        try {
            this.br = new BufferedReader(new InputStreamReader(this.socket.getInputStream(), "UTF-8"));
            this.pw = new PrintWriter(new OutputStreamWriter(this.socket.getOutputStream()), true);
            this.username = "";
            this.password = "";            
        } catch (IOException ex) {
            Logger.getLogger(EIndeksClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Metoda prolazi i pravi poruku sa trenutno povezanik korisnicima u formatu
     * Users: ImePrvog ImeDrugog ImeTreceg ... kada se napravi poruka tog
     * formata, ona se salje svim povezanim korisnicima
     */
    void connectedClientsUpdateStatus() {
        String connectedUsers = "Users:";
        for (EIndeksClient c : this.allClients) {
            connectedUsers += " " + c.getUserName();
        }

        System.out.println(connectedUsers);
    }

    @Override
    public void run() {
        student student_korisnik = new student("placeholder","placeholder");
        while (true) {
            try {
                //cekanje na login
                if (this.username.equals("") || this.password.equals("")) {
                    this.username = this.br.readLine();
                    this.password = this.br.readLine();
                    this.admin = this.br.readLine();
                    
                    //admin
                    if(this.admin.equals("admin"))
                    {
                        for(admin admin1:EIndeksServer.admini){
                            if(this.username.equals(admin1.getUsername()) && this.password.equals(admin1.getPassword()))
                            {
                                System.out.println("Uspesan login - Admin");
                                
                                //this.pw.println('\n');
                                this.logged=true;
                                this.firstLogLine=false;
                                break;
                            }
                        }
                        if(logged==false){
                            System.out.println("Pogresan username/password");
                            this.pw.println("FAIL");
                            this.username = "";
                            this.password = "";
                        }
                    }
                    
                    //student
                    else if(this.admin.equals("student"))
                    {
                        for(student student1:EIndeksServer.studenti){
                            if(this.username.equals(student1.getUsername()) && this.password.equals(student1.getPassword()))
                            {
                                System.out.println("Uspesan login - Student");
                                //this.pw.println("STUDENT MODE");
                                //this.pw.println('\n');
                                this.logged=true;
                                this.firstLogLine=false;
                                student_korisnik.setUsername(student1.getUsername());
                                student_korisnik.setPassword(student1.getPassword());
                                student_korisnik.setIme(student1.getIme());
                                student_korisnik.setPrezime(student1.getPrezime());
                                student_korisnik.setJmbg(student1.getJmbg());
                                student_korisnik.setIndeks(student1.getIndeks());
                                student_korisnik.setPredmeti(student1.getPredmeti());
                                break;
                            }
                        }
                        if(this.logged==false){
                            System.out.println("Pogresan username/password");
                            this.pw.println("FAIL");
                            this.username = "";
                            this.password = "";
                        }
                    }
                    else{
                        System.out.println("LOGIN FAIL");
                        this.pw.println("FAIL");
                        break;
                    }
                    
                    //prosao je login uspesno
                    if (this.logged == true) {
                        System.out.println("Povezan korisnik " + this.username);
                    } else {
                        //ako je username null to znaci da je terminiran klijent thread
                        System.out.println("Diskonektovan korisnik: " + this.username);
                        for (EIndeksClient cl : this.allClients) {
                            if (cl.getUserName().equals(this.username)) {
                                this.allClients.remove(cl);
                                break;
                            }
                        }
                        connectedClientsUpdateStatus();
                        break;
                    }
                } 
                // ulogovan korisnik, cekaju se dodatne poruke
                else {  
                    
                    if(this.admin.equals("admin") && this.firstLogLine==false){
                        this.pw.println("ADMIN MODE");
                        firstLogLine=true;
                        //this.pw.
                    }
                    else if(this.admin.equals("student") && this.firstLogLine==false){
                        this.pw.println("STUDENT MODE");
                        this.pw.println(student_korisnik.getIme());
                        this.pw.println(student_korisnik.getPrezime());
                        this.pw.println(student_korisnik.getJmbg());
                        this.pw.println(student_korisnik.getIndeks());
                        firstLogLine=true;
                    }
                    System.out.println("cekam poruku");
                    String line = this.br.readLine();
                    System.out.println(line);
                    //System.out.println("stigla poruka");
                    if (line != null) {
                        /*if(line.equals("Uneti tekst")){
                            System.out.println("Prazna linija");
                        }*/
                        if (line.equals("request_bodovi")){
                            for (predmeti predmet1 : student_korisnik.getPredmeti()) {
                                //System.out.println(predmet1.getImePredmeta());
                                this.pw.println("Ime predmeta: "+predmet1.getImePredmeta());
                                for(int i=0;i<predmet1.getKategorije().length;i++)
                                {
                                    this.pw.println("Ime kategorije: "+predmet1.getKategorije()[i]);
                                    this.pw.println("BO/"+predmet1.getOsvojeni_bodovi()[i]);
                                    this.pw.println("BM/"+predmet1.getBodovi_minimum()[i]);
                                    this.pw.println("BU/"+predmet1.getBodovi()[i]);
                                }
                                this.pw.println("Novi predmet");
                            }
                            this.pw.println("kraj");
                        }
                        
                        if (line.equals("request_predmeti")){
                            for (predmeti predmet1 : student_korisnik.getPredmeti()) {
                                predmet1.checkIfPolozeno();
                                this.pw.println("Ime predmeta: " + predmet1.getImePredmeta());
                                this.pw.println("Ocena: "+predmet1.getOcena());
                                if(predmet1.isPolozeno()==true){
                                    this.pw.println("Stanje: Polozen");
                                }
                                else{
                                    this.pw.println("Stanje: Nije polozen");
                                }
                                this.pw.println("Novi predmet");
                            }
                            this.pw.println("kraj");
                        }
                        
                        if (line.equals("add_student")){
                            try{
                            FileWriter writer = new FileWriter("users.txt",true);  
                            BufferedWriter writer1 = new BufferedWriter(writer);
                            FileWriter writerb = new FileWriter("studenti.txt",true);  
                            BufferedWriter writer2 = new BufferedWriter(writerb);
                            String username = this.br.readLine();
                            String password = this.br.readLine();
                            String ime = this.br.readLine();
                            String prezime  = this.br.readLine();
                            String indeks = this.br.readLine();
                            String jmbg = this.br.readLine();
                            student newStudent = new student(indeks,ime,prezime,jmbg,username,password);
                            EIndeksServer.studenti.add(newStudent);
                            writer1.newLine();
                            writer1.write(username+":"+password+":"+"student");
                            writer1.close();
                            writer2.newLine();
                            writer2.write(ime+":"+prezime+":"+indeks+":"+jmbg);
                            writer2.close();
                            this.pw.println("PASS");
                            }
                            catch(IllegalArgumentException ex){
                                System.out.println("GRESKA");
                                this.pw.println("FAIL");
                            }
                        }
                        
                        if (line.equals("add_admin")){
                            FileWriter writer = new FileWriter("users.txt",true);  
                            BufferedWriter writer1 = new BufferedWriter(writer);
                            String username = this.br.readLine();
                            String password = this.br.readLine();
                            admin newAdmin = new admin(username,password);
                            try {
                            EIndeksServer.admini.add(newAdmin);                          
                            writer1.newLine();
                            writer1.write(username+":"+password+":"+"admin");
                            writer1.close();
                            this.pw.println("PASS");
                            }
                            catch(IllegalArgumentException ex){
                                System.out.println("GRESKA");
                                this.pw.println("FAIL");
                            }
                        }
                        
                        if (line.equals("add_predmet")){
                            try{
                            int i=0;
                            FileWriter writer = new FileWriter("predmeti.txt",true);  
                            BufferedWriter writer1 = new BufferedWriter(writer);
                            String ime = this.br.readLine();
                            int brojKat = parseInt(this.br.readLine());
                            String[] kat = new String[brojKat];
                            int[] bodMin = new int[brojKat];
                            int[] bod = new int[brojKat];                            
                            writer1.newLine();
                            writer1.write(ime);
                            //int sending = parseInt(br.readLine());
                            while(i < brojKat){
                                String kategorija = this.br.readLine();
                                kat[i]=kategorija;
                                String bodovi_min = this.br.readLine();
                                bodMin[i]=parseInt(bodovi_min);
                                String bodovi = this.br.readLine();
                                bod[i]=parseInt(bodovi);
                                
                                writer1.write(":"+kategorija);
                                writer1.write(","+bodovi_min);
                                writer1.write(","+bodovi);
                                //sending = parseInt(br.readLine());
                                i++;
                            }
                            
                            predmeti predmet1=new predmeti(ime,kat,bod,bodMin);
                            int counter1=0;
                            while(counter1<EIndeksServer.sviPredmeti.size()){ 
                                if(predmet1.getImePredmeta().equals(EIndeksServer.sviPredmeti.get(counter1).getImePredmeta())){    
                                    System.out.println("Predmet je vec unet");
                                    throw new IllegalArgumentException();
                                }
                                counter1++;
                            }
                            EIndeksServer.sviPredmeti.add(predmet1);
                            writer1.close();
                            
                            /*for(kategorija ){
                                predmet1.getBodovi();
                            }*/
                            this.pw.println("PASS");
                            }
                            catch(NumberFormatException e){
                                System.out.println("GRESKA");
                                this.pw.println("FAIL");
                            }
                            catch(IllegalArgumentException e){
                                System.out.println("GRESKA");
                                this.pw.println("FAIL");
                            }
                        }
                        
                        if (line.equals("change_student_predmeti")){
                            try{
                            ArrayList<predmeti> predmetiStudenta = new ArrayList<predmeti>();
                            int cnt=0;
                            //int brojPredmeta = parseInt(this.br.readLine());
                            String ime = this.br.readLine();
                            String prezime = this.br.readLine();
                            String imePredmeta =this.br.readLine();
                            int brojKategorija = parseInt(this.br.readLine());
                            for(student student1: EIndeksServer.studenti){
                                cnt++;
                                if(ime.equals(student1.getIme()) && prezime.equals(student1.getPrezime())){
                                    System.out.println("Ime pronadjeno");
                                    System.out.println(ime);
                                    for(predmeti predmetT: EIndeksServer.sviPredmeti){  
                                        if(imePredmeta.equals(predmetT.getImePredmeta()))
                                        {
                                            System.out.println(imePredmeta);
                                            System.out.println("Predmet pronadjen");
                                            predmeti predmetStudentaT = new predmeti(predmetT.getImePredmeta(),predmetT.getKategorije(),predmetT.getBodovi(),predmetT.getBodovi_minimum());
                                            int [] osvBodovi = new int[brojKategorija];

                                            for(int j=0;j<brojKategorija;j++){
                                                osvBodovi[j]=parseInt(this.br.readLine());
                                            }

                                            predmetStudentaT.setOsvojeni_bodovi(osvBodovi);
                                            int counter1=0;
                                            if(student1.getPredmeti()!=null){
                                                while(counter1<student1.getPredmeti().size()){ 
                                                    if(predmetStudentaT.getImePredmeta().equals(student1.getPredmeti().get(counter1).getImePredmeta())){    
                                                        System.out.println("Predmet je vec unet");
                                                        throw new IllegalArgumentException();
                                                    }
                                                    counter1++;
                                                }
                                                EIndeksServer.studenti.get(cnt-1).getPredmeti().add(predmetStudentaT);
                                            }
                                            else{
                                                ArrayList<predmeti> predmetiStudentaT = new ArrayList<predmeti>();
                                                predmetiStudentaT.add(predmetStudentaT);
                                                EIndeksServer.studenti.get(cnt-1).setPredmeti(predmetiStudentaT);
                                            }
                                            break;
                                        }                                            
                                    }
                                }
                            }
                            //EIndeksServer.studenti.get(cnt-1).getPredmeti().add(predmetStudentaT);
                            //System.out.println(EIndeksServer.studenti.get(cnt-1).getPredmeti().get(0).getImePredmeta());
                            FileWriter writer = new FileWriter("studenti.txt");  
                            BufferedWriter writer1 = new BufferedWriter(writer);
                            //upisivanje u tekst fajl
                            //System.out.println("SIZE");
                            //System.out.println(EIndeksServer.studenti.size());
                            for(student studentT:EIndeksServer.studenti)
                            {
                                System.out.println("ISPIS PONOVO");
                                int j;
                                String ime1 = studentT.getIme();
                                String prezime1  = studentT.getPrezime();
                                String indeks1 = studentT.getIndeks();
                                String jmbg1 = studentT.getJmbg();           
                                //System.out.println("");
                                writer1.write(ime1+":"+prezime1+":"+indeks1+":"+jmbg1);
                                if(studentT.getPredmeti()!=null){
                                    for(predmeti predmetT:studentT.getPredmeti()){
                                        String imePredmetaT = predmetT.getImePredmeta();
                                        writer1.write(":"+imePredmetaT+",");
                                        for(j=0;j<predmetT.getKategorije().length;j++){
                                            int osvBod=predmetT.getOsvojeni_bodovi()[j];
                                            System.out.println(osvBod);
                                            if(j==0){
                                                writer1.write(""+osvBod);
                                            }
                                            else{
                                                writer1.write("-"+osvBod);
                                            }
                                        }
                                    }
                                }
                                writer1.newLine();
                            }
                            writer1.close();
                            this.pw.println("PASS");
                            }
                            catch(IllegalArgumentException a){
                                System.out.println("GRESKA");
                                this.pw.println("FAIL");
                            }
                        }
                    }
                    else{
                        //slicno kao gore, ako je line null, klijent se diskonektovao
                        //ukloni tog korisnika iz liste povezanih korisnika u chat room-u
                        //i obavesti ostale da je korisnik napustio sobu
                        System.out.println("Disconnected user: " + this.username);

                        //Ovako se uklanja element iz kolekcije 
                        //ne moze se prolaziti kroz kolekciju sa foreach a onda u 
                        //telu petlje uklanjati element iz te iste kolekcije
                        Iterator<EIndeksClient> it = this.allClients.iterator();
                        while (it.hasNext()) {
                            if (it.next().getUserName().equals(this.username)) {
                                it.remove();
                            }
                        }
                        connectedClientsUpdateStatus();

                        this.socket.close();
                        break;
                    }
                }
            } catch (IOException ex) {
                System.out.println("Disconnected user: " + this.username);
                //npr, ovakvo uklanjanje moze dovesti do izuzetka, pogledajte kako je 
                //to gore uradjeno sa iteratorom
                for (EIndeksClient cl : this.allClients) {
                    if (cl.getUserName().equals(this.username)) {
                        this.allClients.remove(cl);
                        connectedClientsUpdateStatus();
                        return;
                    }
                }

            }

        }
    }

}
