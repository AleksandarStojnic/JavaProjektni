package Vozila;

import static mapa.Simulacija.mapa;

import java.awt.Color;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import gui.MainGui;
import mapa.FileWatcher;
import mapa.Simulacija;
import mapa.Tacka;
import vozovi.PokretniObjekat;

public class Vozilo extends Thread {

	public static int id=0;
	private int mojID;
	private String marka;
	private String model;
	private int godiste;
	private String pocetna;
	private int brzina;
	private Tacka trenutna;
	private Tacka start = new Tacka();
	private Tacka buduca = new Tacka();
	private Tacka konacna = new Tacka();
	
	public Vozilo (Tacka start, Tacka konacna, int brzina) {
		byte[] array = new byte[7]; // length is bounded by 7
	    new Random().nextBytes(array);
	    this.marka = new String(array, Charset.forName("UTF-8"));
	    this.model = new String(array, Charset.forName("UTF-8"));
	    this.godiste = new Random().nextInt();
		this.brzina=brzina;
		this.start=start;
		this.konacna=konacna;
		trenutna = new Tacka(this.start.getX(),this.start.getY());
		id++;
		mojID=id;
	}
	
	
	public Tacka getTrenutna() {
		return trenutna;
	}
	
	public Integer getMojId() {
		return mojID;
	}
	
	public Integer getBrzina() {
		return brzina;
	}
	
	public void run()
	{
		Simulacija.kola.put(mojID, this);
		
		while (trenutna.getX()!=konacna.getX() || trenutna.getY()!=konacna.getY()) { //Kretacemo se dok ima puta
			
		 buduca = racunajBuducu(start,trenutna,konacna);
		 if (nemaVoz()) {
			 if(slobodno(buduca)) {
			napraviKorak(buduca);
			try {
				Thread.sleep(10000/brzina);
			} catch (InterruptedException e) {
				Logger.getLogger(Vozilo.class.getName()).log(Level.SEVERE,e.toString());
			}
		 }
			 }
		 
		 else {
		 try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			Logger.getLogger(Vozilo.class.getName()).log(Level.SEVERE,e.toString());
		}
		}
		}
		ukloniSaMape();
		Simulacija.kola.remove(mojID);

	}
	
	
	
	public Tacka racunajBuducu(Tacka start, Tacka trenutna, Tacka konacna) {
 		
		Tacka buduca= new Tacka();
		
		
		if (start.getX()==0) {
			if (trenutna.getX()!=konacna.getX()) { //Idi desno
			  buduca.setX(trenutna.getX()+1);
			  buduca.setY(trenutna.getY());
			} 
			else if (trenutna.getX()==konacna.getX()) { //Idi dole
				buduca.setX(trenutna.getX());
				buduca.setY(trenutna.getY()-1);
			}
		}
		else if (start.getX()==29) {
			if (trenutna.getX()!=konacna.getX()) { //Idi lijevo
				buduca.setX(trenutna.getX()-1);
				buduca.setY(trenutna.getY());
			}
			else if (trenutna.getX()==konacna.getX()) { //idi dole
				buduca.setX(trenutna.getX());
				buduca.setY(trenutna.getY()-1);
			}
			
		}
		else if (start.getY()==0) {
			if (trenutna.getY()!=konacna.getY()) { //idi gore
				buduca.setX(trenutna.getX());
				buduca.setY(trenutna.getY()+1);
			}
			else if (trenutna.getY()==konacna.getY() && konacna.getX()==0) {
				buduca.setX(trenutna.getX()-1);
				buduca.setY(trenutna.getY());
			}
			else if (trenutna.getY()==konacna.getY() && konacna.getX()==29) {
				buduca.setX(trenutna.getX()+1);
				buduca.setY(trenutna.getY());
			}
			
		}
		else if (start.getY()==29) {
			buduca.setX(trenutna.getX());
			buduca.setY(trenutna.getY()-1);
			
		}
		
		return buduca;
		
	}
	
	public void napraviKorak(Tacka tacka) {
		
		//Promjeni izgled GUIa
				MainGui.updateTextField(trenutna.getX(), trenutna.getY(), tacka.getX(), tacka.getY(), "Vozilo", Color.CYAN);
				//Updajtuj mapu stanja
				mapa.lockMap();
				mapa.setMapaXY(trenutna.getX(), trenutna.getY(), null);
				mapa.setMapaXY(tacka.getX(),tacka.getY(),this);
				mapa.unlockMap();
				
		//Promjeni trenutno XY
				trenutna.setX(tacka.getX());
				trenutna.setY(tacka.getY());
		
	}
	
	public boolean nemaVoz() {
		
		boolean nemaVoza=true;
		
		Simulacija.mapa.lockMap();
		for (int i = -2; i < 3; i++)
		{for (int j = -2; j<3; j++) {
		 if(Simulacija.mapa.getMapaXY(trenutna.getX()+i , trenutna.getY()+j) instanceof PokretniObjekat)
			nemaVoza=false;
		}
		}
		Simulacija.mapa.unlockMap();
		return nemaVoza;
	}
	
	public boolean slobodno(Tacka tacka) {
		if (Simulacija.mapa.getMapaXY(tacka.getX(), tacka.getY())==null)
			return true;
		else 
			return false;
	}
	
	public void ukloniSaMape() {
		
		mapa.lockMap();
		mapa.setMapaXY(trenutna.getX(), trenutna.getY(), null);
		mapa.unlockMap();
		
		MainGui.setTextField(trenutna.getX(), trenutna.getY());
	}
}
