/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logica;

import java.util.List;

/**
 *
 * @author Monica
 */
public interface OperacionesTarjeta extends java.rmi.Remote
{
    public List <Tarjeta> solicitarTarjetasServidor () throws java.rmi.RemoteException;
    public double consultarTarjeta (int idCliente,String tipoTarjeta, int numeroTarjeta)throws java.rmi.RemoteException;
//    public boolean pagoTarjeta (int idCliente,String tipoTarjeta, int numeroTarjeta,double pago)throws java.rmi.RemoteException;
//    public boolean debitoTarjeta (int idCliente,String tipoTarjeta, int numeroTarjeta,double debito)throws java.rmi.RemoteException;
    
    public boolean actualizarArchivoTarjeta(int idCliente,String tipoOperacion,String tipoTarjeta, int numeroTarjeta,double valorModificado)throws java.rmi.RemoteException;
}
