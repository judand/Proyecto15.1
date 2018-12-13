package com.example.prototipo1.proyecto15;

import android.graphics.Bitmap;

import java.io.File;

public class RecompensaItem {
    private String mobjetid;
    private int IdRecompensa;
    private String TituloRecompensa;
    private String UrlImagen;
    private String DescripcionRecompensa;
    private Bitmap QrcodeRecompensa;
    private int costoRecompensa;

    public RecompensaItem (int id,String titulo,String url,String descripcion,Bitmap qr,String object,int costo){
        IdRecompensa=id;
        TituloRecompensa=titulo;
        UrlImagen=url;
        DescripcionRecompensa=descripcion;
        QrcodeRecompensa=qr;
        mobjetid=object;
        costoRecompensa= costo;
    }

    public int getIdRecompensa() {
        return IdRecompensa;
    }

    public String getTituloRecompensa() {
        return TituloRecompensa;
    }

    public String getUrlImagen() {
        return UrlImagen;
    }

    public String getDescripcionRecompensa() {
        return DescripcionRecompensa;
    }

    public Bitmap getQrcodeRecompensa() {
        return QrcodeRecompensa;
    }

    public String getMobjetid() {
        return mobjetid;
    }

    public int getCostoRecompensa() {
        return costoRecompensa;
    }
}
