package mapa;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import Vozila.Automobil;
import Vozila.Kamion;
import Vozila.Vozilo;
import stanice.Stanica;
import vozovi.Kompozicija;
import vozovi.Lokomotiva;
import vozovi.PokretniObjekat;
import vozovi.PutnickiVagon;
import vozovi.TeretniVagon;
import vozovi.Vagon;
import vozovi.VagonPosebni;

public class Simulacija extends Thread {

	public static HashMap<String,Stanica> stanice;
	public static Mapa mapa;
	//public static ArrayList<Kompozicija> kompozicije; //Ovo je samo za testiranje
	public static ArrayList<Vozilo> vozila; // Ovo mi sluzi da znam ukupan broj vozila pustenih
	private int brojAuta1,brojAuta2,brojAuta3,brzina1,brzina2,brzina3;
	public Tacka pocetak1,pocetak2,pocetak3,pocetak4,pocetak5,pocetak6,kraj1,kraj2,kraj3,kraj4,kraj5,kraj6;
	int brojPokrenutih1=0,brojPokrenutih2=0,brojPokrenutih3=0;
	public static boolean config=false;
	public static boolean voz=false;
	private ArrayList<File>pusteneKompozicije;
	public static HashMap<Integer,Kompozicija> kompozicije;
	public static HashMap<Integer,Vozilo> kola;
	private final static String SEPARATOR = File.separator;
	
	public Simulacija() {
		
		
		vozila = new ArrayList<Vozilo>();
		pusteneKompozicije = new ArrayList<File>();
		kompozicije = new HashMap<Integer,Kompozicija>();
		kola = new HashMap<Integer,Vozilo>();
		
		//Napravimo stanice
		Stanica a = new Stanica("A");
		Stanica b = new Stanica("B");
		Stanica c = new Stanica("C");
		Stanica d = new Stanica("D");
		Stanica e = new Stanica("E");
		
		//Odredimo im pocetne
		a.odrediPocetne("A");
		b.odrediPocetne("B");
		c.odrediPocetne("C");
		d.odrediPocetne("D");
		e.odrediPocetne("E");
		
		//Na pocetku simulacije stavimo da su sve dionice slobodne
		Stanica.dionice.put("AB", false);
		Stanica.dionice.put("BA", false);
		Stanica.dionice.put("BC", false);
		Stanica.dionice.put("CB", false);
		Stanica.dionice.put("CD", false);
		Stanica.dionice.put("DC", false);
		Stanica.dionice.put("CE", false);
		Stanica.dionice.put("EC", false);
		
		//Definisemo pocetne i krajnje tacke za auta
		pocetak1 = new Tacka(8,0);
		pocetak2 = new Tacka(0,8);
		pocetak3 = new Tacka(14,0);
		pocetak4 = new Tacka(13,29);
		pocetak5 = new Tacka(22,0);
		pocetak6 = new Tacka(29,9);
		
		kraj1 = new Tacka(0,9);
		kraj2 = new Tacka(7,0);
		kraj3 = new Tacka(14,29);
		kraj4 = new Tacka(13,0);
		kraj5 = new Tacka(29,8);
		kraj6 = new Tacka(21,0);
		
		
		mapa = new Mapa();
		stanice = new HashMap<String,Stanica>();
		stanice.put("A", a);
		stanice.put("B", b);
		stanice.put("C", c);
		stanice.put("D", d);
		stanice.put("E", e);
		
		
	}
	
	
	public void run() {
		
		//Ovde mi treba noveVrijednosti da inicijalizujem pocetnu vrijednost
		noveVrijednosti();
		pokreniKompozicije();//Ovako cemo pokrenuti pocetne kompozicije a kasnije samo kako se dodaju dodatne dodavati
		
		while(true) {
			pokreni(); //Ovako pokrecemo vozila, provjereno je
			kompozicije(); //Ovako vozove kad se vidi promjena
			provjeriConfig(); //Ako se primjeti promjena ubaci nove vrijednosti
		}
		
	}
	
	public void provjeriConfig() {
		if (config==true) {
			noveVrijednosti();
			config=false;
		}
	}
	
	public void pokreniKompozicije() { 
		//Ovde cemo napraviti da se pokrenu kompozicije koje postoje vec pri pokretanju
		File folder = new File(System.getProperty("user.dir")+ SEPARATOR +"kompozicije");
		File[] listOfFiles = folder.listFiles();

		for (File file : listOfFiles) {
		    if (file.isFile()) {
		        kreirajKomp(file.getName());
		    }
		}
	}
	
	public void kreirajKomp(String komp) {
		
		try {
		//Ovde kreiramo kompoziciju iz jednog fajla
		String path = System.getProperty("user.dir")+ SEPARATOR +"kompozicije"+ SEPARATOR +komp;
		File file = new File(path);
		String tipLokomotive=null,tipVagona=null,brzina=null;
		String tmp[];
		ArrayList<String> putanja = new ArrayList<String>();
		ArrayList<String> raspored = new ArrayList<String>();
		ArrayList<PokretniObjekat> kompozicija = new ArrayList<PokretniObjekat>();
		if (file.isFile()) {
		Scanner myReader = new Scanner(file);
		
		if (myReader.hasNext()) {
		tipLokomotive = myReader.nextLine();
		}
		
		if (myReader.hasNext()) {
		tipVagona = myReader.nextLine();
		}
		
		if (myReader.hasNext()) {
		tmp = myReader.nextLine().split("-");
		for (int k = 0 ; k<tmp.length; k++)
			putanja.add(tmp[k]);
		}
		
		if (myReader.hasNext()) {
		tmp = myReader.nextLine().split("-");
		for (int j = 0 ; j<tmp.length; j++)
			raspored.add(tmp[j]);
		}	
		
		if (myReader.hasNext()) {
		brzina = myReader.nextLine();
		myReader.close();
		}
		
	  if (tipLokomotive != null && tipVagona != null && putanja.size()>0 && raspored.size()>0 && brzina!=null) {
		
		for (int i = 0 ; i<raspored.size() ; i++) {
			
			if (raspored.get(i).equals("L")) {
				kompozicija.add(new Lokomotiva(tipLokomotive));
			}
			
			else if (raspored.get(i).equals("V")) {
				if (tipVagona.equals("P")) {
					kompozicija.add(new PutnickiVagon());
					
				}
				if (tipVagona.equals("T")) {
					kompozicija.add(new TeretniVagon());
				}
				if (tipVagona.equals("U")) {
					kompozicija.add(new VagonPosebni());
				}
			}
		}
		
		Kompozicija vozic = new Kompozicija (kompozicija,Integer.parseInt(brzina) ,putanja);
		vozic.start();
		pusteneKompozicije.add(file);
		voz=false;
	  }
	  else {
		  voz=true;
	  }
		}
		 } catch (FileNotFoundException e) {
			 Logger.getLogger(Simulacija.class.getName()).log(Level.SEVERE,e.toString());
		    }
	}
	
    public void kompozicije() {
	  //Ovde cemo gledati promjenu fajla i pokusati da pokrenemo kompoziciju
		if (voz==true) {
			File folder = new File(System.getProperty("user.dir")+ SEPARATOR +"kompozicije");
			File[] listOfFiles = folder.listFiles();

			for (File file : listOfFiles) {
			    if (file.isFile()) {
			    	if (pusteneKompozicije.contains(file)==false) {
			        kreirajKomp(file.getName());
			    	}
			    }
			}
		}
	}
	
	
	public void pokreni() {
		//Pokrenucemo prvo auta
		while(true) {
			
			
			Random random = new Random();	
			switch(random.nextInt(6)) {
			
			case 0:
			if (brojPokrenutih1<brojAuta1) {
			vozila.add(new Kamion(pocetak1,kraj1,brzina1));
			vozila.get(vozila.size()-1).start();
			brojPokrenutih1++;
			}
			break;
			
			case 1:
			if (brojPokrenutih1<brojAuta1) {
			vozila.add(new Automobil(pocetak2,kraj2,brzina1));
			vozila.get(vozila.size()-1).start();
			brojPokrenutih1++;
			}
			break;
			
			case 2:
			if (brojPokrenutih2<brojAuta2) {
			vozila.add(new Kamion(pocetak3,kraj3,brzina2));
			vozila.get(vozila.size()-1).start();
			brojPokrenutih2++;
			}
			break;
			
			case 3:
			if (brojPokrenutih2<brojAuta2) {
			vozila.add(new Automobil(pocetak4,kraj4,brzina2));
			vozila.get(vozila.size()-1).start();
			brojPokrenutih2++;
			}
			break;
			
			case 4:
			if (brojPokrenutih3<brojAuta3) {
			vozila.add(new Kamion(pocetak5,kraj5,brzina3));
			vozila.get(vozila.size()-1).start();
			brojPokrenutih3++;
			}
			break;
			
			case 5:
			if (brojPokrenutih3<brojAuta3) {
			vozila.add(new Automobil(pocetak6,kraj6,brzina3));
			vozila.get(vozila.size()-1).start();
			brojPokrenutih3++;
			}
			break;
					
			}
			
			try {
				Thread.sleep(2000);
				
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				Logger.getLogger(Simulacija.class.getName()).log(Level.SEVERE,e.toString());
			}	
			
			break;
			
		}
	}
	
	
	//Ovde cemo izvuci nove vrijednosti iz config fajla
	public void noveVrijednosti() {
		 try {
		
		      File myObj = new File("config.txt");
		      Scanner myReader = new Scanner(myObj);
		      //Ovako cemo pokusati izvuci brzine
		     
		      this.brzina1=myReader.nextInt();
		      this.brzina2=myReader.nextInt();
		      this.brzina3=myReader.nextInt();
		      
		      //Sada cemo pokusati izvuci broj auta
		      int tmp1=myReader.nextInt();
		      int tmp2=myReader.nextInt();
		      int tmp3=myReader.nextInt();
		      
		      //Sada vidimo jesu li veci pa ako jesi to napravimo novom vrijednosti
		      if (tmp1>this.brojAuta1)
		    	  this.brojAuta1=tmp1;
		      if(tmp2>this.brojAuta2)
		    	  this.brojAuta2=tmp2;
		      if(tmp3>this.brojAuta3)
		    	  this.brojAuta3=tmp3;
		      
		      
		      myReader.close();
		    } catch (FileNotFoundException e) {
		    	Logger.getLogger(Simulacija.class.getName()).log(Level.SEVERE,e.toString());
		    }
	}
	
	
}
