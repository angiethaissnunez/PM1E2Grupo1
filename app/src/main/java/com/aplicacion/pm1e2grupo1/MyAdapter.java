package com.aplicacion.pm1e2grupo1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    Context context;

    public MyAdapter(Context context, ArrayList<User> lis) {
        this.context = context;
        this.lis = lis;
    }

    ArrayList<User> lis;


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        User user = lis.get(position);
        holder.tvNombre.setText(user.getNombre());
        holder.tvTelefono.setText(user.getTelefono());
        holder.tvLatitud2.setText(user.getLatitud());
        holder.tvLongitud2.setText(user.getLongitud());
    }

    @Override
    public int getItemCount() {
        return lis.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView tvNombre, tvTelefono, tvLatitud2, tvLongitud2;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvNombre = itemView.findViewById(R.id.tvNombre);
            tvTelefono = itemView.findViewById(R.id.tvTelefono);
            tvLatitud2 = itemView.findViewById(R.id.tvLatitud2);
            tvLongitud2 = itemView.findViewById(R.id.tvLongitud2);
        }
    }
}