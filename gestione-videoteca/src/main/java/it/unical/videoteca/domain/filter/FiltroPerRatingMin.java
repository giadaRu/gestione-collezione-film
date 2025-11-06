package it.unical.videoteca.domain.filter;

import it.unical.videoteca.domain.entity.Film;

public class FiltroPerRatingMin implements FiltroFilm {
    private final double ratingMin;

    public FiltroPerRatingMin(double ratingMin) {
        this.ratingMin = ratingMin;
    }

    @Override
    public boolean applica(Film film) {
        return film.getRating() >= ratingMin;
    }
}
