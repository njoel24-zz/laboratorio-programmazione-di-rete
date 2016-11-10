// Progetto di Programmazione di rete A.A. 2003-2004 
// autore:Cristiano Anselmi n.226847

import java.net.*;
import java.io.*;

public class PagaSocio extends Thread {
    String codice;
    AggiornaTot at;
    PagaSocio(String c, AggiornaTot a){
	codice =c;
	at=a;
  }

    public void run(){
	InetAddress indirizzo = null;
	try {
	    indirizzo = InetAddress.getByName("224.0.0.40");

	
	int porta = 3040;
	
	MulticastSocket canale = new MulticastSocket(porta);
	canale.setInterface(InetAddress.getByName("127.0.0.40")); 
	  
	canale.joinGroup(indirizzo);

	byte[] dat = new byte[1024];
	DatagramPacket pacchetto = new DatagramPacket(dat,dat.length);
	System.out.println("Paga Socio avviato");
	System.out.println(codice);
	canale.receive(pacchetto);

	if ((new String (pacchetto.getData(),0,
				 pacchetto.getLength())).equals(codice)) {
	    
	    byte dati [] = new byte[1024];
	    float tot = at.returntot();

	    Float f = new Float(tot);
	    String s = f.toString();
	    dati = s.getBytes();
	    System.out.println("sto spedendo al client il totale " + tot);
	    DatagramPacket pacchettoRisposta = 
		new DatagramPacket(dati,dati.length,indirizzo,3001);
	    
	    canale.send(pacchettoRisposta);
		    
		}

	System.out.println("fine PagaSocio");
	canale.leaveGroup(indirizzo);
	canale.close();
	
	} catch (IOException e) {
	    System.out.println(e);
	    System.exit(0);
	}

}






	}
