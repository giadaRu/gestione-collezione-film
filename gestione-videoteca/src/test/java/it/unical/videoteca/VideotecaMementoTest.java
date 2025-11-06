package it.unical.videoteca;

import org.junit.jupiter.api.Test;
import it.unical.videoteca.domain.dto.*;

public class VideotecaMementoTest {

    @Test
    void testUndoRedo() {
        VideotecaFacade facade = new VideotecaFacade();

        FilmDTO f1 = new FilmDTO("1", "Inception", "Nolan", 2010, "Fantascienza", 5.0, StatoVisione.VISTO);
        FilmDTO f2 = new FilmDTO("2", "Interstellar", "Nolan", 2014, "Fantascienza", 4.8, StatoVisione.DA_VEDERE);
        FilmDTO f3 = new FilmDTO("3", "Dunkirk", "Nolan", 2017, "Guerra", 4.0, StatoVisione.IN_VISIONE);

        facade.aggiungiFilm(f1);
        facade.aggiungiFilm(f2);

        if (facade.listaFilm().size() != 2)
            throw new RuntimeException("Errore: non risultano 2 film aggiunti inizialmente");

        facade.rimuoviFilm("2");

        if (facade.listaFilm().size() != 1)
            throw new RuntimeException("Errore: la rimozione non ha funzionato");

        facade.annullaUltimaOperazione();

        if (facade.listaFilm().size() != 2)
            throw new RuntimeException("Errore: annullaUltimaOperazione non ha ripristinato lo stato precedente");

        System.out.println("VideotecaMementoTest: undo ha ripristinato correttamente lo stato precedente");
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
