package com.example.instagram.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.instagram.R;


public class PostagemFragment extends Fragment {


    public PostagemFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_postagem, container, false);

        setHasOptionsMenu(true);

        Toolbar toolbar = view.findViewById(  R.id.toolbarPrincipal );
        toolbar.setTitle( "" );
        ((AppCompatActivity)getActivity()).setSupportActionBar( toolbar );

        return view;

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate( R.menu.menu_post, menu );
        super.onCreateOptionsMenu(menu, inflater);
    }
}