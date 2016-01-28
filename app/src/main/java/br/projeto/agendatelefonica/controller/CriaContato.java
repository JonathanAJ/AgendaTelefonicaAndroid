package br.projeto.agendatelefonica.controller;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import android.support.v7.widget.Toolbar;

import com.parse.*;

import br.projeto.agendatelefonica.R;

public class CriaContato extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cria_contato);

        Toolbar barraMain = (Toolbar) findViewById(R.id.barraMain);
        barraMain.setTitle("Agenda Telefônica");
        barraMain.setSubtitle("Criar Contato");
        barraMain.setTitleTextColor(getResources().getColor(R.color.colorTextIcon));
        barraMain.setSubtitleTextColor(getResources().getColor(R.color.colorTextIcon));
        barraMain.setLogo(R.mipmap.ic_launcher);
        barraMain.setContentInsetsAbsolute(5, 5);
        setSupportActionBar(barraMain);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        /*
         * Inicializando base do parse com as keys geradas
         */
        //  Parse.initialize(this, "RNihJPcSLheuubmCXlptrimTCStC5T4j3d0nzDYu", "H9MMOZZ6IPTuRpwdAHwhD0VrakQkc2Tr5g7wmwIs");
        /*
         * Linha opcional para analisar o tráfego
         */
        ParseAnalytics.trackAppOpenedInBackground(getIntent());
        /*
         * Cria um objeto parse que cria uma classe chamada Lista_Telefonica
         */
        final ParseObject listaTelefonica = new ParseObject("Lista_Telefonica");

        final EditText nomeEdit = (EditText) findViewById(R.id.nom);
        final EditText telefoneEdit = (EditText) findViewById(R.id.tel);

        Button btSalva = (Button) findViewById(R.id.btSalva);

        btSalva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nome = nomeEdit.getText().toString();
                String telefone = telefoneEdit.getText().toString();
                /*
                 * Adiciona a coluna Nome o valor nome e o
                 * valor telefone a coluna Telefone
                 */
                listaTelefonica.put("Nome", nome);
                listaTelefonica.put("Telefone", telefone);
                /*
                 * Salva as modificações
                 */
                listaTelefonica.saveInBackground();
                startActivity(new Intent(CriaContato.this, MainActivity.class));
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem){
        int id = menuItem.getItemId();
        if(id==android.R.id.home){
            finish();
        }
        return true;
    }
}