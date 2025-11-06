package it.unical.videoteca.domain.factory;

import it.unical.videoteca.domain.*;

public interface FilmFactory {

    //Per convertire un FilmDTO in un'entità Film
    Film fromDTO(FilmDTO dto);
}
