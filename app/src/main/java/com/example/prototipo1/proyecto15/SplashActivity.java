package com.example.prototipo1.proyecto15;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;

import android.support.v7.app.AppCompatActivity;

import com.parse.ParseUser;

public class SplashActivity extends AppCompatActivity {

    MediaPlayer cancion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //extraccion del usuario actual
        ParseUser currentUser = ParseUser.getCurrentUser();

        if (currentUser != null) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent= new Intent();
                    intent = new Intent(SplashActivity.this,EventosDrawActivity.class);
                    startActivity(intent);
                    finish();
                }
            },2000);
            // se establece el tiempo de carga en milisegundos
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(SplashActivity.this,Login_activity.class);
                    startActivity(intent);
                    finish();

                }
            },2000);
            // muestra de nuevo el login
        }




    }

}
