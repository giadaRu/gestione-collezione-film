package it.unical.videoteca.domain.filter;

import it.unical.videoteca.domain.entity.Film;

//Strategy: definisce l'interfaccia comune per tutti i filtri

public interface FiltroFilm {
    boolean applica(Film film);
}
