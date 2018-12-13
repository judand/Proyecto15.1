package com.example.prototipo1.proyecto15;

import android.app.Dialog;
import android.content.Context;
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

import com.parse.ParseUser;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class RecompensaAdaptorCanje extends RecyclerView.Adapter<RecompensaAdaptorCanje.ItemViewHolder> {

    //extraccion del usuario actual
    ParseUser use = ParseUser.getCurrentUser();

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
    public RecompensaAdaptorCanje(Context context, ArrayList<RecompensaItem> itemlist){
        mcontext=context;
        mitemlist=itemlist;
    }
    //funcion que referencia la maqueta a cargar
    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(mcontext).inflate(R.layout.content_recompensa_canjeada, viewGroup , false);
        return new ItemViewHolder(v);
    }
    //funcion que  carga los datos de las recompensas
    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder itemViewHolder, int i) {
        final RecompensaItem currentitem = mitemlist.get(i);
        String mimage= currentitem.getUrlImagen();
        final int recompensaid = currentitem.getIdRecompensa();
        final String mtitle=currentitem.getTituloRecompensa();
        itemViewHolder.mtexttitle.setText(mtitle);
        Picasso.get()
                .load(mimage)
                .fit()
                .centerCrop()
                .into(itemViewHolder.mimageview);


        //consulta de estado de botones del usuario
        Integer[] canj_user_but = new Integer[0];

        boolean canj_bu = false;

        if(use.getList("idrecompensas")!=null){
             canj_user_but= use.getList("idrecompensas").toArray(new Integer[0]);

            for(int count=0;count<canj_user_but.length;count++){
                if(canj_user_but[count].equals(recompensaid)){
                    canj_bu=true;
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return mitemlist.size();
    }

    //funcion que hace referencia a los alementos para sustituir los valores al cargar
    public class ItemViewHolder extends RecyclerView.ViewHolder{
        public ImageView mimageview;
        public TextView mtexttitle;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            mimageview=itemView.findViewById(R.id.image_catrecomp);
            mtexttitle=itemView.findViewById(R.id.title_recomp);
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
