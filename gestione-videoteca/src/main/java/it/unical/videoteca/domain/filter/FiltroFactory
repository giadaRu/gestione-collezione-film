package it.unical.videoteca.domain.filter;

import it.unical.videoteca.domain.entity.StatoVisione;

//Factory: crea l'istanza corretta in base allo specifico filtro richiesto
public class FiltroFactory {

    public static FiltroFilm creaFiltro(String tipo, Object valore) {
        switch (tipo.toLowerCase()) {
            case "genere":
                return new FiltroPerGenere((String) valore);
            case "stato":
                return new FiltroPerStato((StatoVisione) valore);
            case "regista":
                return new FiltroPerRegista((String) valore);
            case "anno_min":
                return new FiltroPerAnnoMin((int) valore);
            case "anno_max":
                return new FiltroPerAnnoMax((int) valore);
            case "rating_min":
                return new FiltroPerRatingMin((double) valore);
            case "rating_max":
                return new FiltroPerRatingMax((double) valore);
            default:
                throw new IllegalArgumentException("Tipo di filtro non supportato: " + tipo);
        }
    }
}
