package gui;

import java.awt.Color;
import java.awt.Font;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import mapa.FileWatcher;
import mapa.Simulacija;









public class MainGui extends JFrame {
	
	private static final long serialVersionUID = 1L;
	public static ReentrantLock mapLock = new ReentrantLock();
	public static JTextField[][] textFieldMatrica;
	private static int MAP_SIZE = 30;
	private static Font font = new Font("Tahoma", Font.PLAIN, 10);
	private static final int BLOCK_SIZE = 23;
	private static final int SPACING = 2;
	private JPanel contentPane;
	public static int [][] myArray = new int[MAP_SIZE][MAP_SIZE];
	private JButton startButton;
	
	//Ovde definisemo boje za mapu
	
	public static final Color pruga = Color.GRAY;
	public static final Color prelaz = Color.BLACK;
	public static final Color put = Color.BLUE;
	public static final Color stanica = Color.YELLOW;
	
	public MainGui () {
		
		Simulacija simulacija = new Simulacija();
		FileWatcher vozilaWatcher = new FileWatcher(System.getProperty("user.dir"));
		FileWatcher kompozicijeWatcher = new FileWatcher(System.getProperty("user.dir")+"\\kompozicije");
		
		
		textFieldMatrica = new JTextField[MAP_SIZE][MAP_SIZE];
		setTitle("Projekat");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(1200,830);
		setResizable(true);
		setLocationRelativeTo(null);
		
		contentPane = new JPanel(true);
		contentPane.setBackground(Color.LIGHT_GRAY);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(null);
		try {
			initMatrica(); //Inicijalizujemo matricu
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			Logger.getLogger(MainGui.class.getName()).log(Level.SEVERE,e.toString());;
		}
		setContentPane(contentPane);
		
		startButton = new JButton("OMOGUCI KRETANJE");
		startButton.addActionListener(e -> {
			simulacija.start(); // Pokrenenmo tredove za vozila i vagone
			vozilaWatcher.start(); //Ovako gledamo oce li se config promjeniti
			kompozicijeWatcher.start();//Ovako gledamo hoce li se promjeniti sadrzaj u kompozicijama
			
		});
		
		startButton.setBounds(830, 435, 171, 31);
		contentPane.add(startButton);
		
		JButton btnNewButton_1 = new JButton("STATISTIKA");
		btnNewButton_1.addActionListener(e -> statistika());
		btnNewButton_1.setBounds(830, 563, 171, 31);
		contentPane.add(btnNewButton_1);
	}
	
	
	public void statistika() {
		Statistika tabela = new Statistika();
		tabela.setVisible(true);
	}
	
	public void initMatrica() throws FileNotFoundException {
		
		//Ovde iscitamo matricu iz fajla za pocetnu postavku
		  Scanner sc;
		  sc = new Scanner(new BufferedReader(new FileReader("pocetak.txt")));
	  
	      while(sc.hasNextLine()) {
	         for (int i=0; i<myArray.length; i++) {
	            String[] line = sc.nextLine().trim().split(" ");
	            for (int j=0; j<line.length; j++) {
	               myArray[i][j] = Integer.parseInt(line[j]);
	            }
	         }
	      }
		sc.close();
		
		//Iz nekog retardiranog razloga moram rotirati matricu za 90 stepeni jbg
		 for (int i = 0; i < MAP_SIZE / 2; i++)
		    {
		        for (int j = i; j < MAP_SIZE - i - 1; j++)
		        {
		 
		            // Swap elements of each cycle
		            // in clockwise direction
		            int temp = myArray[i][j];
		            myArray[i][j] = myArray[MAP_SIZE - 1 - j][i];
		            myArray[MAP_SIZE - 1 - j][i] = myArray[MAP_SIZE - 1 - i][MAP_SIZE - 1 - j];
		            myArray[MAP_SIZE - 1 - i][MAP_SIZE - 1 - j] = myArray[j][MAP_SIZE - 1 - i];
		            myArray[j][MAP_SIZE - 1 - i] = temp;
		        }
		    }
		
		//Ovde farbamo matricu
		for(int i=0; i<MAP_SIZE; i++) {
			for(int j=0; j<MAP_SIZE; j++) {
				
				int broj = myArray[i][j];
				
				textFieldMatrica[i][j] = new JTextField();
				textFieldMatrica[i][j].setHorizontalAlignment(SwingConstants.CENTER);
				textFieldMatrica[i][j].setEditable(false);
				textFieldMatrica[i][j].setFont(font);
				textFieldMatrica[i][j].setBackground(odrediBoju(broj));
				textFieldMatrica[i][j].setBounds(i * BLOCK_SIZE + SPACING , (MAP_SIZE -j) * BLOCK_SIZE + SPACING , BLOCK_SIZE - 2*SPACING, BLOCK_SIZE - 2*SPACING);
				contentPane.add(textFieldMatrica[i][j]);
			}
		}
	}
	
	public static Color odrediBoju (int i) //Ovo da vidimo koja boja ide iz fajla
	{
		if (i==1)
			return pruga;
		else if (i==2)
			return prelaz;
		else if (i==3)
			return put;
		else if (i==4)
			return stanica;
		else
			return Color.WHITE;
	}
	
	
	public static void updateTextField(int xStaro, int yStaro, int xNovo, int yNovo, String tekst, Color color) {
	    int i = myArray[xStaro][yStaro];
		textFieldMatrica[xStaro][yStaro].setBackground(odrediBoju(i));
		textFieldMatrica[xStaro][yStaro].setText("");
		textFieldMatrica[xNovo][yNovo].setBackground(color);
		textFieldMatrica[xNovo][yNovo].setText(tekst);
	}
	
	public static void setTextField(int x, int y) {
		textFieldMatrica[x][y].setBackground(odrediBoju(myArray[x][y]));
		textFieldMatrica[x][y].setText("");
	}
	
	public static void lockMap() {
		mapLock.lock();
	}
	
	public static void unlockMap() {
		if(mapLock.isHeldByCurrentThread()) {
			mapLock.unlock();
		}
	}
	
	
	public static void main(String[] args) {
		MainGui maingui = new MainGui();
		maingui.setVisible(true);
	}
}
