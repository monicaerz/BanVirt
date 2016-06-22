/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conexion;

import java.rmi.Naming;
import logica.OperacionesCliente;

/**
 *
 * @author Monica
 */
public class ConexionCliente 
{

    public static void main(String[] args) 
    {
        try {
                       
            OperacionesCliente conexionCliente = (OperacionesCliente)Naming.lookup("rmi://localhost:2007/BanVirt");
                        
            String idTransaccion = "";
            idTransaccion = conexionCliente.inicioSesion(51905126,"Mama Gatita");
            
            //System.out.println("idTransaccion: "+idTransaccion);
            System.out.println("idTransaccion (conexion): "+ idTransaccion);
            conexionCliente.consultarCuenta(idTransaccion ,51905126, "corriente", 23298845);
            conexionCliente.consultarTarjeta(idTransaccion, 51905126, "Mastercard",2334444);
            
            conexionCliente.debitoTarjeta(idTransaccion, 51905126, "Mastercard", 2334444,100000);
            conexionCliente.consultarTarjeta(idTransaccion, 51905126, "Mastercard",2334444);
            conexionCliente.pagoTarjeta(idTransaccion, 51905126, "Mastercard", 2334444,100000);
            conexionCliente.consultarTarjeta(idTransaccion, 51905126, "Mastercard",2334444);
            /*conexionCliente.consultarCuenta(idTransaccion, 51905126, "corriente", 23298845);
            conexionCliente.ingresoCuenta(idTransaccion, 51905126, "corriente", 23298845,1000);
            conexionCliente.consultarCuenta(idTransaccion, 51905126, "corriente", 23298845);
            conexionCliente.retiroCuenta(idTransaccion, 51905126, "corriente", 23298845, 50);
            conexionCliente.pagoTarjeta(idTransaccion, 51905126, "corriente", 23298845, 15000);*/
            
            
            
        } catch (Exception e) {
            System.err.println("System exception client: " + e);
            e.printStackTrace();
        }
        
        
    }
    
}
