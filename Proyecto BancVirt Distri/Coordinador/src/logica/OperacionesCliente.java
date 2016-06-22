/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logica;

/**
 *
 * @author Monica
 * Por medio de esta clase el Cliente se comunica con el Coordinador, solicitando las transacciones que desea realizar
 */

public interface OperacionesCliente extends java.rmi.Remote
{
    public String inicioSesion(int idCliente,String nombreCliente) throws java.rmi.RemoteException;
    public void cerrarSesion(String idTransaccion,int idCliente) throws java.rmi.RemoteException;
    public boolean confirmarTransaccion(String idTransaccion,int idCliente)throws java.rmi.RemoteException;
    public boolean abortar(String idTransaccion)throws java.rmi.RemoteException;
    
    public double consultarCuenta(String idTransaccion,int idCliente,String tipoCuenta, int numeroCuenta) throws java.rmi.RemoteException;
    public boolean ingresoCuenta (String idTransaccion,int idCliente,String tipoCuenta, int numeroCuenta,double ingreso)throws java.rmi.RemoteException;
    public boolean retiroCuenta (String idTransaccion,int idCliente,String tipoCuenta, int numeroCuenta,double retiro)throws java.rmi.RemoteException;
    public double consultarTarjeta (String idTransaccion,int idCliente,String tipoTarjeta, int numeroTarjeta)throws java.rmi.RemoteException;
    public boolean pagoTarjeta (String idTransaccion,int idCliente,String tipoTarjeta, int numeroTarjeta,double pago)throws java.rmi.RemoteException;
    public boolean debitoTarjeta (String idTransaccion,int idCliente,String tipoTarjeta, int numeroTarjeta,double debito)throws java.rmi.RemoteException;
}




