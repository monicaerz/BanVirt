/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logica;

/**
 *
 * @author Monica
 */
public class Operacion 
{
    int    idCliente;
    String tipoOperacion; //consultar, pago ....
    String tipoObjeto; //Cuenta - Tarjeta
    int idObjetoModificado;
    double valorModificado;
    
    //-----Constructor-------------

    public Operacion(int idCliente, String tipoOperacion, String tipoObjeto, 
                        int idObjetoModificado, double valorModificado) 
    {
        this.idCliente = idCliente;
        this.tipoOperacion = tipoOperacion;
        this.tipoObjeto = tipoObjeto;
        this.idObjetoModificado = idObjetoModificado;
        this.valorModificado = valorModificado;
    }

    //-----------Getter & Setter ------------------------------

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public String getTipoOperacion() {
        return tipoOperacion;
    }

    public void setTipoOperacion(String tipoOperacion) {
        this.tipoOperacion = tipoOperacion;
    }

    public String getTipoObjeto() {
        return tipoObjeto;
    }

    public void setTipoObjeto(String tipoObjeto) {
        this.tipoObjeto = tipoObjeto;
    }

    
    public int getIdObjetoModificado() {
        return idObjetoModificado;
    }

    public void setIdObjetoModificado(int idObjetoModificado) {
        this.idObjetoModificado = idObjetoModificado;
    }

    public double getValorModificado() {
        return valorModificado;
    }

    public void setValorModificado(double valorModificado) {
        this.valorModificado = valorModificado;
    }
    
    
    
    
    
}
