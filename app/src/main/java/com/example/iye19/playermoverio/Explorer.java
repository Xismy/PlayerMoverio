package com.example.iye19.playermoverio;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;


public class Explorer extends AppCompatActivity{
    private Socket socket;
    private BufferedReader input;
    private PrintWriter output;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explorer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        String ip = getIntent().getStringExtra("ip");
        Connect connect = new Connect(this);
        connect.execute(ip);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_player, menu);
        return true;
    }

    /**
     * Cierra los stream de entrada y salida y el socket.
     */
    @Override
    protected void onStop(){
        super.onStop();
        output.close();
        try {
            input.close();
        }
        catch(IOException ex){}
        try {
            socket.close();
        }
        catch(IOException ex){}
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.disconnect){
            output.println("disconnect");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    String getIP(){
        return socket.getInetAddress().toString();
    }

    /**
     * Actualiza el ListView que contiene los nombres de archivos y directorios del directorio actual.
     * @param ls Lista de archivos y directorios.
     */
    public void ls(String[] ls){
        final ListView listView = (ListView)findViewById(R.id.listView2);
        listView.setAdapter(new ArrayAdapter<String>(this, R.layout.item, ls));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                output.println("open");
                output.println(position);
            }
        });
    }

    /**
     * Establece el socket que conecta con el servidor y extrae los stream de entrada y salida.
     * @param socket Socket que conecta con el servidor.
     */
    public void setSocket(Socket socket){
        try {
            this.socket = socket;
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            output = new PrintWriter(socket.getOutputStream(), true);
            Reader reader = new Reader(this, input);
            reader.execute();
            output.println("ls");
        }
        catch (Exception ex){}
    }

}