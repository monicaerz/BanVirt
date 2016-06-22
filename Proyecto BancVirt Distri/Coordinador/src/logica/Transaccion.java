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
 * @author Monica
 */
public class Transaccion 
{
    String          idTransaccion;
    long            tiempoInicial;
    long            tiempoFinal;
    long            tiempoConsumacion;
    int             estado; //0:Fase de Trabajo ; 1: Validación ; 2: Consumación; 3: Abortado
    Cliente         cliente;
    List<Operacion> operaciones;
    
    //---------Constructores-----------------

    public Transaccion() 
    {
        this.operaciones = new ArrayList<>();
    }

    public Transaccion(String idTransaccion, long tiempoInicial, int estado, Cliente cliente) {
        this.idTransaccion = idTransaccion;
        this.tiempoInicial = tiempoInicial;
        this.tiempoFinal = 0;
        this.tiempoConsumacion = 0;
        this.estado = estado;
        this.cliente = cliente;
    }

    
    //-------------Getter & Setter ----------------------

    public String getIdTransaccion() {
        return idTransaccion;
    }

    public void setIdTransaccion(String idTransaccion) {
        this.idTransaccion = idTransaccion;
    }

    public long getEstado() {
        return estado;
    }

    public long getTiempoInicial() {
        return tiempoInicial;
    }

    public void setTiempoInicial(long tiempoInicial) {
        this.tiempoInicial = tiempoInicial;
    }

    public long getTiempoFinal() {
        return tiempoFinal;
    }

    public void setTiempoFinal(long tiempoFinal) {
        this.tiempoFinal = tiempoFinal;
    }

    public long getTiempoConsumacion() {
        return tiempoConsumacion;
    }

    public void setTiempoConsumacion(long tiempoConsumacion) {
        this.tiempoConsumacion = tiempoConsumacion;
    }
    
           
    public void setEstado(int estado) {
        this.estado = estado;
    }
    
    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
    
    public List<Operacion> getOperaciones() {
        return operaciones;
    }

    public void setOperaciones(List<Operacion> operaciones) {
        this.operaciones = operaciones;
    }
    
    //---------Metodos-------------------
    public void agregarOperacion (Operacion operacion)
    {
        operaciones.add(operacion);
        System.out.println("Operación agregada : "+operacion.getTipoOperacion());
    }
    
   
    
    
}
