// Progetto di Programmazione di rete A.A. 2003-2004 
// autore:Cristiano Anselmi n.226847

import java.net.*;
import java.io.*;

public class Server extends Thread {
    
    static	String [] Giochi = new String [100] ;
    static	String [] Alimentari = new String [100];
    static	String [] Soci = new String [100];
  
    static boolean cicloGestioneCasse = true;
    static boolean cicloGestioneSocio = true;	 
    static  boolean cicloNuovoSocio = true;

 

    public static void main ( String args []){
	 for (int z = 0 ; z <= 99 ; z++) {
		Soci[z]="vuoto";

	    }


	int fine=0;
	char x = 0;
	carica();
	
	
	    try{
		MulticastSocket canale = new MulticastSocket();
		
		GestioneCasse s = new GestioneCasse();
		s.start();
		GestioneSocio t = new GestioneSocio();
		t.start();

		while (fine == 0){
		   
		
		    System.out.println("1- Aggiornamento prezzi");
		    System.out.println("2- Invio liste alle casse");
		    System.out.println("3- Chiusura");

		    BufferedReader scelta  = new BufferedReader(new InputStreamReader(System.in));	
		    x= (char) scelta.read();
		  
		
		    if (x =='1') {
			carica();
  
		    }

		    else if (x=='2'){

			InetAddress	indirizzo=InetAddress.getByName("224.10.10.10");
			canale.setInterface(InetAddress.getByName("127.10.10.10"));
			canale.joinGroup(indirizzo);
			
			int i=0;
			   while(Alimentari[i]!= null){
			       System.out.println("Sto spedendo alla cassa "+ Alimentari[i]);
			       byte dati [] = new byte[1024];
			       dati = Alimentari[i].getBytes();
			       DatagramPacket pacchettoRisposta = 
				   new DatagramPacket(dati,dati.length,indirizzo,3040);
			       canale.send(pacchettoRisposta);
			       i++;
			   }    
		
			i=0;
			
		  while(Giochi[i]!= null){
			System.out.println("Sto spedendo alla cassa "+ Giochi[i]);
			byte dati [] = new byte[1024];
			dati = Giochi[i].getBytes();
			DatagramPacket pacchettoRisposta = 
			new DatagramPacket(dati,dati.length,indirizzo,3040);
	    	  	
			canale.send(pacchettoRisposta);
			i++;
		  }
		    	canale.leaveGroup(indirizzo);
	  }

		    else if (x=='3'){
			fine = 1;

			//termino GestioneCasse
			cicloGestioneCasse = false;
			InetAddress	indirizzo=InetAddress.getByName("224.10.10.12");
			canale.setInterface(InetAddress.getByName("127.10.10.12"));
			canale.joinGroup(indirizzo);
			byte dati [] = new byte[1024];
			dati = "chiudi".getBytes();
			DatagramPacket    pacchetto = new DatagramPacket(dati,dati.length,indirizzo,3002);
			canale.send(pacchetto);
			canale.leaveGroup(indirizzo);
			
			//termino GestioneSocio

			cicloGestioneSocio = false;
			 indirizzo=InetAddress.getByName("225.1.2.3");
			canale.setInterface(InetAddress.getByName("127.10.10.14"));
			canale.joinGroup(indirizzo);
			byte dati2 [] = new byte[1024];
			dati2 = "chiu".getBytes();
		        pacchetto = new DatagramPacket(dati2,dati2.length,indirizzo,3007);
			canale.send(pacchetto);
			canale.leaveGroup(indirizzo);
			



			//chiudo i client
			 indirizzo=InetAddress.getByName("224.10.10.20");
			canale.setInterface(InetAddress.getByName("127.10.10.20"));
			canale.joinGroup(indirizzo);
			dati = "chiudi".getBytes();
		        pacchetto = new DatagramPacket(dati,dati.length,indirizzo,3004);
			canale.send(pacchetto);
			canale.leaveGroup(indirizzo);


			// chiudo NuovoSocio non strettamente necessario
			cicloNuovoSocio=false;
			    indirizzo=InetAddress.getByName("127.0.0.1");
			DatagramSocket canaleUDP = new DatagramSocket();
			dati = "chiudi".getBytes();
		        pacchetto = new DatagramPacket(dati,dati.length,indirizzo,3020);
			canaleUDP.send(pacchetto);
			canaleUDP.close();

			
			//chiudo PagaSocio non strettamente necessario
			indirizzo=InetAddress.getByName("224.0.0.40");
			canale.setInterface(InetAddress.getByName("127.0.0.40"));
			canale.joinGroup(indirizzo);
			dati = "chiudi".getBytes();
		        pacchetto = new DatagramPacket(dati,dati.length,indirizzo,3040);
			canale.send(pacchetto);
			canale.leaveGroup(indirizzo);
			
 
			canale.close();
		
		    }
	    
		    
		}  
	    }	catch (IOException e){System.out.println(e);}
 
   

  
}

    static  void carica(){
	// Carico le strutture dati dai file da inviare alle casse piu' la lista dei soci
	boolean continua = true;
	int i=0;
	try{

	    BufferedReader in= new BufferedReader(new FileReader("Giochi.txt")); 

	    i=0;
	    while(continua){
		Giochi[i] = in.readLine();
		
		while(continua){
		    Giochi[i] = in.readLine();
		    if (Giochi[i] == null)  {
			continua = false;
			in.close();
		    }
		    else
			i++;
		}	 
	    }	
	    
	    continua =true;
	    i=0;
	    BufferedReader in2= new BufferedReader(new FileReader("Alimentari.txt")); 
	    
	    while(continua) {
		Alimentari[i] = in2.readLine();
		if (Alimentari[i] == null) {
		    continua = false;
		    in2.close();
		}
		else
		    i++;
	    }
	    
	    BufferedReader in3= new BufferedReader(new FileReader("Lista_Soci.txt")); 
	    continua=true;
	    i=0;



		 	 while(continua) {
			     Soci[i] = in3.readLine();

			     if (Soci[i] == null) {
				 continua = false;
				 in3.close();
			     }
			     else
				 i++;
			 }	
			 
	}catch (IOException e){System.out.println(e);}
    }

} 

  
		
		   


	
	   



	  
		
	 
      








	








    
 
  

 

	    
	    
		 



	
	
	
 
		 

	
		 

	
    
	     


   
;
		

	


	


	
