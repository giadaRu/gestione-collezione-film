package it.unical.videoteca.domain.collection;

import java.util.ArrayList;
import java.util.List;
import it.unical.videoteca.domain.*;

//Gestisce le raccolte e si aggiorna automaticamente quando un film viene eliminato dal catalogo (Observer Pattern)
public class RaccoltaService implements Observer {

    private final RaccoltaRepository raccolte;
    private final FilmRepository filmRepo;

    public RaccoltaService() {
        this.raccolte = new RaccoltaRepositoryConcrete();
        this.filmRepo = new FilmRepositoryConcrete();
    }

    public void creaRaccolta(String id, String nome, String descrizione) {
        if (raccolte.existsByName(nome))
            throw new IllegalStateException("Esiste già una raccolta con questo nome");
        Raccolta r = new Raccolta(id, nome, descrizione);
        raccolte.save(r);
    }

    public void eliminaRaccolta(String id) {
        raccolte.delete(id);
    }

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
        Film f = filmRepo.findById(filmId);
        if (f == null) 
            throw new IllegalArgumentException("Film non trovato");
        r.addFilm(filmId);
        raccolte.save(r);
    }

    public void rimuoviFilm(String raccoltaId, String filmId) {
        Raccolta r = raccolte.findById(raccoltaId);
        if (r == null) 
            throw new IllegalArgumentException("Raccolta non trovata");
        r.removeFilm(filmId);
        raccolte.save(r);
    }

    public List<Raccolta> tutte() {
        return raccolte.findAll();
    }

    public List<Film> filmDellaRaccolta(String raccoltaId) {
        Raccolta r = raccolte.findById(raccoltaId);
        if (r == null) 
            throw new IllegalArgumentException("Raccolta non trovata");
        List<Film> out = new ArrayList<>();
        for (String filmId : r.getFilmIds()) {
            Film f = filmRepo.findById(filmId);
            if (f != null) 
                out.add(f);
        }
        return out;
    }

    /** Observer: aggiornamento automatico quando un film viene eliminato. */
    @Override
    public void update(String filmId) {
        List<Raccolta> lista = raccolte.findAll();
        for (Raccolta r : lista) {
            if (r.contiene(filmId)) {
                r.removeFilm(filmId);
                raccolte.save(r);
                System.out.println("Film " + filmId + " rimosso anche dalla raccolta '" + r.getNome() + "'");
            }
        }
    }
}

