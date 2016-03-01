package br.projeto.agendatelefonica.controller;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.*;
import android.widget.*;

import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.*;

import br.projeto.agendatelefonica.R;
import br.projeto.agendatelefonica.model.Contato;

public class ListarContato extends AppCompatActivity {
    // URL firebase
    private Firebase url = new Firebase("https://minhagendatelefonica.firebaseio.com/");
    private AuthData authData = url.getAuth();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista_contato);

        Toolbar barraMain = (Toolbar) findViewById(R.id.barraMain);
        barraMain.setTitle("Agenda Telefônica");
        barraMain.setSubtitle("Meus Contatos");
        barraMain.setTitleTextColor(getResources().getColor(R.color.colorTextIcon));
        barraMain.setSubtitleTextColor(getResources().getColor(R.color.colorTextIcon));
        barraMain.setLogo(R.mipmap.ic_launcher);
        barraMain.inflateMenu(R.menu.menu_add);
        barraMain.inflateMenu(R.menu.menu_exit);
        barraMain.setContentInsetsAbsolute(5, 5);
        barraMain.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.item_add) {
                    Intent activityCria = new Intent(ListarContato.this, CriaContato.class);
                    startActivity(activityCria);
                }else if (id == R.id.item_exit) {
                    url.unauth();
                    Intent activityLogin = new Intent(ListarContato.this, LoginContato.class);
                    startActivity(activityLogin);
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
        final ArrayAdapter<Contato> contatosAdapter = new ArrayAdapter<Contato>(ListarContato.this, R.layout.item_lista_contatos, listaDeContatos);

        listaView.setAdapter(contatosAdapter);
        atualizaListaContatos(listaDeContatos, "");

        listaView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Contato contatoSelecionado = listaDeContatos.get(position);

                Intent activityEdita = new Intent(ListarContato.this, EditaContato.class);
                activityEdita.putExtra("Id", contatoSelecionado.getId());
                activityEdita.putExtra("Nome", contatoSelecionado.getNome());
                activityEdita.putExtra("Telefone", contatoSelecionado.getTelefone());
                activityEdita.putExtra("Imagem", contatoSelecionado.getImagem());
                startActivity(activityEdita);
            }
        });

        final SearchView buscaContato = (SearchView) findViewById(R.id.buscaContato);
        buscaContato.setSubmitButtonEnabled(true);
        buscaContato.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String contatoNome) {
                if (contatoNome.isEmpty()) {
                    atualizaListaContatos(listaDeContatos, "");
                } else {
                    atualizaListaContatos(listaDeContatos, contatoNome);
                }
                return true;
            }

            @Override
            public boolean onQueryTextSubmit(String contatoNome) {
                atualizaListaContatos(listaDeContatos, contatoNome);
                return true;
            }
        });
    }

    public void atualizaListaContatos(final ArrayList<Contato> listaDeContatos, String contatoNome) {
        url.child("Contatos").orderByChild("idPertence").equalTo(authData.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listaDeContatos.clear();
                System.out.println("Existem " + dataSnapshot.getChildrenCount() + " contatos");
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Contato contato = postSnapshot.getValue(Contato.class);
                    contato.setId(postSnapshot.getKey());
                    listaDeContatos.add(contato);
                }
                // Atualiza a lista
                ListView listaView = (ListView) findViewById(R.id.listView);
                ((BaseAdapter) listaView.getAdapter()).notifyDataSetChanged();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("Erro de leitura do baco: " + firebaseError.getMessage());
            }
        });
    }
}