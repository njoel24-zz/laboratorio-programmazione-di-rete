// Progetto di Programmazione di rete A.A. 2003-2004 
// autore:Cristiano Anselmi n.226847

public class Incrementa extends Server  {

    final int k= 3020;
    int conta = 0;
    int porta = 0;
    
 public synchronized int richiestaPorta () {  

     conta++;
     porta = k + conta;

 return porta;


 } 

 }
