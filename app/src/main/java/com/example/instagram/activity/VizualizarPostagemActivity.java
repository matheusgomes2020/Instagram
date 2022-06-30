package com.example.instagram.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;

import com.bumptech.glide.Glide;
import com.example.instagram.R;
import com.example.instagram.databinding.ActivityMainBinding;
import com.example.instagram.databinding.ActivityVizualizarPostagemBinding;
import com.example.instagram.model.Postagem;
import com.example.instagram.model.Usuario;

public class VizualizarPostagemActivity extends AppCompatActivity {

    private ActivityVizualizarPostagemBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityVizualizarPostagemBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        Toolbar toolbar = findViewById(  R.id.toolbarPrincipal );
        toolbar.setTitle("Postagem");
        setSupportActionBar( toolbar );

        getSupportActionBar().setDisplayHomeAsUpEnabled( true );
        getSupportActionBar().setHomeAsUpIndicator( R.drawable.ic_baseline_close_24 );


        //Recupera dados da activity
        Bundle bundle = getIntent().getExtras();
        if ( bundle != null ){

            Postagem postagem = (Postagem) bundle.getSerializable( "postagem" );
            Usuario usuario = (Usuario) bundle.getSerializable( "usuario" );

            //Exibe dados do usuário
            Uri uri = Uri.parse( usuario.getCaminhoFoto() );
            Glide.with( VizualizarPostagemActivity.this )
                    .load( uri )
                    .into( binding.imagePerfilPostagem );

            binding.textNomePostagem.setText( usuario.getNome() );

            //exibe dados da postagem
            Uri uriPostagem = Uri.parse( postagem.getCaminhoFoto() );
            Glide.with( VizualizarPostagemActivity.this )
                    .load( uriPostagem )
                    .into( binding.imagePostagem );

            binding.textNomeAbaixoPostagem.setText( postagem.getDescricao() );

        }


    }

    @Override
    public boolean onSupportNavigateUp() {

        finish();
        return false;

    }


}