package java_app;

import gnu.io.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.awt.Color;
import javax.swing.JTextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class GUI implements ActionListener, SerialPortEventListener{

	private JFrame window;
	private JButton sendButton,com1,com2,com3,com4;
	private SerialPort portSerie;
	private JTextField champ;
	private JLabel distance,result;
	private PrintWriter pw;
	private BufferedReader br;
	private CommPortIdentifier numPort;
	
	/**
	 * 
	 * Construction interface graphique
	 * 
	 */
	public GUI() {
		window = new JFrame("App Java");
		window.setSize(600, 600);
		window.setResizable(false);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.getContentPane().setBackground(Color.lightGray);
		window.setLayout(null);
		window.getContentPane();

		com1 = new JButton("COM1");
		com1.setBounds(50, 300, 100, 30);
		com1.setFocusPainted(false);
		com1.addActionListener(this);
		
		com2 = new JButton("COM2");
		com2.setBounds(175, 300, 100, 30);
		com2.setFocusPainted(false);
		com2.addActionListener(this);
		
		com3 = new JButton("COM3");
		com3.setBounds(300, 300, 100, 30);
		com3.setFocusPainted(false);
		com3.addActionListener(this);
		
		com4 = new JButton("COM4");
		com4.setBounds(430, 300, 100, 30);
		com4.setFocusPainted(false);
		com4.addActionListener(this);
		
		sendButton = new JButton("Envoyer");
		sendButton.setBounds(225, 350, 150, 30);
		sendButton.setFocusPainted(false);
		sendButton.addActionListener(this);
		
		champ = new JTextField();
		champ.setBounds(225, 150, 150, 30);
		
		distance = new JLabel("La distance est:");
		distance.setBounds(250, 225, 300, 50);
		distance.setForeground(Color.black);
		
		result = new JLabel("");
		result.setBounds(345, 225, 50, 50);
		result.setForeground(Color.black);
		
		window.getContentPane().add(champ);
		window.getContentPane().add(com1);
		window.getContentPane().add(com2);
		window.getContentPane().add(com3);
		window.getContentPane().add(com4);
		window.getContentPane().add(sendButton);
		window.getContentPane().add(distance);
		window.getContentPane().add(result);
		window.setVisible(true);
	}
	
	/**
	 * 
	 * Méthode permettant l'ouverture d'une connexion d'un port COM donné
	 *  
	 * @param port port à écouter
	 * 
	 */
	public void connexionPort(String port){
		try {
			numPort=CommPortIdentifier.getPortIdentifier(port);
			portSerie=(SerialPort)numPort.open("",100);
			portSerie.addEventListener(this);
			portSerie.notifyOnDataAvailable(true);
			portSerie.setSerialPortParams(9600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
			champ.setText("");
			
		}catch(Exception e) {
			JOptionPane.showMessageDialog(null, "Connexion au port "+ port +" impossible");
			}
	}
	
	/**
	 * 
	 * Méthode permettant la lecture des données arrivant sur le port COM et affichage de ces données dans l'UI. 
	 * 
	 */
	@Override
	public void serialEvent(SerialPortEvent event) {
		try {
			br = new BufferedReader(new InputStreamReader(portSerie.getInputStream()));
			String str = br.readLine();
			result.setText(str);			
			System.out.println(str);
			br.close();	

		}catch(Exception e) {JOptionPane.showMessageDialog(null, e.toString());}
	}
	
	/**
	 * 
	 * Lors d'un clic sur le bouton "Envoyer", la valeur introduite dans le champ texte est envoyé au PIC
	 * afin de définir le seuil au-delà (ou en dessous) duquel l’alerte est déclenchée.
	 * 
	 * Pour la connexion au port COM, quatres boutons sont mis à disposition afin de permettre à l'utilisateur
	 * de choisir entre les ports COM1 à COM4.
	 * 
	 */
	@Override
	public void actionPerformed(ActionEvent event) {
		if(event.getSource() == sendButton) {
			String str = champ.getText() + "\r";
			try {
				pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(portSerie.getOutputStream())), true);
				pw.print(str);
				pw.close();
				champ.setText("");
			}catch(Exception e) {JOptionPane.showMessageDialog(null, e.toString());}
		}
		else if(event.getSource() == com1){
			connexionPort("COM1");
		}
		else if(event.getSource() == com2){
			connexionPort("COM2");
		}
		else if(event.getSource() == com3){
			connexionPort("COM3");
		}
		else if(event.getSource() == com4){
			connexionPort("COM4");
		}
	}
	
	public static void main(String[] args) {
		
		GUI app = new GUI();
		System.out.println(app);
	}
	
	

}
