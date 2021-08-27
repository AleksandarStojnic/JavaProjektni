package vozovi;

import java.io.Serializable;
import java.util.ArrayList;

public class Serijalizacija implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ArrayList<String> putanja;
	String zaustavljanje;
	long vrijeme;
	
	public Serijalizacija(ArrayList<String> putanja) {
		this.putanja=putanja;
		zaustavljanje=putanja.get(0);
		vrijeme=0;
		
	}

	public String getZaustavljanje() {
		return zaustavljanje;
	}

	public void setZaustavljanje(String zaustavljanje) {
		this.zaustavljanje += zaustavljanje;
	}

	public long getVrijeme() {
		return vrijeme;
	}

	public void setVrijeme(long vrijeme) {
		this.vrijeme = vrijeme;
	}
	
	
	
}
