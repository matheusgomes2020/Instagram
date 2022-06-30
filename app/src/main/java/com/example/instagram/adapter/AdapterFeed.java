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
import com.example.instagram.helper.ConfiguracaoFirebase;
import com.example.instagram.helper.UsuarioFirebase;
import com.example.instagram.model.Feed;
import com.example.instagram.model.PostagemCurtida;
import com.example.instagram.model.Usuario;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterFeed extends RecyclerView.Adapter<AdapterFeed.MyViewHolder> {

    private List<Feed> listaFeed;
    private Context context;

    public AdapterFeed(List<Feed> listaFeed, Context context) {
        this.listaFeed = listaFeed;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_feed, parent, false);
        return new AdapterFeed.MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        final Feed feed = listaFeed.get(position);
        Usuario usuarioLogado = UsuarioFirebase.getDadosusuarioLogado();

        //Carrega dados do feed
        Uri uriFotoUsuario = Uri.parse( feed.getFotoUsuario() );
        Uri uriFotoPostagem = Uri.parse( feed.getFotoPostagem() );

        Glide.with( context ).load( uriFotoUsuario ).into(holder.fotoPerfil);
        Glide.with( context ).load( uriFotoPostagem ).into(holder.fotoPostagem);

        holder.descricao.setText( feed.getDescricao() );
        holder.nome.setText( feed.getNomeUsuario() );
        //holder.localizacaoFeed.setText(  );
        //holder.qtdCurtidas.setText(  );

        /*
        postagens-curtidas
            + id_postagem
                + qtdCurtidas
                + id_usuario
                    nome_usuario
                    caminho_foto
        * */
        //Recuperar dados da postagem curtida
        DatabaseReference curtidasRef = ConfiguracaoFirebase.getFirebaseDatabase()
                .child("postagens-curtidas")
                .child( feed.getId() );

        curtidasRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                int qtdCurtidas = 0;
                if ( snapshot.hasChild( "qtdCurtidas"  ) ){

                    PostagemCurtida postagemCurtida = snapshot.getValue( PostagemCurtida.class );
                    qtdCurtidas = postagemCurtida.getQtdCurtidas();

                }

                //Monta objeto postagem curtida
                PostagemCurtida curtida = new PostagemCurtida();
                curtida.setFeed( feed );
                curtida.setUsuario( usuarioLogado );
                curtida.setQtdCurtidas( qtdCurtidas );



                //Adiciona eventos para curtir uma foto
                holder.like.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        curtida.salvar();
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    @Override
    public int getItemCount() {
        return listaFeed.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        CircleImageView fotoPerfil;
        TextView nome, localizacaoFeed, descricao, qtdCurtidas, verComentarios, hDias, verTraducao;
        ImageView fotoPostagem, pontos, like, coment, dm, salvar;

        public MyViewHolder(View itemView) {
            super(itemView);

            fotoPerfil   = itemView.findViewById(R.id.imagePerfilFeed);
            fotoPostagem = itemView.findViewById(R.id.imageFeed);
            nome = itemView.findViewById(R.id.textNomeFeed);
            localizacaoFeed = itemView.findViewById(R.id.textLocalizacaoFeed);
            qtdCurtidas = itemView.findViewById(R.id.textCurtidasFeed);
            descricao  = itemView.findViewById(R.id.textNomeAbaixoFeed);
            verComentarios  = itemView.findViewById(R.id.textVerComentariosFeed);
            hDias  = itemView.findViewById(R.id.textHaFeed);
            verTraducao  = itemView.findViewById(R.id.textVerTraducaoFeed);
            pontos = itemView.findViewById(R.id.imagePontosFeed);
            like = itemView.findViewById(R.id.imageLikeFeed);
            coment = itemView.findViewById(R.id.imageChatFeed);
            dm = itemView.findViewById(R.id.imageEnviarDmFeed);
            salvar = itemView.findViewById(R.id.imageSalvarFeed);


        }
    }

}