// Progetto di Programmazione di rete A.A. 2003-2004 
// autore:Cristiano Anselmi n.226847

import java.net.*;
import java.io.*;


public class GestioneCasse extends Server implements Runnable {
    

    public void run(){


	InetAddress indirizzo = null;
	try {
	    indirizzo = InetAddress.getByName("225.1.2.3");
	} catch (UnknownHostException e) {
	    System.out.println(e);
	    System.exit(0);
	}

	int porta = 3002;
		

try {
	MulticastSocket canale = new MulticastSocket(porta);
		canale.setInterface(InetAddress.getByName("127.10.10.12")); 
	  
	  canale.joinGroup(indirizzo);

	  


	  while(cicloGestioneCasse){
	     byte[] dat = new byte[1024];
	       DatagramPacket pacchetto = new DatagramPacket(dat,dat.length);
	
 
	        canale.receive(pacchetto);
		      
            
		int  y=0;
		int i=0;
		
		
		if ((new String (pacchetto.getData(),0,
				 pacchetto.getLength())).equals("giochi")) {
		    while(Giochi[i]!= null){
			System.out.println("Sto spedendo alla cassa "+ Giochi[i]);
			byte dati [] = new byte[1024];
			dati = Giochi[i].getBytes();
			DatagramPacket pacchettoRisposta = 
			new DatagramPacket(dati,dati.length,indirizzo,3001);
	    	  	
			canale.send(pacchettoRisposta);
	    i++;
	    }    	 


		
		}

		

			
		i=0;
		if ((new String (pacchetto.getData(),0,
			     pacchetto.getLength())).equals("alimentari")) {
		
	    while(Alimentari[i]!= null){
		System.out.println("Sto spedendo alla cassa "+ Alimentari[i]);
		    byte dati [] = new byte[1024];
		    dati = Alimentari[i].getBytes();
			     DatagramPacket pacchettoRisposta = 
				 new DatagramPacket(dati,dati.length,indirizzo,3001);
		    canale.send(pacchettoRisposta);
	    i++;
	    }    

	 	}

	}   

	  	canale.leaveGroup(indirizzo);
		canale.close();
		System.out.println("fine GestioneCasse");

	} catch (IOException e) {}
  
	}
	}

//Gestione Casse













 




 
