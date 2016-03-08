package br.projeto.agendatelefonica.controller;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import android.support.v7.widget.Toolbar;

import com.firebase.client.Firebase;
import com.firebase.client.AuthData;

import java.io.ByteArrayOutputStream;

import br.projeto.agendatelefonica.R;
import br.projeto.agendatelefonica.model.Contato;
import de.hdodenhof.circleimageview.CircleImageView;

public class CriaContato extends AppCompatActivity {
    // URL firebase
    private Firebase url = new Firebase("https://minhagendatelefonica.firebaseio.com/");
    private AuthData authData = url.getAuth();

    //Variaveis
    private Bundle pertenceBundle;
    private String pertence;
    private String idPertence;

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

        pertenceBundle = getIntent().getExtras();
        pertence = pertenceBundle.getString("Pertence");
        idPertence = pertenceBundle.getString("idPertence");
    }

    @Override
    protected void onStart(){
        super.onStart();

        // Views
        final EditText nomeEdit = (EditText) findViewById(R.id.nom);
        final EditText telefoneEdit = (EditText) findViewById(R.id.tel);
        final CircleImageView imageView = (CircleImageView) findViewById(R.id.imgFoto);

        Button btSalva = (Button) findViewById(R.id.btSalva);
        btSalva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Contato contato = new Contato();
                String nome = nomeEdit.getText().toString();
                String telefone = telefoneEdit.getText().toString();

                Bitmap imagemBit = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
                byte[] imgByte = converteBitmapParaByte(imagemBit);
                String imgBase64 = Base64.encodeToString(imgByte, Base64.NO_WRAP);

                salvaContato(nome, telefone, imgBase64, idPertence);
                finish();
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // criando a intent
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                // inicia atividade com resposta
                startActivityForResult(intent, 0);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(data != null){
            Bundle bundle = data.getExtras();
            if(bundle != null){
                Bitmap img = (Bitmap) bundle.get("data");
                CircleImageView imageView = (CircleImageView) findViewById(R.id.imgFoto);
                imageView.setImageBitmap(img);
            }
        }
    }

    public byte[] converteBitmapParaByte(Bitmap img){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        img.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    public void salvaContato(String nome, String telefone, String imagem, String idPertence){
        Contato novoContato = new Contato();
        novoContato.setNome(nome);
        novoContato.setTelefone(telefone);
        novoContato.setImagem(imagem);
        novoContato.setIdPertence(idPertence);
        if(pertence.equals("grupo")) {
            url.child("Contatos_Grupo").push().setValue(novoContato);
        }else if(pertence.equals("usuario")) {
            url.child("Contatos").push().setValue(novoContato);
        }
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