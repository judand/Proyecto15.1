package com.example.prototipo1.proyecto15;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class SignupActivity extends AppCompatActivity {

    //creacion de variables que reciben los datos del usuario

    private EditText usernameView;
    private EditText passwordView;
    private EditText passwordAgainView;
    private EditText correoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //se carga el xml de la actividad
        setContentView(R.layout.activity_signup);

        //boton atras
        final Button back_button = findViewById(R.id.back_button);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog dlg = new ProgressDialog(SignupActivity.this);
                dlg.setTitle("Espere un momento");
                dlg.setMessage("Volviendo a la sección del login...");
                dlg.show();
                Intent intent = new Intent(SignupActivity.this, Login_activity.class);
                dlg.dismiss();
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        //referencia de los textos con el xml
        usernameView = (EditText) findViewById(R.id.username);
        passwordView = (EditText) findViewById(R.id.password);
        passwordAgainView = (EditText) findViewById(R.id.passwordAgain);
        correoView = (EditText) findViewById(R.id.input_correo);

        //boton crear usuario
        final Button signup_button = findViewById(R.id.signup_button);
        signup_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Validating the log in data
                boolean validationError = false;

                StringBuilder validationErrorMessage = new StringBuilder("Por favor coloque");
                if (isEmpty(usernameView)) {
                    if(usernameView.getText().length()==0){
                        usernameView.setError("Nombre o id para entrar en el sistema");
                    }
                    validationError = true;
                    validationErrorMessage.append("un nombre de usuario");
                }
                if (isEmpty(passwordView)) {
                    if (validationError) {
                        validationErrorMessage.append(" y ");
                    }
                    validationError = true;
                    validationErrorMessage.append("una contraseña");
                }
                if (isEmpty(passwordAgainView)) {
                    if (validationError) {
                        validationErrorMessage.append(" y ");
                    }
                    validationError = true;
                    validationErrorMessage.append("su contraseña de nuevo");
                }
                else {
                    if (!isMatching(passwordView, passwordAgainView)) {
                        if (validationError) {
                            validationErrorMessage.append(" y ");
                        }
                        validationError = true;
                        validationErrorMessage.append("la misma contraseña dos veces");
                    }
                }
                validationErrorMessage.append(".");

                if (validationError) {
                    Toast.makeText(SignupActivity.this, validationErrorMessage.toString(), Toast.LENGTH_LONG).show();
                    return;
                }

                //configuracion de la barra de carga
                final ProgressDialog dlg = new ProgressDialog(SignupActivity.this);
                dlg.setTitle("Espera un momento");
                dlg.setMessage("Conectando...");
                dlg.show();

                //extracion del usuario actual y algunos de sus datos
                ParseUser user = new ParseUser();
                user.setUsername(usernameView.getText().toString());
                user.setPassword(passwordView.getText().toString());
                user.setEmail(correoView.getText().toString());
                user.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {


                            dlg.dismiss();
                            alertDisplayer("login exitoso","Bienvenido " + usernameView.getText().toString() + "!");

                        } else {
                            dlg.dismiss();
                            ParseUser.logOut();
                            Toast.makeText(SignupActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }


                    }
                });


            }
        });

    }

    private boolean isEmpty(EditText text) {
        if (text.getText().toString().trim().length() > 0) {
            return false;
        } else {
            return true;
        }
    }

    private boolean isMatching(EditText text1, EditText text2){
        if(text1.getText().toString().equals(text2.getText().toString())){
            return true;
        }
        else{
            return false;
        }
    }
//funcion del cuadro emergente
    private void alertDisplayer(String title,String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(SignupActivity.this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();

                        Intent intent= new Intent();
                        intent = new Intent(SignupActivity.this,SettingsActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);

                    }
                });
        AlertDialog ok = builder.create();
        ok.show();
    }


}
