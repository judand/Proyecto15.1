package com.example.prototipo1.proyecto15;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Fragment_asistire extends Fragment implements EventoAsistireItemAdaptor.OnItemClickListener {

    //creacion de las variables a utilizar
    private RecyclerView mreciclerview;
    private EventoAsistireItemAdaptor mitemadaptor;
    public static List<ParseObject> hil;
    private ArrayList<EventoItem> mitemevent;
    //extracion del usuario actual
    ParseUser user = ParseUser.getCurrentUser();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //se habilitan las opciones del menu ordenar en el frame
        setHasOptionsMenu(true);

        //se crea un View referencia que carga el xml del frame
        View rootView = inflater.inflate(R.layout.fragment_asistire, container, false);

        //se referencia el recyclerview
        mreciclerview = (RecyclerView) rootView.findViewById(R.id.recicler_view_interes);

        //se personalizar la forma en que se vera el layout, en este caso sera lineal
        mreciclerview.setLayoutManager(new LinearLayoutManager(getActivity()));

        //creacion de las variables para cargar
        mreciclerview.setHasFixedSize(true);
        mitemevent = new ArrayList<>();


        //parseJson();


        //se consulta si el usuario tiene eventos a los cuales asistira
        Integer vacio;
        if(user.getList("favoritos")==null){
            vacio=0;
        }
        else {
            vacio= user.getList("favoritos").size();
        }

        if (vacio!=0) {
            //se crea un array para los asistire del usuario
            Integer[] favoritosArray = user.getList("favoritos").toArray(new Integer[0]);
            //se consultan los eventos con el id de los favoritos del usuario
            ParseQuery<ParseObject> eventos = new ParseQuery<ParseObject>("eventos");
            eventos.whereContainedIn("idevento", Arrays.asList(favoritosArray));
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
                        String morganiza = evento.getString("organizador");

                        //creacion de cada objeto EventoItem
                        mitemevent.add(new EventoItem(titulo, imageurl, lugar, capacidad, descrpcion, fecha, hora, costoboleta, paga, categoria, megusta, idevento, objet, mlogistica, mcomodidad, mentretenido, minteresante, mcompartidos, minteresados,morganiza));

                    }

                    mitemadaptor = new EventoAsistireItemAdaptor(getActivity(), mitemevent);
                    mreciclerview.setAdapter(mitemadaptor);
                    mitemadaptor.setOnItemClickLister(Fragment_asistire.this);

                }
            });
        }
        //si el usuario no tiene favoritos se carga este xml
        if (vacio==0) {

            rootView = inflater.inflate(R.layout.fragment_sininteres, container, false);

        }

        return rootView;
    }

    //funcion de lectura del json que ya no se implementa
    /*private void parseJson(){
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
                    mitemadaptor.setOnItemClickLister(fragment_asistire.this);

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
    }
    */
    //funcion que envia los datos a la actividad DetalleActivityEventosAsistire
    @Override
    public void OnItemClick(int position) {
        Intent detalleintent =new Intent(getActivity(),DetalleActivityEventosAsistire.class);

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

        mreciclerview = (RecyclerView) getActivity().findViewById(R.id.recicler_view_interes);

        if(mreciclerview!=null){
            mreciclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
            mreciclerview.setHasFixedSize(true);
        }

        mitemevent = new ArrayList<>();

        Integer vacio;
        if(user.getList("favoritos")==null){
            vacio=0;
        }
        else {
            vacio= user.getList("favoritos").size();
        }

        switch (id) {
            case R.id.popularidad:
                if (vacio!=0) {
                    //se ordenan los eventos por meGusta
                    Integer[] asistireArray = user.getList("favoritos").toArray(new Integer[0]);
                    ParseQuery<ParseObject> eventos = new ParseQuery<ParseObject>("eventos");
                    eventos.whereContainedIn("idevento", Arrays.asList(asistireArray));
                    eventos.orderByDescending("meGusta");
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
                                String morganiza = evento.getString("organizador");

                                //creacion de cada objeto EventoItem
                                mitemevent.add(new EventoItem(titulo, imageurl, lugar, capacidad, descrpcion, fecha, hora, costoboleta, paga, categoria, megusta, idevento, objet, mlogistica, mcomodidad, mentretenido, minteresante, mcompartidos, minteresados,morganiza));

                            }

                            mitemadaptor = new EventoAsistireItemAdaptor(getActivity(), mitemevent);
                            mreciclerview.setAdapter(mitemadaptor);
                            mitemadaptor.setOnItemClickLister(Fragment_asistire.this);

                        }
                    });
                }
                if (vacio==0) {
                    Fragment_asistire.ViewDialog alert = new ViewDialog();
                    alert.showDialog(getActivity(), "No hay eventos para ordenar");
                }

                break;
            case R.id.fecha_event:
                if (vacio!=0) {


                    Integer[] asistireArray = user.getList("favoritos").toArray(new Integer[0]);

                    ParseQuery<ParseObject> eventos = new ParseQuery<ParseObject>("eventos");
                    eventos.whereContainedIn("idevento", Arrays.asList(asistireArray));
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
                                String morganiza = evento.getString("organizador");

                                //creacion de cada objeto itemevent
                                mitemevent.add(new EventoItem(titulo, imageurl, lugar, capacidad, descrpcion, fecha, hora, costoboleta, paga, categoria, megusta, idevento, objet, mlogistica, mcomodidad, mentretenido, minteresante, mcompartidos, minteresados,morganiza));
                            }

                            mitemadaptor = new EventoAsistireItemAdaptor(getActivity(), mitemevent);
                            mreciclerview.setAdapter(mitemadaptor);
                            mitemadaptor.setOnItemClickLister(Fragment_asistire.this);

                        }
                    });
                }
                if (vacio==0) {
                    Fragment_asistire.ViewDialog alert = new ViewDialog();
                    alert.showDialog(getActivity(), "No hay eventos para ordenar");

                }
                break;
            case R.id.organizador_event:
                if (vacio!=0) {


                    Integer[] asistireArray = user.getList("favoritos").toArray(new Integer[0]);

                    ParseQuery<ParseObject> eventos = new ParseQuery<ParseObject>("eventos");
                    eventos.whereContainedIn("idevento", Arrays.asList(asistireArray));
                    eventos.orderByAscending("organizador");
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
                                String morganiza = evento.getString("organizador");

                                //creacion de cada objeto itemevent
                                mitemevent.add(new EventoItem(titulo, imageurl, lugar, capacidad, descrpcion, fecha, hora, costoboleta, paga, categoria, megusta, idevento, objet, mlogistica, mcomodidad, mentretenido, minteresante, mcompartidos, minteresados,morganiza));
                            }

                            mitemadaptor = new EventoAsistireItemAdaptor(getActivity(), mitemevent);
                            mreciclerview.setAdapter(mitemadaptor);
                            mitemadaptor.setOnItemClickLister(Fragment_asistire.this);

                        }
                    });
                }
                if (vacio==0) {
                    Fragment_asistire.ViewDialog alert = new ViewDialog();
                    alert.showDialog(getActivity(), "No hay eventos para ordenar");

                }
                break;

        }
        return super.onOptionsItemSelected(item);
    }
    //funcion que muestra la ventana emergente
    public class ViewDialog {

        public void showDialog(Activity activity, String msg){
            final Dialog dialog = new Dialog(activity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.dialog_maqueta);

            TextView text = (TextView) dialog.findViewById(R.id.text_dialog);
            text.setText(msg);
            Button dialogButton = (Button) dialog.findViewById(R.id.btn_dialog);
            dialogButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    onStart();
                }
            });
            dialog.show();
        }
    }
}
