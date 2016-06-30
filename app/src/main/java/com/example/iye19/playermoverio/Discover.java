package com.example.iye19.playermoverio;

import android.os.AsyncTask;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * Created by iye19 on 15/03/2016.
 */
public class Discover extends AsyncTask <Object,  ArrayList<Servidor>, ArrayList<Servidor>>{
    final int LDATOS = 64;
    private Client client;

    /**
     * Tarea encargada del descubrimiento de servidores en la red local.
     * @param client Activity donde se actualizará la UI.
     * @throws IOException
     */
    Discover(Client client) throws IOException{
        this.client = client;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected ArrayList<Servidor> doInBackground(Object[] params) {
        ArrayList<Servidor> lista = new ArrayList<Servidor>();
        try {
            Enumeration<NetworkInterface> ifs = NetworkInterface.getNetworkInterfaces();
            DatagramSocket socket = new DatagramSocket();
            socket.setSoTimeout(5000);
            byte[] buf = "RemotePlayer_Discovery".getBytes();
            DatagramPacket p = new DatagramPacket(buf, buf.length, InetAddress.getByName("0.0.0.0"), 1235);
            while(ifs.hasMoreElements()) {
                List<InterfaceAddress> addrs = ifs.nextElement().getInterfaceAddresses();
                for(InterfaceAddress addr:addrs) {
                    p.setAddress(addr.getBroadcast());
                    if(p.getAddress()!=null)
                        socket.send(p);
                }
            }

            while(true){
                p.setData(new byte[LDATOS]);
                p.setLength(LDATOS);
                socket.receive(p);
                lista.add(new Servidor(p.getAddress().getHostAddress() + " " + new String(p.getData())));
                publishProgress(lista);
            }
        }
        catch(SocketTimeoutException toe){
        }
        catch(IOException ioe){
        }
        return lista;
    }

    @Override
    protected void onProgressUpdate(ArrayList<Servidor>... values) {
        super.onProgressUpdate(values);
        client.update(values[0]);
    }

    @Override
    protected void onPostExecute(ArrayList<Servidor> servidores) {
        super.onPostExecute(servidores);
        client.update(servidores);
    }
}
