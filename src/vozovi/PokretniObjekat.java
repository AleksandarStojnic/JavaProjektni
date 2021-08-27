package vozovi;

import java.awt.Color;

import mapa.Tacka;

public class PokretniObjekat {

  private Tacka trenutna;
  private Tacka buduca;
  private Tacka prosla;
  private Color color;
  
  public PokretniObjekat() {
	  trenutna = new Tacka();
	  buduca = new Tacka();
	  prosla = new Tacka();
	  this.color = Color.CYAN;
  }
  
public Tacka getTrenutna() {
	return trenutna;
}
public void setTrenutna(Tacka trenutna) {
	this.trenutna = trenutna;
}
public Tacka getBuduca() {
	return buduca;
}
public void setBuduca(Tacka buduca) {
	this.buduca = buduca;
}
public Tacka getProsla() {
	return prosla;
}
public void setProsla(Tacka prosla) {
	this.prosla = prosla;
}

public Color getColor() {
	return color;
}

public void setColor(Color color) {
	this.color = color;
}


}
