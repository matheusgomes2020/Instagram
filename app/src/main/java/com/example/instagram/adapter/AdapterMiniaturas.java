package com.example.instagram.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.instagram.R;
import com.example.instagram.model.Filtros;

import java.util.List;

public class AdapterMiniaturas extends RecyclerView.Adapter<AdapterMiniaturas.MyViewHolder> {

    private List<Filtros> listaFiltros;
    private Context context;

    public AdapterMiniaturas(List<Filtros> listaFiltros, Context context) {
        this.listaFiltros = listaFiltros;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_filtros, parent, false);
        return new AdapterMiniaturas.MyViewHolder(itemLista);

    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        Filtros item = listaFiltros.get( position );

        holder.foto.setImageBitmap( item.getImagem() );
        holder.nomeFiltro.setText( item.getNome() );

    }

    @Override
    public int getItemCount() {
        return listaFiltros.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView foto;
        TextView nomeFiltro;


        public MyViewHolder(View itemView) {
            super(itemView);

            foto = itemView.findViewById(com.example.instagram.R.id.imageFotoFiltro);
            nomeFiltro = itemView.findViewById(R.id.textNomeFiltro);

        }
    }

}