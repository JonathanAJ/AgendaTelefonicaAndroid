package br.projeto.agendatelefonica.controller;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.*;
import android.widget.*;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;

import java.util.*;

import br.projeto.agendatelefonica.R;
import br.projeto.agendatelefonica.model.Contato;

public class MainActivity extends AppCompatActivity {

    private Firebase url = new Firebase("https://minhagendatelefonica.firebaseio.com/");

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
    }

    @Override
    protected void onStart(){
        super.onStart();


        final ArrayList<Contato> listaDeContatos = new ArrayList<Contato>();
        final ListView listaView = (ListView) findViewById(R.id.listView);
        final ArrayAdapter<Contato> contatosAdapter = new ArrayAdapter<Contato>(MainActivity.this, R.layout.item_lista_contatos, listaDeContatos);

        listaView.setAdapter(contatosAdapter);
        atualizaListaContatos(listaDeContatos);

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

        final SearchView buscaContato = (SearchView) findViewById(R.id.buscaContato);
        buscaContato.setSubmitButtonEnabled(true);
        buscaContato.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String contatoNome) {
                if(contatoNome.isEmpty()){
                    atualizaListaContatos(listaDeContatos);
                }
                return true;
            }

            @Override
            public boolean onQueryTextSubmit(String contatoNome) {
                procuraContato(listaDeContatos, contatoNome);
                return true;
            }
        });
    }

    public void atualizaListaContatos(final ArrayList<Contato> listaDeContatos){
        listaDeContatos.clear();

        url.child("Contatos").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                System.out.println("Existem " + dataSnapshot.getChildrenCount() + " contatos");
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Contato contato = postSnapshot.getValue(Contato.class);
                    contato.setId(postSnapshot.getKey());
                    listaDeContatos.add(contato);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("Erro de leitura do baco: " + firebaseError.getMessage());
            }
        });
    }

    public void procuraContato(final ArrayList<Contato> listaDeContatos, String contatoNome){
        listaDeContatos.clear();

        Query urlQuery = url.child("Contatos").orderByChild("nome").startAt(contatoNome);
        urlQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Contato contato = dataSnapshot.getValue(Contato.class);
                contato.setId(dataSnapshot.getKey());
                listaDeContatos.add(contato);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }
}
