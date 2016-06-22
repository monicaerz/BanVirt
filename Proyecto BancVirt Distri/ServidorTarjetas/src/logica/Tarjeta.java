/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logica;

import java.io.Serializable;

/**
 *
 * @author sala_bd
 */
public class Tarjeta implements Serializable
{
    int    idCliente;
    String nombreCliente;
    String tipoTarjeta;
    int    numeroTarjeta;
    double saldo;
    
    //------Constructor-----------------------

    public Tarjeta() 
    {
    }

    
      
    public Tarjeta(int idCliente, String nombreCliente, String tipoTarjeta, int numeroTarjeta, double saldo) 
    {
        this.idCliente = idCliente;
        this.nombreCliente = nombreCliente;
        this.tipoTarjeta = tipoTarjeta;
        this.numeroTarjeta = numeroTarjeta;
        this.saldo = saldo;
    }
    
    
    
   //-------Getter & Setter ------------------ 

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }
    
       
    public String getTipoTarjeta() {
        return tipoTarjeta;
    }

    public void setTipoTarjeta(String tipoTarjeta) {
        this.tipoTarjeta = tipoTarjeta;
    }

    public int getNumeroTarjeta() {
        return numeroTarjeta;
    }

    public void setNumeroTarjeta(int numeroTarjeta) {
        this.numeroTarjeta = numeroTarjeta;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }
    
    //------Transacciones -----------
    public double consultarTarjeta ()
    {
        System.out.println(" -- consultarTarjeta: Servidor --");
        return saldo;       
    }
    
    public void pagoTarjeta (double pago)
    {
        System.out.println(" -- pagoTarjeta: Servidor --");
        saldo = saldo + pago;
    }
    
    public void debitoTarjeta (double debito)
    {
        System.out.println(" -- debitoTarjeta: Servidor --");
        if( saldo > debito)
        {
            saldo = saldo - debito;
        }
        else
        {
            System.out.println("No es posible hacer un debito para la tarjeta: "+ numeroTarjeta);
        }
    }
         
}
