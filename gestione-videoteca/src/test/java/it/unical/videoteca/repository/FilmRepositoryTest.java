package it.unical.videoteca.repository;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import it.unical.videoteca.domain.entity.*;
import it.unical.videoteca.domain.repository.*;


public class FilmRepositoryTest {

    @BeforeEach
    void cleanPersistence() throws Exception {Files.deleteIfExists(Path.of("videoteca_data.csv"));}


    @Test
    void testAggiuntaFilm() {
        FilmRepositoryConcrete repo = new FilmRepositoryConcrete();

        Film film1 = new Film("ID1", "Inception", "Nolan", 2010, "Fantascienza", 5.0, StatoVisione.VISTO);
        Film film2 = new Film("ID2", "Interstellar", "Nolan", 2014, "Fantascienza", 4.5, StatoVisione.DA_VEDERE);

        repo.save(film1);
        repo.save(film2);

        if (repo.findAll().size() != 2)
            throw new RuntimeException("Errore: non ci sono 2 film nell'archivio");

        if (!repo.exists("Inception", "Nolan", 2010))
            throw new RuntimeException("Errore: Inception non trovato nel repository");

        Film trovato = repo.findById("ID1");
        if (trovato == null || !trovato.getTitolo().equals("Inception"))
            throw new RuntimeException("Errore: findById non funziona correttamente");

        repo.delete("ID2");
        if (repo.findAll().size() != 1)
            throw new RuntimeException("Errore: delete non ha rimosso il film");

        System.out.println(" FilmRepositoryTest: tutti i controlli passati con successo");
    }

    @Test
    void testDuplicatoFilm() {
        FilmRepositoryConcrete repo = new FilmRepositoryConcrete();

        Film f1 = new Film("X1", "Dunkirk", "Nolan", 2017, "War", 4.0, StatoVisione.VISTO);
        Film f2 = new Film("X2", "Dunkirk", "Nolan", 2017, "War", 3.5, StatoVisione.DA_VEDERE);

        assertDoesNotThrow(()->repo.save(f1));
        assertThrows(IllegalArgumentException.class, () -> repo.save(f2));
        
    }
}
