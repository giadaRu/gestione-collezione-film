package it.unical.videoteca.collection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import it.unical.videoteca.domain.facade.*;
import it.unical.videoteca.domain.dto.*;
import it.unical.videoteca.domain.entity.*;

public class RaccoltaTest {

    @BeforeEach
    void resetPersistenza() throws Exception{Files.deleteIfExists(Path.of("videoteca_data.csv"));}

    @Test
    void testAggiornaRaccolte() {
        VideotecaFacade facade = new VideotecaFacade();
        
    //inserisco i film
    facade.aggiungiFilm(new FilmDTO("F1", "Inception", "Nolan", 2010, "Fantascienza", 5.0, StatoVisione.VISTO));
    facade.aggiungiFilm(new FilmDTO("F2", "Tenet", "Nolan", 2020, "Fantascienza", 4.5, StatoVisione.IN_VISIONE));

    //recupero gli id realmente salvati
    String idInception = facade.cercaPerTitolo("Inception").get(0).getId();
    String idTenet = facade.cercaPerTitolo("Tenet").get(0).getId();

    //elimino un'eventuale raccolta con lo stesso nome
    facade.tutteLeRaccolte().stream()
            .filter(r -> r.getNome().equalsIgnoreCase("Preferiti Nolan"))
            .forEach(r -> facade.eliminaRaccolta(r.getId()));

    //creo una raccolta con ID/NOME unici (per evitare conflitti tra i test)
    String rid   = "R-" + UUID.randomUUID();
    String rname = "Preferiti Nolan " + rid.substring(2, 8);
    facade.creaRaccolta(rid, rname, "Film personali");

    //aggiungo i film usando gli id reali
    facade.aggiungiFilmARaccolta(rid, idInception);
    facade.aggiungiFilmARaccolta(rid, idTenet);

    assertEquals(2, facade.filmDellaRaccolta(rid).size(), "La raccolta deve contenere 2 film");

    //rimozione dal catalogo → Observer deve aggiornare la raccolta
    facade.rimuoviFilm(idInception);
    var titoli = facade.filmDellaRaccolta(rid).stream().map(f -> f.getTitolo()).collect(Collectors.toList());
    assertEquals(List.of("Tenet"), titoli, "Dopo la rimozione deve restare solo Tenet");
    

    }
}
