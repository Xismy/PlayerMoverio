package com.example.iye19.playermoverio;

import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.IOException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;

/**
 * Created by iye19 on 06/04/2016.
 */

public class Reader extends AsyncTask <Object, Object , String>{
    private Explorer explorer;
    private BufferedReader input;

    /**
     * Tarea encargada de la recepción de mensajes enviados por el servidor.
     * @param explorer Activity de la clase Explorer donde se actualizará la UI.
     * @param bufferedReader Entrada del Socket conectado al servidor.
     */
    Reader(Explorer explorer, BufferedReader bufferedReader){
        this.explorer = explorer;
        input = bufferedReader;
    }

    /**
     * Recive respuestas del servidor y las manda a la hebra principal.
     * @param params No se utiliza.
     * @return Razón por la que finaliza el bucle.
     */
    @Override
    protected String doInBackground(Object[] params) {
        String type= "";
        ArrayList<String> response;
        boolean end = false;
        String reason = "Desconectado";

        while(!end) {
            response = new ArrayList<>();
            try {
                type = input.readLine();
                String line = input.readLine();
                if(type.equals("disconnect")) {
                    end = true;
                }


                while (!line.equals("")) {
                    response.add(line);
                    line = input.readLine();
                }
                publishProgress(new Object[]{type, response});
            }
            catch (SocketTimeoutException sotoex){
                end = true;
                reason = "La conexión ha expirado";
            }
            catch (SocketException | NullPointerException ceex){
                end = true;
                reason = "Error de conexión";
            }
            catch (IOException ioex) {}
        }

        return reason;
    }

    /**
     * Interpreta las respuestas del servidor y actualiza la UI.
     * @param values Respuesta del servidor.
     */
    @Override
    protected void onProgressUpdate(Object[] values) {
        super.onProgressUpdate(values);
        String type = (String)((Object[])values)[0];
        ArrayList<String> response = (ArrayList)((Object[])values)[1];
        String[] response_array = new String[response.size()];
        response.toArray(response_array);
        switch (type){
            case "ls":
                explorer.ls(response_array);
                break;

            case "open":
                Intent intent = new Intent(explorer, Player.class);
                String port = response_array[0];
                String res = response_array[1];
                String ip = explorer.getIP();
                intent.putExtra("URI", "http:/"+ip+":"+port+"/"+res);
                explorer.startActivity(intent);
                break;

            default:
                break;
        }
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Toast.makeText(explorer.getApplicationContext(), s, Toast.LENGTH_SHORT).show();
        explorer.finish();
    }
}