package it.unical.videoteca.domain.service;

import java.util.*;
import it.unical.videoteca.domain.dto.FilmDTO;
import it.unical.videoteca.domain.factory.*;
import it.unical.videoteca.domain.filter.FiltroFilm;
import it.unical.videoteca.domain.entity.Film;
import it.unical.videoteca.domain.observer.Observer;
import it.unical.videoteca.domain.observer.Observable;
import it.unical.videoteca.domain.repository.*;


public class FilmService implements Observable{

    private final FilmRepository repository;
    private final FilmFactory factory;
    private final List<Observer> observers = new ArrayList<>();

    public FilmService() {
        this.repository = new FilmRepositoryConcrete();
        this.factory = new FilmFactoryConcrete();
    }

    public FilmService(FilmRepository repository, FilmFactory factory) {
        this.repository = repository;
        this.factory = factory;
    }

    // Aggiunge un nuovo film a partire dal DTO (se c'è già -> errore)
    public void aggiungiFilm(FilmDTO dto) {
        if (dto == null)
            throw new IllegalArgumentException("Il DTO del film non può essere null");

        Film film = factory.fromDTO(dto);

        if (repository.exists(film.getTitolo(), film.getRegista(), film.getAnno()))
            throw new IllegalStateException("Il film esiste già nel catalogo");

        repository.save(film);
        notifyObservers(film.getId());
    }

    //Modifica un film esistente (stesso ID)
    public void modificaFilm(FilmDTO dto) {
        if (dto == null)
            throw new IllegalArgumentException("Il DTO non può essere null");

        Film film = factory.fromDTO(dto);
        repository.save(film);
        notifyObservers(film.getId());
    }

    //Restituisce la lista di utti i film presenti
    public List<Film> trovaTutti() {
        return repository.findAll();
    }

    //Trova un film per ID (se non esiste->null)
    public Film trovaPerId(String id) {
       return repository.findById(id);
    }

    //Restituisce tutti i film che appartengono al genere passato come parametro
    public List<Film> trovaPerGenere(String genere) {
        List<Film> risultato = new ArrayList<>();
        for (Film f : repository.findAll()) {
            if (f.getGenere() != null &&
                f.getGenere().equalsIgnoreCase(genere)) {
                risultato.add(f);
            }
        }
        return risultato;
    }


    //Restituisce i film il cui titolo contiene la stringa passata come parametro 
    public List<Film> trovaPerTitolo(String titoloRicerca) {
        if (titoloRicerca == null || titoloRicerca.isBlank()) {
            return repository.findAll(); 
        }
        
        List<Film> risultato = new ArrayList<>();
        String titoloNorm = titoloRicerca.trim().toLowerCase();

        for (Film f : repository.findAll()) {
            if (f.getTitolo() != null && f.getTitolo().toLowerCase().contains(titoloNorm)) {
                risultato.add(f);
            }
        }
        return risultato;
    }

    //Restituisce i film di un regista specifico 
    public List<Film> trovaPerRegista(String registaRicerca) {
        if (registaRicerca == null || registaRicerca.isBlank()) {
            return new ArrayList<>();
        }
        
        List<Film> risultato = new ArrayList<>();
        String registaNorm = registaRicerca.trim().toLowerCase();

        for (Film f : repository.findAll()) {
            if (f.getRegista() != null && f.getRegista().trim().toLowerCase().equals(registaNorm)) {
                risultato.add(f);
            }
        }
        return risultato;
    }

    

    // Per ripristinare lo stato quando si fa un UNDO
    public void ripristinaStato(List<Film> stato) {
        repository.clearAndSaveAll(stato); 
    }


    //Metodi per supportare la ricerca filtrata------------------------------------------------ 
    
    //Applica una lista di filtri (come strategies) ai film presenti nel repository
    public List<Film> cercaConFiltri(List<FiltroFilm> filtri) {
        List<Film> tutti = repository.findAll();
        List<Film> risultato = new ArrayList<>();

        for (Film f : tutti) {
            boolean accetta = true;
            for (FiltroFilm filtro : filtri) {
                if (!filtro.applica(f)) {
                    accetta = false;
                    break;
                }
            }
            if (accetta) 
                risultato.add(f);
        }
        return risultato;
    }

    //Ricerca con filtri + ordinamento
    //nb: se si passa una lista di filtri vuoti, si effettua semplicemente l'ordinamento di tutti i film (senza filtri)
    public List<Film> cercaConFiltriOrdinati(List<FiltroFilm> filtri, FilmSortStrategy.Criterio criterio, FilmSortStrategy.Ordine ordine) {
        List<Film> filtrati = cercaConFiltri(filtri);
        Collections.sort(filtrati, FilmSortStrategy.comparator(criterio, ordine));
        return filtrati;
    }

    //Metodi per implementare Observable---------------------------------------------------------
    @Override
    public void addObserver(Observer o) {
        if (o != null && !observers.contains(o)) 
            observers.add(o);
    }

    @Override
    public void removeObserver(Observer o) {
        observers.remove(o);
    }

    @Override
    public void notifyObservers(String filmId) {
        for (Observer o : observers) 
            o.update(filmId);
    }

    public void rimuoviFilm(String id) {
        Film f = repository.findById(id);
        if (f == null) 
            throw new IllegalArgumentException("Film non trovato");
        repository.delete(id);
        notifyObservers(id); // avvisa le raccolte (che qui sono gli osservatori)
    }
}
