package br.projeto.agendatelefonica.controller;

import android.widget.Toast;
import android.content.Context;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

/**
 * Created by jonathan on 01/03/16.
 */
public class Util {

    // URL firebase de conexao
    private static Firebase conexao = new Firebase("https://minhagendatelefonica.firebaseio.com/.info/connected");

    public static void listenerConnection(final Context context){
        conexao.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                boolean connected = snapshot.getValue(Boolean.class);
                if (connected) {
                    mensagem(context, "Conectado!");
                } else {
                    mensagem(context, "Desconectado!");
                }
            }

            @Override
            public void onCancelled(FirebaseError error) {
                mensagem(context, "Listener foi cancelado!!");
            }
        });
    }

    public static void mensagem(Context context, String msg){
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }
}
