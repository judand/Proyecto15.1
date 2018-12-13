package com.example.prototipo1.proyecto15;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.parse.ParseUser;

public class LogrosDrawActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,Fragment_logros.OnFragmentInteractionListener,Fragment_recompensas.OnFragmentInteractionListener,CatalogoRecompensaActivity.OnFragmentInteractionListener {
    //creacion de variables de fragmento para referenciar y menu inferior
    Fragment_logros logros;
    Fragment_recompensas recomp;
    private BottomNavigationView Menuinferior;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //carga del layout en esta actividad
        setContentView(R.layout.activity_logros_draw);

        //barra superior donde se desplega como ordenar los eventos y el nombre
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //extraccion del usuario actual, y correo
        final ParseUser pUser= ParseUser.getCurrentUser();
        String correo_usuario = pUser.getEmail();
        String usuario = pUser.getUsername();

        View headerView = navigationView.getHeaderView(0);
        //carga del usuario y correo en el menu
        TextView nombre = (TextView) headerView.findViewById(R.id.nombre_user);
        nombre.setText(usuario);
        TextView correo = (TextView) headerView.findViewById(R.id.correo_user);
        correo.setText(correo_usuario);

        //declaracion de los fragmentos
        logros = new Fragment_logros();
        recomp = new Fragment_recompensas();

        getSupportFragmentManager().beginTransaction().add(R.id.contenedor,logros).commit();
        //menu inferior declaracion
        Menuinferior = findViewById(R.id.navega_logros);
        Menuinferior.setOnNavigationItemSelectedListener(navlister);
        Menuinferior.getMenu().getItem(0).setCheckable(true);

    }
    //funcion que referencia el menu de abajo y referencia sus interacciones
    private BottomNavigationView.OnNavigationItemSelectedListener navlister=
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    Fragment selectfragment = null;

                    switch (menuItem.getItemId()){
                        case R.id.botton_tienda:
                            selectfragment =new CatalogoRecompensaActivity();
                            Menu menu3 = Menuinferior.getMenu();
                            MenuItem menuitem3 = menu3.getItem(1);
                            menuitem3.setChecked(true);
                            menuitem3.setCheckable(true);
                            break;

                        case R.id.botton_logros:
                            selectfragment = new Fragment_logros();
                            Menu menu1 = Menuinferior.getMenu();
                            MenuItem menuitem1 = menu1.getItem(0);
                            menuitem1.setChecked(true);
                            menuitem1.setCheckable(true);

                            break;


                        case R.id.botton_recompensas:
                            selectfragment = new Fragment_recompensas();
                            Menu menu = Menuinferior.getMenu();
                            MenuItem menuitem = menu.getItem(2);
                            menuitem.setChecked(true);
                            menuitem.setCheckable(true);
                            break;
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.contenedor,selectfragment).commit();


                    return false;
                }
            };
    //funcion que va atras
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    //funcion que ejecuta cada opcion de los items del menu que no se usan en esta actividad
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    //menu lateral y referencias de a donde ir al seleccionar
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id){
            case R.id.nav_eventos:
                Intent intentevento = new Intent(LogrosDrawActivity.this,EventosDrawActivity.class);
                startActivity(intentevento);


                break;

            case R.id.nav_logros:
                startActivity(getIntent());
                break;
            case R.id.nav_settings:
                Intent intentperfil = new Intent(LogrosDrawActivity.this,SettingsActivity.class);

                startActivity(intentperfil);
                break;
            case R.id.nav_info:
                Intent intentayuda = new Intent(LogrosDrawActivity.this, InfoActivity.class);
                startActivity(intentayuda);
                break;
            case R.id.nav_sesion:
                ParseUser.logOut();
                alertDisplayer("Cerrando sesión", "vuelve pronto...!!!");
                break;

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
    //funcion del cuadro de dialogo emergente
    private void alertDisplayer(String title,String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(LogrosDrawActivity.this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        Intent intent = new Intent(LogrosDrawActivity.this, Login_activity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                });
        AlertDialog ok = builder.create();
        ok.show();
    }
}
