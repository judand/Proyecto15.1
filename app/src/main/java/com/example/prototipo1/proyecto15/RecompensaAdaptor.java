package com.example.prototipo1.proyecto15;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.PorterDuff;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class RecompensaAdaptor extends RecyclerView.Adapter<RecompensaAdaptor.ItemViewHolder> {
    //extraccion del usuario actual
    ParseUser user = ParseUser.getCurrentUser();

    //creacion de variables a utilizar
    private Context mcontext;
    private ArrayList<RecompensaItem> mitemlist;
    public OnItemClickListener mlistener;

    //funcion cuando se hace click
    public interface OnItemClickListener{
        void OnItemClick(int position);
    }

    public void setOnItemClickLister(OnItemClickListener listener){
        mlistener =listener;


    }

    public RecompensaAdaptor(Context context, ArrayList<RecompensaItem> itemlist){
        mcontext=context;
        mitemlist=itemlist;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(mcontext).inflate(R.layout.content_recompensa, viewGroup , false);
        return new ItemViewHolder(v);
    }

    //funcion que  carga los datos del json en el context

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder itemViewHolder, int i) {
        final RecompensaItem currentitem = mitemlist.get(i);
        String mimage= currentitem.getUrlImagen();
        final int recompensaid = currentitem.getIdRecompensa();
        final String mtitle=currentitem.getTituloRecompensa();
        final int costore=currentitem.getCostoRecompensa();

        Log.d("Recompensaid", String.valueOf(recompensaid));
        Log.d("titulorecomp", mtitle);


        itemViewHolder.mtexttitle.setText(mtitle);
        itemViewHolder.costo.setText("Costo: "+costore);

        Picasso.get()
                .load(mimage)
                .fit()
                .centerCrop()
                .into(itemViewHolder.mimageview);


        //consulta de estado de botones del usuario

        Integer[] canj_user_but = new Integer[0];


        boolean canj_bu = false;


        if(user.getList("idrecompensas")!=null){
             canj_user_but= user.getList("idrecompensas").toArray(new Integer[0]);

            for(int count=0;count<canj_user_but.length;count++){
                if(canj_user_but[count].equals(recompensaid)){
                    canj_bu=true;
                }
            }
        }


        if(canj_bu==true){
            itemViewHolder.boton_canj.getBackground().setColorFilter(0xe0F2F2F2,PorterDuff.Mode.SRC_ATOP);
            itemViewHolder.boton_canj.setEnabled(false);
        }
        //acciones del boton canjear
        itemViewHolder.boton_canj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                final Number total;
                if (user.getNumber("Puntos") != null) {
                    total = user.getNumber("Puntos");
                } else {
                    total = 0;
                }
                if (total.intValue()<costore){
                    RecompensaAdaptor.ViewDialog alert = new ViewDialog();
                    alert.showDialog(v.getContext(), "no tienes puntos suficientes para canjear esta recompensa");
                }else{
                    AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
                    alert.setMessage("¿Quieres canjear la recompensa?").setCancelable(false)
                            .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    int resta =total.intValue()-costore;
                                    Log.d("resta", String.valueOf(resta));
                                    user.put("Puntos",resta);
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
                                    user.remove("asistir");
                                    user.saveInBackground(new SaveCallback() {

                                            @Override
                                            public void done(ParseException e) {
                                                // TODO Auto-generated method stub
                                                if (e != null){
                                                    e.printStackTrace();
                                                }else{
                                                    //updated successfully
                                                } }
                                    });

                                    user.remove("calificar");
                                    user.saveInBackground(new SaveCallback() {

                                            @Override
                                            public void done(ParseException e) {
                                                // TODO Auto-generated method stub
                                                if (e != null){
                                                    e.printStackTrace();
                                                }else{
                                                    //updated successfully
                                                } }
                                    });

                                    user.remove("compartir");
                                    user.saveInBackground(new SaveCallback() {

                                            @Override
                                            public void done(ParseException e) {
                                                // TODO Auto-generated method stub
                                                if (e != null){
                                                    e.printStackTrace();
                                                }else{
                                                    //updated successfully
                                                } }
                                    });
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

                                    RecompensaAdaptor.ViewDialog alert = new ViewDialog();
                                    alert.showDialog(v.getContext(), "Has canjeado la recompensa con exito!");
                                    user.addUnique("idrecompensas",recompensaid);
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

                                    v.getBackground().setColorFilter(0xe0F2F2F2,PorterDuff.Mode.SRC_ATOP);
                                    v.setEnabled(false);
                                }
                            })
                            .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog al = alert.create();
                    al.setTitle("Advertencia");
                    al.show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mitemlist.size();
    }

    //funcion que hace referencia a los alementos para sustituir los valores al cargar
    public class ItemViewHolder extends RecyclerView.ViewHolder{
        public ImageView mimageview;
        public TextView mtexttitle;
        public TextView costo;

        Button boton_canj;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            boton_canj = itemView.findViewById(R.id.boton_canjear);

            mimageview=itemView.findViewById(R.id.image_catrecomp);
            mtexttitle=itemView.findViewById(R.id.title_recomp);
            costo=itemView.findViewById(R.id.text_costorecomp_h);


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
    //funcion del cuadro emergente
    public class ViewDialog {

        public void showDialog(Context context, String msg){
            final Dialog dialog = new Dialog(context);
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

                }
            });

            dialog.show();

        }
    }
}
