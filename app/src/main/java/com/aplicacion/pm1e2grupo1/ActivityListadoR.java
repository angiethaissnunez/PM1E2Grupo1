package com.aplicacion.pm1e2grupo1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.aplicacion.pm1e2grupo1.Models.RegistrosC;
import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ActivityListadoR extends AppCompatActivity {

    Button btnAtras;

    ListView lv;
    FirebaseListAdapter adapter;

    ListView listaP;


    RecyclerView recyclerView;
    DatabaseReference database;
    MyAdapter myAdapter;
    ArrayList<User> list;

    RegistrosC contactoSeleccionado;
    AlertDialog.Builder mBuilderSelector;
    CharSequence options[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listado_r);



        ////

        listaP = (ListView) findViewById(R.id.listaP);

        Query query = FirebaseDatabase.getInstance().getReference().child("personas");
        FirebaseListOptions<User> options = new FirebaseListOptions.Builder<User>()
                .setLayout(R.layout.item)
                .setQuery(query, User.class)
                .build();


        adapter = new FirebaseListAdapter(options) {
            @Override
            protected void populateView(View v, Object model, int position) {
                TextView tvNombre = v.findViewById(R.id.tvNombre);
                TextView tvTelefono = v.findViewById(R.id.tvTelefono);
                TextView tvLatitud = v.findViewById(R.id.tvLatitud2);
                TextView tvLongitud = v.findViewById(R.id.tvLongitud2);
                //TextView tvContenido = v.findViewById(R.id.tvContenido);
                ImageView imagenFoto = v.findViewById(R.id.imagenFoto);

                User std = (User) model;
                tvNombre.setText(std.getNombre().toString());
                tvTelefono.setText(std.getTelefono().toString());
                tvLatitud.setText(std.getLatitud().toString());
                tvLongitud.setText(std.getLongitud().toString());
                Picasso.with(ActivityListadoR.this).load(std.getImagenUrl()).into(imagenFoto);
            }
        };
        listaP.setAdapter(adapter);

        listaP.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(ActivityListadoR.this, "Clik", Toast.LENGTH_LONG).show();
            }
        });






        ////
/*
        contactoSeleccionado = null;

        //recyclerView = findViewById(R.id.userList);
        database = FirebaseDatabase.getInstance().getReference("personas");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();
        myAdapter = new MyAdapter(this,list);
        recyclerView.setAdapter(myAdapter);

        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    User user = dataSnapshot.getValue(User.class);
                    list.add(user);
                }

                myAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });*/

        btnAtras = (Button) findViewById(R.id.btnRegresar);
        btnAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });







        /*recyclerView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            private long lastTouchTime = 0;
            private long currentTouchTime = 0;

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                lastTouchTime = currentTouchTime;
                currentTouchTime = System.currentTimeMillis();

                contactoSeleccionado = (RegistrosC) adapterView.getAdapter().getItem(i);

                //Esto es cuando se preciona dos veces
                if (currentTouchTime - lastTouchTime < 250) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(ActivityListadoR.this);

                    builder.setMessage("¿Desea ir a la ubicacion de "+contactoSeleccionado.getNombre()+" ?").setTitle("Alerta");

                    builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            // IR A LA UBICACION
                            selectOptionMap();
                        }
                    });

                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {}
                    });

                    AlertDialog dialog = builder.create();
                    dialog.show();

                    lastTouchTime = 0;
                    currentTouchTime = 0;
                }
            }
        });*/




    }








    private void selectOptionMap() {
        mBuilderSelector.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (i == 0) { // Google Maps
                    Intent intent = new Intent(ActivityListadoR.this, MapsActivity.class);
                    intent.putExtra("LATITUD", contactoSeleccionado.getLatitud());
                    intent.putExtra("LONGITUD", contactoSeleccionado.getLongitud());
                    intent.putExtra("NOMBRE", contactoSeleccionado.getNombre());
                    startActivity(intent);
                }
            }
        });

        mBuilderSelector.show();
    }


    @Override
    protected void onStart(){
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop(){
        super.onStop();
        adapter.stopListening();
    }

}