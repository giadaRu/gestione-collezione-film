package it.unical.videoteca.domain.entity;

import java.util.*;

public class Film {
    private String id;              //viene generato dal repository
    private String titolo;
    private String regista;
    private Integer anno;           //può anche non esserci
    private String genere;
    private Double rating;          //da 1 a 5 (può essere null se non valutato)
    private StatoVisione stato;

    // Costruttori
    public Film() {}

    public Film(String id, String titolo, String regista, Integer anno, String genere, Double rating, StatoVisione stato) {
        this.id = id;
        this.titolo = titolo;
        this.regista = regista;
        this.anno = anno;
        this.genere = genere;
        this.rating = rating;
        this.stato = stato;
    }

    // Getter/Setter
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTitolo() { return titolo; }
    public void setTitolo(String titolo) { this.titolo = titolo; }

    public String getRegista() { return regista; }
    public void setRegista(String regista) { this.regista = regista; }

    public Integer getAnno() { return anno; }
    public void setAnno(Integer anno) { this.anno = anno; }

    public String getGenere() { return genere; }
    public void setGenere(String genere) { this.genere = genere; }

    public Double getRating() { return rating; }
    public void setRating(Double rating) { this.rating = rating; }

    public StatoVisione getStato() { return stato; }
    public void setStato(StatoVisione stato) { this.stato = stato; }

  
    @Override public boolean equals(Object o) {
        if (this == o) 
            return true;
        if (!(o instanceof Film)) 
            return false;
        Film film = (Film) o;
        return Objects.equals(id, film.id);
    }
    
    @Override public int hashCode() { return Objects.hash(id); }

    @Override public String toString() {
        return "Film{" +
                "id='" + id + '\'' +
                ", titolo='" + titolo + '\'' +
                ", regista='" + regista + '\'' +
                ", anno=" + anno +
                ", genere='" + genere + '\'' +
                ", rating=" + rating +
                ", stato=" + stato +
                '}';
    }
}
