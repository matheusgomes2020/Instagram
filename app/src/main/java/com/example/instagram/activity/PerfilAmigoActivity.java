package com.example.instagram.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.instagram.R;
import com.example.instagram.helper.ConfiguracaoFirebase;
import com.example.instagram.helper.UsuarioFirebase;
import com.example.instagram.model.Usuario;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class PerfilAmigoActivity extends AppCompatActivity {

    private Usuario usuarioSelecionado;

    private Button buttonSeguir, buttomMensagem;
    private CircleImageView imagePerfil;
    private GridView gridViewperfil;
    private TextView textPublicacoes, textSeguidores, textSeguindo, textNome, textBio;

    private DatabaseReference firebaseRef;
    private DatabaseReference usuariosRef;
    private DatabaseReference usuarioAmigoRef;
    private DatabaseReference seguidoresRef;

    private ValueEventListener valueEventListenerPerfilAmigo;

    private String idUsuarioLogado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_amigo);

        //Configurações iniciais

        firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();
        usuariosRef = ConfiguracaoFirebase.getFirebaseDatabase().child( "usuarios" );
        seguidoresRef = firebaseRef.child( "seguidores" );
        idUsuarioLogado = UsuarioFirebase.getIdentificadorUsuario();

        //Inicializar componentes
        inicializarComponentes();

        //Recuperar usuário selecionado
        Bundle bundle = getIntent().getExtras();
        if ( bundle != null ){
            usuarioSelecionado = ( Usuario ) bundle.getSerializable( "usuarioSelecionado" );

            //Configura nome do usuário na toolbar
            getSupportActionBar().setTitle( usuarioSelecionado.getNome() );

            //Recuperar foto do usuario
            String caminhoFoto = usuarioSelecionado.getCaminhoFoto();

            if ( caminhoFoto != null ){

                Uri url = Uri.parse( caminhoFoto );
                Glide.with( PerfilAmigoActivity.this )
                        .load( url )
                        .into( imagePerfil );

            }

            //Recuperar nome do usuario
            String nome = usuarioSelecionado.getNome();
            textNome.setText( nome );

            //Recuperar bio do usuario
            String bio = usuarioSelecionado.getBio();
            textBio.setText( bio );

        }

        verificaSegueUsuarioAmigo();


    }

    private void verificaSegueUsuarioAmigo(){

        DatabaseReference seguidorRef = seguidoresRef
                .child( idUsuarioLogado )
                .child( usuarioSelecionado.getId() );

        seguidorRef.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if ( snapshot.exists() ){

                            //Já está seguindo
                            Toast.makeText(PerfilAmigoActivity.this, "Segue", Toast.LENGTH_SHORT).show();
                            habitilarBotaoSeguir( true );

                        }else {

                          //Ainda não está seguindo
                            Toast.makeText(PerfilAmigoActivity.this, "Não segue", Toast.LENGTH_SHORT).show();
                            habitilarBotaoSeguir( false );

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                }
        );

    }

    @SuppressLint("ResourceAsColor")
    private void habitilarBotaoSeguir(Boolean segueusuario ){

        if ( segueusuario ){

            buttonSeguir.setText( "Seguindo" );
            buttonSeguir.setClickable( false );
            buttonSeguir.setTextColor( R.color.black);

        }else {

            buttonSeguir.setText( "Seguir" );

        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        recuperarDadosPerfilAmigo();
    }

    @Override
    protected void onStop() {
        super.onStop();
        usuariosRef.removeEventListener( valueEventListenerPerfilAmigo );

    }

    private void recuperarDadosPerfilAmigo(){

        usuarioAmigoRef = usuariosRef.child( usuarioSelecionado.getId() );
        valueEventListenerPerfilAmigo = usuarioAmigoRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Usuario usuario = snapshot.getValue( Usuario.class );

                String postagens = String.valueOf( usuario.getPostagens() );
                String seguindo = String.valueOf( usuario.getSeguindo() );
                String seguidores = String.valueOf( usuario.getSeguidores() );

                //Configura os valores recuperados
                textPublicacoes.setText( postagens );
                textSeguidores.setText( seguidores );
                textSeguindo.setText( seguindo );


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void inicializarComponentes(){

        gridViewperfil = findViewById( R.id.gridViewPerfilAmigo );
        imagePerfil = findViewById( R.id.imagePerfilAmigo );
        textPublicacoes = findViewById( R.id.textPublicacoesAmigo );
        textSeguidores = findViewById( R.id.textSeguidoresAmigo );
        textSeguindo = findViewById( R.id.textSeguindoAmigo );
        textNome = findViewById( R.id.textViewNomeAmigo );
        textBio = findViewById( R.id.textBioAmigo );
        buttonSeguir = findViewById( R.id.buttonSeguirPerfilAmigo );
        buttomMensagem = findViewById( R.id.buttonMensagemPerfilAmigo );

        Toolbar toolbar = findViewById(  R.id.toolbarPrincipal );
        toolbar.setTitle( "Perfil" );
        setSupportActionBar( toolbar );

        getSupportActionBar().setDisplayHomeAsUpEnabled( true );
        getSupportActionBar().setHomeAsUpIndicator( R.drawable.ic_baseline_close_24 );

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return false;
    }
}