package com.example.instagram.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.instagram.R;
import com.example.instagram.activity.PerfilAmigoActivity;
import com.example.instagram.adapter.AdapterPesquisa;
import com.example.instagram.helper.ConfiguracaoFirebase;
import com.example.instagram.helper.RecyclerItemClickListener;
import com.example.instagram.model.Usuario;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class PesquisaFragment extends Fragment {

    private SearchView searchViewPesquisa;
    private RecyclerView recyclerPesquisa;

    private List<Usuario> listausuarios;
    private DatabaseReference usuariosRef;

    private AdapterPesquisa adapterPesquisa;

    public PesquisaFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_pesquisa, container, false);

        searchViewPesquisa = view.findViewById( R.id.searchViewPesquisa );
        recyclerPesquisa = view.findViewById( R.id.recyclerViewPesquisa );

        //Configurações iniciais

        listausuarios = new ArrayList<>();
        usuariosRef = ConfiguracaoFirebase.getFirebaseDatabase().child( "usuarios" );

        //Configurar recyclerView
        recyclerPesquisa.setHasFixedSize( true );
        recyclerPesquisa.setLayoutManager( new LinearLayoutManager(getActivity()));

        adapterPesquisa = new AdapterPesquisa( listausuarios, getActivity() );
        recyclerPesquisa.setAdapter( adapterPesquisa );

        //Configurar evento de clique
        recyclerPesquisa.addOnItemTouchListener( new RecyclerItemClickListener(
                getActivity(),
                recyclerPesquisa,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                        Usuario usuarioSelecionado = listausuarios.get( position );
                        Intent i = new Intent( getActivity(), PerfilAmigoActivity.class );
                        i.putExtra( "usuarioSelecionado", usuarioSelecionado );
                        startActivity( i );

                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

                    }

                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    }
                }
        ));

        //Configura searchview
        searchViewPesquisa.setQueryHint( "Buscar usuários" );
        searchViewPesquisa.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                Toast.makeText(getActivity(), "Submit: " + s, Toast.LENGTH_SHORT).show();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {

                String textoDigitado = s.toUpperCase();
                pesquisarUsuarios( textoDigitado );

                return true;
            }
        });

        return view;
    }

    private void pesquisarUsuarios(String texto) {

        //limpar lista
        listausuarios.clear();

        //Pesquisar usuários caso tenha texto na pesquisa
        if ( texto.length() >=2 ){

            Query query = usuariosRef.orderByChild( "nome" )
                    .startAt( texto.toUpperCase() )
                    .endAt( texto.toLowerCase() + "\uf8ff" );

            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    //limpar lista
                    listausuarios.clear();


                    for ( DataSnapshot ds : snapshot.getChildren() ){

                        listausuarios.add( ds.getValue( Usuario.class ) );

                    }

                    adapterPesquisa.notifyDataSetChanged();

                    /*
                    int total = listausuarios.size();
                    Toast.makeText(getActivity(), "Total: " + total, Toast.LENGTH_SHORT).show();
                     */

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }

    }
}