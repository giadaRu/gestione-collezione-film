package it.unical.videoteca.dto;

import org.junit.jupiter.api.Test;

import it.unical.videoteca.domain.dto.FilmDTO;
import it.unical.videoteca.domain.entity.*;

public class FilmDTOTest {

    @Test
    void testConversione() {
        Film film = new Film("ID-9", "Before Sunrise", "Richard Linklater", 1995,
                "Drammatico", 4.5, StatoVisione.VISTO);

        FilmDTO dto = FilmDTO.from(film);
        Film back = dto.toEntity();

        
        if (!dto.getTitolo().equals("Before Sunrise"))
            throw new RuntimeException("Errore nel titolo in DTO");

        if (!back.getRegista().equals("Richard Linklater"))
            throw new RuntimeException("Errore nella conversione regista DTO→Entity");

        if (back.getAnno() != 1995)
            throw new RuntimeException("Errore nell'anno in DTO→Entity");

        if (back.getStato() != StatoVisione.VISTO)
            throw new RuntimeException("Errore nello stato in DTO→Entity");

        //se arrivo qui, è tutto ok (test superato)
        System.out.println("FilmDTOTest completato correttamente");
    }
}
