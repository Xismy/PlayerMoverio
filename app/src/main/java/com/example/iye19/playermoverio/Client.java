package com.example.iye19.playermoverio;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.IOException;
import java.util.ArrayList;

public class Client extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_client, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_update) {
            try {
                search_server();
            }
            catch (IOException ex){}
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Lanza un AsyncTask encargado de hacer el descubrimiento del servidor.
     * @throws IOException
     */
    public void search_server() throws IOException {
        new Discover(this).execute();
    }

    /**
     * Lanza la actividad del reproductor pasándole en un Intent la dirección IP del servidor.
     * La conexión con el servidor se realiza al crear la actividad.
     * @param ip Dirección IP del servidor.
     */
    public void connect(String ip){
        Intent intent = new Intent(this, Explorer.class);
        intent.putExtra("ip", ip);
        startActivity(intent);
    }

    /**
     * Actualiza el ListView que contiene los nombres de los servidores descubiertos.
     * @param lista Servidores descubiertos, representados por un nombre y una IP.
     */
    public void update(final ArrayList<Servidor> lista){
        final ListView listView = (ListView)findViewById(R.id.listView);
        listView.setAdapter(new ArrayAdapter<Servidor>(this, R.layout.item, lista));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                connect(lista.get(position).getIp().getHostAddress());
            }
        });

        if(lista.size()>0)
            findViewById(R.id.notFound).setVisibility(View.INVISIBLE);
        else
            findViewById(R.id.notFound).setVisibility(View.VISIBLE);
    }
}
