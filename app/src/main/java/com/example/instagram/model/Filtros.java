package com.example.instagram.model;

import android.graphics.Bitmap;

public class Filtros {

    private String nome;
    private Bitmap imagem;

    public Filtros(String nome, Bitmap imagem) {
        this.nome = nome;
        this.imagem = imagem;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Bitmap getImagem() {
        return imagem;
    }

    public void setImagem(Bitmap imagem) {
        this.imagem = imagem;
    }
}
