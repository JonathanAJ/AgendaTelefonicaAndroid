package br.projeto.agendatelefonica.model;

public class Contato {
    private String id;
    private String nome;
    private String telefone;
    private String imagem;

    public Contato(){
    }

    public Contato(String id, String nome, String telefone, String imagem) {
        this.id = id;
        this.nome = nome;
        this.telefone = telefone;
        this.imagem = imagem;
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

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getImagem() {
        return imagem;
    }

    public void setImagem(String imagem) {
        this.imagem = imagem;
    }

    @Override
    public String toString() {
        return getNome();
    }
}
