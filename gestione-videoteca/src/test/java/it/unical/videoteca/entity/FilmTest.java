package it.unical.videoteca.entity;

import org.junit.jupiter.api.Test;
import it.unical.videoteca.domain.entity.*;

public class FilmTest {

    @Test
    void testBase() {
        Film f = new Film();
        f.setId("X");
        f.setTitolo("Il favoloso mondo di Amelie");
        f.setRegista("JeanPierre Jeunet");
        f.setAnno(2001);
        f.setGenere("Commedia");
        f.setRating(4.0);
        f.setStato(StatoVisione.IN_VISIONE);

        if (f.getTitolo() == null || f.getTitolo().isBlank())
            throw new RuntimeException("Titolo non impostato correttamente");

        if (f.getRating() < 0 || f.getRating() > 5)
            throw new RuntimeException("Rating fuori range");

        if (!f.getRegista().contains("Jeunet"))
            throw new RuntimeException("Regista errato");

        if (f.getAnno() != 2001)
            throw new RuntimeException("Anno non valido");

        //non è stata sollevata alcuna eccezione, quindi è tutto ok
        System.out.println("FilmTest completato con successo");
    }
}

