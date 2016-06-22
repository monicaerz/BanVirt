/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logica;

import java.util.HashMap;

/**
 *
 * @author Monica
 */
public class GestorBloqueo 
{
    private HashMap<Object, Bloqueo> losBloqueos;
    private static int cont=0;

    //------- Constructor -------------------
    public GestorBloqueo() 
    {
        this.losBloqueos = new HashMap<>();
    }
    
    //------ Getter & Setter

    public static int getCont() {
        return cont;
    }

    public static void setCont(int cont) {
        GestorBloqueo.cont = cont;
    }
    
    
    //----Metodos ------------------
    public  void ponBloqueo(Object objeto, String trans, String tipoBloqueo)
    {
         Bloqueo bloqueoEncontrado = new Bloqueo();
         boolean encontro=false;
         synchronized (this){
        	 for(Object key: losBloqueos.keySet()){
        		 if(losBloqueos.get(key).getIdObjeto().equals(objeto)){
        			bloqueoEncontrado = losBloqueos.get(key);
        			encontro=true;
        		 }
        	 } 
        	 if(!encontro){
        		 bloqueoEncontrado.setIdObjeto(objeto);
        		 losBloqueos.put(cont, bloqueoEncontrado);
        		 cont++;
        	 }
        	 
        	 // busca el bloqueo asociado con el objeto
        	 // si no hay ninguno, lo crea y lo agrega a la tabla de dispersi�n
         }
         bloqueoEncontrado.adquiere(trans, tipoBloqueo);
    }
    
    //sincroniza este dado que queremos eliminar todas las entradas
   public synchronized void desBloqueo(String trans) 
   {
        
        for(Object key: losBloqueos.keySet()){
             Bloqueo unBloqueo = losBloqueos.get(key);
             if(unBloqueo.getPropietarios().contains(trans) ) unBloqueo.libera(trans);
        }
    }
    
}
