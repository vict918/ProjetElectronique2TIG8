package rxtx;

import gnu.io.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Font;
import javax.swing.JTextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class GUI implements ActionListener, SerialPortEventListener{

	private JFrame window;
	private JPanel sendButtonPanel;
	private JButton sendButton;
	private SerialPort portSerie;
	private JTextField champ;
	CommPortIdentifier numPort;
	
	public GUI() {
		
		window = new JFrame("App Java");
		window.setSize(600, 600);
		window.setResizable(false);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.getContentPane().setBackground(Color.lightGray);
		window.setLayout(null);
		window.getContentPane();
		
		sendButton = new JButton("Envoyer");
		sendButton.setBounds(225, 350, 150, 30);
		sendButton.setFocusPainted(false);
		sendButton.addActionListener(this);
		
		champ = new JTextField();
		champ.setBounds(225, 150, 150, 30);
		
		window.getContentPane().add(champ);
		window.getContentPane().add(sendButton);
		window.setVisible(true);
		
		try {
			numPort=CommPortIdentifier.getPortIdentifier("COM2");
			portSerie=(SerialPort)numPort.open("",100);
			portSerie.addEventListener(this);
			portSerie.notifyOnDataAvailable(true);
		}
		catch(Exception e) {
			System.out.print("Erreur, "+ e.toString());
		}
	}
	

	public void actionPerformed(ActionEvent event) {
		Object  source = event.getSource();
		
		   if(source == sendButton) {
			   //envoyer infos au PIC
			   System.out.println(champ.getText());
			   }
		   }
	
	public static void main(String [] args) {
		GUI gui = new GUI();
		System.out.println(gui);
	}

	@Override
	public void serialEvent(SerialPortEvent sev) {
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(this.portSerie.getInputStream()));
			if(sev.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
				String str = br.readLine();
				champ.setText(str);
				br.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} 

			
		
	} 


}
