package com.example.instagram.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.example.instagram.R;
import com.example.instagram.activity.EditarPerfilActivity;

import de.hdodenhof.circleimageview.CircleImageView;

public class PerfilFragment extends Fragment {

    private Button buttonEditarPerfil;
    private CircleImageView imagePerfil;
    private GridView gridViewperfil;
    private TextView textPublicacoes, textSeguidores, textSeguindo;

    public PerfilFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_perfil, container, false);

        gridViewperfil = view.findViewById( R.id.gridViewPerfil );
        imagePerfil = view.findViewById( R.id.imagePerfil );
        textPublicacoes = view.findViewById( R.id.textPublicacoes );
        textSeguidores = view.findViewById( R.id.textSeguidores );
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


}