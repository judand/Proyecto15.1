package com.example.prototipo1.proyecto15;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;




public class Fragment_recompensas extends Fragment  implements RecompensaAdaptorCanje.OnItemClickListener{

    private RecyclerView mreciclerview;
    private RecompensaAdaptorCanje mitemadaptor;
    private ArrayList<RecompensaItem> mitemevent;
    public static List<ParseObject> hil;


    private OnFragmentInteractionListener mListener;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //se crea un View referencia que carga el xml del frame
        View root;
        root =inflater.inflate(R.layout.fragment_recompensas, container, false);
        mreciclerview = root.findViewById(R.id.recicler_recompensa);
        mreciclerview.setLayoutManager(new LinearLayoutManager(getActivity()));

        //creacion de las variables para cargar
        mreciclerview.setHasFixedSize(true);
        mitemevent = new ArrayList<>();
        //se extrae el usuario actual
        ParseUser user = ParseUser.getCurrentUser();
        //se revisa si el usuario tiene recompensas
        Integer vacio;
        if(user.getList("idrecompensas")==null){
            vacio=0;
        }
        else {
            vacio= user.getList("idrecompensas").size();

        }

        if (vacio!=0) {
            Integer[] RecompensasArray = user.getList("idrecompensas").toArray(new Integer[0]);
            ParseQuery<ParseObject> recomps = new ParseQuery<ParseObject>("Recompensa");
            recomps.whereContainedIn("idrecompensa", Arrays.asList(RecompensasArray));

            recomps.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> objects, ParseException e) {

                    hil = objects;

                    for(int i=0; i<hil.size();i++){

                        ParseObject Recom =hil.get(i);
                        int idevento=Recom.getInt("idrecompensa");
                        String objet =Recom.getObjectId();
                        String titulo=Recom.getString("TituloRecompensa");
                        String imageurl=Recom.getString("UrlImageRecompensa");
                        String descrpcion =Recom.getString("DescripcionRecompensa");
                        int costoR=Recom.getInt("costo");

                        byte[] bitmapdata = new byte[0];
                        try {
                            bitmapdata = Recom.getParseFile("QrRecompensa").getData();
                        } catch (ParseException e1) {
                            e1.printStackTrace();
                        }
                        Bitmap bm = BitmapFactory.decodeByteArray(bitmapdata, 0, bitmapdata.length);

                        //creacion de cada objeto recompensa

                        mitemevent.add(new RecompensaItem(idevento,titulo,imageurl,descrpcion,bm,objet,costoR));
                    }

                    mitemadaptor =new RecompensaAdaptorCanje(getActivity(),mitemevent);
                    mreciclerview.setAdapter(mitemadaptor);
                    mitemadaptor.setOnItemClickLister(Fragment_recompensas.this);


                }
            });
        }
        //sino hay recompensas se muestra este xml
        if(vacio==0){
            root = inflater.inflate(R.layout.fragment_sininteres, container, false);
            TextView text =root.findViewById(R.id.te);
            text.setText("no tienes recompensas por el momento");
        }


        return root;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
    //funcion que envia los datos a DetalleRecompensa
    @Override
    public void OnItemClick(int position) {
        Intent detalleintent =new Intent(getActivity(),DetalleRecompensa.class);

        //se llenan las variables para llamarlas en la otra clase
        RecompensaItem clickitem=mitemevent.get(position);
        detalleintent.putExtra("extra_url", clickitem.getUrlImagen());
        detalleintent.putExtra("extra_id",clickitem.getIdRecompensa());
        detalleintent.putExtra("extra_titulo",clickitem.getTituloRecompensa());
        detalleintent.putExtra("extra_descripcion",clickitem.getDescripcionRecompensa());
        detalleintent.putExtra("extra_objet",clickitem.getMobjetid());
        detalleintent.putExtra("extra_qr",clickitem.getQrcodeRecompensa());
        startActivity(detalleintent);

    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


}
