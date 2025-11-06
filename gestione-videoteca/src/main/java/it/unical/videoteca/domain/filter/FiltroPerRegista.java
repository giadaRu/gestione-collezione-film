package it.unical.videoteca.domain.filter;

import it.unical.videoteca.domain.entity.Film;

public class FiltroPerRegista implements FiltroFilm {
    private final String regista;

    public FiltroPerRegista(String regista) {
        this.regista = regista;
    }

    @Override
    public boolean applica(Film film) {
        return film.getRegista() != null && film.getRegista().equalsIgnoreCase(regista);
    }
}
