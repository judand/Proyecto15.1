package com.example.prototipo1.proyecto15;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.RequestPasswordResetCallback;

public class PaswordForgotActivity extends AppCompatActivity {

    //creacion de referencias a texto, campo de datos que el usuario coloca y el boton de enviar
    TextView resetTv;
    EditText resetEt;
    Button resetButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //se carga la referencia del xml
        setContentView(R.layout.activity_pasword_forgot);
        //se modifica el boton atras
        final Button back_button = findViewById(R.id.back_button);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(PaswordForgotActivity.this, Login_activity.class);

                startActivity(intent);
            }
        });

        resetTv = (TextView)findViewById(R.id.reset_text);
        resetEt = (EditText)findViewById(R.id.correo);
        resetButton = (Button)findViewById(R.id.recovery_button);

        //acciones del boton enviar datos para recuperar
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                String email = resetEt.getText().toString();

                try{
                    ParseUser.requestPasswordResetInBackground(email, new RequestPasswordResetCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                resetTv.setText("An Email has been send to your email account!\nFollow the procedure");
                                resetTv.setVisibility(v.VISIBLE);
                                resetButton.setEnabled(false);
                                resetButton.getBackground().setColorFilter(0xe0F2F2F2,PorterDuff.Mode.SRC_ATOP);
                                resetButton.setTextColor(0xe0000000);
                            } else {
                                Toast.makeText(getApplicationContext(), "Some Error! Try later" + e, Toast.LENGTH_LONG);
                            }

                        }
                    });
                }
                catch(Exception e){
                    Toast.makeText(getApplicationContext(),"Error"+e,Toast.LENGTH_LONG).show();
                }
            }
        });

    }
}
