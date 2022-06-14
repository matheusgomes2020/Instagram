package com.example.instagram.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;

import com.example.instagram.R;

public class PostagemActivity extends AppCompatActivity {

    private Button buttonAbrirCamera, buttonAbrirGaleria;
    private static final  int SELECAO_CAMERA = 100;
    private static final  int SELECAO_GALERIA = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postagem);

        Toolbar toolbar = findViewById(  R.id.toolbarPrincipal );
        toolbar.setTitle( "Nova Publicação" );
        setSupportActionBar( toolbar );

        getSupportActionBar().setDisplayHomeAsUpEnabled( true );
        getSupportActionBar().setHomeAsUpIndicator( R.drawable.ic_baseline_close_24 );

        buttonAbrirCamera = findViewById( R.id.buttonAbrirCamera );
        buttonAbrirGaleria = findViewById( R.id.buttonAbrirGaleria );

        buttonAbrirCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if ( i.resolveActivity( getApplicationContext().getPackageManager() ) != null ){

                    startActivityForResult( i, SELECAO_CAMERA );

                }

            }
        });

        buttonAbrirGaleria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                if ( i.resolveActivity( getApplicationContext().getPackageManager() ) != null ){

                    startActivityForResult( i, SELECAO_GALERIA );

                }

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate( R.menu.menu_postagem, menu );

        return super.onCreateOptionsMenu(menu);
    }
}