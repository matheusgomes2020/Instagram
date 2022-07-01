package com.example.instagram.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.instagram.R;
import com.example.instagram.adapter.AdapterComentarios;
import com.example.instagram.databinding.ActivityComentariosBinding;
import com.example.instagram.databinding.ActivityVizualizarPostagemBinding;
import com.example.instagram.helper.ConfiguracaoFirebase;
import com.example.instagram.helper.UsuarioFirebase;
import com.example.instagram.model.Comentario;
import com.example.instagram.model.Usuario;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ComentariosActivity extends AppCompatActivity {

    private ActivityComentariosBinding binding;
    private String idPostagem;
    private Usuario usuario;
    private AdapterComentarios adapterComentarios;
    private List<Comentario> listComentarios = new ArrayList<>();
    private ValueEventListener valueEventListenerComentarios;
    private DatabaseReference firebaseRef;
    private DatabaseReference comentarioRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityComentariosBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //Configuracoes iniciais
        usuario = UsuarioFirebase.getDadosusuarioLogado();
        firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();

        Toolbar toolbar = findViewById(  R.id.toolbarPrincipal );
        toolbar.setTitle( "Comentários" );
        setSupportActionBar( toolbar );

        getSupportActionBar().setDisplayHomeAsUpEnabled( true );
        getSupportActionBar().setHomeAsUpIndicator( R.drawable.ic_baseline_close_24 );

        adapterComentarios = new AdapterComentarios( listComentarios, getApplicationContext() );
        binding.recyclerComentarios.setHasFixedSize( true );
        binding.recyclerComentarios.setLayoutManager( new LinearLayoutManager( this) );
        binding.recyclerComentarios.setAdapter( adapterComentarios );

        //Recuperar id postagem
        Bundle bundle = getIntent().getExtras();
        if ( bundle != null ){

            idPostagem = bundle.getString( "idPostagem" );

        }

        //Recuperar foto do usuário
        String caminhoFoto = usuario.getCaminhoFoto();
        if( caminhoFoto != null ){
            Uri url = Uri.parse( caminhoFoto );
            Glide.with(getApplicationContext())
                    .load( url )
                    .into( binding.imagePerfilLogado );
        }

    }

    private void recuperarComentarios(){

        comentarioRef = firebaseRef.child( "comentarios" )
                .child( idPostagem );

        valueEventListenerComentarios = comentarioRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                listComentarios.clear();
                for ( DataSnapshot ds : snapshot.getChildren() ){
                    listComentarios.add( ds.getValue( Comentario.class ) );
                }

                adapterComentarios.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void  salvarComentario( View view ){

        String textoComentario = binding.editComentario.getText().toString();
        if ( textoComentario != null && !textoComentario.equals("") ){

            Comentario comentario = new Comentario();
            comentario.setIdPostagem( idPostagem );
            comentario.setIdusuario( usuario.getId() );
            comentario.setNomeUsuario( usuario.getNome() );
            comentario.setCaminhoFoto( usuario.getCaminhoFoto() );
            comentario.setComentario( textoComentario );

            if ( comentario.salvar() ){
                Toast.makeText(this, "Comentário salvo com sucesso!", Toast.LENGTH_SHORT).show();
            }

        }else {
            Toast.makeText(this, "Insira o comentário antes de salvar!", Toast.LENGTH_SHORT).show();
        }

        //Limpa comentário digitado
        binding.editComentario.setText("");

    }


    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate( R.menu.menu_comentarios, menu );

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onStart() {
        super.onStart();
        recuperarComentarios();
    }

    @Override
    protected void onStop() {
        super.onStop();
        comentarioRef.removeEventListener( valueEventListenerComentarios );
    }
}