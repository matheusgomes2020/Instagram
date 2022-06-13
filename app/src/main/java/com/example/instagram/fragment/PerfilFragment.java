package com.example.instagram.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.instagram.R;
import com.example.instagram.activity.EditarPerfilActivity;
import com.example.instagram.activity.PerfilAmigoActivity;
import com.example.instagram.helper.ConfiguracaoFirebase;
import com.example.instagram.helper.UsuarioFirebase;
import com.example.instagram.model.Usuario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class PerfilFragment extends Fragment {

    private Button buttonEditarPerfil;
    private CircleImageView imagePerfil;
    private GridView gridViewperfil;
    private TextView textPublicacoes, textSeguidores, textSeguindo, textNome, textBio;

    private DatabaseReference firebaseRef;
    private DatabaseReference usuariosRef;
    private DatabaseReference usuarioLogadoRef;

    private FirebaseAuth firebaseAuth;


    //private Usuario usuarioLogado;
    private Usuario usuarioL;
    private String idUsuarioLogado;

    public PerfilFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_perfil, container, false);

        //Configurações iniciais

        firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();
        usuariosRef = ConfiguracaoFirebase.getFirebaseDatabase().child( "usuarios" );
        idUsuarioLogado = UsuarioFirebase.getIdentificadorUsuario();


        gridViewperfil = view.findViewById( R.id.gridViewPerfil );
        imagePerfil = view.findViewById( R.id.imagePerfil );
        textPublicacoes = view.findViewById( R.id.textPublicacoes );
        textSeguidores = view.findViewById( R.id.textSeguidores );
        textSeguindo = view.findViewById( R.id.editBioPerfil );
        textNome = view.findViewById( R.id.textViewNome );
        textBio = view.findViewById( R.id.textBioPerfil );
        textSeguindo = view.findViewById( R.id.textSeguindo );
        buttonEditarPerfil = view.findViewById( R.id.buttonEditarPerfil );


        buttonEditarPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent( getActivity(), EditarPerfilActivity.class);
                startActivity( i );
            }
        });


        return view;
    }

    public void recuperarDadosUsuarioLogado(  ){

        usuarioLogadoRef  = usuariosRef.child( idUsuarioLogado );
        usuarioLogadoRef.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        usuarioL = snapshot.getValue( Usuario.class );

                        try {
                            String caminhoFoto = usuarioL.getCaminhoFoto();
                            if ( caminhoFoto != null ){

                                Uri url = Uri.parse( caminhoFoto );
                                Glide.with( PerfilFragment.this )
                                        .load( url )
                                        .into( imagePerfil );

                            }
                        }catch ( Exception e ){
                            e.printStackTrace();
                        }


                        //usuarioL.setNome( "TESTE" );

                        textNome.setText( usuarioL.getNome() );
                        //textNome.setText( "usuarioLogado.getNome()" );
                        textBio.setText( usuarioL.getBio() );
                        //Toast.makeText(getActivity(), "Método", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });




    }



    @Override
    public void onStart() {
        super.onStart();
        recuperarDadosUsuarioLogado();
    }
}