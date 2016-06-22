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
public interface OperacionesCuenta extends java.rmi.Remote
{
    public List <Cuenta> solicitarCuentasServidor () throws java.rmi.RemoteException;
    public double consultarCuenta(int idCliente,String tipoCuenta, int numeroCuenta) throws java.rmi.RemoteException;
//    public boolean ingresoCuenta (int idCliente,String tipoCuenta, int numeroCuenta,double ingreso)throws java.rmi.RemoteException;
//    public boolean retiroCuenta (int idCliente,String tipoCuenta, int numeroCuenta,double retiro)throws java.rmi.RemoteException;
    
    public boolean actualizarArchivoCuenta(int idCliente,String tipoOperacion,String tipoCuenta, int numeroCuenta,double valorModificado)throws java.rmi.RemoteException;
    
}
