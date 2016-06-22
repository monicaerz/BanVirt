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
import logica.ImplCuentaCoordinador;

/**
 *
 * @author sala_bd
 */
public class ConexionServidorCuentas {

    
    
    public static void main(String[] args) {
        
        try {
            
            final Registry registry = LocateRegistry.createRegistry(2000);
            ImplCuentaCoordinador cuentaRegistry = new ImplCuentaCoordinador("rmi://localhost:2000/ServidorCuentas");
                      
            cuentaRegistry.iniciarCuenta();
            
        } catch (RemoteException ex) {
            Logger.getLogger(ConexionServidorCuentas.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        } catch (IOException ex) {
            Logger.getLogger(ConexionServidorCuentas.class.getName()).log(Level.SEVERE, null, ex);
        }
        
                    
    }
    
}
