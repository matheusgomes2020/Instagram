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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

public class LoginActivity extends AppCompatActivity {

    private EditText campoEmail, campoSenha;
    private Button botaoEntrar;
    private FirebaseAuth autenticacao;
    private ProgressDialog progressDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.example.instagram.R.layout.activity_login);

        inicializarComponentes();
        //autenticacao.signOut();
        verificarUsuarioLogado();




        botaoEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validarCampos();
            }
        });

    }

    public void abrirCadastro( View view ){
        Intent i = new Intent( LoginActivity.this, CadastroActivity.class );
        startActivity( i );
    }

    public void abrirPrincipal(){
        Intent i = new Intent( LoginActivity.this, MainActivity.class );
        startActivity( i );
        finish();
    }

    public void verificarUsuarioLogado(){

        if ( autenticacao.getCurrentUser() != null ){
            abrirPrincipal();
        }

    }


    public void logar( String email, String senha ){

        progressDialog.show();

        autenticacao.signInWithEmailAndPassword(
                email,
                senha
        ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if ( task.isSuccessful() ){
                    progressDialog.dismiss();
                    abrirPrincipal();
                }else {

                    progressDialog.dismiss();
                    String excecao = "";
                    try {
                        throw task.getException();
                    } catch (FirebaseAuthInvalidUserException e) {
                        excecao = "Usuário não está cadastrado.";
                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        excecao = "E-mail e senha não correspondem a um usuário cadastrado";
                    } catch (Exception e) {
                        excecao = "Erro ao Logar usuário: " + e.getMessage();
                        e.printStackTrace();
                    }
                    Toast.makeText(LoginActivity.this,
                            excecao,
                            Toast.LENGTH_SHORT).show();
                }

            }
        });

    }


    public void validarCampos(){

        String email = campoEmail.getText().toString();
        String senha = campoSenha.getText().toString();

        if ( !email.isEmpty() ){
            if ( !senha.isEmpty() ){

                logar( email, senha );

            }else {
                Toast.makeText(this, "Campo nome não pode ser vazio", Toast.LENGTH_SHORT).show();
            }

        }else {
            Toast.makeText(this, "Campo e-mail não pode ser vazio", Toast.LENGTH_SHORT).show();
        }
    }

    public void inicializarComponentes(){

        campoEmail = findViewById( R.id.textEmailEntrar );
        campoSenha = findViewById( R.id.textSenhaEntrar );
        botaoEntrar = findViewById( R.id.buttonEntrar );

        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();

        //init progressDialog
        progressDialog = new ProgressDialog(this);
        //set properties
        progressDialog.setTitle("Por favor, espere!");             //set title
        progressDialog.setMessage("Cadastrando usuário...");   //set message
        progressDialog.setCanceledOnTouchOutside(false);    //disable dismiss when touching outside of progress dialog

    }

}