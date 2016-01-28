package br.projeto.agendatelefonica.controller;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.*;
import android.widget.*;
import com.parse.*;
import java.util.*;

import br.projeto.agendatelefonica.R;
import br.projeto.agendatelefonica.model.Contato;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar barraMain = (Toolbar) findViewById(R.id.barraMain);
        barraMain.setTitle("Agenda Telefônica");
        barraMain.setSubtitle("Meus Contatos");
        barraMain.setTitleTextColor(getResources().getColor(R.color.colorTextIcon));
        barraMain.setSubtitleTextColor(getResources().getColor(R.color.colorTextIcon));
        barraMain.setLogo(R.mipmap.ic_launcher);
        barraMain.inflateMenu(R.menu.menu_add);
        barraMain.setContentInsetsAbsolute(5, 5);
        barraMain.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.item_add) {
                    Intent activityCria = new Intent(MainActivity.this, CriaContato.class);
                    startActivity(activityCria);
                }
                return true;
            }
        });

        SearchView buscaContato = (SearchView) findViewById(R.id.buscaContato);
        buscaContato.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String contato) {

                return true;
            }

            @Override
            public boolean onQueryTextSubmit(String contato) {

                return false;
            }
        });

        final ArrayList<Contato> listaDeContatos = new ArrayList<Contato>();

        final ListView listaView = (ListView) findViewById(R.id.listView);

        listaView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Contato contatoSelecionado = listaDeContatos.get(position);

                Intent activityEdita = new Intent(MainActivity.this, EditaContato.class);

                activityEdita.putExtra("Id", contatoSelecionado.getId());
                activityEdita.putExtra("Nome", contatoSelecionado.getNome());
                activityEdita.putExtra("Telefone", contatoSelecionado.getTelefone());

                startActivity(activityEdita);
            }
        });

        ParseQuery<ParseObject> buscaContatos = ParseQuery.getQuery("Lista_Telefonica");
        buscaContatos.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> lista, ParseException e) {
                if (e == null) {

                    Iterator<ParseObject> listaIterator = lista.iterator();

                    while (listaIterator.hasNext()) {
                        ParseObject contatoParse = listaIterator.next();

                        Contato contato = new Contato();
                        contato.setId(contatoParse.getObjectId());
                        contato.setNome(contatoParse.getString("Nome"));
                        contato.setTelefone(contatoParse.getString("Telefone"));

                        listaDeContatos.add(contato);
                    }

                    ArrayAdapter<Contato> contatosAdapter;
                    contatosAdapter = new ArrayAdapter<Contato>(MainActivity.this, R.layout.item_lista_contatos, listaDeContatos);
                    listaView.setAdapter(contatosAdapter);

                } else {
                    System.out.println("ERRO" + e.toString());
                }
            }
        });
    }
}
