package br.projeto.agendatelefonica.controller;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;

import java.io.ByteArrayOutputStream;

import br.projeto.agendatelefonica.R;
import br.projeto.agendatelefonica.model.Contato;
import de.hdodenhof.circleimageview.CircleImageView;

public class EditaContato extends AppCompatActivity {
    // URL firebase
    private Firebase url = new Firebase("https://minhagendatelefonica.firebaseio.com/");
    private AuthData authData = url.getAuth();

    // Variaveis que recebem os extras
    private String id;
    private String nome;
    private String telefone;
    private String imagem;
    private String pertence;
    private String idPertence;

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

        // Views
        final EditText nomeEdit = (EditText) findViewById(R.id.nom);
        final EditText telefoneEdit = (EditText) findViewById(R.id.tel);
        final CircleImageView imageView = (CircleImageView) findViewById(R.id.imgFoto);

        // Retorna os valores passados na lista
        Bundle extra = getIntent().getExtras();
        id = extra.getString("Id");
        nome = extra.getString("Nome");
        telefone = extra.getString("Telefone");
        imagem = extra.getString("Imagem");
        pertence = extra.getString("Pertence");
        idPertence = extra.getString("idPertence");

        // Seta os valores passados
        nomeEdit.setText(nome);
        telefoneEdit.setText(telefone);
        //decodifica base64 para byte
        byte[] imgByte = Base64.decode(imagem, Base64.DEFAULT);
        //decodifica byte para Bitmap
        Bitmap bitmap = BitmapFactory.decodeByteArray(imgByte, 0, imgByte.length);
        //seta o Bitmap
        imageView.setImageBitmap(bitmap);
    }

    @Override
    protected void onStart(){
        super.onStart();

        // Views
        final EditText nomeEdit = (EditText) findViewById(R.id.nom);
        final EditText telefoneEdit = (EditText) findViewById(R.id.tel);
        final CircleImageView imageView = (CircleImageView) findViewById(R.id.imgFoto);
        final Button btSalva = (Button) findViewById(R.id.btSalva);
        final Button btDeleta = (Button) findViewById(R.id.btDeletaContato);

        btSalva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nome = nomeEdit.getText().toString();
                String telefone = telefoneEdit.getText().toString();

                Bitmap imagemBit = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
                byte[] imgByte = converteBitmapParaByte(imagemBit);
                String imgBase64 = Base64.encodeToString(imgByte, Base64.NO_WRAP);

                editaContato(nome, telefone, imgBase64, idPertence);
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

        btDeleta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editaContato(null, null, null, null);
                finish();
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

    public void editaContato(String nome, String telefone, String imagem, String idPertence){
        Contato novoContato = new Contato();
        novoContato.setNome(nome);
        novoContato.setTelefone(telefone);
        novoContato.setImagem(imagem);
        novoContato.setIdPertence(idPertence);
        if(pertence.equals("grupo")) {
            url.child("Contatos_Grupo").child(this.id).setValue(novoContato);
        }else if(pertence.equals("usuario")) {
            url.child("Contatos").child(this.id).setValue(novoContato);
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