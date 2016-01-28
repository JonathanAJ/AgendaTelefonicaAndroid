package br.projeto.agendatelefonica.controller;

import com.parse.*;

public class Aplicacao extends android.app.Application {
    @Override
    public void onCreate(){
        super.onCreate();

        Parse.initialize(this, "RNihJPcSLheuubmCXlptrimTCStC5T4j3d0nzDYu", "H9MMOZZ6IPTuRpwdAHwhD0VrakQkc2Tr5g7wmwIs");
    }
}