package it.unical.videoteca.domain.facade;

import java.util.*;
import it.unical.videoteca.domain.*;


public class VideotecaFacade {

    private final FilmService service;
    private final List<VideotecaMemento> cronologia = new ArrayList<>(); //per gestire gli stati precedenti
    private final RaccoltaService raccoltaService;

    public VideotecaFacade() {
        this.service = new FilmService();
        this.raccoltaService = new RaccoltaService();
        this.service.addObserver(raccoltaService); // collegamento Observer

    }

    //Metodi base  (per gestione di film)------------------------------
    public void aggiungiFilm(FilmDTO dto) {
        service.aggiungiFilm(dto);
    }

    public void rimuoviFilm(String id) {
        service.rimuoviFilm(id);
    }

    public void modificaFilm(FilmDTO dto) {
        service.modificaFilm(dto);
    }

    public List<Film> listaFilm() {
        return service.trovaTutti();
    }

    public List<Film> cercaPerGenere(String genere) {
        return service.trovaPerGenere(genere);
    }

    public Film cercaPerId(String id) {
        return service.trovaPerId(id);
    }

    public List<Film> cercaPerTitolo(String titolo) {
        return service.trovaPerTitolo(titolo);
    }

    public List<Film> cercaPerRegista(String regista) {
        return service.trovaPerRegista(regista);
    }

    //Metodi per la gestione delle raccolte -----
    public void creaRaccolta(String id, String nome, String descrizione) {
        raccoltaService.creaRaccolta(id, nome, descrizione);
    }

    public void eliminaRaccolta(String id) {
        raccoltaService.eliminaRaccolta(id);
    }

    public void aggiungiFilmARaccolta(String raccoltaId, String filmId) {
        raccoltaService.aggiungiFilm(raccoltaId, filmId);
    }

    public void rimuoviFilmDaRaccolta(String raccoltaId, String filmId) {
        raccoltaService.rimuoviFilm(raccoltaId, filmId);
    }

    public List<Raccolta> tutteLeRaccolte() {
        return raccoltaService.tutte();
    }

    public List<Film> filmDellaRaccolta(String id) {
        return raccoltaService.filmDellaRaccolta(id);
    }

    //Metodi per integrare il pattern Memento (cioè supportare operazioni di undo/redo)
    
    //Salva uno snapshot dello stato corrente prima di una modifica
    private void salvaStato() {
        List<Film> statoCorrente = service.trovaTutti();
        VideotecaMemento memento = new VideotecaMemento(statoCorrente);
        cronologia.add(memento);
    }

    //Per annullare l'ultima modifica (nb: è consentito solo se esiste uno stato precedente)
    public void annullaUltimaOperazione() {
        if (cronologia.size()<=1) {
            System.out.println("Nessuna operazione da annullare");
            return;
        }

        cronologia.remove(cronologia.size() - 1);
        VideotecaMemento ultimoStato = cronologia.get(cronologia.size()-1);

        service.ripristinaStato(ultimoStato.getStatoSalvato());
        System.out.println("Stato precedente ripristinato");
    }

    //Per cercare film usando una combinazione di filtri 
    public List<Film> cercaConFiltri(List<FiltroFilm> filtri) {
        return service.cercaConFiltri(filtri);
    }
}
