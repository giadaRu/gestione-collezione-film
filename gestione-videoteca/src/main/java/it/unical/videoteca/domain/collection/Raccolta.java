package it.unical.videoteca.domain.collection;

import java.util.ArrayList;
import java.util.List;

//Rappresenta una raccolta tematica personalizzata di film.

public class Raccolta {
    private final String id;
    private String nome;
    private String descrizione;
    private final List<String> filmIds = new ArrayList<>();

    public Raccolta(String id, String nome, String descrizione) {
        if (id == null || id.isBlank()) 
            throw new IllegalArgumentException("id raccolta non valido");
        if (nome == null || nome.isBlank()) 
            throw new IllegalArgumentException("nome raccolta non valido");
        this.id = id;
        this.nome = nome;
        this.descrizione = (descrizione == null ? "" : descrizione);
    }

    public String getId() { return id; }
    public String getNome() { return nome; }
    public String getDescrizione() { return descrizione; }
    public List<String> getFilmIds() { return new ArrayList<>(filmIds); }

    public void setNome(String nome) {
        if (nome == null || nome.isBlank()) 
            throw new IllegalArgumentException("nome raccolta non valido");
        this.nome = nome;
    }

    public void setDescrizione(String d) { this.descrizione = (d == null ? "" : d); }

    public void addFilm(String filmId) {
        if (filmId != null && !filmId.isBlank() && !filmIds.contains(filmId)) {
            filmIds.add(filmId);
        }
    }

    public void removeFilm(String filmId) {
        filmIds.remove(filmId);
    }

    public boolean contiene(String filmId) { return filmIds.contains(filmId); }
    public int size() { return filmIds.size(); }
}
