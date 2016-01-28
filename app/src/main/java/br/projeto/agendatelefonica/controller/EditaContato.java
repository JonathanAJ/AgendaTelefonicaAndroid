package br.projeto.agendatelefonica.controller;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.parse.*;

import br.projeto.agendatelefonica.R;

public class EditaContato extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edita_contato);

        Toolbar barraMain = (Toolbar) findViewById(R.id.barraMain);
        barraMain.setTitle("Agenda Telefônica");
        barraMain.setSubtitle("Editar Contato");
        barraMain.setTitleTextColor(getResources().getColor(R.color.colorTextIcon));
        barraMain.setSubtitleTextColor(getResources().getColor(R.color.colorTextIcon));
        barraMain.setLogo(R.mipmap.ic_launcher);
        barraMain.setContentInsetsAbsolute(5, 5);
        setSupportActionBar(barraMain);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle extra = getIntent().getExtras();

        final String id = extra.getString("Id");
        String nome = extra.getString("Nome");
        String telefone = extra.getString("Telefone");

        final EditText nomeEdit = (EditText) findViewById(R.id.nom);
        final EditText telefoneEdit = (EditText) findViewById(R.id.tel);

        nomeEdit.setText(nome);
        telefoneEdit.setText(telefone);

        Button btSalva = (Button) findViewById(R.id.btSalva);

        btSalva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseQuery<ParseObject> query = ParseQuery.getQuery("Lista_Telefonica");
                query.getInBackground(id, new GetCallback<ParseObject>() {
                    public void done(ParseObject listaTelefonica, ParseException e) {
                        if (e == null) {
                            String nome = nomeEdit.getText().toString();
                            String telefone = telefoneEdit.getText().toString();
                            /*
                             * Edita o objeto que foi retomado
                             */
                            listaTelefonica.put("Nome", nome);
                            listaTelefonica.put("Telefone", telefone);
                            /*
                             * Salva as modificações
                             */
                            listaTelefonica.saveInBackground();
                            startActivity(new Intent(EditaContato.this, MainActivity.class));
                        } else {
                            System.out.println("Erro :" + e);
                        }
                    }
                });
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