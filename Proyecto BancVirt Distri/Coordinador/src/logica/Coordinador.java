/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logica;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Monica
 */
public class Coordinador extends UnicastRemoteObject implements OperacionesCliente
{
    HashMap <Integer, Cliente> clientes; //copia temporal
    HashMap <String, Transaccion> transacciones; 
    static int contador = 0;
    GestorBloqueo gBloqueo;
    OperacionesCuenta operacionesCuenta;
    OperacionesTarjeta operacionesTarjeta;
    
    //-----------Constructores--------------------
    public Coordinador(String name) throws RemoteException 
    {
        try {
            clientes = new HashMap<>();
            transacciones = new HashMap<>();
            gBloqueo = new GestorBloqueo();
            
            System.out.println("Rebind Object " + name);
            Naming.rebind(name, this);
            
        } catch (Exception ex) {
            System.out.println("Exception: " + ex.getMessage());
            ex.printStackTrace();
        } 
    }

    public Coordinador() throws RemoteException 
    {
        clientes = new HashMap<>();
        transacciones = new HashMap<>();
    }
    
    
    //----------- Getter & Setter ---------------------

    public HashMap<Integer, Cliente> getClientes() {
        return clientes;
    }

    public void setClientes(HashMap<Integer, Cliente> clientes) {
        this.clientes = clientes;
    }
        
    //_---------------------------------------------
    //----------- Metodos Sesion --------------------------
    
    @Override
    public String inicioSesion(int idCliente,String nombreCliente)
    {
        System.out.println("--- Inicio Sesion --- ");
        String idTransaccion = "";
        long tiempoActual = System.currentTimeMillis();
        
        System.out.println("* Id Cliente: "+ idCliente);
        
        if(clientes.containsKey(idCliente) && clientes.get(idCliente).getNombreCliente().equalsIgnoreCase(nombreCliente))
        {
                if(!transacciones.isEmpty())
                {
                    Iterator itTransaccion = transacciones.keySet().iterator();
                    while(itTransaccion.hasNext())
                    {
                        String key = (String) itTransaccion.next();
                        if(clientes.get(idCliente).getIdCliente() == transacciones.get(key).getCliente().getIdCliente())
                        {
                            if(transacciones.get(key).getEstado()== 0)
                            {
                                System.out.println("key recuperacion: " +key);
                                return key;
                            }
                            if(transacciones.get(key).getEstado()==1)
                            {
                                transacciones.get(key).setEstado(3);
                            }
                        }
                    }
                    
                }
            
                contador = contador + 1;

                Transaccion transaccion = new Transaccion();
                transaccion.setCliente(clientes.get(idCliente));
                transaccion.setIdTransaccion(Integer.toString(idCliente)+Integer.toString(contador));
                System.out.println("* Id transaccion: "+ transaccion.getIdTransaccion());
                transaccion.setTiempoInicial(tiempoActual);
                transaccion.setEstado(0);

                idTransaccion = transaccion.getIdTransaccion();
                transacciones.put(idTransaccion, transaccion);
                System.out.println("* Contador: "+ contador);
                
                
    //            System.out.println("tam transaccion: "+transacciones.size());
        }
        //imprimirTransacciones();
        
        return idTransaccion;
    }
    
    @Override
    public void cerrarSesion(String idTransaccion,int idCliente) throws RemoteException 
    {
        System.out.println("--- Cerrar Sesion --- ");
        //imprimirTransacciones();
        boolean consumacion = false;
        
        System.out.println("* Id Cliente: "+ idCliente);
        
        Iterator<String> itTransaccion = transacciones.keySet().iterator();
        while(itTransaccion.hasNext())
        {
            String key = (String) itTransaccion.next();
            if(key.equalsIgnoreCase(idTransaccion))
            {
                consumacion = consumarTransaccion(idTransaccion);
                if(consumacion)
                {
                    transacciones.get(key).setEstado(2);
                                     
                }
                else
                {
                    transacciones.get(key).setEstado(3);
                }
                
            } 
        }
        imprimirTransacciones();
        System.out.println("- FIN: Cerrar Sesion - "+ idTransaccion);
        System.out.println(" ");
    }

    //---------Metodos Transacciones ----------------
    @Override
    public boolean confirmarTransaccion(String idTransaccion, int idCliente) throws RemoteException 
    {
        System.out.println(" --- Confirmar Transaccion ---");
        System.out.println("*ID Transaccion: "+ idTransaccion);
        System.out.println("*ID Cliente: " + idCliente);
        
        long tiempoActualValidacion = System.currentTimeMillis();
        System.out.println("*Tiempo Validacion: "+ tiempoActualValidacion);
        boolean validacion = false;
        
        Iterator<String> itTransaccion = transacciones.keySet().iterator();
        while(itTransaccion.hasNext())
        {
            String key = (String) itTransaccion.next();
            if(key.equalsIgnoreCase(idTransaccion))
            {
                transacciones.get(key).setTiempoFinal(tiempoActualValidacion);
                transacciones.get(key).setEstado(1);
                validacion = validacionTransaccion(idTransaccion,transacciones.get(key).getTiempoInicial(),
                             tiempoActualValidacion, idCliente);
                
            } 
        }
        //imprimirTransacciones();
        
        return validacion;
    }
    
    public boolean validacionTransaccion(String idTransaccion,long tiempoInicial,long tiempoFinal,int idCliente) throws RemoteException 
    {
        System.out.println(" --- Validar Transaccion ---");
        System.out.println("*ID Transaccion: "+ idTransaccion);
        System.out.println("*ID Cliente: " + idCliente);
        
        boolean validacion = false;
        List<Operacion> operacionesAux = new ArrayList<>();
        List<Operacion> opAuxTransaccion = transacciones.get(idTransaccion).getOperaciones();
        String tipoObjAux = "";
        int idObjMAux = 0;
                             
        Iterator<String> itTransaccion = transacciones.keySet().iterator();
        while(itTransaccion.hasNext())
        {
            String key = (String) itTransaccion.next();
            if(transacciones.get(key).getEstado()== 2) //Operaciones Consumadas
            {   
                System.out.println("id t: "+idTransaccion);
                System.out.println(tiempoInicial >= transacciones.get(key).getTiempoInicial());
                System.out.println(tiempoFinal > transacciones.get(key).getTiempoConsumacion());
                if(tiempoInicial >= transacciones.get(key).getTiempoInicial() && 
                   (tiempoFinal > transacciones.get(key).getTiempoConsumacion()))
                {
                    operacionesAux = transacciones.get(key).getOperaciones();
                    for(Operacion op:operacionesAux)
                    {
                        System.out.println(op.getTipoOperacion().contains("consultar"));
                        if(!op.getTipoOperacion().contains("consultar"))
                        {
                            System.out.println("-> Tipo Operacion: "+ op.getTipoOperacion());
                            tipoObjAux = op.getTipoObjeto();
                            System.out.println("-> Tipo Objeto: "+ tipoObjAux);
                            idObjMAux = op.getIdObjetoModificado();
                            System.out.println("-> Objeto Modificado: "+ idObjMAux);

                            for(Operacion op2:opAuxTransaccion)
                            {   System.out.println("op2 tipo obj "+op2.getTipoObjeto());
                                System.out.println("idobjmaux: "+ idObjMAux);
                                System.out.println("op2. obj mod: "+op2.getIdObjetoModificado());
                                if(tipoObjAux.equalsIgnoreCase(op2.getTipoObjeto()) && idObjMAux == op2.getIdObjetoModificado())
                                {
                                    System.out.println("key: "+idTransaccion);
                                    transacciones.get(idTransaccion).setEstado(3);
                                    System.out.println("La transacción: "+idTransaccion+ " ABORTO su estado es: "+ "3");
                                    return validacion; 
                                }
                            }
                        }
                        
                    }
                }
                
            }
        }
        validacion = true;      
        return validacion;
    }
    
    public boolean consumarTransaccion(String idTransaccion) throws RemoteException
    {
        System.out.println(" --- Consumar Transaccion ---");
        System.out.println("*ID Transaccion: "+ idTransaccion);
               
        boolean consumacion = false;
        long tiempoActualConsumacion = 0;
        Bloqueo bloqueo = new Bloqueo();
        int idClienteAux = 0;
        
        if(transacciones.get(idTransaccion).getEstado()== 1)
        {
            transacciones.get(idTransaccion).setEstado(2);
            System.out.println("*Estado Transaccion: "+ "2");
            tiempoActualConsumacion = System.currentTimeMillis();
            transacciones.get(idTransaccion).setTiempoConsumacion(tiempoActualConsumacion);
            System.out.println("*Tiempo Consumacion: "+ tiempoActualConsumacion);

            List<Operacion> operacionesAux = transacciones.get(idTransaccion).getOperaciones();

            for(Operacion op:operacionesAux)
            {
                if(!op.getTipoOperacion().contains("consultar"))
                {
                    System.out.println("Tipo OBj: "+op.getTipoObjeto());
                    if(op.getTipoObjeto().equalsIgnoreCase("Cuenta"))
                    {
                        System.out.println("-> Tipo Objeto: "+ op.getTipoObjeto());
                        System.out.println("-> ID Objeto Modificado: "+ op.getIdObjetoModificado());
                        gBloqueo.ponBloqueo(op.getIdObjetoModificado(), idTransaccion, "escritura");
                        idClienteAux = op.getIdCliente();
                        System.out.println("*ID Cliente: "+idClienteAux);
                        Iterator<Integer> itcliente = clientes.keySet().iterator();
                        while(itcliente.hasNext())
                        {
                                              
                            for(Cuenta c:clientes.get(itcliente.next()).getCuentas())
                            {
                                if(c.getNumeroCuenta() == op.getIdObjetoModificado())
                                {
                                    if(op.getTipoOperacion().contains("ingreso"))
                                    {
                                        System.out.println("-> Tipo Operacion: "+op.getTipoOperacion());

                                        boolean opcuen=false;

                                        c.ingresoCuenta(op.getValorModificado());

                                        while(!opcuen )
                                        {
                                            try {

                                                    opcuen = operacionesCuenta.actualizarArchivoCuenta(idClienteAux,op.getTipoOperacion(),
                                                            c.getTipoCuenta(),c.getNumeroCuenta(),
                                                            op.getValorModificado());

                                            } catch (RemoteException ex) {
    //                                            Logger.getLogger(Coordinador.class.getName()).log(Level.SEVERE, null, ex);
                                                 System.out.println("Servidor Caido");
                                                try {
                                                    operacionesCuenta = (OperacionesCuenta)Naming.lookup("rmi://10.5.2.164:2000/ServidorCuentas");
                                                } catch (NotBoundException ex1) {
                                                    Logger.getLogger(Coordinador.class.getName()).log(Level.SEVERE, null, ex1);
                                                } catch (MalformedURLException ex1) {
                                                    Logger.getLogger(Coordinador.class.getName()).log(Level.SEVERE, null, ex1);
                                                } catch (RemoteException ex1) {
                                                    System.out.println("Intentando conexion con el servidor");
                                                }
                                            }
                                        }
                                        consumacion = true;
        //                                operacionesCuenta.ingresoCuenta(idClienteAux, c.getTipoCuenta(),
        //                                                                c.getNumeroCuenta(),op.getValorModificado());

                                    }
                                    if(op.getTipoOperacion().contains("retiro"))
                                    {
                                        System.out.println("-> Tipo Operacion: "+op.getTipoOperacion());

                                        boolean opcuen=false;
                                        c.retiroCuenta(op.getValorModificado());
                                        while(!opcuen )
                                        {
                                            try {
                                                    opcuen = operacionesCuenta.actualizarArchivoCuenta(idClienteAux,op.getTipoOperacion(),
                                                            c.getTipoCuenta(),c.getNumeroCuenta(),
                                                            op.getValorModificado());

                                            } catch (RemoteException ex) {
    //                                            Logger.getLogger(Coordinador.class.getName()).log(Level.SEVERE, null, ex);
                                                 System.out.println("Servidor Caido");
                                                try {
                                                    operacionesCuenta = (OperacionesCuenta)Naming.lookup("rmi://10.5.2.164:2000/ServidorCuentas");
                                                } catch (NotBoundException ex1) {
                                                    Logger.getLogger(Coordinador.class.getName()).log(Level.SEVERE, null, ex1);
                                                } catch (MalformedURLException ex1) {
                                                    Logger.getLogger(Coordinador.class.getName()).log(Level.SEVERE, null, ex1);
                                                } catch (RemoteException ex1) {
                                                    System.out.println("Intentando conexion con el servidor");
                                                }
                                            }
                                        }
                                        consumacion = true;

    //                                    operacionesCuenta.actualizarArchivoCuenta(idClienteAux,op.getTipoOperacion() ,
    //                                                                    c.getTipoCuenta(),c.getNumeroCuenta(),
    //                                                                    op.getValorModificado());
        //                                operacionesCuenta.retiroCuenta(idClienteAux, c.getTipoCuenta(),
        //                                                                c.getNumeroCuenta(),op.getValorModificado());
                                    }
                                }
                            } 
                        }
                        gBloqueo.desBloqueo(idTransaccion);
                    }

                    if(op.getTipoObjeto().equalsIgnoreCase("Tarjeta"))
                    {
                        System.out.println("-> Tipo Objeto: "+ op.getTipoObjeto());
                        System.out.println("-> ID Objeto Modificado: "+ op.getIdObjetoModificado());
                        gBloqueo.ponBloqueo(op.getIdObjetoModificado(), idTransaccion, "escritura");
                        idClienteAux = op.getIdCliente();
                        System.out.println("*ID Cliente: "+idClienteAux);
                        Iterator<Integer> itcliente = clientes.keySet().iterator();
                        while(itcliente.hasNext())
                        {
                            for(Tarjeta t:clientes.get(itcliente.next()).getTarjetas())
                            {
                                if(t.getNumeroTarjeta() == op.getIdObjetoModificado())
                                {
                                    if(op.getTipoOperacion().contains("pago"))
                                    {
                                        System.out.println("-> Tipo Operacion: "+op.getTipoOperacion());

                                        boolean optarj=false;
                                        t.pagoTarjeta(op.getValorModificado());

                                        while(!optarj )
                                        {
                                            try {
                                                    optarj = operacionesTarjeta.actualizarArchivoTarjeta(idClienteAux,op.getTipoOperacion(),
                                                            t.getTipoTarjeta(),t.getNumeroTarjeta(),
                                                            op.getValorModificado());
                                                System.out.println("optraj : ---------------------------" + optarj);
                                            } catch (RemoteException ex) {
    //                                            Logger.getLogger(Coordinador.class.getName()).log(Level.SEVERE, null, ex);
                                                 System.out.println("Servidor Caido");
                                                try {
                                                    operacionesTarjeta = (OperacionesTarjeta)Naming.lookup("rmi://10.5.2.164:2005/ServidorTarjetas");
                                                } catch (NotBoundException ex1) {
                                                    Logger.getLogger(Coordinador.class.getName()).log(Level.SEVERE, null, ex1);
                                                } catch (MalformedURLException ex1) {
                                                    Logger.getLogger(Coordinador.class.getName()).log(Level.SEVERE, null, ex1);
                                                } catch (RemoteException ex1) {
                                                    System.out.println("Intentando conexion con el servidor");
                                                }
                                            }
                                        }
                                        consumacion = true;

    //                                    operacionesTarjeta.actualizarArchivoTarjeta(idClienteAux,op.getTipoOperacion() ,
    //                                                                    t.getTipoTarjeta(),t.getNumeroTarjeta(),
    //                                                                    op.getValorModificado());

        //                                operacionesTarjeta.pagoTarjeta(idClienteAux, t.getTipoTarjeta(),
        //                                                                t.getNumeroTarjeta(), op.getValorModificado());

                                    }
                                    if(op.getTipoOperacion().contains("debito"))
                                    {
                                        System.out.println("-> Tipo Operacion: "+op.getTipoOperacion());
                                        System.out.println("valor modificado: "+op.getValorModificado());

                                        boolean optarj=false;
                                        t.debitoTarjeta(op.getValorModificado());

                                        while(!optarj )
                                        {
                                            try {
                                                    optarj = operacionesTarjeta.actualizarArchivoTarjeta(idClienteAux,op.getTipoOperacion(),
                                                            t.getTipoTarjeta(),t.getNumeroTarjeta(),
                                                            op.getValorModificado());

                                            } catch (RemoteException ex) {
    //                                            Logger.getLogger(Coordinador.class.getName()).log(Level.SEVERE, null, ex);
                                                 System.out.println("Servidor Caido");
                                                try {
                                                    operacionesTarjeta = (OperacionesTarjeta)Naming.lookup("rmi://10.5.2.164:2005/ServidorTarjetas");
                                                } catch (NotBoundException ex1) {
                                                    Logger.getLogger(Coordinador.class.getName()).log(Level.SEVERE, null, ex1);
                                                } catch (MalformedURLException ex1) {
                                                    Logger.getLogger(Coordinador.class.getName()).log(Level.SEVERE, null, ex1);
                                                } catch (RemoteException ex1) {
                                                    System.out.println("Intentando conexion con el servidor");
                                                }
                                            }
                                        }
                                        consumacion = true;

        //                                operacionesTarjeta.debitoTarjeta(idClienteAux, t.getTipoTarjeta(),
        //                                                                t.getNumeroTarjeta(), op.getValorModificado());
    //                                    operacionesTarjeta.actualizarArchivoTarjeta(idClienteAux,op.getTipoOperacion() ,
    //                                                                    t.getTipoTarjeta(),t.getNumeroTarjeta(),
    //                                                                    op.getValorModificado());

    //                                    consumacion = true;
                                    }
                                }
                            }
                        }
                        gBloqueo.desBloqueo(idTransaccion);
                    }

                }else{
                    consumacion= true;
                }

            }
        }else
        {
            if(transacciones.get(idTransaccion).getEstado()== 0)
            {
                transacciones.get(idTransaccion).setEstado(3);
                System.out.println("La transacción fue Aborta pues no fue validada");
            }
        }
                
        return consumacion;
    }
    
    @Override
    public boolean abortar(String idTransaccion) throws RemoteException 
    {
        boolean abortar = true;
        System.out.println(" --- Abortar Transaccion ---");
        System.out.println("*ID Transaccion: "+ idTransaccion); 
        
        transacciones.get(idTransaccion).setEstado(3);
        return abortar;
    }
    
   
       
    //-------------Inicio Coordinador ----------------
    
    public void iniciarCoordinador(List<Cuenta> cuentas, List<Tarjeta> tarjetas,
                                    OperacionesCuenta conexionServidorCuenta,
                                    OperacionesTarjeta conexionServidorTarjeta )
    {
        operacionesCuenta = conexionServidorCuenta;
        operacionesTarjeta = conexionServidorTarjeta;
        
        Integer idAux = 0;
        Cuenta nCuenta = new Cuenta();
        Tarjeta nTarjeta = new Tarjeta();
        Cliente nCliente = new Cliente();
        
        
        Iterator<Cuenta> itCuenta = cuentas.iterator();
        while(itCuenta.hasNext())
        {
            Cuenta c = itCuenta.next();
            idAux = c.getIdCliente();
            System.out.println("* IdCliente: "+idAux);
            
            if(!clientes.containsKey(idAux))
            {          
                
                nCuenta = new Cuenta(c.getIdCliente(), c.getNombreCliente(),c.getTipoCuenta(), c.getNumeroCuenta(), 
                                     c.getSaldo());
                
                imprimirCuenta(nCuenta);
                
                   
                nCliente = new Cliente(c.getIdCliente(), c.getNombreCliente(),Integer.toString(c.getIdCliente()));
                nCliente.getCuentas().add(nCuenta);
                
                System.out.println("Contraseña: "+Integer.toString(c.getIdCliente()));
                System.out.println("Agrego cliente con ID = "+ idAux);
                clientes.put(c.getIdCliente(), nCliente);
                
                System.out.println("Tamaño clientes = "+clientes.size());
                //imprimirClientes();
                System.out.println(" ");
                
            }
            else
            {
                //System.out.println("Else : Cuenta Nueva ");
                
                nCuenta = new Cuenta(c.getIdCliente(), c.getNombreCliente(),c.getTipoCuenta(), c.getNumeroCuenta(), 
                                     c.getSaldo());
                
                imprimirCuenta(nCuenta);
                
                clientes.get(c.getIdCliente()).getCuentas().add(nCuenta);
                //imprimirClientes();
                
            }
              
        }
        idAux = 0;
        
        Iterator<Tarjeta> itTarjeta = tarjetas.iterator();
        while(itTarjeta.hasNext())
        {
            Tarjeta t = itTarjeta.next();
            idAux = t.getIdCliente();
            System.out.println("* IdCliente: "+idAux);
            
            if(!clientes.containsKey(idAux))
            {
                
                nTarjeta = new Tarjeta(t.getIdCliente(),t.getNombreCliente(), t.getTipoTarjeta(), 
                                        t.getNumeroTarjeta(), t.getSaldo());
                
                imprimirTarjeta(nTarjeta);
                
                nCliente = new Cliente(t.getIdCliente(), t.getNombreCliente(),Integer.toString(t.getIdCliente()));
                nCliente.getTarjetas().add(nTarjeta);
                
                System.out.println("Agrego cliente con ID = "+ idAux);
                clientes.put(t.getIdCliente(), nCliente);
                
                System.out.println("Tamaño clientes = "+clientes.size());
                System.out.println(" ");
                
            }
            else
            {
                //System.out.println("Else : Cuenta Nueva ");
                
                nTarjeta = new Tarjeta(t.getIdCliente(),t.getNombreCliente(), t.getTipoTarjeta(), 
                                        t.getNumeroTarjeta(), t.getSaldo());
                
                imprimirTarjeta(nTarjeta);
                
                clientes.get(t.getIdCliente()).getTarjetas().add(nTarjeta);
            }
        }
        
        imprimirClientes();
        
    }
    
    //--------Métodos Cliente ------------------------
    
    @Override
    public double consultarCuenta(String idTransaccion,int idCliente, String tipoCuenta, int numeroCuenta) throws RemoteException 
    {
        System.out.println(" --- Consultar Cuenta --- ");
        System.out.println("*Id Transaccion: "+idTransaccion);
        
        double saldo = 0;
        List<Cuenta> cuentasAux = new ArrayList<>();
        Iterator<Integer> itcliente = clientes.keySet().iterator();
        while(itcliente.hasNext())
        {
            cuentasAux = clientes.get(itcliente.next()).getCuentas();
        
            for(Cuenta c:cuentasAux)
            {
                System.out.println("ID Cliente: "+ c.getIdCliente());

                if(c.getNumeroCuenta() == numeroCuenta && c.getTipoCuenta().equalsIgnoreCase(tipoCuenta))
                {
                    saldo = c.consultarCuenta();
                    Operacion operacion = new Operacion(idCliente,"consultarCuenta","cuenta", numeroCuenta, 0);
                    System.out.println("*Id Transaccion: "+ transacciones.get(idTransaccion).getIdTransaccion());
                    transacciones.get(idTransaccion).getOperaciones().add(operacion);
                    System.out.println("*# Cuenta: "+c.getNumeroCuenta());
                    System.out.println("*Saldo: "+ saldo);
                    System.out.println(" ");

                    return saldo;
                    //imprimirOperaciones(idTransaccion);
                }
            }    
           // break;
        }
        
        return saldo;
    }

    @Override
    public boolean ingresoCuenta(String idTransaccion,int idCliente, String tipoCuenta, int numeroCuenta, double ingreso) throws RemoteException
    {
        boolean i = false;
        System.out.println(" --- Ingreso Cuenta --- ");
        List<Cuenta> cuentasAux = new ArrayList<>();
        
        Iterator<Integer> itcliente = clientes.keySet().iterator();
        while(itcliente.hasNext())
        {
            cuentasAux = clientes.get(itcliente.next()).getCuentas();
        
            for(Cuenta c:cuentasAux)
            {
                if(c.getNumeroCuenta() == numeroCuenta && c.getTipoCuenta().equalsIgnoreCase(tipoCuenta))
                {
                  //  c.ingresoCuenta(ingreso);
                    Operacion operacion = new Operacion(idCliente, "ingresoCuenta", "cuenta", numeroCuenta, ingreso);
                    transacciones.get(idTransaccion).getOperaciones().add(operacion);
                    System.out.println("Transaccion: " + idTransaccion +"Ingreso a la cuenta: "+c.getNumeroCuenta()+
                            "el valor de: "+ingreso
                                        );
                    i = true;
                    return i;

                }
            }
        }
        return i;
    }

    @Override
    public boolean retiroCuenta(String idTransaccion,int idCliente, String tipoCuenta, int numeroCuenta, double retiro) throws RemoteException 
    {
        System.out.println(" --- Retiro Cuenta --- ");
        boolean r = false;
        List<Cuenta> cuentasAux = new ArrayList<>();
        
        Iterator<Integer> itcliente = clientes.keySet().iterator();
        while(itcliente.hasNext())
        {
            cuentasAux = clientes.get(itcliente.next()).getCuentas();
        
        
        
            for(Cuenta c:cuentasAux)
            {
                if(c.getNumeroCuenta() == numeroCuenta && c.getTipoCuenta().equalsIgnoreCase(tipoCuenta))
                {
                   //c.retiroCuenta(retiro);
                    Operacion operacion = new Operacion(idCliente, "retiroCuenta", "cuenta", numeroCuenta, retiro);
                    transacciones.get(idTransaccion).getOperaciones().add(operacion);

                    if(c.getSaldo()< retiro)
                    {
                        System.out.println("No es posible hacer un retiro de esta cuenta : Saldo insuficiente");
                    }else
                    {
                        System.out.println("Transaccion: " + idTransaccion +"Retiro cuenta: "+c.getNumeroCuenta()+" el valor de: "+retiro
                                        );
                        r = true;
                        return r;
                    }


                }
            }    //break;
        }
        return r;
    }

    @Override
    public double consultarTarjeta(String idTransaccion,int idCliente, String tipoTarjeta, int numeroTarjeta) throws RemoteException 
    {
        System.out.println(" --- Consultar Tarjeta --- ");
        System.out.println("* Id Transaccion: "+idTransaccion);
        double saldo = 0;
        
        List<Tarjeta> tarjetasAux = new ArrayList<>();
        
        Iterator<Integer> itcliente = clientes.keySet().iterator();
        while(itcliente.hasNext())
        {
            tarjetasAux = clientes.get(itcliente.next()).getTarjetas();
        
            for(Tarjeta t:tarjetasAux)
            {
                System.out.println("* ID Cliente: "+ t.getIdCliente());

                if(t.getNumeroTarjeta() == numeroTarjeta && t.getTipoTarjeta().equalsIgnoreCase(tipoTarjeta))
                {

                    saldo = t.consultarTarjeta();
                    System.out.println("* SaldoTarjeta: "+saldo);
                    Operacion operacion = new Operacion(idCliente, "consultarTarjeta", "tarjeta", numeroTarjeta, 0);
                    System.out.println("* Id Transaccion: "+transacciones.get(idTransaccion).getIdTransaccion());
                    transacciones.get(idTransaccion).getOperaciones().add(operacion);
                    System.out.println("* SaldoTarjeta: "+ saldo);
                    System.out.println(" ");
                  //  imprimirOperaciones(idTransaccion);
                    break;
                }
            }//break;
        }
        return saldo; 
    }

    @Override
    public boolean pagoTarjeta(String idTransaccion,int idCliente, String tipoTarjeta, int numeroTarjeta, double pago) throws RemoteException 
    {
        boolean p = false;
        System.out.println(" --- Pago Tarjeta --- ");
        System.out.println("* Id Transaccion: "+idTransaccion);
        List<Tarjeta> tarjetasAux = new ArrayList<>();
        
        Iterator<Integer> itcliente = clientes.keySet().iterator();
        while(itcliente.hasNext())
        {
            tarjetasAux = clientes.get(itcliente.next()).getTarjetas();
        
            for(Tarjeta t:tarjetasAux)
            {
                System.out.println("* ID Cliente: "+ t.getIdCliente());
                if(t.getNumeroTarjeta() == numeroTarjeta && t.getTipoTarjeta().equalsIgnoreCase(tipoTarjeta))
                {
                   // t.pagoTarjeta(pago);
                    Operacion operacion = new Operacion(idCliente, "pagoTarjeta", "tarjeta", numeroTarjeta, pago);
                //    imprimirOperaciones(idTransaccion);
                    transacciones.get(idTransaccion).getOperaciones().add(operacion);
                    System.out.println("Transaccion: " + idTransaccion +"Pago tarjeta: "+t.getNumeroTarjeta()+" el valor de: "+pago 
                                        );
                    p = true;
                    return p;
                }
            }
        }    
        return p;    
    }

    @Override
    public boolean debitoTarjeta(String idTransaccion,int idCliente, String tipoTarjeta, int numeroTarjeta, double debito) throws RemoteException 
    {
        boolean d = false;
        System.out.println(" --- Debito Tarjeta --- ");
        System.out.println("* Id Transaccion: "+idTransaccion);
        List<Tarjeta> tarjetasAux = new ArrayList<>();
        
        Iterator<Integer> itcliente = clientes.keySet().iterator();
        while(itcliente.hasNext())
        {
            tarjetasAux = clientes.get(itcliente.next()).getTarjetas();
        
            for(Tarjeta t:tarjetasAux)
            {
                if(t.getNumeroTarjeta() == numeroTarjeta && t.getTipoTarjeta().equalsIgnoreCase(tipoTarjeta))
                {
                    //t.debitoTarjeta(debito);
                    Operacion operacion = new Operacion(idCliente, "debitoTarjeta", "tarjeta", numeroTarjeta, debito);
            //        imprimirOperaciones(idTransaccion);
                    transacciones.get(idTransaccion).getOperaciones().add(operacion);
                    if(t.getSaldo()< debito)
                    {
                        System.out.println("No es posible hacer un debitp de esta tarjeta : Saldo insuficiente");
                    }else
                    {
                        System.out.println("Transaccion: " + idTransaccion +"Debito tarjeta: "+t.getNumeroTarjeta()+" el valor de: "+debito
                                        );
                        d = true;
                        return d;
                    }
                }
            }
            
        }    
        return d;
    }
    
    //----------Metodos Auxiliares ---------------------------
    
    public void imprimirClientes()
    {
        Iterator itClientes = clientes.keySet().iterator();
        while(itClientes.hasNext())
        {
            Integer key = (Integer) itClientes.next();
            System.out.println("  ");
            System.out.println("... Clientes ... ");
            System.out.println("ID Cliente : " + key);
            System.out.println("-> Nombre: " + clientes.get(key).getNombreCliente()); 
            System.out.println("-> Numero de Cuentas: " + clientes.get(key).getCuentas().size());
            System.out.println("-> Numero de Tarjetas: " + clientes.get(key).getTarjetas().size());
            System.out.println("  ");
                   
        }
        
    }
    
    public void imprimirTransacciones()
    {
        Iterator itTransaccion = transacciones.keySet().iterator();
        while(itTransaccion.hasNext())
        {
            String key = (String) itTransaccion.next();
            System.out.println("  ");
            System.out.println(".. Transacciones ... ");
            System.out.println("ID Transaccion: "+ transacciones.get(key).getIdTransaccion());
            System.out.println("-> Tiempo Inicial: "+ transacciones.get(key).getTiempoInicial());
            System.out.println("-> Tiempo Final: "+ transacciones.get(key).getTiempoFinal());
            System.out.println("-> Estado: "+ transacciones.get(key).getEstado());
            System.out.println("-> # Operaciones: "+ transacciones.get(key).getOperaciones().size());
            System.out.println("-> ID Cliente: "+ transacciones.get(key).getCliente().getIdCliente());
            System.out.println("-> Nombre Cliente: "+ transacciones.get(key).getCliente().getNombreCliente());
            System.out.println("  ");
        }
        if(transacciones.isEmpty())
        {
            System.out.println("No hay transacciones ");
        }
    }
    
    public void imprimirOperaciones(String idTransaccion)
    {
        List<Operacion> operacionesAux = new ArrayList<>();
        operacionesAux = transacciones.get(idTransaccion).getOperaciones();
        
        for(Operacion op:operacionesAux)
        {
            System.out.println(" ");
            System.out.println("... Operaciones ...");
            System.out.println("ID Transaccion: "+ idTransaccion);
            System.out.println("-> ID Cliente: "+ op.getIdCliente());
            System.out.println("-> Tipo Objeto: " + op.getTipoObjeto());
            System.out.println("-> Tipo Operacion: " + op.getTipoOperacion());
            System.out.println("-> Id Objeto Modificado: " + op.getIdObjetoModificado());
            System.out.println("-> Valor Modificado: "+ op.getValorModificado());
            System.out.println("  ");
        }
            
        
    }
    public void imprimirCuenta(Cuenta c)
    {
        System.out.println("--- Cuenta --- ");
        System.out.println("IdCliente: "+ c.getIdCliente());
        System.out.println("NombreCliente: "+c.getNombreCliente());
        System.out.println("TipoCuenta: "+c.getTipoCuenta());
        System.out.println("# Cuenta: "+c.getNumeroCuenta());
        System.out.println("Saldo: "+c.getSaldo());
        System.out.println("----------------");
        //System.out.println(" ");
    }
    
    public void imprimirTarjeta(Tarjeta t)
    {
        System.out.println("--- Tarjeta --- ");
        System.out.println("IdCliente: "+ t.getIdCliente());
        System.out.println("NombreCliente: "+t.getNombreCliente());
        System.out.println("TipoTarjeta: "+t.getTipoTarjeta());
        System.out.println("# Tarjeta: "+t.getNumeroTarjeta());
        System.out.println("Saldo: "+t.getSaldo());
        System.out.println("----------------");
        System.out.println(" ");
    }

    

    
}
