package com.example.prototipo1.proyecto15;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.SwitchPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.RingtonePreference;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A {@link PreferenceActivity} that presents a set of application settings. On
 * handset devices, settings are presented as a single list. On tablets,
 * settings are split by category, with category headers shown to the left of
 * the list of settings.
 * <p>
 * See <a href="http://developer.android.com/design/patterns/settings.html">
 * Android Design: Settings</a> for design guidelines and the <a
 * href="http://developer.android.com/guide/topics/ui/settings.html">Settings
 * API Guide</a> for more information on developing a Settings UI.
 */
public class SettingsActivity extends AppCompatPreferenceActivity {

    /**
     * A preference value change listener that updates the preference's summary
     * to reflect its new value.
     */
    private static Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object value) {
            String stringValue = value.toString();
            //instancias predefinidas creadas por defecto
            if (preference instanceof ListPreference) {
                // For list preferences, look up the correct display value in
                // the preference's 'entries' list.
                ListPreference listPreference = (ListPreference) preference;
                int index = listPreference.findIndexOfValue(stringValue);

                // Set the summary to reflect the new value.
                preference.setSummary(
                        index >= 0
                                ? listPreference.getEntries()[index]
                                : null);

            } else if (preference instanceof RingtonePreference) {
                // For ringtone preferences, look up the correct display value
                // using RingtoneManager.
                if (TextUtils.isEmpty(stringValue)) {
                    // Empty values correspond to 'silent' (no ringtone).
                    preference.setSummary(R.string.pref_ringtone_silent);

                } else {
                    Ringtone ringtone = RingtoneManager.getRingtone(
                            preference.getContext(), Uri.parse(stringValue));

                    if (ringtone == null) {
                        // Clear the summary if there was a lookup error.
                        preference.setSummary(null);
                    } else {
                        // Set the summary to reflect the new ringtone display
                        // name.
                        String name = ringtone.getTitle(preference.getContext());
                        preference.setSummary(name);
                    }
                }

            } else {
                // For all other preferences, set the summary to the value's
                // simple string representation.
                preference.setSummary(stringValue);
            }
            return true;
        }
    };


    /**
     * Helper method to determine if the device has an extra-large screen. For
     * example, 10" tablets are extra-large.
     */
    private static boolean isXLargeTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_XLARGE;
    }

    /**
     * Binds a preference's summary to its value. More specifically, when the
     * preference's value is changed, its summary (line of text below the
     * preference title) is updated to reflect the value. The summary is also
     * immediately updated upon calling this method. The exact display format is
     * dependent on the type of preference.
     *
     * @see #sBindPreferenceSummaryToValueListener
     */
    private static void bindPreferenceSummaryToValue(Preference preference) {
        // Set the listener to watch for value changes.
        preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);

        // Trigger the listener immediately with the preference's
        // current value.
        sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,
                PreferenceManager
                        .getDefaultSharedPreferences(preference.getContext())
                        .getString(preference.getKey(), ""));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupActionBar();

    }



    /**
     * Set up the {@link android.app.ActionBar}, if the API is available.
     */
    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            if (!super.onMenuItemSelected(featureId, item)) {
                NavUtils.navigateUpFromSameTask(this);
            }
            return true;
        }
        return super.onMenuItemSelected(featureId, item);
    }

    //funcion que muestra la ventana emergente
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
                }
            });

            dialog.show();

        }
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onIsMultiPane() {
        return isXLargeTablet(this);
    }

    /**
     * {@inheritDoc}
     */


    @Override
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void onBuildHeaders(List<Header> target) {

        //menu donde se escoge que personalizar y se crean los mensajes hacia el usuario
        loadHeadersFromResource(R.xml.pref_headers, target);
        boolean b_IsConnect = isNetworkAvailable();
        if(b_IsConnect==false){
            Log.d("hola","evalue");
            SettingsActivity.ViewDialog alert = new ViewDialog();
            alert.showDialog(SettingsActivity.this, "Upps! no tienes conexion a internet por el momento");
        }
        else
        {
            ParseUser Userr = ParseUser.getCurrentUser();
            Integer dat;
            if(Userr.getList("preferencias")==null){
                SettingsActivity.ViewDialog alert = new ViewDialog();
                alert.showDialog(SettingsActivity.this, "Bienvenido!!!!\n Recuerda establecer tus preferencias de eventos en configurar eventos");

            }
            else{
                dat=Userr.getList("preferencias").size();

                if(dat==0){

                    SettingsActivity.ViewDialog alert = new ViewDialog();
                    alert.showDialog(SettingsActivity.this, "Recuerda establecer tus preferencias de eventos en configurar eventos");
                }
            }

        }
           }
    /**
     * This method stops fragment injection in malicious applications.
     * Make sure to deny any unknown fragments here.
     */
    protected boolean isValidFragment(String fragmentName) {
        return PreferenceFragment.class.getName().equals(fragmentName)
                || GeneralPreferenceFragment.class.getName().equals(fragmentName)
                || NotificationPreferenceFragment.class.getName().equals(fragmentName)
                || usuarioPreferenceFragment.class.getName().equals(fragmentName)
                ;
    }

    /**
     * This fragment shows general preferences only. It is used when the
     * activity is showing a two-pane settings UI.
     */
    //fragmento de configuraciones generales
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class GeneralPreferenceFragment extends PreferenceFragment {

        //extraccion del usuario actual
        ParseUser Userr = ParseUser.getCurrentUser();
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_general);
            setHasOptionsMenu(true);

            // Bind the summaries of EditText/List/Dialog/Ringtone preferences
            // to their values. When their values change, their summaries are
            // updated to reflect the new value, per the Android Design
            // guidelines.
            //referencia de los botones a cargar
            final SwitchPreference switch_all = (SwitchPreference)findPreference("all_switch");
            final CheckBoxPreference check_music = (CheckBoxPreference)findPreference("music_check");
            final CheckBoxPreference check_teatro = (CheckBoxPreference)findPreference("teatro_check");
            final CheckBoxPreference check_conferencia = (CheckBoxPreference)findPreference("conferencia_check");
            final CheckBoxPreference check_danza = (CheckBoxPreference)findPreference("danza_check");
            final CheckBoxPreference check_foro = (CheckBoxPreference)findPreference("foro_check");
            final CheckBoxPreference check_taller = (CheckBoxPreference)findPreference("taller_check");
            final CheckBoxPreference check_exposicion = (CheckBoxPreference)findPreference("exposicion_check");

            //arrays de preferencias de los usuarios
            final ArrayList<String> arraypreferencias = new ArrayList<String>();
            final ArrayList<String> array_todo= new ArrayList<String>();
            String[] prefereuser = new String[0];
            final ArrayList<String> userprefer= new ArrayList<String>();

            array_todo.add("musica");
            array_todo.add("teatro");
            array_todo.add("conferencia");
            array_todo.add("danza");
            array_todo.add("foro");
            array_todo.add("taller");
            array_todo.add("exposicion");
            array_todo.add("all");
            Integer dat;
            //se comprueba si usuario tiene preferencias guardadas
            boolean all=false;
            boolean music=false;
            boolean teatro=false;
            boolean conferencia=false;
            boolean danza=false;
            boolean foro=false;
            boolean taller =false;
            boolean exposicion=false;
            if(Userr.getList("preferencias")==null){
                arraypreferencias.add("all");
                check_music.setEnabled(false);
                check_music.setChecked(false);
                check_teatro.setEnabled(false);
                check_teatro.setChecked(false);
                check_conferencia.setEnabled(false);
                check_conferencia.setChecked(false);
                check_danza.setEnabled(false);
                check_danza.setChecked(false);
                check_foro.setEnabled(false);
                check_foro.setChecked(false);
                check_taller.setEnabled(false);
                check_taller.setChecked(false);
                check_exposicion.setEnabled(false);
                check_exposicion.setChecked(false);
            }
            else{
                dat=Userr.getList("preferencias").size();

                if(dat!=0){
                    prefereuser = Userr.getList("preferencias").toArray(new String[0]);
                    userprefer.addAll(Arrays.asList(prefereuser));
                    }
            }



            for(int pr=0;pr<prefereuser.length;pr++){
                if (prefereuser[pr].equals("all")) {
                    all=true;
                }
                if(prefereuser[pr].equals("musica")){

                    music=true;
                }
                if(prefereuser[pr].equals("teatro")){

                    teatro=true;
                }
                if(prefereuser[pr].equals("conferencia")){

                    conferencia=true;
                }
                if(prefereuser[pr].equals("danza")){

                    danza=true;
                }
                if(prefereuser[pr].equals("foro")){

                    foro=true;
                }
                if(prefereuser[pr].equals("taller")){

                    taller=true;
                }
                if(prefereuser[pr].equals("exposicion")){

                    exposicion=true;
                }
            }
            //si la configuracion all esta presente en los datos del usuario se hace lo siguiente
            if (all==true){
                //switch del boton all
                switch_all.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                    @Override
                    public boolean onPreferenceChange(Preference preference, Object newValue) {
                        boolean isVibrateOn = (Boolean) newValue;
                        //si el boton se activa se hace lo siguiente
                        if(isVibrateOn){
                            check_music.setEnabled(false);
                            check_music.setChecked(false);
                            check_teatro.setEnabled(false);
                            check_teatro.setChecked(false);
                            check_conferencia.setEnabled(false);
                            check_conferencia.setChecked(false);
                            check_danza.setEnabled(false);
                            check_danza.setChecked(false);
                            check_foro.setEnabled(false);
                            check_foro.setChecked(false);
                            check_taller.setEnabled(false);
                            check_taller.setChecked(false);
                            check_exposicion.setEnabled(false);
                            check_exposicion.setChecked(false);
                            arraypreferencias.removeAll(array_todo);
                            arraypreferencias.add("all");

                        }
                        //si el boton se desactiva se habilitan los clicks de los otros botones
                        else{
                            check_music.setEnabled(true);
                            check_teatro.setEnabled(true);
                            check_conferencia.setEnabled(true);
                            check_danza.setEnabled(true);
                            check_foro.setEnabled(true);
                            check_taller.setEnabled(true);
                            check_exposicion.setEnabled(true);
                            arraypreferencias.remove("all");

                            check_music.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                                @Override
                                public boolean onPreferenceChange(Preference preference, Object valor) {
                                    boolean check = (Boolean) valor;
                                    if(check){
                                        arraypreferencias.add("musica");
                                    }
                                    else {
                                        arraypreferencias.remove("musica");
                                    }
                                    return true;
                                }
                            });
                            check_teatro.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                                @Override
                                public boolean onPreferenceChange(Preference preference, Object newValue) {
                                    boolean check = (Boolean) newValue;
                                    if(check){
                                        arraypreferencias.add("teatro");
                                    }else{
                                        arraypreferencias.remove("teatro");
                                    }
                                    return true;
                                }
                            });
                            check_conferencia.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                                @Override
                                public boolean onPreferenceChange(Preference preference, Object newValue) {
                                    boolean check = (Boolean) newValue;
                                    if(check){
                                        arraypreferencias.add("conferencia");
                                    }
                                    else{
                                        arraypreferencias.remove("conferencia");
                                    }
                                    return true;
                                }
                            });
                            check_danza.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                                @Override
                                public boolean onPreferenceChange(Preference preference, Object newValue) {
                                    boolean check = (Boolean) newValue;
                                    if(check){
                                        arraypreferencias.add("danza");
                                    }
                                    else{
                                        arraypreferencias.remove("danza");
                                    }
                                    return true;
                                }
                            });
                            check_foro.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                                @Override
                                public boolean onPreferenceChange(Preference preference, Object newValue) {
                                    boolean check = (Boolean) newValue;
                                    if(check){
                                        arraypreferencias.add("foro");
                                    }
                                    else {
                                        arraypreferencias.remove("foro");
                                    }
                                    return true;
                                }
                            });
                            check_taller.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                                @Override
                                public boolean onPreferenceChange(Preference preference, Object newValue) {
                                    boolean check = (Boolean) newValue;
                                    if(check){
                                        arraypreferencias.add("taller");
                                    }
                                    else {
                                        arraypreferencias.remove("taller");
                                    }
                                    return true;
                                }
                            });
                            check_exposicion.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                                @Override
                                public boolean onPreferenceChange(Preference preference, Object newValue) {
                                    boolean check = (Boolean) newValue;
                                    if(check){
                                        arraypreferencias.add("exposicion");
                                    }else{
                                        arraypreferencias.remove("exposicion");
                                    }
                                    return true;
                                }
                            });
                        }
                        return true;
                    }

                });
            }
            //sino tiene all en las preferencias
            else{
                switch_all.setDefaultValue(false);
                check_music.setEnabled(true);
                check_teatro.setEnabled(true);
                check_conferencia.setEnabled(true);
                check_danza.setEnabled(true);
                check_foro.setEnabled(true);
                check_taller.setEnabled(true);
                check_exposicion.setEnabled(true);
            }
            //se comprueba y activa cada boton del usuario con sus preferencias
            if(music==true){
                check_music.setChecked(true);
            }
            if(teatro==true){
                check_teatro.setChecked(true);
            }
            if(conferencia==true){
                check_conferencia.setChecked(true);
            }
            if(danza==true){
                check_danza.setChecked(true);
            }
            if(foro==true){
                check_foro.setChecked(true);
            }
            if(taller==true){
                check_taller.setChecked(true);
            }
            if(exposicion==true){

                check_exposicion.setChecked(true);
            }
            check_music.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object valor) {
                    boolean check = (Boolean) valor;
                    if(check){
                        arraypreferencias.add("musica");
                    }
                    else {
                        arraypreferencias.remove("musica");
                    }
                    return true;
                }
            });
            check_teatro.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    boolean check = (Boolean) newValue;
                    if(check){
                        arraypreferencias.add("teatro");
                    }else{
                        arraypreferencias.remove("teatro");
                    }
                    return true;
                }
            });
            check_conferencia.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    boolean check = (Boolean) newValue;
                    if(check){
                        arraypreferencias.add("conferencia");
                    }
                    else{
                        arraypreferencias.remove("conferencia");
                    }
                    return true;
                }
            });
            check_danza.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    boolean check = (Boolean) newValue;
                    if(check){
                        arraypreferencias.add("danza");
                    }
                    else{
                        arraypreferencias.remove("danza");
                    }
                    return true;
                }
            });
            check_foro.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    boolean check = (Boolean) newValue;
                    if(check){
                        arraypreferencias.add("foro");
                    }
                    else {
                        arraypreferencias.remove("foro");
                    }
                    return true;
                }
            });
            check_taller.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    boolean check = (Boolean) newValue;
                    if(check){
                        arraypreferencias.add("taller");
                    }
                    else {
                        arraypreferencias.remove("taller");
                    }
                    return true;
                }
            });
            check_exposicion.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    boolean check = (Boolean) newValue;
                    if(check){
                        arraypreferencias.add("exposicion");
                    }else{
                        arraypreferencias.remove("exposicion");
                    }
                    return true;
                }
            });

            arraypreferencias.addAll(userprefer);

            //swith all si es activado
            switch_all.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    boolean isVibrateOn = (Boolean) newValue;
                    if(isVibrateOn){
                        check_music.setEnabled(false);
                        check_music.setChecked(false);
                        check_teatro.setEnabled(false);
                        check_teatro.setChecked(false);
                        check_conferencia.setEnabled(false);
                        check_conferencia.setChecked(false);
                        check_danza.setEnabled(false);
                        check_danza.setChecked(false);
                        check_foro.setEnabled(false);
                        check_foro.setChecked(false);
                        check_taller.setEnabled(false);
                        check_taller.setChecked(false);
                        check_exposicion.setEnabled(false);
                        check_exposicion.setChecked(false);
                        arraypreferencias.removeAll(array_todo);
                        arraypreferencias.add("all");
                    }
                    else{
                        check_music.setEnabled(true);
                        check_teatro.setEnabled(true);
                        check_conferencia.setEnabled(true);
                        check_danza.setEnabled(true);
                        check_foro.setEnabled(true);
                        check_taller.setEnabled(true);
                        check_exposicion.setEnabled(true);
                        arraypreferencias.remove("all");

                       check_music.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                           @Override
                           public boolean onPreferenceChange(Preference preference, Object valor) {
                               boolean check = (Boolean) valor;
                               if(check){
                                   arraypreferencias.add("musica");

                               }
                               else {
                                   arraypreferencias.remove("musica");

                               }
                               return true;
                           }
                       });
                       check_teatro.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                           @Override
                           public boolean onPreferenceChange(Preference preference, Object newValue) {
                               boolean check = (Boolean) newValue;
                               if(check){
                                   arraypreferencias.add("teatro");

                               }else{
                                   arraypreferencias.remove("teatro");

                               }
                               return true;
                           }
                       });
                       check_conferencia.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                           @Override
                           public boolean onPreferenceChange(Preference preference, Object newValue) {
                               boolean check = (Boolean) newValue;
                               if(check){
                                   arraypreferencias.add("conferencia");

                               }
                               else{
                                   arraypreferencias.remove("conferencia");

                               }
                               return true;
                           }
                       });
                      check_danza.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                          @Override
                          public boolean onPreferenceChange(Preference preference, Object newValue) {
                              boolean check = (Boolean) newValue;
                              if(check){
                                  arraypreferencias.add("danza");

                              }
                              else{
                                  arraypreferencias.remove("danza");

                              }
                              return true;
                          }
                      });
                      check_foro.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                          @Override
                          public boolean onPreferenceChange(Preference preference, Object newValue) {
                              boolean check = (Boolean) newValue;
                              if(check){
                                  arraypreferencias.add("foro");

                              }
                              else {
                                  arraypreferencias.remove("foro");

                              }
                              return true;
                          }
                      });
                      check_taller.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                          @Override
                          public boolean onPreferenceChange(Preference preference, Object newValue) {
                              boolean check = (Boolean) newValue;
                              if(check){
                                  arraypreferencias.add("taller");

                              }
                              else {
                                  arraypreferencias.remove("taller");

                              }
                              return true;
                          }
                      });
                      check_exposicion.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                          @Override
                          public boolean onPreferenceChange(Preference preference, Object newValue) {
                              boolean check = (Boolean) newValue;
                              if(check){
                                  arraypreferencias.add("exposicion");

                              }else{
                                  arraypreferencias.remove("exposicion");

                              }
                              return true;
                          }
                      });

                    }
                    return true;
                }

            });
            //boton que guarda los datos del usuario
            Preference button = (Preference)getPreferenceManager().findPreference("savedata");
            if (button != null) {
                button.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                    @Override
                    public boolean onPreferenceClick(Preference arg0) {

                        Userr.removeAll("preferencias",array_todo);
                        Userr.saveInBackground(new SaveCallback() {

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
                        if(arraypreferencias.size()==0&&userprefer.size()!=0){
                            Userr.addAll("preferencias",userprefer);
                            Userr.saveInBackground(new SaveCallback() {

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

                        }else{
                            Userr.addAll("preferencias",arraypreferencias);
                            Userr.saveInBackground(new SaveCallback() {

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
                        getActivity().finish();
                        return true;
                    }
                });
            }

        }


        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == android.R.id.home) {
                startActivity(new Intent(getActivity(), SettingsActivity.class));
                return true;
            }
            return super.onOptionsItemSelected(item);
        }
    }

    /**
     * This fragment shows notification preferences only. It is used when the
     * activity is showing a two-pane settings UI.
     */
    //fragmento de configuraciones de notificaciones
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class NotificationPreferenceFragment extends PreferenceFragment {
        //extraccion del usuario actual
        ParseUser Userr = ParseUser.getCurrentUser();
        @Override
        public void onCreate(Bundle savedInstanceState) {

            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_notification);
            setHasOptionsMenu(true);

            //consulta de datos del usuario con respecto a las notificaciones
            Integer dat;
            String[] prefereuser = new String[0];
            String[] channeluser = new String[0];
            boolean all = false;


            //referencia de switchs a cargar con los datos del usuario
            final SwitchPreference switch_all = (SwitchPreference)findPreference("notifications_all_message");
            final SwitchPreference switch_interes = (SwitchPreference)findPreference("notifications_new_message");
            final ArrayList<String> channels_user = new ArrayList<>();
            final ArrayList<String> channels = new ArrayList<>();
            final ParseInstallation installation = ParseInstallation.getCurrentInstallation();


            if(installation.getList("channels")==null){
                channeluser[0]="all";
            }
            else{
                channeluser = installation.getList("channels").toArray(new String[0]);
            }
            channels_user.addAll(Arrays.asList(channeluser));

            for(int pr=0;pr<channels_user.size();pr++){
                if (channels_user.get(pr).equals("all")) {
                    all=true;
                }
            }
            if(all==true){
                switch_all.setChecked(true);
                switch_interes.setChecked(false);
            }
            else{
                switch_interes.setChecked(true);
                switch_all.setChecked(false);
            }

            if(Userr.getList("preferencias")==null){
                prefereuser[0]="all";
            }
            else{
                dat=Userr.getList("preferencias").size();

                if(dat!=0){
                    prefereuser = Userr.getList("preferencias").toArray(new String[0]);


                    Log.d("arraypreferencias", String.valueOf(prefereuser.length));
                    Log.d("arraydatos", String.valueOf(channels.size()));


                }
            }
            final String[] finalPrefereuser = prefereuser;
           switch_all.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
               @Override
               public boolean onPreferenceChange(Preference preference, Object newValue) {
                   boolean isVibrateOn = (Boolean) newValue;
                   if(isVibrateOn){
                       switch_interes.setChecked(false);
                       channels.removeAll(Arrays.asList(finalPrefereuser));
                       channels.add("all");
                   }
                   else{
                       channels.remove("all");
                   }
                   return true;
               }
           });


            switch_interes.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    boolean isVibrateOn = (Boolean) newValue;
                    if(isVibrateOn){
                        switch_all.setChecked(false);
                        channels.remove("all");
                        channels.addAll(Arrays.asList(finalPrefereuser));
                    }
                    else{
                        channels.removeAll(Arrays.asList(finalPrefereuser));
                    }
                    return true;
                }
            });
            //boton que guarda los datos sobre notificaciones
            Preference button = (Preference)getPreferenceManager().findPreference("savedata");
            if (button != null) {
                button.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                    @Override
                    public boolean onPreferenceClick(Preference arg0) {
                        if(channels.size()==0){
                            installation.put("channels", channels_user);
                            installation.saveInBackground();
                        }
                        else{
                            installation.put("channels", channels);
                            installation.saveInBackground();
                        }

                        getActivity().finish();
                        return true;
                    }
                });
            }

        }


        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == android.R.id.home) {
                startActivity(new Intent(getActivity(), SettingsActivity.class));

                return true;
            }
            return super.onOptionsItemSelected(item);
        }
    }

    /**
     * This fragment shows data and sync preferences only. It is used when the
     * activity is showing a two-pane settings UI.
     */
      //fragmento de configuraciones del usuario
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class usuarioPreferenceFragment extends PreferenceFragment {
        ParseUser pUser= ParseUser.getCurrentUser();
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.prefe_usuario);
            setHasOptionsMenu(true);

            // Bind the summaries of EditText/List/Dialog/Ringtone preferences
            // to their values. When their values change, their summaries are
            // updated to reflect the new value, per the Android Design
            // guidelines.
            bindPreferenceSummaryToValue(findPreference("id_usuario_set"));
            bindPreferenceSummaryToValue(findPreference("correo_usuario_set"));

            if (pUser != null) {

                String objId= pUser.getObjectId();
                String correo_usuario = pUser.getEmail();
                final String usuario = pUser.getUsername();

                //muestra el correo del usuario
                EditTextPreference editcoreo = (EditTextPreference) findPreference("correo_usuario_set");

                editcoreo.setSummary(correo_usuario);
                //muestra el usuario
                EditTextPreference editusuario = (EditTextPreference) findPreference("id_usuario_set");
                editusuario.setSummary(usuario);


                final SharedPreferences editpreference = PreferenceManager.getDefaultSharedPreferences(getActivity());

                final SharedPreferences editpreference2 = PreferenceManager.getDefaultSharedPreferences(getActivity());



                editcoreo.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                    @Override
                    public boolean onPreferenceClick(Preference preference) {

                        final String correousuario = editpreference.getString("correo_usuario_set", "");

                        pUser.put("email", correousuario);

                        pUser.saveInBackground(new SaveCallback() {

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

                        return true;
                    }
                });

                editusuario.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                    @Override
                    public boolean onPreferenceClick(Preference preference) {
                        //string que muestra el usuario

                        final String usuario_prefer = editpreference2.getString("id_usuario_set", "");

                        pUser.put("username", usuario_prefer);

                        pUser.saveInBackground(new SaveCallback() {

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
                        ParseUser.logOut();
                        alertDisplayer("Cerrando sesión", "vuelve a ingresar con el nuevo usuario");

                        return true;
                    }
                });




                Preference button = (Preference)getPreferenceManager().findPreference("exitlink");
                if (button != null) {
                    button.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                        @Override
                        public boolean onPreferenceClick(Preference arg0) {
                            ParseUser.logOut();
                            alertDisplayer("Cerrando sesión", "vuelve pronto...!!!");
                            return true;
                        }
                    });
                }
                Preference button1 = (Preference)getPreferenceManager().findPreference("savedata");
                if (button1 != null) {
                    button1.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                        @Override
                        public boolean onPreferenceClick(Preference arg0) {
                            getActivity().finish();
                            return true;
                        }
                    });
                }
            } else {

                Intent intent = new Intent(getActivity(),Login_activity.class);
            }




        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == android.R.id.home) {
                startActivity(new Intent(getActivity(), SettingsActivity.class));
                return true;
            }
            return super.onOptionsItemSelected(item);
        }


        private void alertDisplayer(String title,String message){
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                    .setTitle(title)
                    .setMessage(message)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            Intent intent = new Intent(getActivity(), Login_activity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    });
            AlertDialog ok = builder.create();
            ok.show();
        }


    }

    //funcion de ir a los eventos
    @Override
    public void onHeaderClick(Header header, int position) {
        super.onHeaderClick(header, position);
        if (header.id == R.id.back_evento) {
            Intent intent = new Intent(SettingsActivity.this, EventosDrawActivity.class);
            startActivity(intent);

        }

    }
    //funcion que comprueba la conexion a internet
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager
                .getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }
}
