package com.example.prototipo1.proyecto15;

import java.util.ArrayList;

public class EventoItem {
    private String mobjetid;
    private int midevento;
    private String mtitle;
    private String mimageurl;
    private String mlugar;
    private String mfecha;
    private String mhora;
    private int mcostoboleta;
    private int mcapacidad;
    private boolean mpaga;
    private String mcategoria;
    private int mgusta;
    private String mdescripcion;
    private int mlogistica;
    private int mcomodidad;
    private int mentretenido;
    private int minteresante;
    private int mcompartidos;
    private int minteresados;


    public EventoItem(String title, String imageurl, String lugar, int capacidad, String descripcion, String fecha, String hora, int costoboleta, boolean paga, String categoria, int gusta, int idevento, String objetid, int logistica, int comodidad, int entretenido, int interesante, int compartidos, int interesados){
        mtitle=title;
        mimageurl=imageurl;
        mlugar=lugar;
        mcapacidad=capacidad;
        mdescripcion=descripcion;
        mfecha=fecha;
        mhora=hora;
        mcostoboleta=costoboleta;
        mpaga=paga;
        mcategoria=categoria;
        mgusta=gusta;
        midevento=idevento;
        mobjetid=objetid;
        mlogistica=logistica;
        mcomodidad= comodidad;
        mentretenido= entretenido;
        minteresante= interesante;
        mcompartidos=compartidos;
        minteresados=interesados;

    }

    public String getMimageurl() {
        return mimageurl;
    }

    public String getMtitle() {
        return mtitle;
    }

    public String getMlugar() {
        return mlugar;
    }

    public int getMcapacidad() {
        return mcapacidad;
    }

    public String getMdescripcion() {
        return mdescripcion;
    }

    public String getMfecha() {
        return mfecha;
    }

    public String getMhora() {
        return mhora;
    }

    public int getMcostoboleta() {
        return mcostoboleta;
    }

    public boolean isMpaga() {
        return mpaga;
    }

    public int getMgusta() {
        return mgusta;
    }

    public String getMcategoria() {
        return mcategoria;
    }

    public int getMidevento() {
        return midevento;
    }

    public String getMobjetid() {
        return mobjetid;
    }

    public int getMcomodidad() {
        return mcomodidad;
    }

    public int getMentretenido() {
        return mentretenido;
    }

    public int getMinteresante() {
        return minteresante;
    }

    public int getMlogistica() {
        return mlogistica;
    }

    public int getMcompartidos() {
        return mcompartidos;
    }

    public int getMinteresados() {
        return minteresados;
    }
}
