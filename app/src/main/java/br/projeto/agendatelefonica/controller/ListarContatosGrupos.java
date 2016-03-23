package br.projeto.agendatelefonica.controller;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;

import br.projeto.agendatelefonica.R;
import br.projeto.agendatelefonica.model.Contato;

public class ListarContatosGrupos extends AppCompatActivity {
    // URL firebase
    private Firebase url = new Firebase("https://minhagendatelefonica.firebaseio.com/");
    private AuthData authData = url.getAuth();

    // Variaveis que recebem os extras
    private String id;
    private String nome;
    private String imagem;

    // Views
    private FloatingActionButton btFloat;

    final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista_contatos_grupos);

        Bundle contatos = getIntent().getExtras();
        id  = contatos.getString("Id");
        nome  = contatos.getString("Nome");
        imagem = contatos.getString("Imagem");

        // decodifica base64 para byte
        byte[] imgByte = Base64.decode(imagem, Base64.DEFAULT);
        // decodifica byte para Bitmap
        Bitmap bitmap = BitmapFactory.decodeByteArray(imgByte, 0, imgByte.length);
        // converte bitmap para drawable
        Drawable logo = new BitmapDrawable(getResources(), bitmap);

        Toolbar barraMain = (Toolbar) findViewById(R.id.barraMain);
        barraMain.setTitle("Agenda Telefônica");
        barraMain.setSubtitle("Grupo " + nome);
        barraMain.setTitleTextColor(getResources().getColor(R.color.colorTextIcon));
        barraMain.setSubtitleTextColor(getResources().getColor(R.color.colorTextIcon));
        barraMain.setLogo(R.mipmap.ic_launcher);
        barraMain.inflateMenu(R.menu.menu_add_member_group);
        barraMain.setContentInsetsAbsolute(5, 5);
        barraMain.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.item_add_member) {
                    chamaDialogEmailMember();
                }
                return true;
            }
        });

        btFloat = (FloatingActionButton) findViewById(R.id.btFloatContatos);
        btFloat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chamaCriaContato();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        /*
         * Lista de Meus Contatos
         */
        final ArrayList<Contato> listaDeContatos = new ArrayList<Contato>();
        final ListView listaView = (ListView) findViewById(R.id.listViewGrupoContatos);
        atualizaListaContatosGrupos(listaDeContatos, "", id);

        listaView.setAdapter(new BaseAdapterLista(this, listaDeContatos, "contato"));

        listaView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Contato contatoSelecionado = listaDeContatos.get(position);
                chamaEditaContato(contatoSelecionado);
            }
        });
    }

    public void atualizaListaContatosGrupos(final ArrayList<Contato> listaDeContatos, String contato, String idGrupo){
        url.child("Contatos_Grupo").orderByChild("idPertence").equalTo(idGrupo).addValueEventListener(new ValueEventListener() {
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
                ListView listaView = (ListView) findViewById(R.id.listViewGrupoContatos);
                ((BaseAdapter) listaView.getAdapter()).notifyDataSetChanged();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.e("Erro no banco", firebaseError.getMessage());
            }
        });
    }

    public void chamaCriaContato(){
        Intent activityCria = new Intent(getApplicationContext(), CriaContato.class);
        activityCria.putExtra("Pertence", "grupo");
        activityCria.putExtra("idPertence", id);
        startActivity(activityCria);
    }

    public void chamaEditaContato(Contato contatoSelecionado){
        Intent activityEdita = new Intent(getApplicationContext(), EditaContato.class);
        activityEdita.putExtra("Id", contatoSelecionado.getId());
        activityEdita.putExtra("Nome", contatoSelecionado.getNome());
        activityEdita.putExtra("Telefone", contatoSelecionado.getTelefone());
        activityEdita.putExtra("Imagem", contatoSelecionado.getImagem());
        activityEdita.putExtra("Pertence", "grupo");
        activityEdita.putExtra("idPertence", id);
        startActivity(activityEdita);
    }

    public void chamaDialogEmailMember(){
        Log.i("ADD MEMBER", "OK");
        // get prompts.xml view
        LayoutInflater li = LayoutInflater.from(context);
        View promptsView = li.inflate(R.layout.email_member_dialog, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);

        final EditText userInput = (EditText) promptsView.findViewById(R.id.emailMember);

        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("Adicionar",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                adicionaMembro(userInput.getText().toString());
                            }
                        })
                .setNegativeButton("Cancelar",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    public void adicionaMembro(String emailMembro){
        url.child("Usuarios").orderByChild("email").equalTo(emailMembro).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    String idUsuario = postSnapshot.getKey();
                    url.child("Grupos").child(id).child("membros").child(idUsuario).setValue("true");
                    url.child("Usuarios").child(idUsuario).child("grupos").child(id).setValue("true");
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.e("Erro no banco", firebaseError.getMessage());
            }
        });

    }
}
