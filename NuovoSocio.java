// Progetto di Programmazione di rete A.A. 2003-2004 
// autore:Cristiano Anselmi n.226847


import java.net.*;
import java.io.*;


public class NuovoSocio extends Server {
    boolean cicla = true;
    int contatore=0;
    AggiornaTot at = new AggiornaTot();
    String codice;
    float tot=0;
    int ind,ind2,ind3,length,intero=0;
    String sub1, sub2, sub3;
    float decimale,dec,prod;
    int portain, portaout=0;

    NuovoSocio(String c ,int pi, int po){
	portain=pi;
	portaout=po;
	codice = c;
	PagaSocio p= new PagaSocio(codice, at);
	p.start();
    }

public  void run(){

   int cont=0;
   String Prodotti []= new String [150];

   for (int x= 0; x<=149; x++){
       Prodotti[x]="vuoto";
 }
       
       //comunicazioni
   try   {    
       System.out.println("portain nuovoSocio" + portain);
       System.out.println("portaout nuovoSocio" + portaout);
       DatagramSocket server = new DatagramSocket (portain);

       String localhost= "127.0.0.1";
       
       InetAddress indirizzo = InetAddress.getByName(localhost);

       
	  //algoritmo 
	   while(cicloNuovoSocio){ 
	     
	   byte[] dati = new byte[1024];
	   DatagramPacket pacchetto = 
		    new DatagramPacket(dati, dati.length);
	   
	   server.receive(pacchetto);
			
	   String richiesta0 = new String(pacchetto.getData());
	   String comando = richiesta0.substring(0,6);
	   System.out.println("comando" + comando);
	   if (comando.equals("chiudi"))
	       cicloNuovoSocio=false;
	   

  
	   else{

	   pacchetto.setLength(dati.length);
	   
	   String operazione =  richiesta0.substring(0,1);
	   String richiesta = richiesta0.substring(1, richiesta0.length());
		
	   System.out.println("operazione " + operazione);
	   System.out.println("prodotto " + richiesta);
	   
	   if (operazione.equals("a")) {
	       cont = 0;
	       boolean continua=true;
	       if (richiesta == null)
		   System.out.println("digitare prodotto ");
	       else{
		   
		   while(continua){
		   
		       if(Prodotti[cont].equals("vuoto")){
			   Prodotti[cont]=richiesta;
			   continua=false;
	    
		       }  	   
		       
		       cont++; 
		   } 
	       } 
	      
	    
	        ind = richiesta.indexOf("|");	
		ind2 = richiesta.lastIndexOf("|"); 
		sub1 =new String(richiesta.substring(ind+2, ind2-1));
		ind3 = sub1.indexOf(".");
		if(ind3 !=-1) {
		    sub2= new String(sub1.substring(0,ind3));
		    length = sub1.length();
		    sub3= new String(sub1.substring(ind3+1, length));
		    intero =  Integer.parseInt(sub2);
		    decimale = ((Integer.parseInt(sub3)));
		    dec = (decimale/100);
		    prod = intero+dec;
		    tot=tot+prod;
		    at.aggiornaFloat(prod);
		    contatore ++;
		    
		}
		else{
		    
		    intero = Integer.parseInt(sub1);
		    tot = tot + intero;
		    at.aggiornaInt(intero);
		    contatore++;
		 
	       }

	      }  	   
		   
		if (operazione.equals("r")) {
		    cicla = true;
		    int  contatore2=0;
		    
		    while(cicla) {
			if  (  Prodotti[contatore2].equals(richiesta)){
			    Prodotti[contatore2]="vuoto";
			    cicla=false;
			    System.out.println("prodotto" + richiesta + "rimosso");
			}
			else
			   contatore2++;
			
		   }	

		      ind = richiesta.indexOf("|");	
		ind2 = richiesta.lastIndexOf("|"); 
		sub1 =new String(richiesta.substring(ind+2, ind2-1));
		ind3 = sub1.indexOf(".");
		if(ind3 !=-1) {
		    sub2= new String(sub1.substring(0,ind3));
		    length = sub1.length();
		    sub3= new String(sub1.substring(ind3+1, length));
		    intero =  Integer.parseInt(sub2);
		    decimale = ((Integer.parseInt(sub3)));
		    dec = (decimale/100);
		    prod = intero+dec;
		    tot=tot-prod;
		    contatore ++; 
		}
		else{
		    
		    intero = Integer.parseInt(sub1);
		    tot = tot - intero;
		    contatore++;
		} 

		}


	        if (operazione.equals("l")) {
		    contatore=0;
		    boolean continua=true;
		    while(contatore != 149){
			
			if(Prodotti[contatore].equals("vuoto")) {
			    contatore++;
					}	
		  
			else{
			    System.out.println("Sto spedendo al socio " + Prodotti[contatore]);
			    byte dati2 [] = new byte[1024];
			    dati2 = Prodotti[contatore].getBytes();
			    DatagramPacket pacchettoInvio = 
				new DatagramPacket(dati2,dati2.length,indirizzo,portaout);
			    server.send(pacchettoInvio);
			    contatore++;
			}
			
		    }
		    byte  dati3 [] = "end".getBytes();
		    System.out.println("Sto spedendo al socio end");
		    DatagramPacket pacchettoInvio = 
			new DatagramPacket(dati3,dati3.length,indirizzo,portaout);
		    server.send(pacchettoInvio);
		    
		}
		
		
	        if (operazione.equals("t")) {
		 
			   
			
		    
		    	System.out.println("totale : "+ tot);
			Float f = new Float(tot);
			String s = f.toString();
			
			 byte  dati3 [] = s.getBytes();
			
			 DatagramPacket pacchettoInvio = 
			     new DatagramPacket(dati3,dati3.length,indirizzo,portaout);
			 server.send(pacchettoInvio);
		    
		    
		} 
		
     	    
		}

	 	}
       System.out.println("fineNuovoSocio");
       server.close();
  
	}catch (IOException e){System.out.println("Problema in NuovoSocio " + e);}
}

}
	    

  
  
