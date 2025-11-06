package it.unical.videoteca;

import org.junit.jupiter.api.Test;
import it.unical.videoteca.domain.*;
import java.util.*;

public class FiltroFactoryTest {

    @Test
    void testFiltriMultipli() {
        VideotecaFacade facade = new VideotecaFacade();

        facade.aggiungiFilm(new FilmDTO("1","Inception","Nolan",2010,"Fantascienza",5.0,StatoVisione.VISTO));
        facade.aggiungiFilm(new FilmDTO("2","Tenet","Nolan",2020,"Fantascienza",4.5,StatoVisione.IN_VISIONE));
        facade.aggiungiFilm(new FilmDTO("3","The Prestige","Nolan",2006,"Thriller",4.7,StatoVisione.VISTO));
        facade.aggiungiFilm(new FilmDTO("4","Memento","Nolan",2000,"Thriller",4.6,StatoVisione.VISTO));

        List<FiltroFilm> filtri = new ArrayList<>();
        filtri.add(FiltroFactory.creaFiltro("genere", "Thriller"));
        filtri.add(FiltroFactory.creaFiltro("stato", StatoVisione.VISTO));

        var risultato = facade.cercaConFiltri(filtri);

        if (risultato.size() != 2)
            throw new RuntimeException("Errore: la combinazione di filtri non funziona correttamente");

        System.out.println("FiltroFactoryTest: combinazione filtri funzionante");
    }
}
