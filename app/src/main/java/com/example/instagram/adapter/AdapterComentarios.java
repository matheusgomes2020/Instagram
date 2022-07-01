package com.example.instagram.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.instagram.R;
import com.example.instagram.helper.UsuarioFirebase;
import com.example.instagram.model.Comentario;
import com.example.instagram.model.Feed;
import com.example.instagram.model.Usuario;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterComentarios extends RecyclerView.Adapter<AdapterComentarios.MyViewHolder> {

    private Context context;
    private List<Comentario> listComentario;

    public AdapterComentarios(List<Comentario> listComentario, Context context) {
        this.listComentario = listComentario;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemLista = LayoutInflater.from( parent.getContext() ).inflate( R.layout.adapter_comentarios, parent, false );
        return new MyViewHolder( itemLista );
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        final Comentario comentario = listComentario.get( position );


        //Carrega dados do comentario
        Uri uriFotoUsuario = Uri.parse( comentario.getCaminhoFoto() );

        Glide.with( context ).load( uriFotoUsuario ).into(holder.foto);

        holder.nome.setText( comentario.getNomeUsuario() + " " + comentario.getComentario() );
        holder.qtdCurtidas.setText( "0 curtidas" );
        holder.hora.setText( "2 h" );

    }

    @Override
    public int getItemCount() {
        return listComentario.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        CircleImageView foto;
        TextView nome;
        TextView qtdCurtidas;
        TextView hora;
        ImageView like;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            foto = itemView.findViewById( R.id.imagePerfilComentario );
            nome = itemView.findViewById( R.id.textComentarioAdapter );
            qtdCurtidas = itemView.findViewById( R.id.textCurtidasComentario );
            hora = itemView.findViewById( R.id.textHoraComentario );
            like = itemView.findViewById( R.id.imageLikeComentario );
        }


    }



}
