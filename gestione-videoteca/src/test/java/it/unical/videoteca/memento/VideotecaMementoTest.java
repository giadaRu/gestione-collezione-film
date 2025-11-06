package it.unical.videoteca.memento;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import it.unical.videoteca.domain.dto.*;
import it.unical.videoteca.domain.entity.*;
import it.unical.videoteca.domain.facade.*;

public class VideotecaMementoTest {
    
    @BeforeEach
    void cleanPersistence() throws Exception {Files.deleteIfExists(Path.of("videoteca_data.csv"));}


    @Test
    void testUndo() {
        VideotecaFacade facade = new VideotecaFacade();

        FilmDTO f1 = new FilmDTO("1", "Inception", "Nolan", 2010, "Fantascienza", 5.0, StatoVisione.VISTO);
        FilmDTO f2 = new FilmDTO("2", "Interstellar", "Nolan", 2014, "Fantascienza", 4.8, StatoVisione.DA_VEDERE);
        FilmDTO f3 = new FilmDTO("3", "Dunkirk", "Nolan", 2017, "Guerra", 4.0, StatoVisione.IN_VISIONE);

        facade.aggiungiFilm(f1);
        facade.aggiungiFilm(f2);

        assertEquals(2, facade.listaFilm().size(), "Dopo due aggiunte, size deve essere 2");

        // faccio una terza aggiunta
        facade.aggiungiFilm(f3);
        assertEquals(3, facade.listaFilm().size(), "Dopo terza aggiunta, size deve essere 3");

        //UNDO: deve tornare allo stato precedente (2 elementi)
        facade.annullaUltimaOperazione();
        assertEquals(2, facade.listaFilm().size(), "Undo non ha ripristinato size=2");

    }

    @Test
    void testUndoSenzaOperazioni() { 
        VideotecaFacade facade = new VideotecaFacade();

        try {
            facade.annullaUltimaOperazione();
            System.out.println("Nessuna eccezione lanciata: nessuno stato precedente da ripristinare");
        } catch (Exception e) {
            throw new RuntimeException("Errore: non dovrebbe lanciare eccezione se non ci sono stati salvati");
        }
    }
}
