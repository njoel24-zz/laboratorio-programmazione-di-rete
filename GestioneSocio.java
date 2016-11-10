
// Progetto di Programmazione di rete A.A. 2003-2004 
// autore:Cristiano Anselmi n.226847

import java.net.*;
import java.io.*;

public class GestioneSocio extends Server {
    int kap=0;
    Incrementa I;

     GestioneSocio() {
	 I = new Incrementa();
		}
  
    public void run(){
	try {
	    
	    String vuoto="vuoto";
	    String codicebase = "Open";
	    boolean Salvatempoocc [] = new boolean [10];

	    String codici [] = new String[10];

	    // inizializzo l'array dei codici;
	    int i=0;
	    for ( i=0;i<=8;i++){
		String sub1 = Soci[i].substring(0,2);
		codici[i]=sub1.concat(codicebase);
	   	}
	    		
	      
	      InetAddress indirizzo = null;
	try {
	    indirizzo = InetAddress.getByName("224.10.10.14");
	} catch (UnknownHostException e) {
	    System.out.println("nel creare indirizzo " + e);
	    System.exit(0);
	}

	int porta = 3007;
	MulticastSocket canale = new MulticastSocket(porta);
		canale.setInterface(InetAddress.getByName("127.10.10.14")); 

	  while(cicloGestioneSocio) {

	      canale.joinGroup(indirizzo);
	      byte[] dat = new byte[1024];
	      
	      DatagramPacket pacchettos = new DatagramPacket(dat,dat.length);
	      System.out.println("In attesa di ricevere");
	      canale.receive(pacchettos);
	      System.out.println("ricevo");	 
		  	  String Socio = new String(pacchettos.getData(),0,

					    pacchettos.getLength());
			  
			  String comando = Socio.substring(0,4);
			  
			  //un socio ha finito, libero una occorrenza in Salvatempoocc

			  if (comando.equals("fine")){
			      boolean boo=true;
			      int t=0;
			      byte dati2 []= new byte[768];
			      pacchettos = new DatagramPacket(dati2,dati2.length);
			      canale.receive(pacchettos);
			      //ricevo il codice
			      String z = new String(pacchettos.getData(),0,
						    pacchettos.getLength());
			      z= z.substring(0,6);
			      
			      while((boo ==true)&&(t<Salvatempoocc.length)){
				  if(Salvatempoocc[t]==true){
				      boolean b =codici[t].equals(z);
				      if(codici[t].equals(z)){
					  Salvatempoocc[t]=true;
					  boo=false;
					  System.out.println("liberata un occorrenza di Salvatempoocc");
					  canale.leaveGroup(indirizzo);
				      }
				      else
					  t++;
				  }
				  else
				      t++;
			      }	  
			      
			  }
			  
			  else  if (comando.equals("chiu")) {
			      System.out.println("ho ricevuto il segnale di chiusura");
			  }
			  else {
			      System.out.println("Si e' collegato un socio per richiesta codice salvatempo con indirizzo  "
						 +pacchettos.getAddress());
		 
			      boolean cicla = true;
			      int contatore=0;
			      boolean riscontro= false;
			      
			      while((cicla)&&(contatore< Soci.length)&&(Soci[contatore]!= null)){
				  
				  if (Soci[contatore].equals(Socio)){
				      cicla=false;
				      riscontro=true;
		  }
		      contatore++;
		  }
		  
		  if(Soci[contatore] ==  null){
		      System.out.println("Socio non presente");
		      	byte [] dati="Stop".getBytes(); 
			DatagramPacket comunica = new DatagramPacket(dati,dati.length,indirizzo, 3008);
			canale.send(comunica);
		  }
		  
		    if(riscontro){
			System.out.println("Il socio" + Socio + "è stato autorizzato");
			int k=0;
			boolean bool=true;
			while((bool ==true)&&(k<Salvatempoocc.length)){
			    if(Salvatempoocc[k]==false){
				Salvatempoocc[k]=true;
			
				bool=false;
				int portain = I.richiestaPorta();
				int portaout = I.richiestaPorta();
				NuovoSocio t= new NuovoSocio(codici[k] ,portain, portaout);
				Integer portai = new Integer(portain);
				Integer portao = new Integer(portaout);
				String pi = portai.toString();
				String po = portao.toString();
	
				byte [] dati=codici[k].getBytes(); 
				DatagramPacket comunica = new DatagramPacket(dati,dati.length,indirizzo, 3008);
				canale.send(comunica);

				dati=pi.getBytes(); 
				comunica = new DatagramPacket(dati,dati.length,indirizzo, 3008);
				canale.send(comunica);
				
				try {
				    sleep(1000);

				} catch (InterruptedException e) {System.out.println("eccezione in gestione Socio" + e); }
				dati=po.getBytes(); 
				comunica = new DatagramPacket(dati,dati.length,indirizzo, 3008);
				canale.send(comunica);
				
				
				t.start();	  
				System.out.println("E' stato assegnato al socio "+ Socio + "il codice del salvatempo " + codici[k]);
				
			    }
			    else 
				k++;
		}
			 
		    }
		 
		    canale.leaveGroup(indirizzo);
		
			  }
			 
	  }	
	  canale.leaveGroup(indirizzo);
	  canale.close();
	  System.out.println("fineGestioneSocio");
	
	} catch (IOException e) {System.out.println("eccezzione in gestione Socio" + e); }
 	}
    
    
}



		  
		  
		   
		  
		  
			

		   
		    
		   
		
	
