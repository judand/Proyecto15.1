package com.example.prototipo1.proyecto15;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import java.util.ArrayList;



public class EventoAsistireItemAdaptor extends RecyclerView.Adapter<EventoAsistireItemAdaptor.ItemViewHolder> {


    //extraccion del usuario actual
    ParseUser user = ParseUser.getCurrentUser();



    //creacion de variables a utilizar
    private Context mcontext;
    private ArrayList<EventoItem> mitemlist;
    public OnItemClickListener mlistener;

    //funcion cuando se hace click
    public interface OnItemClickListener{
        void OnItemClick(int position);
    }

    public void setOnItemClickLister(OnItemClickListener listener){
        mlistener =listener;
    }

    public EventoAsistireItemAdaptor(Context context, ArrayList<EventoItem> itemlist){
        mcontext=context;
        mitemlist=itemlist;
    }

    //funcion que referencia la maqueta a utilizar
    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(mcontext).inflate(R.layout.items_evento_interes_maqueta, viewGroup , false);
        return new ItemViewHolder(v);
    }

    //funcion que  carga los datos del evento en la maqueta

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder itemViewHolder, int i) {
        final EventoItem currentitem = mitemlist.get(i);
        String mimage= currentitem.getMimageurl();
        final int compartidos = currentitem.getMcompartidos();
        final String mobject = currentitem.getMobjetid();
        final int eventoid = currentitem.getMidevento();
        final String mtitle=currentitem.getMtitle();
        String mlugar=currentitem.getMlugar();
        String mfecha=currentitem.getMfecha();
        final int likes=currentitem.getMgusta();
        int mcostoboleta=currentitem.getMcostoboleta();
        boolean paga =currentitem.isMpaga();


        itemViewHolder.mtexttitle.setText(mtitle);
        itemViewHolder.mtextlugar.setText("Lugar: "+mlugar);
        itemViewHolder.mtextfecha.setText("Fecha: "+mfecha);
        itemViewHolder.mlikes.setText(" "+likes);
        Picasso.get()
                .load(mimage)
                .fit()
                .centerCrop()
                .into(itemViewHolder.mimageview);
        if (paga==true){
            itemViewHolder.mcostoboleta.setText("Precio: $"+mcostoboleta);
        }
        else{
            itemViewHolder.mcostoboleta.setText("Entrada Libre");
        }



        //consulta de estado de botones del usuario

        Integer[] share_user_but = new Integer[0];
        Integer[] like_user_but= new Integer[0];
        Integer[] cali_user_but= new Integer[0];

        boolean share_bu = false;
        boolean cali_bu = false;

        boolean like_bu= false;


        if(user.getList("share_button")!=null){
             share_user_but= user.getList("share_button").toArray(new Integer[0]);

            for(int count=0;count<share_user_but.length;count++){
                if(share_user_but[count].equals(eventoid)){
                    share_bu=true;
                }
            }
        }

        if(user.getList("califi_button")!=null){
            cali_user_but= user.getList("califi_button").toArray(new Integer[0]);
            for(int count=0;count<cali_user_but.length;count++){
                if(cali_user_but[count].equals(eventoid)){
                    cali_bu=true;
                }
            }
        }

        if(user.getList("like_button")!=null){
            like_user_but= user.getList("like_button").toArray(new Integer[0]);
            for(int count=0;count<like_user_but.length;count++){
                if(like_user_but[count].equals(eventoid)){
                    like_bu=true;
                }
            }
        }



        //verificacion de existencia de los botones y si lo esta se deshabilitan
        if(share_bu==true){
            itemViewHolder.boton_compartir.getBackground().setColorFilter(0xe0F2F2F2,PorterDuff.Mode.SRC_ATOP);
            itemViewHolder.boton_compartir.setEnabled(false);
        }

        if(cali_bu==true){
            itemViewHolder.boton_cali.getBackground().setColorFilter(0xe0F2F2F2,PorterDuff.Mode.SRC_ATOP);
            itemViewHolder.boton_cali.setEnabled(false);
        }


        if(like_bu==true){
            itemViewHolder.boton_megusta.setEnabled(false);
            Drawable drawable = itemViewHolder.boton_megusta.getResources().getDrawable(R.drawable.ic_favorite_black_24dp).mutate();
            drawable.setColorFilter(itemViewHolder.boton_megusta.getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_ATOP);
            itemViewHolder.boton_megusta.setBackground(drawable);
        }


        //acciones del boton like al dar click
        itemViewHolder.boton_megusta.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                final int suma;
                final int sumalikes;

                Number nu=0;

                if(user.getNumber("meGusta")!=null){
                    nu =user.getNumber("meGusta");
                    suma =2+nu.intValue();
                }else {
                    suma =2;

                }
               sumalikes=likes+1;

                //si el valor es menor que 100 se agrega de lo contrario no se agregan mas puntos en ese apartado
                if(nu.intValue()<=100){

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
                }


                //se suma un like al evento dado
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


                //se guarda el id para el boton presionado
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






                //se deshabilita el boton
                v.setEnabled(false);
                Drawable drawable = v.getResources().getDrawable(R.drawable.ic_favorite_black_24dp).mutate();
                drawable.setColorFilter(v.getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_ATOP);
                v.setBackground(drawable);


        }



        });

        //acciones del boton compartir
        itemViewHolder.boton_compartir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                //si el valor es menor que 100 se agrega de lo contrario no se agregan mas puntos en ese apartado
                if(nu.intValue()<=100){
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
                }

                 //se suma un compartido al evento dado
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

                //se guarda el id para el boton presionado
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

                //se envia la accion compartir con los textos del titulo del evento y sharebody
                Intent miintent = new Intent(Intent.ACTION_SEND);
                miintent.setType("text/plain");
                String sharebody ="solo en movil eventos!!!";
                miintent.putExtra(Intent.EXTRA_TEXT,mtitle+"\n"+sharebody);
                v.getContext().startActivity(Intent.createChooser(miintent,"Compartir en"));
                //se deshabilita el boton
                v.getBackground().setColorFilter(0xe0F2F2F2,PorterDuff.Mode.SRC_ATOP);
                v.setEnabled(false);

            }
        });
        //acciones del boton calificar
        itemViewHolder.boton_cali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //se crea un intent para enviar los datos a la actividad CalificarActivity
                Intent i =new Intent(v.getContext(), CalificarActivity.class);
                //se llenan las variables para llamarlas en la otra clase

                i.putExtra("extra_object", currentitem.getMobjetid());
                i.putExtra("extra_logistica", currentitem.getMlogistica());
                i.putExtra("extra_comodidad", currentitem.getMcomodidad());
                i.putExtra("extra_entretenido", currentitem.getMentretenido());
                i.putExtra("extra_interesante", currentitem.getMinteresante());

                //se añade el id del evento calificado
                user.addUnique("califi_button",eventoid);
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
                //se deshabilita el boton
                v.getBackground().setColorFilter(0xe0F2F2F2,PorterDuff.Mode.SRC_ATOP);
                v.setEnabled(false);
                v.getContext().startActivity(i);
            }
        });



    }

    @Override
    public int getItemCount() {
        return mitemlist.size();
    }

    //funcion que hace referencia a los elementos del xml
    public class ItemViewHolder extends RecyclerView.ViewHolder{
        public ImageView mimageview;
        public TextView mtexttitle;
        public TextView mtextlugar;
        public TextView mtextfecha;
        public TextView mcostoboleta;
        public TextView mlikes;
        Button boton_megusta;

        Button boton_compartir;
        Button boton_cali;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            boton_megusta= itemView.findViewById(R.id.like_boton);

            boton_compartir= itemView.findViewById(R.id.boton_compartir);
            boton_cali = itemView.findViewById(R.id.boton_coment);

            mimageview=itemView.findViewById(R.id.image_view_h);
            mtexttitle=itemView.findViewById(R.id.text_title_h);
            mtextlugar=itemView.findViewById(R.id.text_lugar_h);
            mtextfecha=itemView.findViewById(R.id.text_fecha_h);
            mcostoboleta=itemView.findViewById(R.id.text_costoboleta_h);
            mlikes=itemView.findViewById(R.id.text_likes);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mlistener != null){
                        int position =getAdapterPosition();
                        if(position!= RecyclerView.NO_POSITION){
                            mlistener.OnItemClick(position);

                        }
                    }
                }
            });
        }
    }
}
