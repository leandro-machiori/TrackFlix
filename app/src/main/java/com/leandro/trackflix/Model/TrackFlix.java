package com.leandro.trackflix.Model;
public class TrackFlix {
    private int id;
    private String titulo;
    private String imagemUri;
    private String status;
    private String comentario;
    private float nota;
    public TrackFlix(int id, String titulo, String imagemUri, String status, String comentario, float nota) {
        this.id = id;
        this.titulo = titulo;
        this.imagemUri = imagemUri;
        this.status = status;
        this.comentario = comentario;
        this.nota = nota;
    }
    public TrackFlix() {

    }
    // Getters e Setters
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getTitulo() {
        return titulo;
    }
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
    public String getImagemUri() {
        return imagemUri;
    }
    public void setImagemUri(String imagemUri) {
        this.imagemUri = imagemUri;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getComentario() {
        return comentario;
    }
    public void setComentario(String comentario) {
        this.comentario = comentario;
    }
    public float getNota() {
        return nota;
    }
    public void setNota(float nota) {
        this.nota = nota;
    }
}