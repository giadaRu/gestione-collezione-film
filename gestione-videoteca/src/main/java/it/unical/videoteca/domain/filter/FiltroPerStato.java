package it.unical.videoteca.domain.filter;

import it.unical.videoteca.domain.entity.*;

public class FiltroPerStato implements FiltroFilm {
    private final StatoVisione stato;

    public FiltroPerStato(StatoVisione stato) {
        this.stato = stato;
    }

    @Override
    public boolean applica(Film film) {
        return film.getStato() == stato;
    }
}
