// Progetto di Programmazione di rete A.A. 2003-2004 
// autore:Cristiano Anselmi n.226847

import java.net.*;
import java.io.*;


public class FineClient extends Client implements Runnable{

   

 

    public void run(){

	

		try {
		    int porta = 3004;
		    InetAddress indirizzo = InetAddress.getByName("224.10.10.20");
		    MulticastSocket canale = new MulticastSocket(porta);
		    canale.setInterface(InetAddress.getByName("127.10.10.20"));
		    canale.joinGroup(indirizzo);
		    byte[] dat = new byte[1024];
		    DatagramPacket pacchettos = new DatagramPacket(dat,dat.length);
		    canale.receive(pacchettos);
		    ciclo=false;
		    canale.close();
    
		}	catch (IOException e){System.out.println(e);}
  
	
    }

}
