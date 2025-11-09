package it.unical.videoteca.domain.filter;

import it.unical.videoteca.domain.entity.Film;

public class FiltroPerRatingMax implements FiltroFilm {
    private final double ratingMax;

    public FiltroPerRatingMax(double ratingMax) {
        this.ratingMax = ratingMax;
    }

    @Override
    public boolean applica(Film film) {
        return film.getRating() <= ratingMax && film.getRating()!=null;
    }
}
