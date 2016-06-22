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
public class Cuenta implements Serializable
{
    int    idCliente;
    String nombreCliente;
    String tipoCuenta;
    int    numeroCuenta;
    double saldo;
    
    //-----------Constructor------------

    public Cuenta() 
    {
    }
    
    
    public Cuenta(int idCliente, String nombreCliente, String tipoCuenta, int numeroCuenta, double saldo) 
    {
        this.idCliente = idCliente;
        this.nombreCliente = nombreCliente;
        this.tipoCuenta = tipoCuenta;
        this.numeroCuenta = numeroCuenta;
        this.saldo = saldo;
    }
    
    
    //-----Getter & Setter--------------
    public int getIdCliente ()    
    {
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

      
    public String getTipoCuenta() {
        return tipoCuenta;
    }

    public void setTipoCuenta(String tipoCuenta) {
        this.tipoCuenta = tipoCuenta;
    }

    public int getNumeroCuenta() {
        return numeroCuenta;
    }

    public void setNumeroCuenta(int numeroCuenta) {
        this.numeroCuenta = numeroCuenta;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {    
        this.saldo = saldo;
    }

    //------Transacciones -----------
    public double consultarCuenta() {
        return saldo;       
    }
    
    public void ingresoCuenta (double ingreso)
    {
        saldo = saldo + ingreso;
    }
    
    public void retiroCuenta (double retiro)
    {
        if( saldo > retiro)
        {
            saldo = saldo - retiro;
        }
        else
        {
            System.out.println("No es posible hacer un retiro de la cuenta: "+ numeroCuenta);
        }
    }
    
}