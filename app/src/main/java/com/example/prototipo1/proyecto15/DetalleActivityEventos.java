package com.example.prototipo1.proyecto15;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.squareup.picasso.Picasso;


public class DetalleActivityEventos extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //carga del layout en esta actividad
        setContentView(R.layout.activity_detalle);
        //extraccion del usuario actual, y correo
        final ParseUser user = ParseUser.getCurrentUser();

        //extraccion de las variables antes definidas de EventosDrawAdaptor
        final Intent intent =getIntent();
        final String mobject =intent.getStringExtra("extra_objet");
        String imageurl= intent.getStringExtra("extra_url");
        final String title=intent.getStringExtra("extra_titulo");
        String lugar= intent.getStringExtra("extra_lugar");
        int capacidad = intent.getIntExtra("extra_capacidad",0);
        String descrpcion = intent.getStringExtra("extra_descripcion");
        String fecha =intent.getStringExtra("extra_fecha");
        String hora =intent.getStringExtra("extra_hora");

        int costoboleta=intent.getIntExtra("extra_costoboleta",0);
        final int eventoid=intent.getIntExtra("extra_idevento",0);
        final int compartidos=intent.getIntExtra("extra_compartidos",0);
        final int mgusta=intent.getIntExtra("extra_megusta",0);

        //creacion del arraylist del boton compartir y boolean
        Integer[] share_user_but = new Integer[0];
        boolean share_bu = false;
        if(user.getList("share_button")!=null){
            share_user_but= user.getList("share_button").toArray(new Integer[0]);

            //for para comprobar si el boton ya fue accionado antes
            for(int count=0;count<share_user_but.length;count++){
                if(share_user_but[count].equals(eventoid)){
                    share_bu=true;
                }
            }
        }
        //creacion del arraylist del boton like y boolean
        Integer[] like_user_but = new Integer[0];
        boolean like_bu = false;
        if(user.getList("like_button")!=null){
            like_user_but= user.getList("like_button").toArray(new Integer[0]);
            //for para comprobar si el boton ya fue accionado antes
            for(int count=0;count<like_user_but.length;count++){
                if(like_user_but[count].equals(eventoid)){
                    like_bu=true;
                }
            }
        }

        //sustitutucion de cada imageview, textview con los datos del eventos json en cuestion


        ImageView imageview = findViewById(R.id.image_view_deta);
        TextView texttitulo =findViewById(R.id.text_title_deta);
        TextView textlugar = findViewById(R.id.text_lugar_deta);
        TextView textcapacidad = findViewById(R.id.text_capacidad_deta);
        TextView textdescripcion = findViewById(R.id.text_descripcion_deta);
        TextView textfecha=findViewById(R.id.text_fecha_deta);
        TextView texthora= findViewById(R.id.text_hora_deta);
        TextView textcostoboleta=findViewById(R.id.text_costoboleta_deta);
        Button atras = findViewById(R.id.atras);
        Button compartir = findViewById(R.id.compartir);
        Button megusta =findViewById(R.id.like_boton);

        //boton atras
        atras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(DetalleActivityEventos.this, EventosDrawActivity.class));

            }
        });
        //si el boton es del evento actual se deshabilita
        if(share_bu==true){

            compartir.getBackground().setColorFilter(0xe040a8c4,PorterDuff.Mode.SRC_ATOP);

            compartir.setEnabled(false);
        }
        //si el boton es del evento actual se deshabilita
        if(like_bu==true){

            megusta.getBackground().setColorFilter(0xe040a8c4,PorterDuff.Mode.SRC_ATOP);

            megusta.setEnabled(false);
        }
        //accion del boton compartir al dar click
        compartir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //declaracion de variables suma y sumalikes que actualiza los compartir del evento seleccionado
                final int suma;
                final int sumalikes;

                Number nu=0;

                if(user.getNumber("compartir")!=null){
                    nu =user.getNumber("compartir");
                    suma =2+nu.intValue();
                }else {
                    suma =2;

                }
                sumalikes=compartidos+1;


                user.remove("compartir");
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
                user.put("compartir",suma);

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
                //actualizacion de los compartir del evento
                ParseQuery<ParseObject> evento = new ParseQuery<ParseObject>("eventos");
                evento.getInBackground(mobject, new GetCallback<ParseObject>() {
                    @Override
                    public void done(ParseObject object, ParseException e) {
                        object.put("compartidos",sumalikes);
                        object.saveInBackground(new SaveCallback() {

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
                });
                //se añade el evento al array del usuario
                user.addUnique("share_button",eventoid);
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

                //se envia el evento para compartir, el titulo y el texto de sharebody
                Intent miintent = new Intent(Intent.ACTION_SEND);
                miintent.setType("text/plain");
                String sharebody ="solo en movil eventos!!!";
                miintent.putExtra(Intent.EXTRA_TEXT,title+"\n"+sharebody);
                v.getContext().startActivity(Intent.createChooser(miintent,"Compartir en"));

                //se deshabilita el boton al hacer click
                v.getBackground().setColorFilter(0xe040a8c4,PorterDuff.Mode.SRC_ATOP);
                v.setEnabled(false);
            }
        });
        //accion del boton like al dar click
        megusta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //declaracion de variables suma y sumalikes que actualiza likes del evento seleccionado
                final int suma;
                final int sumalikes;

                Number nu=0;

                if(user.getNumber("meGusta")!=null){
                    nu =user.getNumber("meGusta");
                    suma =2+nu.intValue();
                }else {
                    suma =2;

                }
                sumalikes=mgusta+1;


                user.remove("meGusta");
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
                user.put("meGusta",suma);

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

                //actualizacion de likes del evento
                ParseQuery<ParseObject> evento = new ParseQuery<ParseObject>("eventos");
                evento.getInBackground(mobject, new GetCallback<ParseObject>() {
                    @Override
                    public void done(ParseObject object, ParseException e) {
                        object.put("meGusta",sumalikes);
                        object.saveInBackground(new SaveCallback() {

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
                });

                //se añade el evento al array del usuario
                user.addUnique("like_button",eventoid);
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

                //se deshabilita el boton al hacer click
                v.getBackground().setColorFilter(0xe040a8c4,PorterDuff.Mode.SRC_ATOP);
                v.setEnabled(false);
            }
        });
        //funcion para cargar la url de la imagen
        Picasso.get()
                .load(imageurl)
                .fit()
                .centerInside()
                .into(imageview);


        //se sustituyen los valores cargados
        texttitulo.setText(title);
        textlugar.setText("Lugar: "+lugar);
        textcapacidad.setText("Capacidad: "+capacidad);
        textdescripcion.setText(descrpcion);
        textfecha.setText("Fecha:"+fecha);
        texthora.setText("Hora:"+hora);
        if(costoboleta!=0){
            textcostoboleta.setText("Precio: $"+costoboleta);
        }else{
            textcostoboleta.setText("Entrada Libre");
        }



    }
    //funcion del cuadro de dialogo emergente
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
                    startActivity(new Intent(DetalleActivityEventos.this, EventosDrawActivity.class));
                }
            });

            dialog.show();

        }
    }

}
