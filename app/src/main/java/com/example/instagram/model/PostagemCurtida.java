package com.example.instagram.model;

import com.example.instagram.helper.ConfiguracaoFirebase;
import com.google.firebase.database.DatabaseReference;

import java.util.HashMap;

public class PostagemCurtida {

    public int qtdCurtidas;
    public Feed feed;
    public Usuario usuario;

    public PostagemCurtida() {
    }

    public void salvar(){

        DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();

        //Objeto usuario
        HashMap<String, Object> dadosUsuario = new HashMap<>();
        dadosUsuario.put( "nomeUsuario", usuario.getNome() );
        dadosUsuario.put( "caminhoFoto", usuario.getCaminhoFoto() );

        DatabaseReference pCurtidasRef = firebaseRef
                .child( "postagens-curtidas" )
                .child( feed.getId() ) //id_postagem
                .child( usuario.getId() ); //id_usuario_logado

        pCurtidasRef.setValue( dadosUsuario );

        //atualizar quantidade de curtidas
        atualizarQtd( 1 );


    }

    public void atualizarQtd( int valor ){

        DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();

        DatabaseReference pCurtidasRef = firebaseRef
                .child( "postagens-curtidas" )
                .child( feed.getId() ) //id_postagem
                .child( usuario.getId() ) //id_usuario_logado
                .child( "qtdCurtidas" );

        setQtdCurtidas( getQtdCurtidas() + valor );

    }

    public Feed getFeed() {
        return feed;
    }

    public void setFeed(Feed feed) {
        this.feed = feed;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public int getQtdCurtidas() {
        return qtdCurtidas;
    }

    public void setQtdCurtidas(int qtdCurtidas) {
        this.qtdCurtidas = qtdCurtidas;
    }
}
