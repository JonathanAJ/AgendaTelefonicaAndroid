package br.projeto.agendatelefonica.controller;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.firebase.client.Firebase;
import com.parse.*;

import br.projeto.agendatelefonica.R;
import br.projeto.agendatelefonica.model.Contato;

public class EditaContato extends AppCompatActivity {

    private Firebase url = new Firebase("https://minhagendatelefonica.firebaseio.com/");

    private String id;
    private String nome;
    private String telefone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edita_contato);

        Bundle extra = getIntent().getExtras();
        id = extra.getString("Id");
        nome = extra.getString("Nome");
        telefone = extra.getString("Telefone");

        Toolbar barraMain = (Toolbar) findViewById(R.id.barraMain);
        barraMain.setTitle("Agenda Telefônica");
        barraMain.setSubtitle("Editar Contato");
        barraMain.setTitleTextColor(getResources().getColor(R.color.colorTextIcon));
        barraMain.setSubtitleTextColor(getResources().getColor(R.color.colorTextIcon));
        barraMain.setLogo(R.mipmap.ic_launcher);
        barraMain.setContentInsetsAbsolute(5, 5);
        barraMain.inflateMenu(R.menu.menu_delete);
        barraMain.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.item_delete) {
                    editaContato(null, null);
                    Intent activityMain = new Intent(EditaContato.this, MainActivity.class);
                    startActivity(activityMain);
                }
                return true;
            }
        });
    }

    @Override
    protected void onStart(){
        super.onStart();

        final EditText nomeEdit = (EditText) findViewById(R.id.nom);
        final EditText telefoneEdit = (EditText) findViewById(R.id.tel);
        nomeEdit.setText(nome);
        telefoneEdit.setText(telefone);

        Button btSalva = (Button) findViewById(R.id.btSalva);
        btSalva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nome = nomeEdit.getText().toString();
                String telefone = telefoneEdit.getText().toString();
                editaContato(nome, telefone);
                startActivity(new Intent(EditaContato.this, MainActivity.class));
            }
        });
    }

    public void editaContato(String nome, String telefone){
        Contato novoContato = new Contato();
        novoContato.setNome(nome);
        novoContato.setTelefone(telefone);
        url.child("Contatos").child(id).setValue(novoContato);
    }
}