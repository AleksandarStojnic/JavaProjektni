package gui;

import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import Vozila.Vozilo;
import mapa.Simulacija;
import vozovi.Kompozicija;


public class Statistika extends JFrame {

	
	private static final long serialVersionUID = 1L;

	
	private JPanel contentPane;
	private JTable table_1;
	private JTable table_2;
	private String[] koloneKompozicije = {"ID", "Brzina", "TrenutnoX", "TrenutnoY"};
	private String[][] dataKompozicije;
	private JTextArea textArea;
	
	public Statistika () {
		
	  
	    
	    
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 900, 600);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		//contentPane.setLayout(null);
		
		
		textArea = new JTextArea();
		JScrollPane scrollPane = new JScrollPane(textArea); 
		textArea.setEditable(false);
		textArea.setText("Id   Brzina   TrenutnoX  TrenutnoY");
		contentPane.add(textArea);
		
		fillTextAreaKomp();
		fillTextAreaVozila();
				
	
	}
	
	
	public void fillTextAreaKomp () {
		ArrayList<Kompozicija> komp = new ArrayList<Kompozicija>();
		komp.addAll(Simulacija.kompozicije.values());
		for (int i = 0 ; i < komp.size() ; i++) {
		Integer x = komp.get(i).getKomp().getTrenutna().getX();
		Integer y = komp.get(i).getKomp().getTrenutna().getY();
		textArea.append("\n" + komp.get(i).getMojID().toString() + " " + komp.get(i).getBrzina().toString() + " " + x.toString() + " " + y.toString());
		
		}
	}
	
	public void fillTextAreaVozila() {
		ArrayList<Vozilo> vozila = new ArrayList<Vozilo>();
		vozila.addAll(Simulacija.kola.values());
		for (int i = 0 ; i < vozila.size() ; i++) {
			Integer x = vozila.get(i).getTrenutna().getX();
			Integer y = vozila.get(i).getTrenutna().getY();
			textArea.append("\n" + vozila.get(i).getMojId().toString() + " " + vozila.get(i).getBrzina().toString() + " " + x.toString() + " " + y.toString());
			
		}
	}
	
	
}
