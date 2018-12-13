package com.example.prototipo1.proyecto15;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class EventosDrawActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,EventoItemAdaptor.OnItemClickListener {

    //atributo que ya no se usa para cargar el json estatico


    public static String url= "https://api.myjson.com/bins/13nxqg";
    //atributos del recicler,adaptador del evento, lista a cargar, lista estatica para pasar los datos y barra inferior de navegacion
    private RecyclerView mreciclerview;
    private EventoItemAdaptor mitemadaptor;
    private ArrayList<EventoItem> mitemevent;
    public static List<ParseObject> hil;
    private BottomNavigationView Menuinferior;


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);

        //carga del layout en esta actividad
        setContentView(R.layout.activity_centro_draw);
        //barra superior donde se desplega como ordenar los eventos y el nombre
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //extraccion del usuario actual, y correo
        final ParseUser pUser= ParseUser.getCurrentUser();
        String correo_usuario = pUser.getEmail();
        String usuario = pUser.getUsername();


        View headerView = navigationView.getHeaderView(0);

        //carga del usuario y correo en el menu

        TextView nombre = (TextView) headerView.findViewById(R.id.nombre_user);
        nombre.setText(usuario);
        TextView correo = (TextView) headerView.findViewById(R.id.correo_user);
        correo.setText(correo_usuario);

        //menu inferior declaracion
        Menuinferior = findViewById(R.id.navegacion);
        Menuinferior.setOnNavigationItemSelectedListener(navlister);
        Menuinferior.getMenu().getItem(0).setCheckable(true);



        //declaracion del recicler a buscar y configuracion de como mostrarlo

        mreciclerview = findViewById(R.id.recicler_view);
        mreciclerview.setHasFixedSize(true);
        mreciclerview.setLayoutManager(new LinearLayoutManager(this));

        mitemevent =new ArrayList<>();

        //parseJson();
        //query que busca los eventos
        ParseQuery<ParseObject> eventos = new ParseQuery<ParseObject>("eventos");
        eventos.orderByAscending("date");
        eventos.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {

                hil = objects;

                for(int i=0; i<hil.size();i++){

                    //creacion de cada evento
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


                    //creacion de cada objeto Eventoitem

                    mitemevent.add(new EventoItem(titulo,imageurl,lugar,capacidad,descrpcion,fecha,hora,costoboleta,paga,categoria,megusta,idevento,objet,mlogistica,mcomodidad,mentretenido,minteresante,mcompartidos,minteresados));

                }

                //carga de la lista de eventos

                mitemadaptor =new EventoItemAdaptor(EventosDrawActivity.this,mitemevent);
                mreciclerview.setAdapter(mitemadaptor);
                mitemadaptor.setOnItemClickLister(EventosDrawActivity.this);


            }
        });




    }

    //funcion que va atras
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    //funcion que referencia a los items del menu ordenar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.centro_draw, menu);
        return true;
    }

    //funcion que ejecuta cada opcion de los items del menu ordenar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();
        //carga y referencia del recycler a modificar
        mreciclerview = findViewById(R.id.recicler_view);
        mreciclerview.setHasFixedSize(true);
        mreciclerview.setLayoutManager(new LinearLayoutManager(this));

        mitemevent = new ArrayList<>();

        //parseJson();
        ParseQuery<ParseObject> eventos = new ParseQuery<ParseObject>("eventos");
        switch (id) {
            case R.id.popularidad:

                eventos.orderByDescending("meGusta");
                eventos.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> objects, ParseException e) {
                        Log.d("si", String.valueOf(objects.size()));
                        hil = objects;
                        Log.d("si", String.valueOf(hil.size()));
                        for (int i = 0; i < hil.size(); i++) {

                            ParseObject evento = hil.get(i);
                            int idevento = evento.getInt("idevento");
                            String objet = evento.getObjectId();
                            String titulo = evento.getString("nombre");
                            String lugar = evento.getString("lugar");
                            String imageurl = evento.getString("imagen");
                            int capacidad = evento.getInt("capacidad");
                            String descrpcion = evento.getString("descripcion");
                            String fecha = evento.getString("fecha");
                            String hora = evento.getString("hora");
                            int costoboleta = evento.getInt("costo");
                            boolean paga = evento.getBoolean("sePaga");
                            String categoria = evento.getString("categoria");
                            int megusta = evento.getInt("meGusta");
                            int mlogistica = evento.getInt("logistica");
                            int mcomodidad = evento.getInt("comodidad");
                            int mentretenido = evento.getInt("entretenido");
                            int minteresante = evento.getInt("interesante");
                            int mcompartidos = evento.getInt("compartidos");
                            int minteresados = evento.getInt("interesados");


                            //creacion de cada objeto Eventoitem


                            mitemevent.add(new EventoItem(titulo, imageurl, lugar, capacidad, descrpcion, fecha, hora, costoboleta, paga, categoria, megusta, idevento, objet, mlogistica, mcomodidad, mentretenido, minteresante, mcompartidos, minteresados));

                        }

                        mitemadaptor = new EventoItemAdaptor(EventosDrawActivity.this, mitemevent);
                        mreciclerview.setAdapter(mitemadaptor);
                        mitemadaptor.setOnItemClickLister(EventosDrawActivity.this);


                    }
                });
                break;
            case R.id.fecha_event:

                eventos.orderByDescending("date");
                eventos.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> objects, ParseException e) {

                        hil = objects;

                        for (int i = 0; i < hil.size(); i++) {

                            ParseObject evento = hil.get(i);
                            int idevento = evento.getInt("idevento");
                            String objet = evento.getObjectId();
                            String titulo = evento.getString("nombre");
                            String lugar = evento.getString("lugar");
                            String imageurl = evento.getString("imagen");
                            int capacidad = evento.getInt("capacidad");
                            String descrpcion = evento.getString("descripcion");
                            String fecha = evento.getString("fecha");
                            String hora = evento.getString("hora");
                            int costoboleta = evento.getInt("costo");
                            boolean paga = evento.getBoolean("sePaga");
                            String categoria = evento.getString("categoria");
                            int megusta = evento.getInt("meGusta");
                            int mlogistica = evento.getInt("logistica");
                            int mcomodidad = evento.getInt("comodidad");
                            int mentretenido = evento.getInt("entretenido");
                            int minteresante = evento.getInt("interesante");
                            int mcompartidos = evento.getInt("compartidos");
                            int minteresados = evento.getInt("interesados");


                            //creacion de cada objeto Eventoitem


                            mitemevent.add(new EventoItem(titulo, imageurl, lugar, capacidad, descrpcion, fecha, hora, costoboleta, paga, categoria, megusta, idevento, objet, mlogistica, mcomodidad, mentretenido, minteresante, mcompartidos, minteresados));

                        }

                        mitemadaptor = new EventoItemAdaptor(EventosDrawActivity.this, mitemevent);
                        mreciclerview.setAdapter(mitemadaptor);
                        mitemadaptor.setOnItemClickLister(EventosDrawActivity.this);


                    }
                });
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //menu lateral y referencias de a donde ir al seleccionar
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id){

            case R.id.nav_eventos:
                startActivity(getIntent());

                break;

            case R.id.nav_logros:
                Intent intentevento = new Intent(EventosDrawActivity.this,LogrosDrawActivity.class);
                startActivity(intentevento);
                break;
            case R.id.nav_settings:
                Intent intentperfil = new Intent(EventosDrawActivity.this,SettingsActivity.class);

                startActivity(intentperfil);
                break;
            case R.id.nav_info:
                Intent intentayuda = new Intent(EventosDrawActivity.this, InfoActivity.class);
                startActivity(intentayuda);
                break;
            case R.id.nav_sesion:
                ParseUser.logOut();
                alertDisplayer("Cerrando sesión", "vuelve pronto...!!!");
                break;

        }



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    //funcion que referencia el menu de abajo y referencia sus interacciones
    private BottomNavigationView.OnNavigationItemSelectedListener navlister=
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    Fragment selectfragment = null;

                    switch (menuItem.getItemId()){
                        case R.id.Eventos:
                            startActivity(getIntent());


                        case R.id.all_bottom:
                            selectfragment = new Fragment_miseventos();
                            Menu menu1 = Menuinferior.getMenu();
                            MenuItem menuitem1 = menu1.getItem(0);
                            menuitem1.setChecked(true);
                            menuitem1.setCheckable(true);

                            break;


                        case R.id.interes_buttom:
                            selectfragment = new Fragment_asistire();
                            Menu menu = Menuinferior.getMenu();
                            MenuItem menuitem = menu.getItem(1);
                            menuitem.setChecked(true);
                            menuitem.setCheckable(true);
                            break;
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,selectfragment).commit();


                    return true;
                }
            };

    //funcion para leer datos del json que ya no se usa

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
    //funcion que pasa los datos a DetalleActivityEvento al hacer clic en cada item del evento
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
    //funcion del cuadro de dialogo emergente
    private void alertDisplayer(String title,String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(EventosDrawActivity.this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        Intent intent = new Intent(EventosDrawActivity.this, Login_activity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                });
        AlertDialog ok = builder.create();
        ok.show();
    }
}
