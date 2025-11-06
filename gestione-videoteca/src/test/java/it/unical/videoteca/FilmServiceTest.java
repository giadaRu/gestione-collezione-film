package it.unical.videoteca;

import org.junit.jupiter.api.Test;
import it.unical.videoteca.domain.*;

public class FilmServiceTest {

    @Test
    void testAggiuntaRimozioneRicerca() {
        FilmService service = new FilmService();

        FilmDTO f1 = new FilmDTO("1", "Inception", "Nolan", 2010, "Fantascienza", 5.0, StatoVisione.VISTO);
        FilmDTO f2 = new FilmDTO("2", "Interstellar", "Nolan", 2014, "Fantascienza", 4.5, StatoVisione.DA_VEDERE);
        FilmDTO f3 = new FilmDTO("3", "The Prestige", "Nolan", 2006, "Thriller", 4.8, StatoVisione.IN_VISIONE);

        service.aggiungiFilm(f1);
        service.aggiungiFilm(f2);
        service.aggiungiFilm(f3);

        if (service.trovaTutti().size() != 3)
            throw new RuntimeException("Errore: non sono stati aggiunti correttamente tutti i film");

        if (service.trovaPerId("1") == null)
            throw new RuntimeException("Errore: trovaPerId non ha trovato il film con ID=1");

        if (service.trovaPerGenere("Fantascienza").size() != 2)
            throw new RuntimeException("Errore: ricerca per genere non restituisce 2 film");

        service.rimuoviFilm("2");
        if (service.trovaTutti().size() != 2)
            throw new RuntimeException("Errore: rimuoviFilm non ha rimosso correttamente il film");

        System.out.println("FilmServiceTest: aggiunta, ricerca e rimozione funzionano correttamente");
    }

    @Test
    void testDuplicatiGeneranoErrore() {
        FilmService service = new FilmService();

        FilmDTO f1 = new FilmDTO("A1", "Dunkirk", "Nolan", 2017, "War", 4.0, StatoVisione.VISTO);
        FilmDTO f2 = new FilmDTO("A2", "Dunkirk", "Nolan", 2017, "War", 3.0, StatoVisione.DA_VEDERE);

        service.aggiungiFilm(f1);

        try {
            service.aggiungiFilm(f2);
            throw new RuntimeException("Errore: doveva essere lanciata un'eccezione per film duplicato");
        } catch (IllegalStateException e) {
            System.out.println("Eccezione correttamente lanciata per film duplicato");
        }
    }
}
