/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logica;

import java.util.Vector;

/**
 *
 * @author Monica
 */
public class Bloqueo 
{
   private Object idObjeto;                // El objeto que es protegido por el bloqueo
   private Vector<String> propietarios;     // las TID de los propietarios
   private String tipoBloqueo;              // el tipo actual 

   public Bloqueo() 
   {
       this.propietarios = new Vector<String>();
   }

    public Object getIdObjeto() {
        return idObjeto;
    }

    public void setIdObjeto(Object idObjeto) {
        this.idObjeto = idObjeto;
    }

    public Vector<String> getPropietarios() {
        return propietarios;
    }

    public void setPropietarios(Vector<String> propietarios) {
        this.propietarios = propietarios;
    }

    public String getTipoBloqueo() {
        return tipoBloqueo;
    }

    public void setTipoBloqueo(String tipoBloqueo) {
        this.tipoBloqueo = tipoBloqueo;
    }
    
    public synchronized void adquiere(String trans, String unTipoBloqueo)
    {
	
       while(!propietarios.isEmpty()) {
           	try {
        	   	wait( );
        	   	System.out.println("edmakjsuajsd");
           }catch (InterruptedException e) {/*...*/ }
       }
       if (propietarios.isEmpty()) { // ning�n TID posee un bloqueo
          
    	   propietarios.addElement(trans);
           tipoBloqueo = unTipoBloqueo;
       } /*else if (/*otra transacci�n posee el bloqueo, lo comparte ) ) {  
            if (/*esta transacci�n no es un poseedor) propietarios.addElement(trans); 
    
       		} else if  (/* esta transacci�n es un poseedor pero necesita m�s de un bloqueo exclusivo){
       			tipoBloqueo.promueve( );
	        } */
   }
    
   public synchronized void libera (String trans )
   {
	   propietarios.removeElement(trans);     // elimina este poseedor
	   // establece el tipo de bloqueo a ninguno
	   notifyAll( );
    }
    
}
