package com.example.instagram.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.instagram.R;
import com.example.instagram.adapter.AdapterMiniaturas;
import com.example.instagram.helper.ConfiguracaoFirebase;
import com.example.instagram.helper.RecyclerItemClickListener;
import com.example.instagram.helper.UsuarioFirebase;
import com.example.instagram.model.Filtros;
import com.example.instagram.model.Postagem;
import com.example.instagram.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import ja.burhanrashid52.photoeditor.OnSaveBitmap;
import ja.burhanrashid52.photoeditor.PhotoEditor;
import ja.burhanrashid52.photoeditor.PhotoEditorView;
import ja.burhanrashid52.photoeditor.PhotoFilter;

public class FiltroActivity extends AppCompatActivity {

    private ImageView imageFotoEscolhida;
    private Bitmap imagem;
    private Bitmap imagemFiltro;
    private RecyclerView recyclerFiltros;
    private AdapterMiniaturas adapterMiniaturas;
    private ArrayList<Filtros> list;
    private TextInputEditText textDescricaoFiltro;
    private String idUsuarioLogado;
    private ProgressDialog progressDialog;
    private DatabaseReference usuariosRef;
    private DatabaseReference usuarioLogadoRef;
    private DatabaseReference firebaseRef;
    private DataSnapshot seguidoresSnapshot;
    private Usuario usuarioLogado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtro);

        Toolbar toolbar = findViewById(R.id.toolbarPrincipal);
        toolbar.setTitle("Filtros");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_close_24);

        //Configurações iniciais
        idUsuarioLogado = UsuarioFirebase.getIdentificadorUsuario();
        usuariosRef = ConfiguracaoFirebase.getFirebaseDatabase().child( "usuarios" );
        firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();

        //Inicializar componentes
        imageFotoEscolhida = findViewById(R.id.imageFotoEscolhida);
        recyclerFiltros = findViewById(R.id.recyclerFiltros);
        textDescricaoFiltro = findViewById(R.id.textDescricaoFiltro);

        //Recuperar dados do usuário logado
        recuperarDadosPostagem();

        //init progressDialog
        progressDialog = new ProgressDialog(this);
        //set properties
        progressDialog.setTitle("Por favor, espere!");             //set title
        progressDialog.setMessage("Publicando...");   //set message
        progressDialog.setCanceledOnTouchOutside(false);    //disable dismiss when touching outside of progress dialog


        //Recupera a imagem escolhida pelo usuário
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            byte[] dadosImagem = bundle.getByteArray("fotoEscolhida");
            imagem = BitmapFactory.decodeByteArray(dadosImagem, 0, dadosImagem.length);
            imageFotoEscolhida.setImageBitmap(imagem);



            filtros( imagem );

            adapterMiniaturas = new AdapterMiniaturas(list, getApplicationContext());
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
            recyclerFiltros.setLayoutManager(layoutManager);
            recyclerFiltros.setAdapter(adapterMiniaturas);


            recyclerFiltros.addOnItemTouchListener(new RecyclerItemClickListener(

                    getApplicationContext(),
                    recyclerFiltros,
                    new RecyclerItemClickListener.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            Filtros filtroSelecoinado = list.get(position);

                            aplicarFiltros( filtroSelecoinado );

                        }

                        @Override
                        public void onLongItemClick(View view, int position) {

                        }

                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        }
                    }

            ));



        }
    }

    private void publicarPostagem(){
            progressDialog.show();

            final Postagem postagem = new Postagem();
            postagem.setIdUsuario( idUsuarioLogado );
            postagem.setDescricao( textDescricaoFiltro.getText().toString() );

            //Recuperar dados da imagem para o firebase
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            imagemFiltro.compress(Bitmap.CompressFormat.JPEG, 70, baos);
            byte[] dadosImagem = baos.toByteArray();

            //Salvar imagem no firebase storage
            StorageReference storageRef = ConfiguracaoFirebase.getFirebaseStorage();
            StorageReference imagemRef = storageRef
                    .child("imagens")
                    .child("postagens")
                    .child( postagem.getId() + ".jpeg");

            UploadTask uploadTask = imagemRef.putBytes( dadosImagem );
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(FiltroActivity.this,
                            "Erro ao salvar a imagem, tente novamente!",
                            Toast.LENGTH_SHORT).show();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    imagemRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            Uri url = task.getResult();
                            postagem.setCaminhoFoto( url.toString() );

                            //Ataulizar a contagem de postagens

                            int qtdPostagem = usuarioLogado.getPostagens() + 1;
                            usuarioLogado.setPostagens( qtdPostagem );
                            usuarioLogado.atualizarQtdPostagens();

                            //Salvar postagem
                            if( postagem.salvar( seguidoresSnapshot ) ){

                                Toast.makeText(FiltroActivity.this,
                                        "Sucesso ao salvar postagem!",
                                        Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                                finish();
                            }
                        }
                    });

                }
            });




    }

    private void recuperarDadosPostagem(){

        usuarioLogadoRef  = usuariosRef.child( idUsuarioLogado );
        usuarioLogadoRef.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        //Recupera dados do usuário logado
                        usuarioLogado = snapshot.getValue( Usuario.class );

                        /*
                        *Recuperar seguidores
                         */
                        DatabaseReference seguidoresRef = firebaseRef
                                .child( "seguidores" )
                                .child( idUsuarioLogado );

                        seguidoresRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                seguidoresSnapshot = snapshot;

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }

    @Override
    public boolean onSupportNavigateUp() {

        finish();
        return false;

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_filtro, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch ( item.getItemId() ){
            case R.id.ic_salvar_postagem :
                publicarPostagem();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void filtros( Bitmap imagemF ){
        list = new ArrayList<>();
        Filtros filtro00 = new Filtros("None", imagemF);
        list.add(filtro00);
        Filtros filtro0 = new Filtros("Gray scale", imagemF);
        list.add(filtro0);
        Filtros filtro01 = new Filtros("Negative", imagemF);
        list.add(filtro01);
        Filtros filtro02 = new Filtros("Sepia", imagemF);
        list.add(filtro02);
        Filtros filtro03 = new Filtros("Documentary", imagemF);
        list.add(filtro03);
        Filtros filtro04 = new Filtros("Fish eye", imagemF);
        list.add(filtro04);

    }


    public void aplicarFiltros(Filtros filtros){

        //Filtros
        imagemFiltro = imagem.copy(imagem.getConfig(), true);

        PhotoEditorView mPhotoEditorView = findViewById(R.id.photoEditorView);

        mPhotoEditorView.getSource().setImageBitmap(imagem);

        PhotoEditor photoEditor = new PhotoEditor.Builder(this, mPhotoEditorView)
                .setPinchTextScalable(true)
                .setClipSourceImage(true)
                .build();

        if (filtros.getNome() == "Gray scale") {
            photoEditor.setFilterEffect(PhotoFilter.GRAY_SCALE);
            photoEditor.saveAsBitmap(new OnSaveBitmap() {
                @Override
                public void onBitmapReady(@Nullable Bitmap bitmap) {
                    imagemFiltro = bitmap;
                    imageFotoEscolhida.setImageBitmap(bitmap);
                }
                @Override
                public void onFailure(@Nullable Exception e) {
                }
            });
        }else if (filtros.getNome() == "Negative") {
            photoEditor.setFilterEffect(PhotoFilter.NEGATIVE);
            photoEditor.saveAsBitmap(new OnSaveBitmap() {
                @Override
                public void onBitmapReady(@Nullable Bitmap bitmap) {
                    imagemFiltro = bitmap;
                    imageFotoEscolhida.setImageBitmap(bitmap);
                }
                @Override
                public void onFailure(@Nullable Exception e) {
                }
            });
        }
        else if (filtros.getNome() == "Sepia") {
            photoEditor.setFilterEffect(PhotoFilter.SEPIA);
            photoEditor.saveAsBitmap(new OnSaveBitmap() {
                @Override
                public void onBitmapReady(@Nullable Bitmap bitmap) {
                    imagemFiltro = bitmap;
                    imageFotoEscolhida.setImageBitmap(bitmap);
                }
                @Override
                public void onFailure(@Nullable Exception e) {
                }
            });
        }
        else if (filtros.getNome() == "None") {
            photoEditor.setFilterEffect(PhotoFilter.NONE);
            imageFotoEscolhida.setImageBitmap(imagem);
        }
        else if (filtros.getNome() == "Documentary") {
            photoEditor.setFilterEffect(PhotoFilter.DOCUMENTARY);
            photoEditor.saveAsBitmap(new OnSaveBitmap() {
                @Override
                public void onBitmapReady(@Nullable Bitmap bitmap) {
                    imagemFiltro = bitmap;
                    imageFotoEscolhida.setImageBitmap(bitmap);
                }
                @Override
                public void onFailure(@Nullable Exception e) {
                }
            });
        }
        else if (filtros.getNome() == "Fish eye") {
            photoEditor.setFilterEffect(PhotoFilter.FISH_EYE);
            photoEditor.saveAsBitmap(new OnSaveBitmap() {
                @Override
                public void onBitmapReady(@Nullable Bitmap bitmap) {
                    imagemFiltro = bitmap;
                    imageFotoEscolhida.setImageBitmap(bitmap);
                }
                @Override
                public void onFailure(@Nullable Exception e) {
                }
            });

        }

    }

}