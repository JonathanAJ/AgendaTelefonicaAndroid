package br.projeto.agendatelefonica.controller;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.firebase.client.Firebase;

import java.io.ByteArrayOutputStream;

import br.projeto.agendatelefonica.R;
import br.projeto.agendatelefonica.model.Contato;

public class EditaContato extends AppCompatActivity {

    private Firebase url = new Firebase("https://minhagendatelefonica.firebaseio.com/");

    private String id;
    private String nome;
    private String telefone;
    private String imagem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edita_contato);

        Bundle extra = getIntent().getExtras();
        id = extra.getString("Id");
        nome = extra.getString("Nome");
        telefone = extra.getString("Telefone");
        imagem = extra.getString("Imagem");

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
        nomeEdit.setText(nome);
        final EditText telefoneEdit = (EditText) findViewById(R.id.tel);
        telefoneEdit.setText(telefone);
        final ImageView imgEdit = (ImageView) findViewById(R.id.imgFoto);

        byte[] imgByte = Base64.decode(imagem, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(imgByte, 0, imgByte.length);

        imgEdit.setImageBitmap(bitmap);

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