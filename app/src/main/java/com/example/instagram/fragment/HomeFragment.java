package com.example.instagram.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.instagram.R;
import com.example.instagram.activity.LoginActivity;
import com.example.instagram.activity.PostagemActivity;
import com.example.instagram.adapter.AdapterFeed;
import com.example.instagram.helper.ConfiguracaoFirebase;
import com.example.instagram.helper.UsuarioFirebase;
import com.example.instagram.model.Feed;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HomeFragment extends Fragment {

    private FirebaseAuth autenticacao;
    private RecyclerView recyclerFeed;
    private AdapterFeed adapterFeed;
    private List<Feed> listaFeed = new ArrayList<>();
    private ValueEventListener valueEventListenerFeed;
    private DatabaseReference feedRef;
    private String idUsuarioLogado;

    public HomeFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_home, container, false);

        //Configurações iniciais
        idUsuarioLogado = UsuarioFirebase.getIdentificadorUsuario();
        feedRef = ConfiguracaoFirebase.getFirebaseDatabase()
                .child( "feed" )
                .child( idUsuarioLogado );

        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();

        setHasOptionsMenu(true);

        Toolbar toolbar = view.findViewById(  R.id.toolbarPrincipal );
        toolbar.setTitle( "Instagram" );
        ((AppCompatActivity)getActivity()).setSupportActionBar( toolbar );

        recyclerFeed = view.findViewById( R.id.recyclerFeed );



        return view;

    }

    private void recuperarFeed(){

        listaFeed.clear();

        valueEventListenerFeed = feedRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for ( DataSnapshot ds: dataSnapshot.getChildren() ){
                    listaFeed.add( ds.getValue(Feed.class) );

                }

               // adapterFeed.notifyDataSetChanged();

                //Configura recyclerview
                adapterFeed = new AdapterFeed( listaFeed, getActivity() );
                recyclerFeed.setHasFixedSize( true );
                recyclerFeed.setLayoutManager( new LinearLayoutManager(getActivity()));
                recyclerFeed.setAdapter( adapterFeed );
                Collections.reverse( listaFeed );


                adapterFeed.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        recuperarFeed();
    }

    @Override
    public void onStop() {
        super.onStop();
        feedRef.removeEventListener( valueEventListenerFeed );
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {

        inflater.inflate( R.menu.menu_main, menu );

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch ( item.getItemId() ){
            case R.id.menu_direct:
                deslogarUsuario();
                startActivity( new Intent( getActivity(), LoginActivity.class ) );
                break;

            case R.id.menu_adicionar:

                showBottomSheetDialog();
        }

        return super.onOptionsItemSelected(item);
    }

    private void deslogarUsuario(){

        try {
            autenticacao.signOut();
        }catch ( Exception e ){
            e.printStackTrace();
        }

    }

    private void showBottomSheetDialog() {

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getActivity());
        bottomSheetDialog.setContentView(R.layout.sheet_principal);

        LinearLayout publicacao = bottomSheetDialog.findViewById(R.id.publicacao);
        LinearLayout story = bottomSheetDialog.findViewById(R.id.story);
        LinearLayout reels = bottomSheetDialog.findViewById(R.id.reels);
        LinearLayout live = bottomSheetDialog.findViewById(R.id.live);

        bottomSheetDialog.show();

        publicacao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity( new Intent( getActivity(), PostagemActivity.class ) );

                bottomSheetDialog.dismiss();

            }
        });

    }
}