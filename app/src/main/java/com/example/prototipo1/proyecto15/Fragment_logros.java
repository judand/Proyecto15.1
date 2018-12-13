package com.example.prototipo1.proyecto15;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;


public class Fragment_logros extends Fragment {

    //creacion de las variables que albergan  las barras de progreso
    private ProgressBar barra_comentar;
    private ProgressBar barra_compartir;
    private ProgressBar barra_asistir;
    private ProgressBar barra_megusta;
    private OnFragmentInteractionListener mListener;
    //funcion que carga los progresos del usuario
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //se crea un View referencia que carga el xml del frame
        View root;
        root = inflater.inflate(R.layout.fragment_frag_logros, container, false);
        //se extrae el usuario actual
        ParseUser Useer = ParseUser.getCurrentUser();
        int asistirlimite=1000;
        Number calificar;
        Number compartir;
        Number asistir;
        Number megusta;
        Number total;
        Number puntos;
        //se extraen los progresos del usuario
        if (Useer.getNumber("calificar") != null) {
            calificar = Useer.getNumber("calificar");

        }
        else {
            calificar = 0;
            }
            if (Useer.getNumber("compartir") != null) {
            compartir = Useer.getNumber("compartir");
            } else {
            compartir = 0;
            }
            if (Useer.getNumber("asistir") != null) {
            asistir = Useer.getNumber("asistir");
            } else {
                asistir = 0;
            }
            if (Useer.getNumber("meGusta") != null) {
            megusta = Useer.getNumber("meGusta");
            } else {
            megusta = 0;
            }
        if (Useer.getNumber("Puntos") != null) {

            puntos = Useer.getNumber("Puntos");
        } else {
            puntos = 0;
        }
        total=puntos.intValue()+calificar.intValue()+compartir.intValue()+asistir.intValue()+megusta.intValue();


        //se sustituyen los valores en el xml
        barra_comentar = (ProgressBar) root.findViewById(R.id.pro_comentar);

        barra_compartir = (ProgressBar) root.findViewById(R.id.progresbar_compartir);

        barra_asistir = (ProgressBar) root.findViewById(R.id.progresbar_asistir);

        barra_megusta = (ProgressBar) root.findViewById(R.id.progresbar_megusta);

        TextView comenta = root.findViewById(R.id.progreso_comenta);

        TextView compartirtext = root.findViewById(R.id.progreso_compartir);

        TextView asistirtext = root.findViewById(R.id.progreso_asistir);

        TextView megustatext = root.findViewById(R.id.progreso_me_gusta);


        TextView textcalificar = root.findViewById(R.id.progreso_evaluar_puntos);
        textcalificar.setText(calificar + " puntos");


        TextView textcompartir = root.findViewById(R.id.progreso_compartir_puntos);
        textcompartir.setText(compartir + " puntos");

        TextView textasistir = root.findViewById(R.id.progreso_asistir_puntos);
        textasistir.setText(asistir + " puntos");

        TextView textmegusta = root.findViewById(R.id.progreso_megusta_puntos);
        textmegusta.setText(megusta + " puntos");

        TextView texttotal = root.findViewById(R.id.total_puntos);
        texttotal.setText(total + " puntos");

        //se actualizan los puntos totales del usuario
        Useer.remove("Puntos");
        Useer.saveInBackground(new SaveCallback() {

            @Override
            public void done(ParseException e) {
                // TODO Auto-generated method stub
                if (e != null){
                    e.printStackTrace();
                }else{
                    //updated successfully
                }
            }
        });
        Useer.put("Puntos",total);
        Useer.saveInBackground(new SaveCallback() {

            @Override
            public void done(ParseException e) {
                // TODO Auto-generated method stub
                if (e != null){
                    e.printStackTrace();
                }else{
                    //updated successfully
                }
            }
        });
        //se modifica el porcentaje de las barras de progreso con los datos del usuario
        barra_comentar.setProgress(calificar.intValue());
        comenta.setText(calificar.intValue() + "%");
        barra_compartir.setProgress(compartir.intValue());
        compartirtext.setText(compartir.intValue() + "%");
        barra_asistir.setMax(asistirlimite);
        barra_asistir.setProgress(asistir.intValue());
        asistirtext.setText(((asistir.intValue()*100)/asistirlimite) + "%");
        barra_megusta.setProgress(megusta.intValue());
        megustatext.setText(megusta.intValue() + "%");

        // Inflate the layout for this fragment
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
