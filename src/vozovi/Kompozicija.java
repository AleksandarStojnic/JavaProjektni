package vozovi;

import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import mapa.FileWatcher;
import mapa.Simulacija;
import mapa.Tacka;
import stanice.Stanica;

import static mapa.Simulacija.stanice;
import gui.MainGui;
import static mapa.Simulacija.mapa;

public class Kompozicija extends Thread implements Serializable {
	
	//MOZDA CU MORATI KORISTITI ONO GET MAPXY ZBOG VOZILA A MOZDA TO RIJESIM U VOZILIMA
    //IZ NEKOG RETARDIRANOG RAZLOGA && I || NE RADE KAKO TREBA PITAJ NEKOGA 
	
	
	private static final long serialVersionUID = 1L;
	public static int id=0;
	private Integer mojID;
	private int brzina; 
	private int novaBrzina;
	private ArrayList<String> putanja;
	private ArrayList<PokretniObjekat> kompozicija = new ArrayList<PokretniObjekat>();
	private Tacka pocetna;
	private Tacka destinacija;
	private boolean terminacija = false;
    private String prvaStanica;
    private boolean stigao;
    private boolean udji=false;
    private Serijalizacija serijalizacija;
    private long pocetnoVrijeme;
    private long zavrsnoVrijeme;
    private final static String SEPARATOR = File.separator;

	
  
	public Kompozicija(ArrayList<PokretniObjekat> kompozicija, int brzina,ArrayList<String> putanja) {
		super();
		this.brzina = brzina;
		this.putanja = putanja;
		this.stigao = false;
		this.kompozicija = kompozicija;
		prvaStanica = putanja.get(0);
		id++;
		mojID=id;
		this.kompozicija.get(0).setColor(Color.GREEN);
		serijalizacija = new Serijalizacija(putanja);
		
	}
	
	
	public PokretniObjekat getKomp(){
		return kompozicija.get(0);
	}
	
	public Integer getMojID()
	{
		return mojID;
	}
	
	public Integer getBrzina()
	{
		return brzina;
	}

	public void run() {
		
		
		//Zabiljezimo pocetno vrijeme
		pocetnoVrijeme= System.currentTimeMillis();
		
		//Veoma retardiran nacin da izjegnem da oba voza krenu u isto vrijeme
		try {
			Thread.sleep((long)(Math.random() * 3000));
		} catch (InterruptedException e) {
			Logger.getLogger(Kompozicija.class.getName()).log(Level.SEVERE,e.toString());
		}
     
        Simulacija.kompozicije.put(mojID, this);
     
     
		//Dok imam kompozicije kad je unistim na kraju prekinuce tread
		while(kompozicija.size()>0) {
			
			if (putanja.size()>1){
			
			
		//Provjera da li je put slobodan
		
		if (Stanica.slobodno(putanja.get(0), putanja.get(1))==0) {
		
		//Racunanje pocetne tacke i destinacije
		if (putanja.size()>1) {
		racunanjePocetneDestinacije();}
			
		//Kretanje	
		kreciSe(kompozicija);	
		udji=false; //TEST
		try {
			Thread.sleep(10000/brzina);
		} catch (InterruptedException e) {
			Logger.getLogger(Kompozicija.class.getName()).log(Level.SEVERE,e.toString());
		}

		}
		
		else if (Stanica.slobodno(putanja.get(0), putanja.get(1))==1) {
			
			//Korigujemo brzinu
			this.novaBrzina=stanice.get(putanja.get(1)).getBrzina();
			
			//Racunanje pocetne tacke i destinacije
			if (putanja.size()>1) {
			racunanjePocetneDestinacije();}
				
			//Kretanje	
			kreciSe(kompozicija);	
			udji=false; //TEST
			try {
				Thread.sleep(10000/novaBrzina);
			} catch (InterruptedException e) {
				Logger.getLogger(Kompozicija.class.getName()).log(Level.SEVERE,e.toString());
			}

		}
		else {
			try {
				udji=true;
				kreciSe(kompozicija);
				Thread.sleep(10000/brzina);
			} catch (InterruptedException e) {
				Logger.getLogger(Kompozicija.class.getName()).log(Level.SEVERE,e.toString());
			}
		}
			}
			else { 
	//OVO SAM SRANJE MORAO ISKOPIRATI POSTO GUBIM DUZINU NA PUTANJI A && i || JEBENO NE RADE KAKO TREBA
				
				//Provjera da li je put slobodan
				
				if (Stanica.slobodno(prvaStanica, putanja.get(0))==0) {
				
				//Racunanje pocetne tacke i destinacije
				if (putanja.size()>1) {
				racunanjePocetneDestinacije();}
					
				//Kretanje	
				kreciSe(kompozicija);
				udji=false; //TEST
				try {
					Thread.sleep(10000/brzina);
				} catch (InterruptedException e) {
					Logger.getLogger(Kompozicija.class.getName()).log(Level.SEVERE,e.toString());
				}

				}
				
				else if (Stanica.slobodno(prvaStanica, putanja.get(0))==1) {
					
					//Korigujemo brzinu
					this.novaBrzina=stanice.get(putanja.get(0)).getBrzina();
					
					//Racunanje pocetne tacke i destinacije
					if (putanja.size()>1) {
					racunanjePocetneDestinacije();}
						
					//Kretanje	
					kreciSe(kompozicija);
					udji=false; //TEST
					try {
						Thread.sleep(10000/novaBrzina);
					} catch (InterruptedException e) {
						Logger.getLogger(Kompozicija.class.getName()).log(Level.SEVERE,e.toString());
					}

				}
				else {
					try {
						udji=true; //TEST
						kreciSe(kompozicija);
						Thread.sleep(10000/brzina);
					} catch (InterruptedException e) {
						Logger.getLogger(Kompozicija.class.getName()).log(Level.SEVERE,e.toString());
					}
				}
				
			}
	
	}
		
		Simulacija.kompozicije.remove(mojID);
		
	}
	
	
	public void serijalizuj() {
		
         serijalizacija.setVrijeme(zavrsnoVrijeme-pocetnoVrijeme); //Ukupno vrijeme kretanja u milisekundama
		
		//Ovde cemo odraditi serijalizaciju
		File fajl = new File(System.getProperty("user.dir")+ SEPARATOR +"serijalizacija" + SEPARATOR +"kompozicija"+mojID.toString());
		try {
			FileOutputStream fis = new FileOutputStream(fajl);
			ObjectOutputStream out = new ObjectOutputStream(fis);
			out.writeObject(serijalizacija);
			out.close();
			fis.close();
		} catch (Exception e) {
			Logger.getLogger(Kompozicija.class.getName()).log(Level.SEVERE,e.toString());
		}
		
	}
	
	public void racunanjePocetneDestinacije() {
	
		pocetna = stanice.get(putanja.get(0)).getPocetne(putanja.get(1));
		destinacija = stanice.get(putanja.get(1)).getPocetne(putanja.get(0));
	}
		
	public void kreciSe(ArrayList<PokretniObjekat> kompozicija)
	{
		for (int i=0; i<kompozicija.size(); i++) {
			
			if (udji==true) {
				ulazakStanica(kompozicija.get(i));
			}
			
			else if (i==0 && kompozicija.get(i).getBuduca().getX()==0 && kompozicija.get(i).getBuduca().getY()==0 && stigao==false) { 
			
			 //Ako smo na pocetku i trebamo izaci iz stanice sa prvim
			 kompozicija.get(i).setBuduca(pocetna);
			 napraviKorak(kompozicija.get(i));
			 
			 //Zabiljezimo da je putanja zauzeta
			 Stanica.setDionica(putanja.get(0)+putanja.get(1), true); 
			 
			 //Zapisemo da se krecemo od pocetne do destinacije
			 stanice.get(putanja.get(1)).setBrzine(brzina);
			}
			else if (i==0 && kompozicija.get(i).getTrenutna().getX() == destinacija.getX() && kompozicija.get(i).getTrenutna().getY() == destinacija.getY() && stigao==false) {
		    //Ovo idemo u slucaju da se prva lokomotiva nalazi ispred stanice 
			
			 prvaStanica = putanja.get(0);
			 putanja.remove(0);
	
			 
			 if (putanja.size()>1) {
				 //Ako ima jos stanica da krene
				 racunanjePocetneDestinacije();
			     ulazakStanica(kompozicija.get(i));
			     stigao=true;
			     //Da udje u stanicu zbog provjere dostupnosti dionice
			   
			 }
			 else {
				 ulazakStanica(kompozicija.get(i));
				 //Ovde moram iskucati nesto za unistavanje kompozicije jer je na kraju
				 terminacija=true;
				 stigao=true;
			 }
			}
			else if (i==0 && stigao==false) { 
				// Ovde cemo definisati ponasanje izmedju pocetne i destinacije
				
			    kompozicija.get(i).setBuduca(izracunajKorak(kompozicija.get(i)));
				napraviKorak(kompozicija.get(i));
		
			}
			else if (i==0 && stigao==true) {
				kompozicija.get(i).setProsla(new Tacka(0,0));
				}
			else if (i!=0) { 
				//Ovde cemo definisati ponasanje za sve ostale koje ce pratiti dio kompozicije ispred sebe
				kompozicija.get(i).setBuduca(prateciKorak(kompozicija, i));
				if (kompozicija.get(i).getBuduca().getX()!=0 && kompozicija.get(i).getBuduca().getY()!=0) {
					//Ovako provjeravamo je li nas red za izlaziti i da li smo stigli
					napraviKorak(kompozicija.get(i));
				}
				else {
					
					//Uci u stanicu moramo svakako
					ulazakStanica(kompozicija.get(i));	
					
					//Ako ima terminacija rijesi brzinu i oslobodi dionicu i unisti kompoziciju
					if (terminacija==true) {
						zavrsnoVrijeme=System.currentTimeMillis();// Ovde zabiljezimo vrijeme kada smo zavrsili kretanje
						serijalizuj(); // serijalizacija
					if (i==kompozicija.size()-1 && stanice.get(putanja.get(0)).sizeBrzine()) 
					{	 //Ovde provjerimo ima li jos vozova na dionici kojom se krecemo pa ako smo jedini voz da oslobodimo dionicu
						
					Stanica.setDionica(prvaStanica+putanja.get(0), false);
				    stanice.get(putanja.get(0)).ukloniBrzinu();
				    kompozicija.clear();
				    
				    }
					else if (i==kompozicija.size()-1) {
						//Ako ima jos vozova iza nas samo da uklonimo svoju brzinu i unistimo kompoziciju
						stanice.get(putanja.get(0)).ukloniBrzinu();
						kompozicija.clear();
						
					}
					}
					
					//Ako nema terminacija rijesi brzinu samo i nastavi
					else if (terminacija==false && stigao==true) {
						if (i==kompozicija.size()-1 && stanice.get(putanja.get(0)).sizeBrzine()) 
						{	 //Ovde provjerimo ima li jos vozova na dionici kojom se krecemo pa ako smo jedini voz da oslobodimo dionicu
							
						Stanica.setDionica(prvaStanica+putanja.get(0), false);
					    stanice.get(putanja.get(0)).ukloniBrzinu();
					    stigao=false;
					    }
						else if (i==kompozicija.size()-1) {
							//Ako ima jos vozova iza nas samo da uklonimo svoju brzinu i unistimo kompoziciju
							stanice.get(putanja.get(0)).ukloniBrzinu();
							stigao=false;
						}
						
					}
				
				}
				
			}
		}
		
	}
	
	
	public void napraviKorak(PokretniObjekat obj) { //Ovde cemo definisati pravljenje jednog koraka na osnovu buduceg
		obj.setProsla(obj.getTrenutna());//Trenutna postaje prosla
		obj.setTrenutna(obj.getBuduca());//Buduca postaje trenutna
		
		//Promjeni izgled GUIa
		mapa.lockMap();
		MainGui.updateTextField(obj.getProsla().getX(), obj.getProsla().getY(), obj.getTrenutna().getX(), obj.getTrenutna().getY(), "TEST", obj.getColor());
		//Updajtuj mapu stanja
		mapa.setMapaXY(obj.getProsla().getX(), obj.getProsla().getY(), null);
		mapa.setMapaXY(obj.getTrenutna().getX(), obj.getTrenutna().getY(), obj);
		mapa.unlockMap();
		
	}
	
	public void ulazakStanica(PokretniObjekat obj) { //Ovde cemo definisati skidanje sa mape ili cekanje na izlazak
		//Uklonimo dati dio kompozicije sa mape stanja
		mapa.lockMap();
		mapa.setMapaXY(obj.getTrenutna().getX(), obj.getTrenutna().getY(), null);
		//Da rijesimo gui
		
		MainGui.setTextField(obj.getTrenutna().getX(), obj.getTrenutna().getY());
		mapa.unlockMap();
		//Da updajtujemo vrijednosti trenutne i buduce
		obj.setProsla(obj.getTrenutna());
		obj.setBuduca(new Tacka(0,0));
		obj.setTrenutna(new Tacka(0,0));
		udji=false;
		
	}
	
	public Tacka izracunajKorak(PokretniObjekat obj) { //Ovde vodeca lokomotiva racuna korak
		int x1 = obj.getTrenutna().getX();
		int y1 = obj.getTrenutna().getY();
		int x2 = destinacija.getX();
		int y2 = destinacija.getY();
		Tacka trenutna = new Tacka(obj.getTrenutna().getX(),obj.getTrenutna().getY());
		ArrayList<Tacka> tacke = new ArrayList<Tacka>();
		double distanca1, distanca2;
		if (MainGui.myArray[x1+1][y1]==1 || MainGui.myArray[x1+1][y1]==2 ) // Provjeravamo imamo li prugu desno
		{
			tacke.add(new Tacka(x1+1,y1));
		}
		if (MainGui.myArray[x1-1][y1]==1 || MainGui.myArray[x1-1][y1]==2) //Provjeravamo lijevo imamo li prugu
		{
			tacke.add(new Tacka(x1-1,y1));
		}
		if (MainGui.myArray[x1][y1+1]==1 || MainGui.myArray[x1][y1+1]==2)//Provjeravamo dole
		{
			tacke.add(new Tacka(x1,y1+1));
		}
		if (MainGui.myArray[x1][y1-1]==1 || MainGui.myArray[x1][y1-1]==2)//Provjeravamo gore
		{
			tacke.add(new Tacka(x1,y1-1));
		}
		
		if (tacke.size()<2) { //Ako imamo samo jedan moguci smjer
			return tacke.get(0);
		}
		else
		{
	        //Racunamo distancu do destinacije za tacku 1
			distanca1=Math.sqrt((tacke.get(0).getX()-x2)*(tacke.get(0).getX()-x2) + (tacke.get(0).getY()-y2)*(tacke.get(0).getY()-y2));
			//Racunamo distancu do destinacije za tacku 2
			distanca2=Math.sqrt((tacke.get(1).getX()-x2)*(tacke.get(1).getX()-x2) + (tacke.get(1).getY()-y2)*(tacke.get(1).getY()-y2));
			
			
		
			if (Simulacija.mapa.getMapaXY(tacke.get(0).getX(), tacke.get(0).getY())==null && (tacke.get(0).getX() != obj.getProsla().getX() || tacke.get(0).getY() != obj.getProsla().getY()) ) {
				return tacke.get(0);
			
			}
			else if (Simulacija.mapa.getMapaXY(tacke.get(1).getX(), tacke.get(1).getY())==null)
				return tacke.get(1);
			else
				return trenutna;
		}
		
		
	}
	
	public Tacka prateciKorak(ArrayList<PokretniObjekat> kompozicija, int pozicija) { //Ovde ostale kolone racunaju buduci korak na osnovu kolona koje su ispred
		
		Tacka buduca = new Tacka(kompozicija.get(pozicija-1).getProsla().getX(), kompozicija.get(pozicija-1).getProsla().getY());
		return buduca;
	}
}
