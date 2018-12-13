package com.example.prototipo1.proyecto15;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.ParseUser;
import com.squareup.picasso.Picasso;

public class DetalleRecompensa extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //carga del layout en esta actividad
        setContentView(R.layout.activity_detalle_recompensa);

        //extraccion del usuario actual
        ParseUser userr= ParseUser.getCurrentUser();

        //extraccion de las variables antes definidas de EventosDrawActivity
        final Intent intent =getIntent();
        final String mobject =intent.getStringExtra("extra_objet");
        final int idrecomp = intent.getIntExtra("extra_id",0);
        String imageurl= intent.getStringExtra("extra_url");
        final String title=intent.getStringExtra("extra_titulo");
        final String desc=intent.getStringExtra("extra_descripcion");
        Bitmap qrimage=(Bitmap) intent.getParcelableExtra("extra_qr");

        //creacion del arraylist del boton canje y boolean
        Integer[] canje_user = new Integer[0];
        Boolean canje_bolean=false;

        //sustitutucion de cada imageview, textview con los datos de la recompensa
        ImageView imageview = findViewById(R.id.image_recompensa);
        ImageView imageqr = findViewById(R.id.image_qr);
        TextView texttitulo =findViewById(R.id.text_title_recom);

        TextView textdescripcion = findViewById(R.id.text_descripcion_recomp);

        Picasso.get()
                .load(imageurl)
                .fit()
                .centerInside()
                .into(imageview);
        texttitulo.setText(title);
        textdescripcion.setText(desc);

        if(userr.getList("idrecompensas")!=null){
            canje_user= userr.getList("idrecompensas").toArray(new Integer[0]);

            for(int count=0;count<canje_user.length;count++){
                if(canje_user[count].equals(idrecomp)){
                    canje_bolean=true;
                }
            }
        }

        //si se encuentra el id de la recompensa se habilita el qr
        if(canje_bolean==true){
            CardView cardqr =(CardView)findViewById(R.id.card_image);
            cardqr.setVisibility(View.VISIBLE);
            imageqr.setImageBitmap(qrimage);
            CardView cardtext =(CardView)findViewById(R.id.card_descp);
            cardtext.setVisibility(View.VISIBLE);
        }



    }
}
