package br.projeto.agendatelefonica.controller;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.*;
import android.view.*;
import android.widget.*;
import android.widget.TabHost.TabSpec;

import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.melnykov.fab.FloatingActionButton;

import java.util.*;

import br.projeto.agendatelefonica.R;
import br.projeto.agendatelefonica.model.Contato;
import br.projeto.agendatelefonica.model.Grupo;

public class ListarContato extends AppCompatActivity {
    // URL firebase
    private Firebase url = new Firebase("https://minhagendatelefonica.firebaseio.com/");
    private AuthData authData = url.getAuth();

    // Variáveis
    private FloatingActionButton btFloat;
    private TabHost tabHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista_contato);

        Toolbar barraMain = (Toolbar) findViewById(R.id.barraMain);
        barraMain.setTitle("Agenda Telefônica");
        barraMain.setTitleTextColor(getResources().getColor(R.color.colorTextIcon));
        barraMain.setSubtitleTextColor(getResources().getColor(R.color.colorTextIcon));
        barraMain.setLogo(R.mipmap.ic_launcher);
        barraMain.inflateMenu(R.menu.menu_add);
        barraMain.inflateMenu(R.menu.menu_add_group);
        barraMain.inflateMenu(R.menu.menu_exit);
        barraMain.setContentInsetsAbsolute(5, 5);
        barraMain.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.item_add) {
                    chamaCriaContato();
                } else if(id == R.id.item_add_group){
                    Intent activityCria = new Intent(getApplicationContext(), CriarGrupo.class);
                    startActivity(activityCria);
                } else if (id == R.id.item_exit) {
                    url.unauth();
                    Intent activityLogin = new Intent(getApplicationContext(), LoginContato.class);
                    startActivity(activityLogin);
                }
                return true;
            }
        });


        tabHost = (TabHost) findViewById(R.id.tabHost);
        tabHost.setup();

        TabSpec spec1 = tabHost.newTabSpec("TAB 1");
        spec1.setContent(R.id.tab1);
        spec1.setIndicator("Meus Contatos");

        TabSpec spec2=tabHost.newTabSpec("TAB 2");
        spec2.setContent(R.id.tab2);
        spec2.setIndicator("Meus Grupos");

        tabHost.addTab(spec1);
        tabHost.addTab(spec2);

        btFloat = (FloatingActionButton) findViewById(R.id.btFloat);
        btFloat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i = tabHost.getCurrentTab();
                if(i == 0){
                    chamaCriaContato();
                }else if(i == 1){
                    Intent activityCria = new Intent(getApplicationContext(), CriarGrupo.class);
                    startActivity(activityCria);
                }
            }
        });
    }

    @Override
    protected void onStart(){
        super.onStart();
        /*
         * Lista de Meus Contatos
         */
        final ArrayList<Contato> listaDeContatos = new ArrayList<Contato>();
        final ListView listaView = (ListView) findViewById(R.id.listView);

        atualizaListaContatos(listaDeContatos, "");
        listaView.setAdapter(new BaseAdapterLista(this, listaDeContatos, "contato"));

        listaView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Contato contatoSelecionado = listaDeContatos.get(position);
                chamaEditaContato(contatoSelecionado);
            }
        });
        /*
         * Lista de Meus Grupos
         */
        final ArrayList<Grupo> listaDeGrupos = new ArrayList<Grupo>();
        final ListView listaViewGrupo = (ListView) findViewById(R.id.listViewGrupo);

        atualizaListaGrupos(listaDeGrupos, "");
        listaViewGrupo.setAdapter(new BaseAdapterLista(this, listaDeGrupos, "grupo"));

        listaViewGrupo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Grupo grupoSelecionado = listaDeGrupos.get(position);
                chamaGrupo(grupoSelecionado);
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
                Log.e("Erro no banco", firebaseError.getMessage());
            }
        });
    }

    public void atualizaListaGrupos(final ArrayList<Grupo> listaDeGrupos, String contatoNome) {
        url.child("Usuarios").child(authData.getUid()).child("grupos").orderByKey().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listaDeGrupos.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Log.i("DataSnapshot1", getLocalClassName() + " > " + postSnapshot.getKey());
                    url.child("Grupos").orderByKey().equalTo(postSnapshot.getKey()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                Log.i("DataSnapshot2", getLocalClassName() + " > " + postSnapshot.child("nome").getValue().toString());
                                Grupo grupo = new Grupo();
                                grupo.setId(postSnapshot.getKey());
                                grupo.setNome(postSnapshot.child("nome").getValue().toString());
                                grupo.setImagem(postSnapshot.child("imagem").getValue().toString());
                                listaDeGrupos.add(grupo);
                            }
                            // Atualiza a lista
                            ListView listaView = (ListView) findViewById(R.id.listViewGrupo);
                            ((BaseAdapter) listaView.getAdapter()).notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {
                            Log.e("Erro no banco", firebaseError.getMessage());
                        }
                    });
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.e("Erro no banco", firebaseError.getMessage());
            }
        });
    }

    public void chamaCriaContato(){
        Intent activityCria = new Intent(getApplicationContext(), CriaContato.class);
        activityCria.putExtra("Pertence", "usuario");
        activityCria.putExtra("idPertence", authData.getUid());
        startActivity(activityCria);
    }

    public void chamaEditaContato(Contato contatoSelecionado){
        Intent activityEdita = new Intent(getApplicationContext(), EditaContato.class);
        activityEdita.putExtra("Id", contatoSelecionado.getId());
        activityEdita.putExtra("Nome", contatoSelecionado.getNome());
        activityEdita.putExtra("Telefone", contatoSelecionado.getTelefone());
        activityEdita.putExtra("Imagem", contatoSelecionado.getImagem());
        activityEdita.putExtra("Pertence", "usuario");
        activityEdita.putExtra("idPertence", authData.getUid());
        startActivity(activityEdita);
    }

    public void chamaGrupo(Grupo grupoSelecionado){
        Intent activityLista = new Intent(getApplicationContext(), ListarContatosGrupos.class);
        activityLista.putExtra("Id", grupoSelecionado.getId());
        activityLista.putExtra("Nome", grupoSelecionado.getNome());
        activityLista.putExtra("Imagem", grupoSelecionado.getImagem());
        startActivity(activityLista);
    }
}
