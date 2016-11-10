// Progetto di Programmazione di rete A.A. 2003-2004 
// autore:Cristiano Anselmi n.226847

import java.net.*;
import java.io.*;

class Salvatempo   {
    
    static 	int portain, portaout=0;
  
    public static void main ( String args []){
	
	int porta = 3008;

	try{
	    byte [] dati = new byte[2048];
	  	MulticastSocket canale = new MulticastSocket(porta);
	    System.out.println("Inserire il nominativo del Socio.");
	    BufferedReader scelta  = new BufferedReader(new InputStreamReader(System.in));
	    String nome;
	    nome = scelta.readLine();
	
	    InetAddress indirizzo = null;
	try{
		indirizzo = InetAddress.getByName("225.1.2.3");
	    } catch (UnknownHostException e) {
		System.out.println(e);
		System.exit(0);
	    }

		    canale.setInterface(InetAddress.getByName("127.10.10.14")); 
		    canale.joinGroup(indirizzo);
		    dati = nome.getBytes();
		    DatagramPacket pacchetto = new DatagramPacket(dati,dati.length,indirizzo,3007);
		    
		    // spedisco al Server il nome
		    
		    canale.send(pacchetto);
		    byte dati2 []= new byte [1024];
		    DatagramPacket pacchettoRisposta = new DatagramPacket(dati2,dati2.length);
		    canale.receive(pacchettoRisposta);
	     	    String codice = new String(pacchettoRisposta.getData(),0, pacchetto.getLength());

		    String segnale= codice.substring(0,4);

		    if (segnale.equals("Stop")){
			System.out.println("Il nome non è presente nella lista dei soci");
			canale.leaveGroup(indirizzo);
			canale.close();
			System.exit(0);
		    }
	    
		    
		    byte dati3 [] = new byte[1024];
		    DatagramPacket ricevoportain = new DatagramPacket(dati3,dati3.length);
		    canale.receive(ricevoportain);

		    byte dati4 [] = new byte[1024];
		    DatagramPacket ricevoportaout = new DatagramPacket(dati4,dati4.length);
		    canale.receive(ricevoportaout);
	    	    
		    String s =  new String(ricevoportain.getData(),0, ricevoportain.getLength());
		    portaout =  Integer.parseInt(s);
		    String t =  new String(ricevoportaout.getData(),0, ricevoportaout.getLength());
		    portain =  Integer.parseInt(t);
		    
		    System.out.println(portaout);
		    System.out.println("Il codice del salvatempo è  "+ codice);
		    canale.leaveGroup(indirizzo);
	    
		    boolean continua = true;
		    String localhost= "127.0.0.1";
		    
		    System.out.println("portain Salvatempo" + portain);
		    System.out.println("portaout Salvatempo" + portaout);
		    DatagramSocket canaleUDP = new DatagramSocket(portain);

		    indirizzo = InetAddress.getByName(localhost);

		    canaleUDP.connect(indirizzo,portaout);
		    pacchetto = new DatagramPacket(dati, dati.length ,indirizzo,portaout);

		    while(continua){
			
			System.out.println("1- Acquisto");
			System.out.println("2- Rimozione");
			System.out.println("3- Lista");
			System.out.println("4- Totale");
			System.out.println ("Digita x per terminare");

			BufferedReader scelta2  = new BufferedReader(new InputStreamReader(System.in));	
						
			int x= (char) scelta2.read();
			String prodotto = null;
			
			if (x=='x'){
	 
			 dati = "chiudi".getBytes();
			 pacchetto.setData(dati);
			 pacchetto.setLength(dati.length);
			 canaleUDP.send(pacchetto);
			 continua=false;
		
		 
			 //invio a GestioneSocio la stringa fine per liberare un occorrenza
			 	indirizzo = InetAddress.getByName("225.1.2.3");
				dati = "fine".getBytes();
				pacchetto = new DatagramPacket(dati,dati.length,indirizzo,3007);
				canale.send(pacchetto);
					
				for(int ik=0;ik==51000000;ik++);   
			
				dati = codice.getBytes();
				pacchetto = new DatagramPacket(dati,dati.length,indirizzo,3007);
				canale.send(pacchetto);
				canale.close();		 
		     }

	     
		     if (x=='1'){
			 String acquisto = "a";
			 System.out.println("Inserisci il prodotto che vuoi acquistare");
			 BufferedReader scelta3  = new BufferedReader(new InputStreamReader(System.in));
			 prodotto = scelta3.readLine();
			 acquisto = acquisto.concat(prodotto);
			 
			 dati = (acquisto).getBytes();
			 pacchetto.setData(dati);
			 pacchetto.setLength(dati.length);
			 canaleUDP.send(pacchetto);
		     }
		     if (x=='2'){
			 String acquisto = "r";	
			 
			 System.out.println("Inserisci il prodotto che vuoi eliminare");
			 BufferedReader scelta4  = new BufferedReader(new InputStreamReader(System.in)); 
			 prodotto = scelta4.readLine();
			 acquisto = acquisto.concat(prodotto);
			 
			 dati = (acquisto).getBytes();
			 pacchetto.setData(dati);
			 pacchetto.setLength(dati.length);
			 canaleUDP.send(pacchetto);
		     }	
	     
	     
		     if (x=='3'){
			 String acquisto = "l";	
			 
			 dati = (acquisto).getBytes();
			 pacchetto.setData(dati);
			 pacchetto.setLength(dati.length);
			 canaleUDP.send(pacchetto);	
		 
			 byte[] dat = new byte[1024];
			 int i=0;
			 boolean continua2 = true;
		 
			 while(continua2){
			     DatagramPacket pacchettoRicevi = new DatagramPacket(dat,dat.length);
			     canaleUDP.receive(pacchettoRicevi);
			     String converti = new String (pacchettoRicevi.getData(),0,
						   pacchettoRicevi.getLength());
			     System.out.println(converti); 
			     if (converti.equals("end"))
				 continua2 = false;
			     i++;
			 }
		 
			 System.out.println("ok. Ricevuta Lista");    
		 
		    
		     }

		     if (x=='4'){
			 String acquisto = "t";	
		 
			 dati = (acquisto).getBytes();
			 pacchetto.setData(dati);
			 pacchetto.setLength(dati.length);
			 canaleUDP.send(pacchetto);	
		 
			 byte[] dat = new byte[1024];
   
			 DatagramPacket pacchettoRicevi = new DatagramPacket(dat,dat.length);
			 canaleUDP.receive(pacchettoRicevi);
			 String converti = new String (pacchettoRicevi.getData(),0,
						       pacchettoRicevi.getLength());
			 System.out.println("totale :"+ converti + " euro"); 
	 
		     }	 
		     
		 }
	} catch (IOException e ) {System.out.println("errore durante Salvatempo" + e);}	    
	



    }

}
