package com.example.iye19.playermoverio;

import android.os.AsyncTask;

import java.net.Socket;

/**
 * Created by iye19 on 06/04/2016.
 */
public class Connect extends AsyncTask<String, Object, Socket> {
    Explorer explorer;

    /**
     * Tarea encargada de establecer la conexión con el servidor.
     * @param explorer Activity que llama a la tarea.
     */
    Connect(Explorer explorer){
        this.explorer = explorer;
    }

    /**
     * Crea un socket conectado al servidor.
     * @param params Ip del servidor.
     * @return Socket creado.
     */
    @Override
    protected Socket doInBackground(String... params) {
        Socket s = null;

        try {
           s = new Socket(params[0], 1234);
        }
        catch (Exception ex){}
        return s;
    }

    /**
     * Actualiza el socket en la hebra principal.
     * @param socket
     */
    @Override
    protected void onPostExecute(Socket socket) {
        super.onPostExecute(socket);
        explorer.setSocket(socket);
    }
}
