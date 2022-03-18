package com.aplicacion.pm1e2grupo1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import id.zelory.compressor.Compressor;

public class MainActivity extends AppCompatActivity {

    EditText txtNombre;
    EditText txtCell;
    EditText txtLatitud;
    EditText txtlongitud;

    Button btnGuardar;
    Button btnLista;

    //codigo para subida de imagen
    ImageView objImagen;
    Button btnTomFoto;

    DatabaseReference imgref;
    StorageReference storageReference;
    ProgressDialog cargando;
    private Bitmap thumb_bitmap = null;

    String nombre = "";
    String telefono = "";
    String latitud = "";
    String longitud = "";

    DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        //Codigo subida de imagen
        objImagen = (ImageView) findViewById(R.id.objImagen);
        btnTomFoto = (Button) findViewById(R.id.btnTomFoto);

        imgref = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference().child("img_personas");
        cargando = new ProgressDialog(this);

        btnTomFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CropImage.startPickImageActivity(MainActivity.this);
            }
        }); //termina codigo subida de imagen


        //Codigo para base de datos
        mDatabase = FirebaseDatabase.getInstance().getReference();
        //

        txtNombre = (EditText) findViewById(R.id.txtNombre);
        txtCell = (EditText) findViewById(R.id.txtCell);
        txtLatitud = (EditText) findViewById(R.id.txtLatitud);
        txtlongitud = (EditText) findViewById(R.id.txtLongitud);

        btnLista = (Button) findViewById(R.id.btnLista);
        btnLista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ActivityListadoR.class);
                startActivity(intent);
            }
        });

        btnGuardar = (Button) findViewById(R.id.btnGuardar);
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!nombre.isEmpty() && !telefono.isEmpty() && !latitud.isEmpty() && !longitud.isEmpty()){

                } else {
                    Toast.makeText(MainActivity.this, "Complete todos los campos", Toast.LENGTH_LONG).show();
                    Mensaje();

                }




            }
        });
    }

    public void Mensaje(){
        Alertas mensaje=new Alertas();
        mensaje.show(getSupportFragmentManager(),"Mensaje");
    }

    //Subida de imagen

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == Activity.RESULT_OK){
            Uri imageuri = CropImage.getPickImageResultUri(this, data);

            //Recortar imagen
            CropImage.activity(imageuri).setGuidelines(CropImageView.Guidelines.ON)
                    .setRequestedSize(250, 250)
                    .setAspectRatio(1,1).start(MainActivity.this);
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK){
                Uri resultUri = result.getUri();

                File url = new File(resultUri.getPath());

                Picasso.with(this).load(url).into(objImagen);

                //COmprimir la Imagen
                try {
                    thumb_bitmap = new Compressor(this).setMaxWidth(250).setMaxHeight(250).setQuality(90).compressToBitmap(url);
                } catch (IOException e){
                    e.printStackTrace();
                }

                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                thumb_bitmap.compress(Bitmap.CompressFormat.JPEG, 90, byteArrayOutputStream);
                final byte [] thumb_byte = byteArrayOutputStream.toByteArray();
                //Finaliza el compresor

                int p = (int) (Math.random() * 25 + 1); int s = (int) (Math.random() * 25 + 1);
                int t = (int) (Math.random() * 25 + 1); int c = (int) (Math.random() * 25 + 1);
                int numero1 = (int) (Math.random() * 1012 +2111);
                int numero2 = (int) (Math.random() * 1012 +2111);

                String[] elementos = {"a", "b", "c", "d","e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p",
                        "q", "r","s", "t","u", "v","w", "x", "y", "x"};

                final String aleatorio = elementos[p] + elementos[s] + numero1 + elementos[t] + elementos[c] +
                        numero2 + "app.jpg";

                //Subir la imagen
                btnGuardar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        cargando.setTitle("Guardano Datos");
                        cargando.setMessage("Espere por favor...");
                        cargando.show();

                        StorageReference ref = storageReference.child(aleatorio);
                        UploadTask uploadTask = ref.putBytes(thumb_byte);

                        //Subir la imagen a la Base de Datos
                        Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                            @Override
                            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                if (!task.isSuccessful()){
                                    throw Objects.requireNonNull(task.getException());
                                }
                                return ref.getDownloadUrl();
                            }
                        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                Uri dowloaduri = task.getResult();

                                nombre = txtNombre.getText().toString();
                                telefono = txtCell.getText().toString();
                                latitud = txtLatitud.getText().toString();
                                longitud = txtlongitud.getText().toString();

                                //Registra los usuario en la BD
                                Map<String, Object> map = new HashMap<>();
                                map.put("nombre", txtNombre.getText().toString().trim());
                                map.put("telefono", txtCell.getText().toString().trim());
                                map.put("latitud", txtLatitud.getText().toString().trim());
                                map.put("longitud", txtlongitud.getText().toString().trim());
                                map.put("imagenUrl", dowloaduri.toString());

                                imgref.child("personas").push().setValue(map);
                                cargando.dismiss();
                                Toast.makeText(MainActivity.this, "Registro Guardado con Exito", Toast.LENGTH_LONG).show();

                                Intent intent = getIntent();
                                finish();
                                startActivity(intent);
                            }
                        });
                    }
                });
            }
        }
    }
}