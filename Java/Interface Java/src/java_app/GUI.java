package java_app;

import gnu.io.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;

import java.awt.Color;
import javax.swing.JTextField;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * 
 * @author Robin Castermane, Igor Vandervelden, Victor Cotton & Rodrigue Mugisha Tuyishime
 * Groupe Elec 8 
 *
 */

public class GUI implements ActionListener, SerialPortEventListener{

	private JFrame window;
	private JButton sendButton,com1,com2,com3,com4;
	private SerialPort portSerie;
	private SpinnerModel seuil;
	private JSpinner spinner;
	private JLabel distance,result,alerte;
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
		
		seuil =  new SpinnerNumberModel(70,0,1000,1);
		spinner = new JSpinner(seuil);   
        spinner.setBounds(225,150,150,30);
		
		distance = new JLabel("La distance est:");
		distance.setBounds(250, 225, 300, 50);
		distance.setForeground(Color.black);
		
		result = new JLabel("");
		result.setBounds(345, 225, 50, 50);
		result.setForeground(Color.black);
		
		alerte = new JLabel("");
		alerte.setBounds(245, 185, 150, 50);
		alerte.setForeground(Color.black);
		
		window.getContentPane().add(spinner);
		window.getContentPane().add(com1);
		window.getContentPane().add(com2);
		window.getContentPane().add(com3);
		window.getContentPane().add(com4);
		window.getContentPane().add(sendButton);
		window.getContentPane().add(distance);
		window.getContentPane().add(result);
		window.getContentPane().add(alerte);
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
			
		}catch(Exception e) {
			JOptionPane.showMessageDialog(null, "Connexion au port "+ port +" impossible");
			}
	}
	
	/**
	 * 
	 * Méthode permettant la lecture des données arrivant sur le port COM et affichage de ces données dans l'UI. 
	 * Une alerte est affiché si le seuil est dépassé.
	 * 
	 */
	@Override
	public void serialEvent(SerialPortEvent event) {
		try {
			br = new BufferedReader(new InputStreamReader(portSerie.getInputStream()));
			String str = br.readLine();
			result.setText(str + " cm");	
			
			if((Integer)seuil.getValue() > Integer.parseInt(str)){
				alerte.setText("Aucune alerte");
				alerte.setForeground(Color.green);
			}
			else if((Integer)seuil.getValue() < Integer.parseInt(str)){
				alerte.setText("Alerte, seuil dépassé !!");
				alerte.setForeground(Color.red);
			}
			br.close();

		}catch(Exception e) {
			/*JOptionPane.showMessageDialog(null, e.toString());
			 * Suite à des difficultés rencontrées, j'ai enlevé l'exception qui ouvrait une fenêtre message toutes les 5s due à une erreur
			 * dont j'ignore l'origine, ainsi le programme peut fonctionner sans être interrompu constamment. 
			 * */
		}
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
			String str = seuil.getValue() + "\r";
			try {
				pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(portSerie.getOutputStream())), true);
				pw.print(str);
				pw.close();
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

