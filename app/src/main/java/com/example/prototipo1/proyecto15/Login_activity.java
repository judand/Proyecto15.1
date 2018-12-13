package com.example.prototipo1.proyecto15;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class Login_activity extends AppCompatActivity {

    //se crean las variables a referenciar que reciben el usuario y el password

    private EditText usernameView;
    private EditText passwordView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //se carga el xml en la actividad
        setContentView(R.layout.activity_login_activity);

        //se referencian las variables antes creadas
        usernameView = (EditText) findViewById(R.id.username);
        passwordView = (EditText) findViewById(R.id.password);

        //se referencia el boton de login
        final Button login_button = findViewById(R.id.login_button);
        //accion del boton al hacer click
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Validating the log in data
                boolean validationError = false;

                StringBuilder validationErrorMessage = new StringBuilder("Por favor , coloque ");
                if (isEmpty(usernameView)) {

                    if(usernameView.getText().length()==0){
                        usernameView.setError("Usuario del sistema");
                    }
                    validationError = true;
                    validationErrorMessage.append("un nombre de usuario");
                }
                if (isEmpty(passwordView)) {
                    if (validationError) {
                        validationErrorMessage.append(" y  ");
                    }
                    validationError = true;
                    validationErrorMessage.append("una contraseña");
                }
                validationErrorMessage.append(".");

                if (validationError) {
                    Toast.makeText(Login_activity.this, validationErrorMessage.toString(), Toast.LENGTH_LONG).show();
                    return;
                }

                //se configura el mensaje mientras carga
                final ProgressDialog dlg = new ProgressDialog(Login_activity.this);
                dlg.setTitle("Espere un momento");
                dlg.setMessage("conectando ...");
                dlg.show();

                ParseUser.logInInBackground(usernameView.getText().toString(), passwordView.getText().toString(), new LogInCallback() {
                    @Override
                    public void done(ParseUser parseUser, ParseException e) {
                        if (parseUser != null) {
                            dlg.dismiss();
                            alertDisplayer("login exitoso","Bienvenido de nuevo  " + usernameView.getText().toString() + "!");

                        } else {
                            dlg.dismiss();
                            ParseUser.logOut();
                            Toast.makeText(Login_activity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });

        //referencia y accion del boton registrar
        final Button signup_button = findViewById(R.id.signup_button);
        signup_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login_activity.this, SignupActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
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
    //funcion del cuadro emergente
    private void alertDisplayer(String title,String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(Login_activity.this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();

                        Intent intent= new Intent();
                            intent = new Intent(Login_activity.this, EventosDrawActivity.class);


                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                });
        AlertDialog ok = builder.create();
        ok.show();
    }

    //funcion que va a la actividad PaswordForgotActivity
    public void Recuperar(View v)
    {
        TextView tv= (TextView) findViewById(R.id.recovery_text);
        //assign the textview forecolor
        tv.setTextColor(Color.GREEN);

        Intent intent = new Intent(Login_activity.this, PaswordForgotActivity.class);
        startActivity(intent);

    }
}
