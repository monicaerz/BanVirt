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
 */
public class ImplTarjetaCoordinador extends UnicastRemoteObject implements OperacionesTarjeta{

    List<Tarjeta> tarjetas;

    //----------Constructores--------

    public ImplTarjetaCoordinador(String name) throws RemoteException 
    {
        try {
            tarjetas = new ArrayList<>();
            System.out.println("Rebind Object " + name);
            Naming.rebind(name, this);
        } catch (Exception ex) {
            System.out.println("Exception: " + ex.getMessage());
            ex.printStackTrace();
        }   
    }
    
    public ImplTarjetaCoordinador() throws RemoteException 
    {
        tarjetas = new ArrayList<>();
    }
    
    //------------Metodos ---------------
    @Override
    public List<Tarjeta> solicitarTarjetasServidor() throws RemoteException {
        return tarjetas;
    }

    @Override
    public double consultarTarjeta(int idCliente, String tipoTarjeta, int numeroTarjeta) throws RemoteException 
    {
        double saldo = 0;
        
        for(Tarjeta t:tarjetas)
        {
            if(t.getNumeroTarjeta() == numeroTarjeta && t.getTipoTarjeta().equalsIgnoreCase(tipoTarjeta))
            {
                saldo = t.consultarTarjeta();
                return saldo;
            }
            
        }
        return saldo;
    }

    @Override
    public boolean pagoTarjeta(int idCliente, String tipoTarjeta, int numeroTarjeta, double pago) throws RemoteException 
    {
        boolean p = false;
        System.out.println("** Pago Tarjeta **");
        for(Tarjeta t:tarjetas)
        {
            if(t.getNumeroTarjeta() == numeroTarjeta && t.getTipoTarjeta().equalsIgnoreCase(tipoTarjeta))
            {
                t.pagoTarjeta(pago);
                p = true;
                return p;
            }
            
        }
        return p;
    }

    @Override
    public boolean debitoTarjeta(int idCliente, String tipoTarjeta, int numeroTarjeta, double debito) throws RemoteException 
    {
        boolean d = false;
        System.out.println("** Debito Tarjeta **");
        for(Tarjeta t:tarjetas)
        {
            if(t.getNumeroTarjeta() == numeroTarjeta && t.getTipoTarjeta().equalsIgnoreCase(tipoTarjeta))
            {
                t.debitoTarjeta(debito);
                d = true;
                return d;
            }
            
        }
        return d;
    }
    
    //-------Metodo Inicial---------------
    public void iniciarTarjeta () throws FileNotFoundException, IOException
    {
        String cadena;
                
        FileReader f = new FileReader("src/ArchivoServidorTarjetas/tarjetas.txt");
        BufferedReader b = new BufferedReader(f);
        while((cadena = b.readLine())!=null) 
        {
            StringTokenizer st = new StringTokenizer(cadena,";");
            
            Tarjeta tarjeta = new Tarjeta();
            tarjeta.setIdCliente(Integer.parseInt(st.nextToken()));
            System.out.println("* Id cliente "+tarjeta.getIdCliente());
            tarjeta.setNombreCliente(st.nextToken());
            System.out.println("* Nombre: "+tarjeta.getNombreCliente());
            tarjeta.setTipoTarjeta(st.nextToken());
            System.out.println("* Tipo Tarjeta: "+ tarjeta.getTipoTarjeta());
            tarjeta.setNumeroTarjeta(Integer.parseInt(st.nextToken()));
            System.out.println("* Numero Tarjeta: "+ tarjeta.getNumeroTarjeta());
            tarjeta.setSaldo(Double.parseDouble(st.nextToken()));
            System.out.println("* Saldo: "+ tarjeta.getSaldo());
                
            tarjetas.add(tarjeta);
            System.out.println("-> Tamano Tarjetas: "+ tarjetas.size());
            System.out.println(" ");
        }
        b.close();
        
               
    }

    @Override
    public boolean actualizarArchivoTarjeta(int idCliente, String tipoOperacion, 
            String tipoTarjeta, int numeroTarjeta, double valorModificado) throws RemoteException 
    {
        
        System.out.println(" ... Actualizar Archivo Tarjeta ... ");
               
        for(Tarjeta t:tarjetas)
        {
            if( t.getNumeroTarjeta() == numeroTarjeta && 
                    t.getTipoTarjeta().equalsIgnoreCase(tipoTarjeta))
            {
                if(tipoOperacion.equalsIgnoreCase("pagoTarjeta"))
                {
                    System.out.println("valor modificado: "+valorModificado);
                    t.pagoTarjeta(valorModificado);
                    //pagoTarjeta(idCliente, tipoTarjeta, numeroTarjeta, valorModificado);
                }
                if(tipoOperacion.equalsIgnoreCase("debitoTarjeta"))
                {
                    t.debitoTarjeta(valorModificado);
                    //debitoTarjeta(idCliente, tipoTarjeta, numeroTarjeta, valorModificado);
                }
            }
            
        }
             
        borrarArchivo("src/ArchivoServidorTarjetas/tarjetas.txt");
        
        String lineaConcatenada;
        FileWriter f;
        try {
            
            f = new  FileWriter("src/ArchivoServidorTarjetas/tarjetas.txt");
            BufferedWriter bw = new BufferedWriter(f);
            
            for(Tarjeta t:tarjetas)
            {
                lineaConcatenada = "";
                System.out.println("-> ID Cliente: "+ t.getIdCliente());
                lineaConcatenada = lineaConcatenada + Integer.toString(t.getIdCliente())+ ";";
                System.out.println("-> Nombre Cliente: "+ t.getNombreCliente());
                lineaConcatenada = lineaConcatenada + t.getNombreCliente()+ ";";
                System.out.println("-> Tipo Tarjeta: "+ t.getTipoTarjeta());
                lineaConcatenada = lineaConcatenada + t.getTipoTarjeta()+ ";";
                System.out.println("-> Numero Tarjeta: "+ t.getNumeroTarjeta());
                lineaConcatenada = lineaConcatenada + Integer.toString(t.getNumeroTarjeta())+ ";";
                System.out.println("-> Saldo: "+ t.getSaldo());
                lineaConcatenada = lineaConcatenada + Double.toString(t.getSaldo());

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
