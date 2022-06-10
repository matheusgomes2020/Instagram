package com.example.instagram.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.instagram.R;
import com.example.instagram.helper.ConfiguracaoFirebase;
import com.example.instagram.helper.UsuarioFirebase2;
import com.example.instagram.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

public class CadastroActivity extends AppCompatActivity {

    private EditText campoNome, campoEmail, campoSenha;
    private Button botaoCadastar;
    private ProgressDialog progressDialog;

    private FirebaseAuth autenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        inicializarComponentes();

        botaoCadastar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String textNome = campoNome.getText().toString();
                String textEmail = campoEmail.getText().toString();
                String textSenha = campoSenha.getText().toString();

                if ( !textNome.isEmpty() ){
                    if ( !textEmail.isEmpty() ){
                        if ( !textSenha.isEmpty() ){

                            Usuario usuario = new Usuario();

                            usuario.setNome( textNome );
                            usuario.setEmail( textEmail );
                            usuario.setSenha( textSenha );
                            cadastar( usuario );

                        }else {
                            Toast.makeText(CadastroActivity.this, "Preencha a senha!", Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        Toast.makeText(CadastroActivity.this, "Preencha o e-mail!", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(CadastroActivity.this, "Preencha o nome!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void cadastar( Usuario usuario ){

        progressDialog.show();

        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        autenticacao.createUserWithEmailAndPassword(
                usuario.getEmail(),
                usuario.getSenha()
        ).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if ( task.isSuccessful() ){

                    try {

                        progressDialog.dismiss();

                        //Salvar dados no firebase

                        String idUsuario = task.getResult().getUser().getUid();
                        usuario.setId( idUsuario );
                        usuario.salvar();

                        //Salvar os dados no profile do firebase
                        UsuarioFirebase2.atualizarNomeUsuario( usuario.getNome() );


                        Toast.makeText(CadastroActivity.this,
                                "Sucesso ao cadastrar usuário!",
                                Toast.LENGTH_SHORT).show();
                        startActivity( new Intent(getApplicationContext(), MainActivity.class));

                        finish();

                    }catch ( Exception e ){
                        e.printStackTrace();
                    }

                }else {

                    progressDialog.dismiss();

                    String excessao = "";
                    try {
                        throw  task.getException();
                    }catch (FirebaseAuthWeakPasswordException e){
                        excessao = "Digite uma senha mais forte!";
                    }
                    catch (FirebaseAuthInvalidCredentialsException e){
                        excessao = "Por favor, digite um e-mail válido!";
                    }
                    catch (FirebaseAuthUserCollisionException e){
                        excessao = "Esta conta já foi cadastrada!";
                    }
                    catch (Exception e){
                        excessao = "Erro ao cadastrar usuário: " + e.getMessage();
                        e.printStackTrace();
                    }

                    Toast.makeText(CadastroActivity.this,
                            excessao,
                            Toast.LENGTH_SHORT).show();



                }
            }
        });

    }

    public void inicializarComponentes(){

        campoNome = findViewById( R.id.textNomeCadastrar );
        campoEmail = findViewById( R.id.textEmailEntrar);
        campoSenha = findViewById( R.id.textSenhaEntrar);
        botaoCadastar = findViewById( R.id.buttonEntrar);

        //init progressDialog
        progressDialog = new ProgressDialog(this);
        //set properties
        progressDialog.setTitle("Por favor, espere!");             //set title
        progressDialog.setMessage("Cadastrando usuário...");   //set message
        progressDialog.setCanceledOnTouchOutside(false);    //disable dismiss when touching outside of progress dialog

    }

}