package Vozila;

import java.util.Random;

import mapa.Tacka;

public class Automobil extends Vozilo  {
	
	private int brojVrata;

	public Automobil(Tacka start, Tacka konacna, int brzina) {
		super(start, konacna, brzina);
		// TODO Auto-generated constructor stub
		Random random = new Random();
		this.brojVrata= random.nextInt(4)-1;
	}

}
