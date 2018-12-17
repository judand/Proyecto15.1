package com.example.prototipo1.proyecto15;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.RequestQueue;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Fragment_miseventos extends Fragment implements EventoItemAdaptor.OnItemClickListener {

    //creacion de variables a utilizar
    private RecyclerView mreciclerview;
    private EventoItemAdaptor mitemadaptor;
    public static List<ParseObject> hil;
    private ArrayList<EventoItem> mitemevent;
    //se extrae el usuario actual
    ParseUser user = ParseUser.getCurrentUser();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //se crea un View referencia que carga el xml del frame
        View rootView;
        rootView = inflater.inflate(R.layout.fragment_miseventos, container, false);
        //se habilitan las opciones del menu ordenar en el frame
        setHasOptionsMenu(true);

        mreciclerview = (RecyclerView) rootView.findViewById(R.id.recicler_view_all);
        mreciclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        mreciclerview.setHasFixedSize(true);
        mitemevent =new ArrayList<>();
        //parseJson();


        //se consultan las preferencias del usuario
        Integer dat;
        String[] preferenciasEventosUsuario = new String[0];
        if(user.getList("preferencias")==null){
            dat=0;
        }else {
            dat=user.getList("preferencias").size();
        }
        if(dat!=0)
        {
            preferenciasEventosUsuario= user.getList("preferencias").toArray(new String[0]);
        }
        //se busca si las preferencias del usuario son todos los eventos
        boolean proob = false;
        ParseQuery<ParseObject> eventos = new ParseQuery<ParseObject>("eventos");
        // If data is protected by Role based ACLs:
        for(int i=0;i<preferenciasEventosUsuario.length;i++){

            if(preferenciasEventosUsuario[i].equals("all")){
                proob=true;
            }

        }
        //se muestran todos los eventos
        if(proob ==true || preferenciasEventosUsuario==null ){

            eventos.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> objects, ParseException e) {
                    hil = objects;

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
                        String morganiza = evento.getString("organizador");


                        //creacion de cada objeto EventoItem

                        mitemevent.add(new EventoItem(titulo,imageurl,lugar,capacidad,descrpcion,fecha,hora,costoboleta,paga,categoria,megusta,idevento,objet,mlogistica,mcomodidad,mentretenido,minteresante,mcompartidos,minteresados,morganiza));

                    }
                    mitemadaptor =new EventoItemAdaptor(getActivity(),mitemevent);
                    mreciclerview.setAdapter(mitemadaptor);
                    mitemadaptor.setOnItemClickLister(Fragment_miseventos.this);
                    mitemadaptor.notifyDataSetChanged();
                }
            });

        }
        //sino se muestran los eventos de las categorias del usuario
        else{

            eventos.whereContainedIn("categoria",Arrays.asList(preferenciasEventosUsuario));
            eventos.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> objects, ParseException e) {

                    hil = objects;

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
                        String morganiza = evento.getString("organizador");

                        //creacion de cada objeto EventoItem
                        mitemevent.add(new EventoItem(titulo,imageurl,lugar,capacidad,descrpcion,fecha,hora,costoboleta,paga,categoria,megusta,idevento,objet,mlogistica,mcomodidad,mentretenido,minteresante,mcompartidos,minteresados,morganiza));

                    }
                    mitemadaptor =new EventoItemAdaptor(getActivity(),mitemevent);
                    mreciclerview.setAdapter(mitemadaptor);
                    mitemadaptor.setOnItemClickLister(Fragment_miseventos.this);
                    mitemadaptor.notifyDataSetChanged();
                }
            });
        }
        return rootView;
    }
    //funcion que carga datos del json que ya no se usa
   /* private void parseJson(){
        //url donde esta subido el json
        String url= "https://api.myjson.com/bins/13nxqg";

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


                        //mitemevent.add(new EventoItem(titulo,imageurl,lugar,capacidad,descrpcion,fecha,hora,costoboleta,paga,categoria,megusta));

                    }

                    mitemadaptor =new EventoAsistireItemAdaptor(getActivity(),mitemevent);
                    mreciclerview.setAdapter(mitemadaptor);
                    mitemadaptor.setOnItemClickLister(Fragment_miseventos.this);

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
   //funcion que envia datos a DetalleActivityEventos
    @Override
    public void OnItemClick(int position) {
        String senderName = this.getClass().getSimpleName();
        Intent detalleintent =new Intent(getActivity(),DetalleActivityEventos.class);

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
        detalleintent.putExtra("extra_organizador",clickitem.getMorganiza());

        startActivity(detalleintent);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        // 1. get a reference to recyclerView
        mreciclerview = (RecyclerView) getActivity().findViewById(R.id.recicler_view_all);
        // 2. set layoutManger
        mreciclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        //creacion de las variables para cargar
        mreciclerview.setHasFixedSize(true);
        mitemevent = new ArrayList<>();

        //parseJson();

        //se consultan las preferencias del usuario
        Integer dat;
        String[] preferenciasUsuarioArray = new String[0];
        if(user.getList("preferencias")==null){
            dat=0;
        }else {
            dat=user.getList("preferencias").size();
        }
        if(dat!=0)
        {
            preferenciasUsuarioArray= user.getList("preferencias").toArray(new String[0]);
        }

        boolean proob = false;
        ParseQuery<ParseObject> eventos = new ParseQuery<ParseObject>("eventos");
        // If data is protected by Role based ACLs:
        for(int i=0;i<preferenciasUsuarioArray.length;i++){
            if(preferenciasUsuarioArray[i].equals("all")){
                proob=true;
            }

        }

        switch (id) {
            case R.id.popularidad:
                //se ordenan los eventos por meGusta

                if(proob ==true || preferenciasUsuarioArray==null ){

                    eventos.orderByDescending("meGusta");
                    eventos.findInBackground(new FindCallback<ParseObject>() {
                        @Override
                        public void done(List<ParseObject> objects, ParseException e) {
                            hil = objects;
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
                                String morganiza = evento.getString("organizador");

                                //creacion de cada objeto EventoItem
                                mitemevent.add(new EventoItem(titulo,imageurl,lugar,capacidad,descrpcion,fecha,hora,costoboleta,paga,categoria,megusta,idevento,objet,mlogistica,mcomodidad,mentretenido,minteresante,mcompartidos,minteresados,morganiza));
                            }
                            mitemadaptor =new EventoItemAdaptor(getActivity(),mitemevent);
                            mreciclerview.setAdapter(mitemadaptor);
                            mitemadaptor.setOnItemClickLister(Fragment_miseventos.this);
                            mitemadaptor.notifyDataSetChanged();

                        }
                    });

                }
                //se ordenan eventos por meGusta y segun las preferencias del usuario
                else{
                    eventos.whereContainedIn("categoria",Arrays.asList(preferenciasUsuarioArray));
                    eventos.orderByDescending("meGusta");
                    eventos.findInBackground(new FindCallback<ParseObject>() {
                        @Override
                        public void done(List<ParseObject> objects, ParseException e) {

                            hil = objects;

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
                                String morganiza = evento.getString("organizador");

                                //creacion de cada objeto EventoItem
                                mitemevent.add(new EventoItem(titulo,imageurl,lugar,capacidad,descrpcion,fecha,hora,costoboleta,paga,categoria,megusta,idevento,objet,mlogistica,mcomodidad,mentretenido,minteresante,mcompartidos,minteresados,morganiza));
                            }
                            mitemadaptor =new EventoItemAdaptor(getActivity(),mitemevent);
                            mreciclerview.setAdapter(mitemadaptor);
                            mitemadaptor.setOnItemClickLister(Fragment_miseventos.this);
                            mitemadaptor.notifyDataSetChanged();
                        }
                    });
                }

                break;
            case R.id.fecha_event:
                //se ordenan por fecha
                if(proob ==true || preferenciasUsuarioArray==null ){
                    eventos.orderByDescending("date");
                    eventos.findInBackground(new FindCallback<ParseObject>() {
                        @Override
                        public void done(List<ParseObject> objects, ParseException e) {
                            hil = objects;
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
                                String morganiza = evento.getString("organizador");

                                //creacion de cada objeto EventoItem
                                mitemevent.add(new EventoItem(titulo,imageurl,lugar,capacidad,descrpcion,fecha,hora,costoboleta,paga,categoria,megusta,idevento,objet,mlogistica,mcomodidad,mentretenido,minteresante,mcompartidos,minteresados,morganiza));
                            }
                            mitemadaptor =new EventoItemAdaptor(getActivity(),mitemevent);
                            mreciclerview.setAdapter(mitemadaptor);
                            mitemadaptor.setOnItemClickLister(Fragment_miseventos.this);
                            mitemadaptor.notifyDataSetChanged();

                        }
                    });

                }
                else{

                    eventos.whereContainedIn("categoria",Arrays.asList(preferenciasUsuarioArray));
                    eventos.orderByDescending("date");
                    eventos.findInBackground(new FindCallback<ParseObject>() {
                        @Override
                        public void done(List<ParseObject> objects, ParseException e) {
                            hil = objects;
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
                                String morganiza = evento.getString("organizador");

                                //creacion de cada objeto EventoItem

                                mitemevent.add(new EventoItem(titulo,imageurl,lugar,capacidad,descrpcion,fecha,hora,costoboleta,paga,categoria,megusta,idevento,objet,mlogistica,mcomodidad,mentretenido,minteresante,mcompartidos,minteresados,morganiza));

                            }

                            mitemadaptor =new EventoItemAdaptor(getActivity(),mitemevent);
                            mreciclerview.setAdapter(mitemadaptor);
                            mitemadaptor.setOnItemClickLister(Fragment_miseventos.this);
                            mitemadaptor.notifyDataSetChanged();
                        }
                    });
                }
                break;
            case R.id.organizador_event:
                //se ordenan los eventos por organizador

                if(proob ==true || preferenciasUsuarioArray==null ){

                    eventos.orderByAscending("organizador");
                    eventos.findInBackground(new FindCallback<ParseObject>() {
                        @Override
                        public void done(List<ParseObject> objects, ParseException e) {
                            hil = objects;
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
                                String morganiza = evento.getString("organizador");

                                //creacion de cada objeto EventoItem
                                mitemevent.add(new EventoItem(titulo,imageurl,lugar,capacidad,descrpcion,fecha,hora,costoboleta,paga,categoria,megusta,idevento,objet,mlogistica,mcomodidad,mentretenido,minteresante,mcompartidos,minteresados,morganiza));
                            }
                            mitemadaptor =new EventoItemAdaptor(getActivity(),mitemevent);
                            mreciclerview.setAdapter(mitemadaptor);
                            mitemadaptor.setOnItemClickLister(Fragment_miseventos.this);
                            mitemadaptor.notifyDataSetChanged();

                        }
                    });

                }
                //se ordenan eventos por organizador y segun las preferencias del usuario
                else{
                    eventos.whereContainedIn("categoria",Arrays.asList(preferenciasUsuarioArray));
                    eventos.orderByAscending("organizador");
                    eventos.findInBackground(new FindCallback<ParseObject>() {
                        @Override
                        public void done(List<ParseObject> objects, ParseException e) {

                            hil = objects;

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
                                String morganiza = evento.getString("organizador");

                                //creacion de cada objeto EventoItem
                                mitemevent.add(new EventoItem(titulo,imageurl,lugar,capacidad,descrpcion,fecha,hora,costoboleta,paga,categoria,megusta,idevento,objet,mlogistica,mcomodidad,mentretenido,minteresante,mcompartidos,minteresados,morganiza));
                            }
                            mitemadaptor =new EventoItemAdaptor(getActivity(),mitemevent);
                            mreciclerview.setAdapter(mitemadaptor);
                            mitemadaptor.setOnItemClickLister(Fragment_miseventos.this);
                            mitemadaptor.notifyDataSetChanged();
                        }
                    });
                }

                break;
        }
        return super.onOptionsItemSelected(item);
    }


}
