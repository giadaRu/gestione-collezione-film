package it.unical.videoteca.domain.facade;

import java.util.*;
import it.unical.videoteca.domain.service.FilmService;
import it.unical.videoteca.domain.memento.VideotecaMemento;
import it.unical.videoteca.domain.collection.*;
import it.unical.videoteca.domain.dto.FilmDTO;
import it.unical.videoteca.domain.entity.*;
import it.unical.videoteca.domain.filter.FiltroFilm;


public class VideotecaFacade {

    private final FilmService service;
    private final List<VideotecaMemento> cronologia = new ArrayList<>(); //per gestire gli stati precedenti
    private final RaccoltaService raccoltaService;

    public VideotecaFacade() {
        this.service = new FilmService();
        this.raccoltaService = new RaccoltaService(new RaccoltaRepositoryConcrete(), this.service);
        this.service.addObserver(raccoltaService); // collegamento Observer
        salvaStato(); //salvo lo stato iniziale, anche se vuoto
    }

    //Metodi base  (per gestione di film)------------------------------
    public void aggiungiFilm(FilmDTO dto) {
        salvaStato();
        service.aggiungiFilm(dto);
    }

    public void rimuoviFilm(String id) {
        salvaStato();
        service.rimuoviFilm(id);
    }

    public void modificaFilm(FilmDTO dto) {
        salvaStato();
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
        Film film = service.trovaPerId(filmId);
        if (film == null)
            throw new IllegalArgumentException("film non trovato");
        raccoltaService.aggiungiFilm(raccoltaId, film.getId());
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
        if (cronologia.size()<2) { //servono salmeno stato iniziale + 1 modifica (2 snapshot)
            System.out.println("Nessuna operazione da annullare");
            return;
        }

        VideotecaMemento ultimoStato =  cronologia.remove(cronologia.size() - 1);

        service.ripristinaStato(ultimoStato.getStatoSalvato());
        System.out.println("Stato precedente ripristinato");
    }

    //Per cercare film usando una combinazione di filtri 
    public List<Film> cercaConFiltri(List<FiltroFilm> filtri) {
        return service.cercaConFiltri(filtri);
    }
}
