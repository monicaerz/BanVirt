/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logica;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author sala_bd
 */
public class Cliente 
{
    int           idCliente;
    String        nombreCliente;
    String        contraseña;
    List<Tarjeta> tarjetas;
    List<Cuenta>  cuentas;
    
    //----------Constructor---------------

    public Cliente(int idCliente, String nombreCliente, String constraseña) 
    {
        this.idCliente = idCliente;
        this.nombreCliente = nombreCliente;
        this.contraseña = constraseña;
        tarjetas = new ArrayList<>();
        cuentas = new ArrayList<>();
    }
    
    public Cliente() 
    {
        tarjetas = new ArrayList<>();
        cuentas = new ArrayList<>();
    }
     
    //----Getter & Setter ----------

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

    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }

    public List<Tarjeta> getTarjetas() {
        return tarjetas;
    }

    public void setTarjetas(List<Tarjeta> tarjetas) {
        this.tarjetas = tarjetas;
    }

    public List<Cuenta> getCuentas() {
        return cuentas;
    }

    public void setCuentas(List<Cuenta> cuentas) {
        this.cuentas = cuentas;
    }
    
    
}
