package br.projeto.agendatelefonica.controller;

import android.content.Intent;
import android.graphics.Bitmap;
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
import java.util.HashMap;
import java.util.Map;

import br.projeto.agendatelefonica.R;
import br.projeto.agendatelefonica.model.Contato;
import de.hdodenhof.circleimageview.CircleImageView;

public class CriarGrupo extends AppCompatActivity {
    // URL firebase
    private Firebase url = new Firebase("https://minhagendatelefonica.firebaseio.com/");
    private AuthData authData = url.getAuth();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.criar_grupo);

        Toolbar barraMain = (Toolbar) findViewById(R.id.barraMain);
        barraMain.setTitle("Agenda Telefônica");
        barraMain.setSubtitle("Criar Grupo");
        barraMain.setTitleTextColor(getResources().getColor(R.color.colorTextIcon));
        barraMain.setSubtitleTextColor(getResources().getColor(R.color.colorTextIcon));
        barraMain.setLogo(R.mipmap.ic_launcher);
        barraMain.setContentInsetsAbsolute(5, 5);
        setSupportActionBar(barraMain);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onStart(){
        super.onStart();

        // Views
        final EditText nomeGrupo = (EditText) findViewById(R.id.nomeGrupo);
        final CircleImageView fotoGrupo = (CircleImageView) findViewById(R.id.imgFotoGrupo);

        Button btSalvaGrupo = (Button) findViewById(R.id.btSalvaGrupo);
        btSalvaGrupo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nome = nomeGrupo.getText().toString();

                Bitmap imagemBit = ((BitmapDrawable) fotoGrupo.getDrawable()).getBitmap();
                byte[] imgByte = converteBitmapParaByte(imagemBit);
                String imgBase64 = Base64.encodeToString(imgByte, Base64.NO_WRAP);

                salvaGrupo(nome, imgBase64, authData.getUid());
                finish();
            }
        });

        fotoGrupo.setOnClickListener(new View.OnClickListener() {
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
                CircleImageView imageView = (CircleImageView) findViewById(R.id.imgFotoGrupo);
                imageView.setImageBitmap(img);
            }
        }
    }

    public byte[] converteBitmapParaByte(Bitmap img){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        img.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    public void salvaGrupo(String nome, String imagem, String membro){
        Firebase novoGrupo = url.child("Grupos").push();
        Map<String, String> map = new HashMap<String, String>();

        // Cria grupo
        map.put("nome", nome);
        map.put("imagem", imagem);
        novoGrupo.setValue(map);
        map.clear();

        // Adiciona membro no Grupo
        String grupoId = novoGrupo.getKey();
        map.put(membro, "true");
        url.child("Grupos").child(grupoId).child("membros").setValue(map);

        // Adiciona grupo ao membro
        url.child("Usuarios").child(authData.getUid()).child("grupos").child(grupoId).setValue("true");
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
