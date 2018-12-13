package com.example.prototipo1.proyecto15;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;




public class CalificarActivity extends AppCompatActivity {

    //creacion de las estrellas para calificar y el boton
    RatingBar estrella_barra_logistica;
    RatingBar estrella_barra_comodidad;
    RatingBar estrella_barra_entretenido;
    RatingBar estrella_barra_interesante;

    Button boton_cali;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //carga del layout en esta actividad
        setContentView(R.layout.activity_comentar);
        //extracion de las variables enviadas
        Intent intent =getIntent();
        final String object= intent.getStringExtra("extra_object");
        final int logis = intent.getIntExtra("extra_logistica",0);
        final int comod = intent.getIntExtra("extra_comodidad",0);
        final int entre = intent.getIntExtra("extra_entretenido",0);
        final int intere= intent.getIntExtra("extra_interesante",0);

        //declaracion del usuario actual
        final ParseUser user = ParseUser.getCurrentUser();
        //referencia del boton
        boton_cali= (Button) findViewById(R.id.boton_calificar);

        //barra logistica
        estrella_barra_logistica = (RatingBar) findViewById(R.id.ratingBar_logistica);

        estrella_barra_logistica.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                Toast.makeText(CalificarActivity.this, "Logistica :"+v,Toast.LENGTH_SHORT).show();
            }
        });

        //barra_comodidad
        estrella_barra_comodidad = (RatingBar) findViewById(R.id.ratingBar_comodidad);

        estrella_barra_comodidad.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                Toast.makeText(CalificarActivity.this, "Comodidad :"+v,Toast.LENGTH_SHORT).show();
            }
        });

        //barra entretenido
        estrella_barra_entretenido = (RatingBar) findViewById(R.id.ratingBar_entretenido);

        estrella_barra_entretenido.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                Toast.makeText(CalificarActivity.this, "Entretenido :"+v,Toast.LENGTH_SHORT).show();
            }
        });

        //barra interesante
        estrella_barra_interesante = (RatingBar) findViewById(R.id.ratingBar_interesante);

        estrella_barra_interesante.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                Toast.makeText(CalificarActivity.this, "interesante :"+v,Toast.LENGTH_SHORT).show();
            }
        });

        //boton calificar acciones al hacer click
        boton_cali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //creacion de las variables que guarda la calificacion
                final Number sumalogistica;
                final Number sumacomodidad;
                final Number sumaentretenido;
                final Number sumainteresante;
                //variable que extrae la puntuacion del usuario en sus puntos de calificar
                Number calificarusuario;
                if(user.getNumber("calificar")!=null){

                    calificarusuario=user.getNumber("calificar");
                    calificarusuario =2+calificarusuario.intValue();
                }
                else {
                    calificarusuario=2;

                }

                sumalogistica=logis+estrella_barra_logistica.getRating();
                sumacomodidad=comod+estrella_barra_comodidad.getRating();
                sumaentretenido=entre+estrella_barra_entretenido.getRating();
                sumainteresante= intere+estrella_barra_interesante.getRating();

                if(calificarusuario.intValue()<=100){
                    //funcion que elimina
                    user.remove("calificar");
                    user.saveInBackground(new SaveCallback() {

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
                    //funcion que actualizar el valor
                    user.put("calificar",calificarusuario);

                    user.saveInBackground(new SaveCallback() {

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

                }



                ParseQuery<ParseObject> query = ParseQuery.getQuery("eventos");
                // busqueda y actualizacion de la calificacion en el evento
                query.getInBackground(object, new GetCallback<ParseObject>() {
                    public void done(ParseObject calificacion, ParseException e) {
                        if (e == null) {

                           calificacion.put("logistica",sumalogistica);
                           calificacion.put("comodidad",sumacomodidad);
                           calificacion.put("entretenido",sumaentretenido);
                           calificacion.put("interesante",sumainteresante);
                            calificacion.saveInBackground();
                        }
                    }
                });



                finish();
            }
        });


    }
    //funcion que cambia la interaccion de ir atras
    @Override
    public void onBackPressed() {
        Toast toast1 = Toast.makeText(getApplicationContext(), "Califica antes de salir", Toast.LENGTH_SHORT);
        toast1.show();
    }
}
