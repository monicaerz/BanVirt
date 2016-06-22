/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logica;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 *
 * @author Monica
 * Esta clase implementa la interfaz de OperacionesCuenta, que es la interfaz para que la clase conexionCoordinador
 * pueda traer el objeto Cuenta del ServidorCuentas
 */
public class ImplCuentaCoordinador extends UnicastRemoteObject implements OperacionesCuenta
{
    
    List<Cuenta> cuentas;

    //---------- Constructor(es)---------------------

    public ImplCuentaCoordinador(String name) throws RemoteException
    {
        try {
            cuentas = new ArrayList<>();
            
            System.out.println("Rebind Object " + name);
            Naming.rebind(name, this);
            
        } catch (Exception ex) {
            System.out.println("Exception: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
    
    
    public ImplCuentaCoordinador()  throws RemoteException
    {
        cuentas = new ArrayList<>();
    }
    
    //-------- Metodos de OperacionesCuenta ------------------------
    @Override
    public List<Cuenta> solicitarCuentasServidor() throws RemoteException
    {
        System.out.println("tam cuentas impl:" + cuentas.size());
        return cuentas;
    }
    @Override
    public double consultarCuenta(int idCliente, String tipoCuenta, int numeroCuenta) throws RemoteException 
    {
        double saldo = 0;
        
        for(Cuenta c:cuentas)
        {
            if(c.getNumeroCuenta() == numeroCuenta && c.getTipoCuenta().equalsIgnoreCase(tipoCuenta))
            {
                saldo = c.consultarCuenta();
            }
            break;
        }
        return saldo;
    }

//    @Override
/*    public boolean ingresoCuenta(int idCliente, String tipoCuenta, int numeroCuenta, double ingreso) throws RemoteException 
    {
        boolean i = false;
        System.out.println("** Ingreso Cuenta **");
        for(Cuenta c:cuentas)
        {
            if(c.getNumeroCuenta() == numeroCuenta && c.getTipoCuenta().equalsIgnoreCase(tipoCuenta))
            {
                c.ingresoCuenta(ingreso);
                i = true;
                return i;
            }
            
        }
        return i;
    }
//
//    @Override
    public boolean retiroCuenta(int idCliente, String tipoCuenta, int numeroCuenta, double retiro) throws RemoteException 
    {
        boolean r = false;
        System.out.println("** Retiro Cuenta **");
        for(Cuenta c:cuentas)
        {
            if(c.getNumeroCuenta() == numeroCuenta && c.getTipoCuenta().equalsIgnoreCase(tipoCuenta))
            {
                c.retiroCuenta(retiro);
                r = true;
                return r;
            }
            
        }
        return r;
    }*/
    
    //-------Metodo Inicial---------------
    public void iniciarCuenta () throws FileNotFoundException, IOException
    {
        String cadena;
                
        FileReader f = new FileReader("src/ArchivoServidorCuentas/cuentas.txt");
        BufferedReader b = new BufferedReader(f);
        while((cadena = b.readLine())!=null) 
        {
            StringTokenizer st = new StringTokenizer(cadena,";");
            
            Cuenta cuenta = new Cuenta(0, "", "", 0, 0);
            
            cuenta.setIdCliente(Integer.parseInt(st.nextToken()));
            System.out.println("* IdCliente: "+ cuenta.getIdCliente());
            cuenta.setNombreCliente(st.nextToken());
            System.out.println("* Nombre Cliente: "+ cuenta.getNombreCliente());
            cuenta.setTipoCuenta(st.nextToken());
            System.out.println("* Tipo Cuenta: "+ cuenta.getTipoCuenta());
            cuenta.setNumeroCuenta(Integer.parseInt(st.nextToken()));
            System.out.println("* Numero Cuenta: "+cuenta.getNumeroCuenta());
            cuenta.setSaldo(Double.parseDouble(st.nextToken()));
            System.out.println("* Saldo: "+ cuenta.getSaldo());
                
            cuentas.add(cuenta);
            System.out.println("-> Cuenta Tamaño: "+ cuentas.size());
            System.out.println(" ");
        }
        b.close();
        
                   
    }


    @Override
    public boolean actualizarArchivoCuenta(int idCliente, String tipoOperacion, String tipoCuenta, 
                                        int numeroCuenta, double valorModificado) throws RemoteException 
    {
        System.out.println(" ... Actualizar Archivo Cuenta ... ");
        for(Cuenta c:cuentas)
        {
            if(c.getNumeroCuenta() == numeroCuenta && c.getTipoCuenta().equalsIgnoreCase(tipoCuenta))
            {
                if(tipoOperacion.equalsIgnoreCase("ingresoCuenta"))
                {
                    c.ingresoCuenta(valorModificado);
                    //ingresoCuenta(idCliente, tipoCuenta, numeroCuenta, valorModificado);

                }
                if(tipoOperacion.equalsIgnoreCase("retiroCuenta"))
                {
                    c.retiroCuenta(valorModificado);
//                    retiroCuenta(idCliente, tipoCuenta, numeroCuenta, valorModificado);
                }
            }
        }
            
        borrarArchivo("src/ArchivoServidorCuentas/cuentas.txt");
        
        String lineaConcatenada;
        FileWriter f;
        try {
            
            f = new  FileWriter("src/ArchivoServidorCuentas/cuentas.txt");
            BufferedWriter bw = new BufferedWriter(f);
            
            for(Cuenta c:cuentas)
            {
                lineaConcatenada = "";
                System.out.println("-> ID Cliente: "+ c.getIdCliente());
                lineaConcatenada = lineaConcatenada + Integer.toString(c.getIdCliente())+ ";";
                System.out.println("-> Nombre Cliente: "+ c.getNombreCliente());
                lineaConcatenada = lineaConcatenada + c.getNombreCliente()+ ";";
                System.out.println("-> Tipo Cuenta: "+ c.getTipoCuenta());
                lineaConcatenada = lineaConcatenada + c.getTipoCuenta()+ ";";
                System.out.println("-> Numero Cuenta: "+ c.getNumeroCuenta());
                lineaConcatenada = lineaConcatenada + Integer.toString(c.getNumeroCuenta())+ ";";
                System.out.println("-> Saldo: "+ c.getSaldo());
                lineaConcatenada = lineaConcatenada + Double.toString(c.getSaldo());

                bw.write(lineaConcatenada);
                bw.newLine();
                         
            }
             bw.close();
            
        } catch (IOException ex) {
            System.err.println("IOException " + ex);
            ex.printStackTrace();
        }      
        
        return true;
    }
    
    public void borrarArchivo(String rutaArchivo)
    {
        try {
            
            FileWriter f;
            f = new  FileWriter(rutaArchivo);
            
            BufferedWriter bw = new BufferedWriter(f);
            bw.write("");
            
            bw.close();
            //f.close();
            
        } catch (IOException ex) {
            System.err.println("IOException " + ex);
            ex.printStackTrace();
        }
    }
    
    
      
    
}
