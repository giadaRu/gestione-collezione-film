package it.unical.videoteca.domain.collection;

import java.util.ArrayList;
import java.util.List;
import it.unical.videoteca.domain.observer.*;
import it.unical.videoteca.domain.entity.*;
import it.unical.videoteca.domain.service.FilmService;

public class RaccoltaService implements Observer {

    private final RaccoltaRepository raccolte;
    private final FilmService filmService;   //uso lo stesso service della facade

    public RaccoltaService() {
        this(new RaccoltaRepositoryConcrete(), new FilmService());
    }

    public RaccoltaService(RaccoltaRepository raccolte, FilmService filmService) {
        this.raccolte = raccolte;
        this.filmService = filmService;
    }

    public void creaRaccolta(String id, String nome, String descrizione) {
        if(nome==null || nome.isBlank()){
            throw new IllegalArgumentException("Il nome della raccolta non può essere vuoto.");
        }
        if (raccolte.existsByName(nome))
            throw new IllegalStateException("Esiste già una raccolta con questo nome");
        raccolte.save(new Raccolta(id, nome, descrizione));
    }

    public void eliminaRaccolta(String id) { raccolte.delete(id); }

    public void aggiornaMetadati(String id, String nuovoNome, String nuovaDescrizione) {
        Raccolta r = raccolte.findById(id);
        if (r == null) 
            throw new IllegalArgumentException("Raccolta non trovata");
        if (nuovoNome != null && !nuovoNome.isBlank()) 
            r.setNome(nuovoNome);
        if (nuovaDescrizione != null) 
            r.setDescrizione(nuovaDescrizione);
        raccolte.save(r);
    }

    public void aggiungiFilm(String raccoltaId, String filmId) {
        Raccolta r = raccolte.findById(raccoltaId);
        if (r == null) 
            throw new IllegalArgumentException("Raccolta non trovata");
        Film f = filmService.trovaPerId(filmId);              
        if (f == null) 
            throw new IllegalArgumentException("Film non trovato");
        r.addFilm(f.getId());                                 
        raccolte.save(r);
    }

    // overload che accetta direttamente il Film
    public void aggiungiFilm(String raccoltaId, Film film) {
        Raccolta r = raccolte.findById(raccoltaId);
        if (r == null) 
            throw new IllegalArgumentException("Raccolta non trovata");
        if (film == null) 
            throw new IllegalArgumentException("Film nullo");
        r.addFilm(film.getId());
        raccolte.save(r);
    }

    public void rimuoviFilm(String raccoltaId, String filmId) {
        Raccolta r = raccolte.findById(raccoltaId);
        if (r == null) 
            throw new IllegalArgumentException("Raccolta non trovata");
        r.removeFilm(filmId);
        raccolte.save(r);
    }

    public List<Raccolta> tutte() { return raccolte.findAll(); }

    public List<Film> filmDellaRaccolta(String raccoltaId) {
        Raccolta r = raccolte.findById(raccoltaId);
        if (r == null) 
            throw new IllegalArgumentException("Raccolta non trovata");
        List<Film> out = new ArrayList<>();
        for (String filmId : r.getFilmIds()) {
            Film f = filmService.trovaPerId(filmId);          
            if (f != null) 
                out.add(f);
        }
        return out;
    }

    @Override
    public void update(String filmId) {
        for (Raccolta r : raccolte.findAll()) {
            if (r.contiene(filmId)) {
                r.removeFilm(filmId);
                raccolte.save(r);
            }
        }
    }
}
