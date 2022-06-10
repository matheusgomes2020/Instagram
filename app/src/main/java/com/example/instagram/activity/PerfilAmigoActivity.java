package com.example.instagram.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.example.instagram.R;
import com.example.instagram.model.Usuario;

import de.hdodenhof.circleimageview.CircleImageView;

public class PerfilAmigoActivity extends AppCompatActivity {

    private Usuario usuarioSelecionado;

    private Button buttonSeguir, buttomMensagem;
    private CircleImageView imagePerfil;
    private GridView gridViewperfil;
    private TextView textPublicacoes, textSeguidores, textSeguindo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_amigo);

        //Inicializar componentes

        gridViewperfil = findViewById( R.id.gridViewPerfilAmigo );
        imagePerfil = findViewById( R.id.imagePerfilAmigo );
        textPublicacoes = findViewById( R.id.textPublicacoesAmigo );
        textSeguidores = findViewById( R.id.textSeguidoresAmigo );
        textSeguindo = findViewById( R.id.textSeguindoAmigo );
        buttonSeguir = findViewById( R.id.buttonSeguirPerfilAmigo );
        buttomMensagem = findViewById( R.id.buttonMensagemPerfilAmigo );

        Toolbar toolbar = findViewById(  R.id.toolbarPrincipal );
        toolbar.setTitle( "Perfil" );
        setSupportActionBar( toolbar );

        getSupportActionBar().setDisplayHomeAsUpEnabled( true );
        getSupportActionBar().setHomeAsUpIndicator( R.drawable.ic_baseline_close_24 );

        //Recuperar usuário selecionado
        Bundle bundle = getIntent().getExtras();
        if ( bundle != null ){
            usuarioSelecionado = ( Usuario ) bundle.getSerializable( "usuarioSelecionado" );

            //Configura nome do usuário na toolbar
            getSupportActionBar().setTitle( usuarioSelecionado.getNome() );
        }




    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return false;
    }
}