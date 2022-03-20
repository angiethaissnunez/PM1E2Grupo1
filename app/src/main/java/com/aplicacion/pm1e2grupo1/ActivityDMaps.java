package com.aplicacion.pm1e2grupo1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ActivityDMaps extends AppCompatActivity {
    EditText txtUbOrigen, txtUbDestino;
    Button btnLocalizarP;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dmaps);

        txtUbOrigen = (EditText) findViewById(R.id.txtUbicatOrigen);
        txtUbDestino = (EditText) findViewById(R.id.txtUbicaDestino);
        btnLocalizarP = (Button) findViewById(R.id.btnLocalizar);

        btnLocalizarP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sSource = txtUbOrigen.getText().toString().trim();
                String sDestination =  txtUbDestino.getText().toString().trim();

                if (sSource.isEmpty() && sDestination.isEmpty()) {
                    Toast.makeText(ActivityDMaps.this, "INTRODUZCA LOS DATOS.", Toast.LENGTH_SHORT).show();
                } else {
                    displayMapDraving(sSource, sDestination);
                }
            }
        });


    }

    private void displayMapDraving(String sSource, String sDestination) {
        try {
            Uri uri = Uri.parse("https://www.google.co.in/maps/https://www.google.co.in/maps/dir/"+ sSource+"/"+sDestination);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.setPackage("com.google.android.apps.maps");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

        } catch (ActivityNotFoundException e) {
            Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.apps.maps");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }


    }


}