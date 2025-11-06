package it.unical.videoteca.facade;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import it.unical.videoteca.domain.dto.FilmDTO;
import it.unical.videoteca.domain.entity.StatoVisione;
import it.unical.videoteca.domain.facade.VideotecaFacade;

public class VideotecaFacadeTest {

    @BeforeEach
    void cleanPersistence() throws Exception {Files.deleteIfExists(Path.of("videoteca_data.csv"));}

    @Test
    //per verificare se aggiunta, rimozione e ricerca (sia per id sia per genere) di un film funzionano correttamente
    void testAggiungiRimuovi() {
        VideotecaFacade facade = new VideotecaFacade();

        //aggiungo due film non duplicati
        FilmDTO f1 = new FilmDTO("1", "Inception", "Christopher Nolan", 2010, "Fantascienza", 5.0, StatoVisione.VISTO);
        FilmDTO f2 = new FilmDTO("2", "Interstellar", "Christopher Nolan", 2014, "Fantascienza", 4.8, StatoVisione.DA_VEDERE);

        //non devono lanciare eccezioni in inserimento
        assertDoesNotThrow(() -> facade.aggiungiFilm(f1));
        assertDoesNotThrow(() -> facade.aggiungiFilm(f2));

        //dopo 2 insert devono esserci 2 film
        assertEquals(2, facade.listaFilm().size(), "Dopo due inserimenti la lista deve avere size=2");

        //ne rimuovo uno
        assertDoesNotThrow(() -> facade.rimuoviFilm(
                facade.cercaPerTitolo("Inception").get(0).getId()));

        //deve rimanere 1
        assertEquals(1, facade.listaFilm().size(), "Dopo la rimozione deve restare size=1");
    }

    @Test
    //per verificare se un duplicato genera un errore (dovrebbe farlo) -> la facade deve bloccare i duplicati
    void testDuplicato() {
        VideotecaFacade facade = new VideotecaFacade();

        FilmDTO f1 = new FilmDTO("A1", "Dunkirk", "Christopher Nolan", 2017, "Guerra", 4.5, StatoVisione.VISTO);
        FilmDTO f2 = new FilmDTO("A2", "Dunkirk", "Christopher Nolan", 2017, "Guerra", 3.0, StatoVisione.DA_VEDERE);

        facade.aggiungiFilm(f1);

        try {
            facade.aggiungiFilm(f2);
            throw new RuntimeException("Errore: doveva essere lanciata un'eccezione per film duplicato");
        } catch (IllegalStateException e) {
            System.out.println("Eccezione correttamente lanciata per film duplicato");
        }
    }
}
