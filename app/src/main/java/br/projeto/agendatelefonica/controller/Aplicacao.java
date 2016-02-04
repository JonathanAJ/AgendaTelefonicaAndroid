package br.projeto.agendatelefonica.controller;

import com.firebase.client.Firebase;

public class Aplicacao extends android.app.Application {
    @Override
    public void onCreate(){
        super.onCreate();
        Firebase.setAndroidContext(this);
        Firebase.getDefaultConfig().setPersistenceEnabled(true);
    }
}