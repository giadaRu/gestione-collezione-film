package it.unical.videoteca;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import it.unical.videoteca.domain.facade.*;
import it.unical.videoteca.domain.dto.*;
import it.unical.videoteca.domain.entity.*;

public class TestFinale{

    private VideotecaFacade facade;

    @BeforeEach
    void setUp() {
        // Inizializza la Facade (che inizializza i Service, Factory e Repository)
        // Ad ogni test, l'archivio viene ricaricato dallo stato persistente (CSV)
        facade = new VideotecaFacade();
    }

    //Verifica che la Factory gestisca correttamente input non validi e rating fuori range.
    @Test
    void testValidazioneEInputNonValidi() {

        // Test che la Factory forzi il rating non valido a 0.0
        FilmDTO ratingNonValido = new FilmDTO("ID002", "Film B", "Regista Y", 2010, "Commedia", 6.0, StatoVisione.DA_VEDERE);
        facade.aggiungiFilm(ratingNonValido);
        
        // Verifica che il rating sia stato corretto a 0.0 dal FilmFactoryConcrete
        assertEquals(0.0, facade.cercaPerId("ID002").getRating(), 
                     "Il rating fuori range (>5.0) dovrebbe essere normalizzato a 0.0 dalla Factory.");

        // Test che la Factory assegni DA_VEDERE se lo stato è null
        FilmDTO statoNull = new FilmDTO("ID003", "Film C", "Regista Z", 2020, "Dramma", 3.5, null);
        facade.aggiungiFilm(statoNull);
        assertEquals(StatoVisione.DA_VEDERE, facade.cercaPerId("ID003").getStato(), 
                     "La Factory deve assegnare DA_VEDERE se lo stato è nullo.");
    }



    // Verifica che il FilmService impedisca l'inserimento di film duplicati (stesso Titolo/Regista/Anno).
    @Test
    void testUnicitaFilm() {
        // Film di riferimento
        FilmDTO filmRef = new FilmDTO("ID100", "Il Padrino", "Francis Ford Coppola", 1972, "Dramma", 5.0, StatoVisione.VISTO);
        facade.aggiungiFilm(filmRef);
        
        // Tentativo di aggiungere un duplicato (stessi Titolo, Regista, Anno)
        FilmDTO filmDuplicato = new FilmDTO("ID101", "Il Padrino", "Francis Ford Coppola", 1972, "Dramma", 4.5, StatoVisione.VISTO);
        
        assertThrows(IllegalStateException.class, () -> {
            facade.aggiungiFilm(filmDuplicato);
        }, "L'aggiunta di un film duplicato (stesso Titolo/Regista/Anno) dovrebbe sollevare IllegalStateException.");
    }



    //Verifica l'integrità e la gestione degli errori nel RaccoltaService.
    @Test
    void testRaccolteValide() {
        facade = new VideotecaFacade();

        // ID unici
        String filmIdRichiesto = "F-" + java.util.UUID.randomUUID();
        String raccoltaId      = "R-" + java.util.UUID.randomUUID();
        String nomeRaccolta    = "Cult Fantascienza " + java.util.UUID.randomUUID().toString().substring(0, 8);

        //Inserisco il film 
        ensureFilmAbsent(facade, "Matrix", "Wachowski", 1999);
        facade.aggiungiFilm(new FilmDTO(filmIdRichiesto, "Matrix", "Wachowski", 1999, "Fantascienza", 5.0, StatoVisione.VISTO));
        String idRealeFilm = facade.cercaPerTitolo("Matrix").get(0).getId();

        //Creo la raccolta 
        facade.creaRaccolta(raccoltaId, nomeRaccolta, "I migliori di Fantascienza");

        //Aggiunta valida
        assertDoesNotThrow(() -> facade.aggiungiFilmARaccolta(raccoltaId, idRealeFilm), "L'aggiunta di un film valido alla raccolta non dovrebbe sollevare eccezioni.");
        assertEquals(1, facade.filmDellaRaccolta(raccoltaId).size(), "La raccolta deve contenere 1 film");

        //Fallimento per Film inesistente
        assertThrows(IllegalArgumentException.class,() -> facade.aggiungiFilmARaccolta(raccoltaId, "F999"), "L'aggiunta di un film inesistente dovrebbe fallire (IllegalArgumentException).");

        //Fallimento per Raccolta inesistente
        assertThrows(IllegalArgumentException.class, () -> facade.aggiungiFilmARaccolta("R999", idRealeFilm),  "L'aggiunta a una raccolta inesistente dovrebbe fallire.");

    }

    private void ensureFilmAbsent(VideotecaFacade facade, String titolo, String regista, int anno) {
        facade.cercaPerTitolo(titolo).stream()
          .filter(f -> f.getRegista().equalsIgnoreCase(regista) && f.getAnno() == anno)
          .forEach(f -> facade.rimuoviFilm(f.getId()));
}


}