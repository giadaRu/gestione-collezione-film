package it.unical.videoteca.domain.filter;

import it.unical.videoteca.domain.entity.Film;

public class FiltroPerAnnoMin implements FiltroFilm {
    private final int annoMin;

    public FiltroPerAnnoMin(int annoMin) {
        this.annoMin = annoMin;
    }

    @Override
    public boolean applica(Film film) {
        return film.getAnno() >= annoMin;
    }
}
