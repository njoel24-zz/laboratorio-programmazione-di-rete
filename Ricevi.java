// Progetto di Programmazione di rete A.A. 2003-2004 
// autore:Cristiano Anselmi n.226847

import java.net.*;
import java.io.*;


public class Ricevi extends Client implements Runnable{

    String Lista [];
    String tipo;
    public Ricevi( String l [], String t){

	tipo=t;
	Lista=l;
	
      
    }




    public void run(){
   try {
  	int porta = 3040;
	InetAddress indirizzo = null;
	  indirizzo = InetAddress.getByName("224.10.10.10");
	       MulticastSocket canale = new MulticastSocket(porta);
	       canale.setInterface(InetAddress.getByName("127.10.10.10")); 
	       


	while(cicloRicevi) {
	 
	 canale.joinGroup(indirizzo);
	  
	    int i=0;
	    boolean continua = true;
	    
	    while(continua){
		byte dat []= new byte[1024];
		DatagramPacket pacchettoRicevi = new DatagramPacket(dat, dat.length);
		
		canale.receive(pacchettoRicevi);
		
		String converti =new String (pacchettoRicevi.getData(),0,
					     pacchettoRicevi.getLength());
	
			if (converti.equals("chiudi"))
			    continua = false;
	
		if(converti.equals(tipo)){
		    while(continua){
			DatagramPacket pacchettoRicevi2 = new DatagramPacket(dat,dat.length);
			canale.receive(pacchettoRicevi2);
			converti =new String (pacchettoRicevi2.getData(),0,
					      pacchettoRicevi2.getLength());
			System.out.println(converti);  
			Lista[i]= converti;
			if (converti.equals("end"))
			    continua = false;
			i++;
		    }  
		}


	       }

	       System.out.println("ok. Ricevuta Lista");
	     canale.leaveGroup(indirizzo);
		

}

	canale.close();
	System.out.println("fineRicevi");

	
   }   catch (IOException e){System.out.println(e);}

 
    }

}
