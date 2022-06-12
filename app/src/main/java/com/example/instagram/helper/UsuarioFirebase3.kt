package com.example.instagram.helper

import android.net.Uri
import android.util.Log
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.FirebaseAuth
import com.example.instagram.helper.ConfiguracaoFirebase
import com.example.instagram.helper.UsuarioFirebase3
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.android.gms.tasks.OnCompleteListener
import com.example.instagram.model.Usuario
import java.lang.Exception

object UsuarioFirebase3 {
    val usuarioAtual: FirebaseUser?
        get() {
            val usuario = ConfiguracaoFirebase.getFirebaseAutenticacao()
            return usuario.currentUser
        }
    val identificadorUsuario: String
        get() = usuarioAtual!!.uid

    fun atualizarNomeUsuario(nome: String?) {
        try {

            //Usuario logado no app
            val usuarioLogado = usuarioAtual


            //Configurar objeto para alteração do perfil
            val profile = UserProfileChangeRequest.Builder()
                .setDisplayName(nome)
                .build()
            usuarioLogado!!.updateProfile(profile).addOnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.d("Perfil", "Erro ao atualizar nome de perfil")
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun atualizarFotoUsuario(url: Uri?) {
        try {

            //Usuario logado no app
            val usuarioLogado = usuarioAtual


            //Configurar objeto para alteração do perfil
            val profile = UserProfileChangeRequest.Builder()
                .setPhotoUri(url)
                .build()
            usuarioLogado!!.updateProfile(profile).addOnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.d("Perfil", "Erro ao atualizar a foto de perfil")
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    val dadosusuarioLogado: Usuario
        get() {
            val firebaseUser = usuarioAtual
            val usuario = Usuario()
            usuario.email = firebaseUser!!.email
            usuario.nome = firebaseUser.displayName
            usuario.id = firebaseUser.uid
            if (firebaseUser.photoUrl == null) {
                usuario.caminhoFoto = ""
            } else {
                usuario.caminhoFoto = firebaseUser.photoUrl.toString()
            }
            return usuario
        }
}