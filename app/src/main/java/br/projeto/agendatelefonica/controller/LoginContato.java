package br.projeto.agendatelefonica.controller;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.Map;

import br.projeto.agendatelefonica.R;

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

            }
        });
    }

    public void cadastrarUsuario(String nome, String email, String senha) {
        url.createUser(email, senha, new Firebase.ValueResultHandler<Map<String, Object>>() {
            @Override
            public void onSuccess(Map<String, Object> result) {
                System.out.println("Successfully created user account with uid: " + result.get("uid"));
            }
            @Override
            public void onError(FirebaseError firebaseError) {
                // there was an error
            }
        });
    }
}