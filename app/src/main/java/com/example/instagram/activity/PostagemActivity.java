package com.example.instagram.activity;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;

import com.example.instagram.R;
import com.example.instagram.helper.Permissao;

import java.io.ByteArrayOutputStream;

public class PostagemActivity extends AppCompatActivity {

    private Button buttonAbrirCamera, buttonAbrirGaleria;
    private static final  int SELECAO_CAMERA = 100;
    private static final  int SELECAO_GALERIA = 200;
    private static int requestCode;

    private String[] permissoesNecessarias = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    };

    ActivityResultLauncher<Intent> activityResultLauncher;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postagem);

        Toolbar toolbar = findViewById(  R.id.toolbarPrincipal );
        toolbar.setTitle( "Nova Publicação" );
        setSupportActionBar( toolbar );

        getSupportActionBar().setDisplayHomeAsUpEnabled( true );
        getSupportActionBar().setHomeAsUpIndicator( R.drawable.ic_baseline_close_24 );

        //Validar permissões
        Permissao.validarPermissoes( permissoesNecessarias, this, 1 );

        //Inicializar componentes
        buttonAbrirCamera = findViewById( R.id.buttonAbrirCamera );
        buttonAbrirGaleria = findViewById( R.id.buttonAbrirGaleria );

        buttonAbrirCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if ( i.resolveActivity( getApplicationContext().getPackageManager() ) != null ){

                    activityResultLauncher.launch(i);
                    requestCode = SELECAO_CAMERA;
                   // startActivityForResult(i, SELECAO_CAMERA);

                }

            }
        });

        buttonAbrirGaleria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                if ( i.resolveActivity( getApplicationContext().getPackageManager() ) != null ){

                    activityResultLauncher.launch(i);
                    requestCode = SELECAO_GALERIA;
                   // startActivityForResult(i, SELECAO_GALERIA);

                }

            }
        });

        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {

                        if(result.getResultCode() == RESULT_OK ){

                            Bitmap imagem = null;

                            try {

                                //Valida tipo de seleção da imagem
                                switch ( requestCode ){
                                    case SELECAO_CAMERA :
                                        imagem = (Bitmap) result.getData().getExtras().get("data");
                                        break;
                                    case SELECAO_GALERIA :
                                        Uri localImagemSelecionada = result.getData().getData();
                                        imagem = MediaStore.Images.Media.getBitmap(getContentResolver(), localImagemSelecionada);
                                        break;
                                }

                                //Valida imagem selecionada
                                if( imagem != null ){

                                    //Converte imagem em byte array
                                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                    imagem.compress(Bitmap.CompressFormat.JPEG, 70, baos);
                                    byte[] dadosImagem = baos.toByteArray();

                                    //Envia imagem escolhida para aplicação de filtro
                                    Intent i = new Intent(getApplicationContext(), FiltroActivity.class);
                                    i.putExtra("fotoEscolhida", dadosImagem );
                                    startActivity( i );

                                }

                            }catch (Exception e){
                                e.printStackTrace();
                            }

                        }

                    }
                }
        );





    }

/*

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            Bitmap imagem = null;

            try {

                //Valida tipo de seleção da imagem
                switch (requestCode) {

                    case SELECAO_GALERIA:
                        Uri localImagemSelecionada = data.getData();
                        imagem = MediaStore.Images.Media.getBitmap(getContentResolver(), localImagemSelecionada);
                        break;
                }

                //Valida imagem selecionada
                if (imagem != null) {

                    //Converte imagem em byte array
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    imagem.compress(Bitmap.CompressFormat.JPEG, 70, baos);
                    byte[] dadosImagem = baos.toByteArray();

                    //Envia imagem escolhida para aplicação de filtro
                    Intent i = new Intent(this, FiltroActivity.class);
                    i.putExtra("fotoEscolhida", dadosImagem);
                    startActivity(i);

                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }


    }



 */



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate( R.menu.menu_postagem, menu );

        return super.onCreateOptionsMenu(menu);
    }
}