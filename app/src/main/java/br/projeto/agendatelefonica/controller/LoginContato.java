package br.projeto.agendatelefonica.controller;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.Map;

import br.projeto.agendatelefonica.R;
import br.projeto.agendatelefonica.model.Usuario;

public class LoginContato extends AppCompatActivity {
    // URL firebase
    private Firebase url = new Firebase("https://minhagendatelefonica.firebaseio.com/");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_contato);
    }

    @Override
    protected void onStart(){
        super.onStart();

        final Button btCadastro = (Button) findViewById(R.id.btCadastro);
        final Button btCadastrar = (Button) findViewById(R.id.btCadastrar);
        final Button btLogin = (Button) findViewById(R.id.btLogin);
        final Button btEntrar = (Button) findViewById(R.id.btEntrar);
        final EditText nomeCadastro = (EditText) findViewById(R.id.nomeCadastro);
        final EditText senhaCadastro = (EditText) findViewById(R.id.senhaCadastro);
        final EditText emailCadastro = (EditText) findViewById(R.id.emailCadastro);
        /*
         * Esconde a "tela" de Login e habilita a tela de Cadastro
         * ps: para não haver necessidade de criar um layout e um intent
         */
        btCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btCadastro.setVisibility(View.GONE);
                btLogin.setVisibility(View.VISIBLE);
                btCadastrar.setVisibility(View.VISIBLE);
                btEntrar.setVisibility(View.GONE);
                nomeCadastro.setVisibility(View.VISIBLE);
            }
        });
        /*
         * Esconde a "tela" de Cadastro e habilita a tela de Login
         */
        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btCadastro.setVisibility(View.VISIBLE);
                btLogin.setVisibility(View.GONE);
                btCadastrar.setVisibility(View.GONE);
                btEntrar.setVisibility(View.VISIBLE);
                nomeCadastro.setVisibility(View.GONE);
            }
        });
        /*
         * Quando o usuário clicar em Cadastrar, os dados são
         * inseridos na base
         */
        btCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nome = nomeCadastro.getText().toString();
                String email = emailCadastro.getText().toString();
                String senha = senhaCadastro.getText().toString();
                cadastrarUsuario(nome, email, senha);
            }
        });
        /*
         * Quando o usuário clicar em Entrar, ele loga no app
         */
        btEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailCadastro.getText().toString();
                String senha = senhaCadastro.getText().toString();
                logarUsuario(email, senha);
            }
        });
    }

    public void cadastrarUsuario(final String nome, final String email, final String senha) {
        url.createUser(email, senha, new Firebase.ValueResultHandler<Map<String, Object>>() {
            @Override
            public void onSuccess(Map<String, Object> result) {
                mensagem("Conta criada com sucesso! Seu UID é: " + result.get("uid").toString());
            }
            @Override
            public void onError(FirebaseError firebaseError) {
                mensagem("ERRO: " + firebaseError);
            }
        });
    }

    public void logarUsuario(String email, String senha){
        url.authWithPassword(email, senha, new Firebase.AuthResultHandler() {
            @Override
            public void onAuthenticated(AuthData authData) {
                mensagem("Logado! UID: " + authData.getUid() + ", Provider: " + authData.getProvider() + ", Token: " + authData.getToken());

                Usuario user = new Usuario("Mude seu nome", authData.getProviderData().get("email").toString());
                url.child("Usuarios").child(authData.getUid()).setValue(user);
                
                Intent intentMain = new Intent(LoginContato.this, ListarContato.class);
                startActivity(intentMain);
            }

            @Override
            public void onAuthenticationError(FirebaseError firebaseError) {
                mensagem("ERRO: " + firebaseError);
            }
        });
    }

    public void mensagem(String msg){
        Toast.makeText(LoginContato.this, msg, Toast.LENGTH_SHORT).show();
    }
}