package Vozila;

import java.util.Random;

import mapa.Tacka;

public class Kamion extends Vozilo {

	private int nosivost;
	
	public Kamion(Tacka start, Tacka konacna, int brzina) {
		super(start, konacna, brzina);
		// TODO Auto-generated constructor stub
		Random random = new Random();
		this.nosivost = random.nextInt(20)+1;
	}

}
