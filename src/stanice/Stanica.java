package stanice;

import java.util.ArrayList;
import java.util.HashMap;

import mapa.Tacka;
import vozovi.Kompozicija;
import vozovi.PokretniObjekat;

public class Stanica {

	private String ime;
	private HashMap<String,Tacka> pocetne;
	public static HashMap<String,Boolean> dionice;
	private ArrayList<Integer> brzine;
	
	public Stanica (String ime) {
		this.ime=ime;
	}
	
	
	//Ovde cemo dodati pocetne tacke za svaku stanicu
	
	public void odrediPocetne (String ime) {
		
	pocetne = new HashMap<String,Tacka>();
	dionice = new HashMap<String,Boolean>();
	brzine = new ArrayList<Integer>();
	
	if (ime == "A") {
		pocetne.put("B" , new Tacka(2,3));
	}
	else if (ime == "B") {
		pocetne.put("A",new Tacka(5,23));
		pocetne.put("C",new Tacka(8,23));
	}
	else if (ime == "C") {
		pocetne.put("B",new Tacka(19,18));
		pocetne.put("E",new Tacka(20,15));
		pocetne.put("D",new Tacka(21,17));
	}
	else if (ime == "D") {
		pocetne.put("C",new Tacka(25,28));
	}
	else if (ime == "E") {
		pocetne.put("C",new Tacka(26,5));
	}
	}
	
	public Tacka getPocetne(String destinacija){
		return pocetne.get(destinacija);
	}
	
	public synchronized void setBrzine(int brzina) {
		brzine.add(brzina);
	}
	
	public synchronized boolean sizeBrzine() {
		if (brzine.size()>1)
			return false;
		else
			return true;
	}
	
	public synchronized void removeBrzine() {
		brzine.clear();
	}
	
	public synchronized void ukloniBrzinu() {
		brzine.remove(0);
	}
	
	public synchronized int getBrzina () {
		return brzine.get(0);
	}
	
	public static synchronized int slobodno (String a, String b) {
		String ab = a+b; //Od nase stanice do destinacije da li je slobodno
		String ba = b+a; //Od destinacije do nase
		if (dionice.get(ab)==true) //Ako ima voz na nasoj dionici u nasem smjeru
			return 1;
		else if (dionice.get(ba)==true) //Ako ima voz na nasoj dionici u suprotnom smjeru
			return 2;
		else
			return 0; //Ako nema voza na nasoj dionici
	}
	
	public static synchronized void setDionica (String key, boolean value) {
		dionice.replace(key, value);
	}
	
	
}
