package com.example.iye19.playermoverio;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by iye19 on 15/03/2016.
 */
public class Servidor{
    private InetAddress ip;
    private String nombre;

    Servidor(String servidor){
        String[] aux = servidor.split(" ");
        if(aux.length>=2){
            try {
                ip = InetAddress.getByName(aux[0]);
            }
            catch (UnknownHostException ex){}
            nombre = aux[1];
        }
    }

    public InetAddress getIp() {
        return ip;
    }

    public String getNombre() {
        return nombre;
    }


    @Override
    public boolean equals(Object o) {
        if(o instanceof Servidor)
            if(ip.equals(((Servidor)o).getIp()) && nombre.equals(((Servidor)o).getNombre()))
                    return true;
        return false;
    }

    @Override
    public String toString() {
        return (nombre);
    }
}
