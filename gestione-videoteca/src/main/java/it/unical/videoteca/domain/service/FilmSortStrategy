package it.unical.videoteca.domain.service;

import java.util.Comparator;
import it.unical.videoteca.domain.entity.Film;

//Per l'ordinamento dei film secondo criteri diversi
public class FilmSortStrategy {

    private FilmSortStrategy() {}

    public enum Criterio { TITOLO, REGISTA, ANNO, RATING }
    public enum Ordine { CRESCENTE, DECRESCENTE }

    public static Comparator<Film> comparator(Criterio criterio, Ordine ordine) {
        Comparator<Film> cmp;

        switch (criterio) {
            case TITOLO:
                cmp = Comparator.comparing(Film::getTitolo, String.CASE_INSENSITIVE_ORDER);
                break;
            case REGISTA:
                cmp = Comparator.comparing(Film::getRegista, String.CASE_INSENSITIVE_ORDER);
                break;
            case ANNO:
                cmp = Comparator.comparingInt(Film::getAnno);
                break;
            case RATING:
                cmp = Comparator.comparingDouble(Film::getRating);
                break;
            default:
                cmp = Comparator.comparing(Film::getTitolo, String.CASE_INSENSITIVE_ORDER);
        }

        if (ordine == Ordine.DECRESCENTE)
            cmp = cmp.reversed();

        return cmp;
    }
}
