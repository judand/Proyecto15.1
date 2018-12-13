package com.example.prototipo1.proyecto15;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CatalogoRecompensaActivity.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CatalogoRecompensaActivity#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CatalogoRecompensaActivity extends Fragment implements RecompensaAdaptor.OnItemClickListener{
    private RecyclerView mreciclerview;
    private RecompensaAdaptor mitemadaptor;


    private ArrayList<RecompensaItem> mitemevent;
    public static List<ParseObject> hil;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public CatalogoRecompensaActivity() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CatalogoRecompensaActivity.
     */
    // TODO: Rename and change types and number of parameters
    public static CatalogoRecompensaActivity newInstance(String param1, String param2) {
        CatalogoRecompensaActivity fragment = new CatalogoRecompensaActivity();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root;
        root=inflater.inflate(R.layout.fragment_catalogo_recompensa, container, false);
        // Inflate the layout for this fragment

        mreciclerview = root.findViewById(R.id.recicler_recompensa);
        mreciclerview.setLayoutManager(new GridLayoutManager(getActivity(),2));

        //creacion de las variables para cargar

        mreciclerview.setHasFixedSize(true);


        mitemevent = new ArrayList<>();


        ParseQuery<ParseObject> recomps = new ParseQuery<ParseObject>("Recompensa");

        recomps.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                Log.d("si", String.valueOf(objects.size()));
                hil = objects;
                Log.d("si", String.valueOf(hil.size()));
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


                    //creacion de cada objeto itemevent


                    mitemevent.add(new RecompensaItem(idevento,titulo,imageurl,descrpcion,bm,objet,costoR));
                }

                mitemadaptor =new RecompensaAdaptor(getActivity(),mitemevent);
                mreciclerview.setAdapter(mitemadaptor);
                mitemadaptor.setOnItemClickLister(CatalogoRecompensaActivity.this);


            }
        });


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

    @Override
    public void OnItemClick(int position) {
        Intent detalleintent =new Intent(getActivity(),DetalleRecompensa.class);

        //se llenan las variables para llamarlas en la otra clase
        RecompensaItem clickitem=mitemevent.get(position);
        detalleintent.putExtra("extra_url", clickitem.getUrlImagen());
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
