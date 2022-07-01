package com.example.instagram.model;

import com.example.instagram.helper.ConfiguracaoFirebase;
import com.google.firebase.database.DatabaseReference;

public class Comentario {

    private String idComentario;
    private String idPostagem;
    private String idusuario;
    private String nomeUsuario;
    private String caminhoFoto;
    private String comentario;

    public Comentario() {
    }

    public Boolean salvar(){

        /*
        + Comentarios
            + id_postagem
                + id_comentario
                    comentario
        * */
        DatabaseReference comentariosRef = ConfiguracaoFirebase.getFirebaseDatabase()
                .child("comentarios")
                .child( getIdPostagem() );

        String chaveComentario = comentariosRef.push().getKey();
        setIdComentario( chaveComentario );
        comentariosRef.child( getIdComentario() ).setValue( this );

        return true;

    }

    public String getIdComentario() {
        return idComentario;
    }

    public void setIdComentario(String idComentario) {
        this.idComentario = idComentario;
    }

    public String getIdPostagem() {
        return idPostagem;
    }

    public void setIdPostagem(String idPostagem) {
        this.idPostagem = idPostagem;
    }

    public String getIdusuario() {
        return idusuario;
    }

    public void setIdusuario(String idusuario) {
        this.idusuario = idusuario;
    }

    public String getNomeUsuario() {
        return nomeUsuario;
    }

    public void setNomeUsuario(String nomeUsuario) {
        this.nomeUsuario = nomeUsuario;
    }

    public String getCaminhoFoto() {
        return caminhoFoto;
    }

    public void setCaminhoFoto(String caminhoFoto) {
        this.caminhoFoto = caminhoFoto;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }
}
