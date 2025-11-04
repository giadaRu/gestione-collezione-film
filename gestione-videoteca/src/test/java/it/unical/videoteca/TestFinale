package it.unical.videoteca;

import org.junit.jupiter.api.*;
import main.it.unical.videoteca.domain.*;

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
        
        // 1. Test di fallimento per titolo nullo/vuoto (gestito dalla Factory nel DTO -> Entità)
        FilmDTO titoloNonValido = new FilmDTO("ID001", null, "Regista X", 2000, "Azione", 4.0, StatoVisione.VISTO);
        assertThrows(IllegalArgumentException.class, () -> {
            facade.aggiungiFilm(titoloNonValido);
        }, "L'aggiunta di un film con titolo nullo/vuoto dovrebbe sollevare un'eccezione.");

        // 2. Test che la Factory forzi il rating non valido a 0.0
        FilmDTO ratingNonValido = new FilmDTO("ID002", "Film B", "Regista Y", 2010, "Commedia", 6.0, StatoVisione.DA_VEDERE);
        facade.aggiungiFilm(ratingNonValido);
        
        // Verifica che il rating sia stato corretto a 0.0 dal FilmFactoryConcrete
        assertEquals(0.0, facade.cercaPerId("ID002").getRating(), 
                     "Il rating fuori range (>5.0) dovrebbe essere normalizzato a 0.0 dalla Factory.");

        // 3. Test che la Factory assegni DA_VEDERE se lo stato è null
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
        String raccoltaId = "R001";
        String filmIdValido = "F001";
        String filmIdInesistente = "F999";
        
        //prepara il catalogo e la raccolta
        facade.aggiungiFilm(new FilmDTO(filmIdValido, "Matrix", "Wachowski", 1999, "Fantascienza", 5.0, StatoVisione.VISTO));
        facade.creaRaccolta(raccoltaId, "Cult Fantascienza", "I migliori di Fantascienza");

        //1. Aggiunta valida
        assertDoesNotThrow(() -> {
            facade.aggiungiFilmARaccolta(raccoltaId, filmIdValido);
        }, "L'aggiunta di un film valido alla raccolta non dovrebbe sollevare eccezioni.");
        assertEquals(1, facade.filmDellaRaccolta(raccoltaId).size());

        //2. Fallimento per Film inesistente
        assertThrows(IllegalArgumentException.class, () -> {
            facade.aggiungiFilmARaccolta(raccoltaId, filmIdInesistente);
        }, "L'aggiunta di un film inesistente dovrebbe fallire (IllegalArgumentException).");

        //3. Fallimento per Raccolta inesistente
        assertThrows(IllegalArgumentException.class, () -> {
            facade.aggiungiFilmARaccolta("R999", filmIdValido);
        }, "L'aggiunta a una raccolta inesistente dovrebbe fallire.");
        
        //4. Fallimento per eliminazione con ID nullo/vuoto
        assertThrows(IllegalArgumentException.class, () -> {
            facade.eliminaRaccolta(null);
        }, "L'eliminazione con ID nullo dovrebbe fallire.");
    }
}