/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conexion;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.logging.Level;
import java.util.logging.Logger;
import logica.ImplTarjetaCoordinador;


/**
 *
 * @author sala_bd
 */
public class ConexionServidorTarjetas {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) 
    {
        try {
            
            final Registry registry = LocateRegistry.createRegistry(2005);
            ImplTarjetaCoordinador tarjetaRegistry = new ImplTarjetaCoordinador("rmi://localhost:2005/ServidorTarjetas");
            
            tarjetaRegistry.iniciarTarjeta();
            
            
        } catch (RemoteException ex) {
            System.out.println("problema conexion servidor tarjeta");
            ex.printStackTrace();
        } catch (IOException ex) {
            Logger.getLogger(ConexionServidorTarjetas.class.getName()).log(Level.SEVERE, null, ex);
        }
            
        
    }
    
}
