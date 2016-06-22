/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conexion;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;
import logica.Coordinador;
import logica.Cuenta;
import logica.OperacionesCuenta;
import logica.OperacionesTarjeta;
import logica.Tarjeta;


/**
 *
 * @author sala_bd
 */
public class ConexionCoordinador 
{
    
    public static void main(String[] args) 
    {
        try {
            
            final Registry registry = LocateRegistry.createRegistry(2007);
            Coordinador coordinadorRegistry = new Coordinador("rmi://localhost:2007/BanVirt");
            
            //System.out.println("Buscando Objeto ");
            
            OperacionesCuenta conexionServidorCuentas = (OperacionesCuenta)Naming.lookup("rmi://10.5.2.164:2000/ServidorCuentas");
            OperacionesTarjeta conexionServidorTarjeta = (OperacionesTarjeta)Naming.lookup("rmi://10.5.2.164:2005/ServidorTarjetas");
                       
            List<Cuenta> cuentasServidor = new ArrayList<>();
                         cuentasServidor = conexionServidorCuentas.solicitarCuentasServidor();
           
            // System.out.println("cuentasServidor: "+ cuentasServidor.size());  
             
                    
            List<Tarjeta> tarjetasServidor = new ArrayList<>();
                    tarjetasServidor = conexionServidorTarjeta.solicitarTarjetasServidor();
            
            
            coordinadorRegistry.iniciarCoordinador(cuentasServidor, tarjetasServidor,
                            conexionServidorCuentas,conexionServidorTarjeta);
            //System.out.println("cuentas tarjetas: " + tarjetasServidor);
            //coordinadorRegistry.iniciarCoordinador(tarjetasServidor);
            
             
            
            
        } catch (Exception e) {
            System.err.println("System exception !!!!: " + e);
            e.printStackTrace();
        }
        
        
        
    }
    
}
