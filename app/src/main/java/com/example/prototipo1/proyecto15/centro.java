package com.example.prototipo1.proyecto15;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.android.volley.RequestQueue;
import com.parse.FindCallback;
import com.parse.ParseException;

import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class centro extends AppCompatActivity implements EventoItemAdaptor.OnItemClickListener {



    //atributos para pasar al detalle del evento

    public static final String extra_object="objetid";
    public static final String extra_logistica="logistica";
    public static final String extra_comodidad="comodidad";
    public static final String extra_entretenido="entretenido";
    public static final String extra_interesante="interesante";
    public static final String extra_compartidos="compartidos";
    public static final String extra_interesados="interesados";
    public static String url= "https://api.myjson.com/bins/13nxqg";
    public static final String extra_idevento="idevento";

    private RecyclerView mreciclerview;
    private EventoItemAdaptor mitemadaptor;


    private ArrayList<EventoItem> mitemevent;
    public static List<ParseObject> hil;
    private RequestQueue mrequest;
    private BottomNavigationView botton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_centro);
        //boton barra superior
        botton = findViewById(R.id.navegacion);
        botton.setOnNavigationItemSelectedListener(navlister);
        botton.getMenu().getItem(0).setCheckable(false);

//boton barra inferior
        BottomNavigationView bottonbajo = findViewById(R.id.navega_activity);
        Menu menu =bottonbajo.getMenu();
        MenuItem menuitem = menu.getItem(0);
        menuitem.setChecked(true);


            bottonbajo.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    switch (menuItem.getItemId()){

                        case R.id.Eventos:
                            startActivity(getIntent());

                            break;
                        case R.id.recompensas:
                            Intent intentevento = new Intent(centro.this,logros.class);
                            startActivity(intentevento);
                            break;
                        case R.id.perfil:
                            Intent intentperfil = new Intent(centro.this,SettingsActivity.class);

                            int i = 0;
                            intentperfil.putExtra(url,i);
                            startActivity(intentperfil);
                            break;

                    }

                    return true;
                }
            });

            //creacion de las variables para cargar

            mreciclerview = findViewById(R.id.recicler_view);
            mreciclerview.setHasFixedSize(true);
            mreciclerview.setLayoutManager(new LinearLayoutManager(this));

            mitemevent =new ArrayList<>();

            //mrequest= Volley.newRequestQueue(this);
            //parseJson();
        ParseQuery<ParseObject> eventos = new ParseQuery<ParseObject>("eventos");
        eventos.orderByAscending("date");
        eventos.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                Log.d("si", String.valueOf(objects.size()));
                hil = objects;
                Log.d("si", String.valueOf(hil.size()));
                for(int i=0; i<hil.size();i++){

                    ParseObject evento =hil.get(i);
                    int idevento=evento.getInt("idevento");
                    String objet =evento.getObjectId();
                    String titulo=evento.getString("nombre");
                    String lugar =evento.getString("lugar");
                    String imageurl=evento.getString("imagen");
                    int capacidad =evento.getInt("capacidad");
                    String descrpcion =evento.getString("descripcion");
                    String fecha =evento.getString("fecha");
                    String hora= evento.getString("hora");
                    int costoboleta=evento.getInt("costo");
                    boolean paga =evento.getBoolean("sePaga");
                    String categoria= evento.getString("categoria");
                    int megusta=evento.getInt("meGusta");
                    int mlogistica=evento.getInt("logistica");
                    int mcomodidad=evento.getInt("comodidad");
                    int mentretenido=evento.getInt("entretenido");
                    int minteresante=evento.getInt("interesante");
                    int mcompartidos=evento.getInt("compartidos");
                    int minteresados=evento.getInt("interesados");


                    //creacion de cada objeto itemevent


                    mitemevent.add(new EventoItem(titulo,imageurl,lugar,capacidad,descrpcion,fecha,hora,costoboleta,paga,categoria,megusta,idevento,objet,mlogistica,mcomodidad,mentretenido,minteresante,mcompartidos,minteresados));

                }

                mitemadaptor =new EventoItemAdaptor(centro.this,mitemevent);
                mreciclerview.setAdapter(mitemadaptor);
                mitemadaptor.setOnItemClickLister(centro.this);


            }
        });




    }

    private BottomNavigationView.OnNavigationItemSelectedListener navlister=
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    Fragment selectfragment = null;

                    switch (menuItem.getItemId()){

                        case R.id.all_bottom:
                            selectfragment = new Fragment_miseventos();
                            Menu menu1 =botton.getMenu();
                            MenuItem menuitem1 = menu1.getItem(0);
                            menuitem1.setChecked(true);
                            menuitem1.setCheckable(true);

                            break;


                        case R.id.interes_buttom:
                            selectfragment = new Fragment_asistire();
                            Menu menu =botton.getMenu();
                            MenuItem menuitem = menu.getItem(1);
                            menuitem.setChecked(true);
                            menuitem.setCheckable(true);
                            break;
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,selectfragment).commit();


                    return true;
                }
            };

    //funcion para leer datos del json

   /* private void parseJson(){
        //url donde esta subido el json


        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray= response.getJSONArray("eventos");

                    for(int i=0; i<jsonArray.length();i++){
                        JSONObject evento =jsonArray.getJSONObject(i);


                        String titulo=evento.getString("nombre");
                        String lugar =evento.getString("lugar");
                        String imageurl=evento.getString("imagen");
                        int capacidad =evento.getInt("capacidad");
                        String descrpcion =evento.getString("descripcion");
                        String fecha =evento.getString("fecha");
                        String hora= evento.getString("hora");
                        int costoboleta=evento.getInt("costo");
                        boolean paga =evento.getBoolean("sePaga");
                        String categoria= evento.getString("categoria");
                        int megusta=evento.getInt("meGusta");


                        //creacion de cada objeto itemevent


                       // mitemevent.add(new EventoItem(titulo,imageurl,lugar,capacidad,descrpcion,fecha,hora,costoboleta,paga,categoria,megusta));

                    }

                    mitemadaptor =new EventoAsistireItemAdaptor(centro.this,mitemevent);
                    mreciclerview.setAdapter(mitemadaptor);
                    mitemadaptor.setOnItemClickLister(centro.this);


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });


        mrequest.add(request);
    }*/

    @Override
    public void OnItemClick(int position) {



        Intent detalleintent =new Intent(this,DetalleActivityEventos.class);

        //se llenan las variables para llamarlas en la otra clase
        EventoItem clickitem=mitemevent.get(position);
        detalleintent.putExtra("extra_url", clickitem.getMimageurl());
        detalleintent.putExtra("extra_titulo",clickitem.getMtitle());
        detalleintent.putExtra("extra_lugar",clickitem.getMlugar());
        detalleintent.putExtra("extra_capacidad", clickitem.getMcapacidad());
        detalleintent.putExtra("extra_descripcion",clickitem.getMdescripcion());
        detalleintent.putExtra("extra_fecha",clickitem.getMfecha());
        detalleintent.putExtra("extra_hora",clickitem.getMhora());
        detalleintent.putExtra("extra_costoboleta",clickitem.getMcostoboleta());
        detalleintent.putExtra("extra_idevento",clickitem.getMidevento());
        detalleintent.putExtra("extra_objet",clickitem.getMobjetid());
        detalleintent.putExtra("extra_compartidos",clickitem.getMcompartidos());
        detalleintent.putExtra("extra_megusta",clickitem.getMgusta());
        startActivity(detalleintent);







    }






    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public void onRestart()
    {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager
                .getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }
}
