// Progetto di Programmazione di rete A.A. 2003-2004 
// autore:Cristiano Anselmi n.226847

import java.net.*;
import java.io.*;

public class Client extends Thread {
    
   static  boolean ciclo= true;
    static boolean cicloRicevi = true;
    static    byte[] dat = new byte[1024];
 
 
   
  

    
    public static void main(String[] args)  {
		try{
		    String [] Lista= new String[100];
		    String tipo=null;
		    
		    System.out.println("digita tipo di cassa . 1 per Alimentari 2. per Giochi");
		    BufferedReader scelta0  = new BufferedReader(new InputStreamReader(System.in));	
		    int tipocassa = (char) scelta0.read();
		    
		    boolean continua= true;
		    int x=0;
		    InetAddress indirizzo = null;
		    try {
			indirizzo = InetAddress.getByName("225.1.2.3");
		    } catch (UnknownHostException e) {
			System.out.println(e);
			System.exit(0);
		    }

		    int porta = 3001;
		    
		    MulticastSocket canale = new MulticastSocket(porta);
		    canale.setInterface(InetAddress.getByName("127.10.10.12")); 
		    
		    canale.joinGroup(indirizzo);
		
	
		    if(tipocassa=='1'){
			tipo="Alimentari";
			byte[] richiesta = "alimentari".getBytes();
			DatagramPacket pacchetto = new DatagramPacket(richiesta,richiesta.length,indirizzo,3002);
			canale.send(pacchetto);
			
			byte[] dat = new byte[1024];
			x=0;
			continua = true;
	       
			while(continua){
			    DatagramPacket pacchettoRicevi = new DatagramPacket(dat,dat.length);
			    canale.receive(pacchettoRicevi);
			    System.out.println("ok");  
			    String converti =new String (pacchettoRicevi.getData(),0,
					      pacchettoRicevi.getLength());
			    System.out.println(converti);		
			    Lista[x]= converti;
	   
			    if (converti.equals("end"))
				continua = false;
			    x++;
			}
	       

			System.out.println("ok. Ricevuta Lista");
	        }

	       
		    if(tipocassa=='2'){
			tipo="Giochi";
			byte[] richiesta = "giochi".getBytes();
			DatagramPacket pacchetto = new DatagramPacket(richiesta,richiesta.length,indirizzo,3002);
			canale.send(pacchetto);
	       
			byte[] dat = new byte[1024];
			x=0;
			continua= true;
			while(continua){
			    DatagramPacket pacchettoRicevi = new DatagramPacket(dat,dat.length);
			    canale.receive(pacchettoRicevi);
			    String converti =new String (pacchettoRicevi.getData(),0,
							 pacchettoRicevi.getLength());
			    System.out.println(converti);		
			    Lista[x]= converti;
	   
			    if (converti.equals("end"))
				continua = false;
			    x++;
			}
	        
		
			System.out.println("ok. Ricevuta Lista");
	}
	       
		    canale.leaveGroup(indirizzo);
		    
	

	       
		    Ricevi c = new Ricevi(Lista,tipo);
		    c.start();
		    FineClient f = new FineClient();
		    f.start();
	       
		    while(ciclo){
			x= 0;
			System.out.println("1- Pagamento cliente con salvatempo");
			System.out.println("2- Pagamento cliente");
		   
			BufferedReader scelta  = new BufferedReader(new InputStreamReader(System.in));	

			x = (char) scelta.read();
			
			if ( x== '1'){
			  
			    
			    indirizzo = InetAddress.getByName("224.0.0.40");	
			    canale.setInterface(InetAddress.getByName("127.0.0.40")); 
			    canale.joinGroup(indirizzo); 
			 
			   
			    System.out.println("Introdurre il codice del salvatempo");
			    
			    	BufferedReader scelta2  = new BufferedReader(new InputStreamReader(System.in));	
				String codice  = scelta2.readLine();
				byte[] richiesta = codice.getBytes();
				DatagramPacket pacchetto = new DatagramPacket(richiesta,richiesta.length,indirizzo,3040);
				canale.send(pacchetto);
											    
				byte[] dat = new byte[1024];
				DatagramPacket pacchettoRicevi = new DatagramPacket(dat,dat.length);
				canale.receive(pacchettoRicevi);
				String converti =new String (pacchettoRicevi.getData(),0,
							     pacchettoRicevi.getLength());
			

				System.out.println("Il totale da pagare è " + converti);
				canale.leaveGroup(indirizzo);

		   }
	 
			if (x=='2'){
			    continua= true;
			    float tot =0;
			    String prodotto, sub1 ,sub2, sub3;
			    int ind, ind2, ind3,length, intero;
				float prod, decimale,dec=0;
			    BufferedReader input;
			    while(continua){
				System.out.println("Inserire prodotto o Tot per totale");
				
				input  = new BufferedReader(new InputStreamReader(System.in));
				prodotto=input.readLine();
				if(prodotto.equals("Tot"))
				    continua = false;
				else{
				    boolean bool=true;
				    int d=0;
				    while(bool){
					
					if(Lista[d]==null){
					    bool=false;
					System.out.println("prodotto non presente");
						}
					    else {
						if(Lista[d].equals(prodotto)){
					    bool=false;
					    ind = prodotto.indexOf("|");	
					    ind2 = prodotto.lastIndexOf("|"); 
					    sub1 =new String(prodotto.substring(ind+2, ind2-1));
					    ind3 = sub1.indexOf(".");
				
					    if(ind3 !=-1) {
						System.out.println(ind3);
						sub2= new String(sub1.substring(0,ind3));
						length = sub1.length();
						sub3= new String(sub1.substring(ind3+1, length));
						  intero =  Integer.parseInt(sub2);
						  decimale = ((Integer.parseInt(sub3)));
						  dec = (decimale/100);
						  prod = intero+dec;
						  tot=tot+prod;
					    }
					       else  {
						   intero = Integer.parseInt(sub1);
						   tot = tot + intero;
					    } 

					       
				   	}

					else
					    d++;
					    }	
					if(d==Lista.length){
					    bool=false;
						System.out.println("prodotto non presente");
					}
					
					
				    }	 //bool
				    
				}//else
	
		} 
			    System.out.println("Totale: " + tot + "euro");
			}  
			    
		    

}


		    cicloRicevi=false;
		      	indirizzo=InetAddress.getByName("224.10.10.10");
			canale.setInterface(InetAddress.getByName("127.10.10.10"));
			canale.joinGroup(indirizzo);
			


			       System.out.println("Sto spedendo a Ricevi il segnale di chiusura");
			       byte dati [] = new byte[1024];
			       dati = "chiudi".getBytes();
			       DatagramPacket pacchettoRisposta = 
				   new DatagramPacket(dati,dati.length,indirizzo,3040);
			       canale.send(pacchettoRisposta);
			      
			     
			   canale.close();
	
	
		    System.out.println("fine client");
		    
	}	catch (IOException e){System.out.println("errore nel client: " + e);}
	

    }
 
}

