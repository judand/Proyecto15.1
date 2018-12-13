package com.example.prototipo1.proyecto15;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class logros extends AppCompatActivity implements Fragment_logros.OnFragmentInteractionListener,Fragment_recompensas.OnFragmentInteractionListener{


    Fragment_logros logros;
    Fragment_recompensas recomp;
    private BottomNavigationView botton;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logros);

            logros = new Fragment_logros();
            recomp = new Fragment_recompensas();

            getSupportFragmentManager().beginTransaction().add(R.id.contenedor,logros).commit();
            //boton barra superior
            botton = findViewById(R.id.navega_logros);
            botton.setOnNavigationItemSelectedListener(navlister);
            botton.getMenu().getItem(0).setCheckable(true);

            //boton barra inferior
            BottomNavigationView bottonbajo = findViewById(R.id.navega_activity);
            Menu menu =bottonbajo.getMenu();
            MenuItem menuitem = menu.getItem(1);
            menuitem.setChecked(true);



            bottonbajo.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    switch (menuItem.getItemId()){

                        case R.id.Eventos:
                            Intent intentevento = new Intent(logros.this,centro.class);
                            startActivity(intentevento);
                            break;



                        case R.id.recompensas:
                            break;

                        case R.id.perfil:
                            Intent intentperfil = new Intent(logros.this,SettingsActivity.class);
                            startActivity(intentperfil);
                            break;


                    }

                    return true;
                }
            });





    }

    private BottomNavigationView.OnNavigationItemSelectedListener navlister=
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    Fragment selectfragment = null;

                    switch (menuItem.getItemId()){

                        case R.id.botton_logros:
                            selectfragment = new Fragment_logros();
                            Menu menu1 =botton.getMenu();
                            MenuItem menuitem1 = menu1.getItem(0);
                            menuitem1.setChecked(true);
                            menuitem1.setCheckable(true);

                            break;


                        case R.id.botton_recompensas:
                            selectfragment = new Fragment_recompensas();
                            Menu menu =botton.getMenu();
                            MenuItem menuitem = menu.getItem(1);
                            menuitem.setChecked(true);
                            menuitem.setCheckable(true);
                            break;
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.contenedor,selectfragment).commit();


                    return false;
                }
            };

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
    public void Recompensas(View v)
    {

        Intent intent = new Intent(v.getContext(), DetalleRecompensa.class);
        startActivity(intent);

    }

}
