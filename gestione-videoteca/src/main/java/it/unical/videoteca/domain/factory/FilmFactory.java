package it.unical.videoteca.domain.factory;

import it.unical.videoteca.domain.dto.FilmDTO;
import it.unical.videoteca.domain.entity.Film;

public interface FilmFactory {

    //Per convertire un FilmDTO in un'entità Film
    Film fromDTO(FilmDTO dto);
}
