package br.projeto.agendatelefonica.model;

import java.util.List;

public class Grupo {
    private String id;
    private String nome;
    private String imagem;
    private List<String> membros;
    private List<String> contatos;

    public Grupo() {
    }

    public Grupo(String id, String nome, String imagem) {
        this.id = id;
        this.nome = nome;
        this.imagem = imagem;
        this.membros = membros;
        this.contatos = contatos;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getImagem() {
        return imagem;
    }

    public void setImagem(String imagem) {
        this.imagem = imagem;
    }

    public List<String> getMembros() {
        return membros;
    }

    public void setMembros(List<String> membros) {
        this.membros = membros;
    }

    public List<String> getContatos() {
        return contatos;
    }

    public void setContatos(List<String> contatos) {
        this.contatos = contatos;
    }

    @Override
    public String toString() {
        return getNome();
    }
}
