package it.unical.videoteca.domain.repository;

import java.io.*;
import java.util.*;
import it.unical.videoteca.domain.entity.Film;
import it.unical.videoteca.domain.entity.StatoVisione;


public class FilmRepositoryConcrete implements FilmRepository {

    private final List<Film> archivio = new ArrayList<>();
    private static final String FILM_FILE_PATH = "videoteca_data.csv"; //per persistenza CSV
    

    public FilmRepositoryConcrete() {
        caricaDaFile();
    }

    private void caricaDaFile() {
        File file = new File(FILM_FILE_PATH);
        if (!file.exists()) 
            return;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String film;
            while ((film = br.readLine()) != null) {
                String[] parts = film.split(";");
                if (parts.length == 7) {
                    try {
                        Film f = new Film(
                            parts[0], //id
                            parts[1], //titolo
                            parts[2], //regista
                            Integer.parseInt(parts[3]), //anno
                            parts[4], //genere
                            Double.parseDouble(parts[5]), //rating
                            StatoVisione.valueOf(parts[6]) //stato
                        );
                        archivio.add(f);
                    } catch (IllegalArgumentException e) {
                        System.err.println("Errore nel parsing riga: " + film);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Errore di lettura dal file");
        }
    }

    private void salvaSuFile() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILM_FILE_PATH))) {
            for (Film f : archivio) {
                String film = String.join(";",
                    f.getId(),
                    f.getTitolo(),
                    f.getRegista().toString(),
                    String.valueOf(f.getAnno()),
                    f.getGenere() != null ? f.getGenere() : "",
                    String.valueOf(f.getRating()),
                    f.getStato().toString()
                );
                bw.write(film);
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Errore durante il salvataggio dei film su file");
        }
    }

    @Override
    public void save(Film film) {
        if (film == null)
            throw new IllegalArgumentException("Il film da salvare non può essere null");

        //se l'id è mancante, lo genera
        final boolean genera = (film.getId()==null || film.getId().isBlank());
        if (genera){
            film.setId(UUID.randomUUID().toString());
            archivio.add(film);
            salvaSuFile();
            return;
        }

        //cerco per ID (update)
        int idx = -1;
        for (int i = 0; i < archivio.size(); i++) {
            if (archivio.get(i).getId().equals(film.getId())) {
                idx = i;
                break;
            }
        }

        if (idx >= 0) {
            archivio.set(idx, film); //aggiorno: sostituisco la versione esistente (stesso ID)
            salvaSuFile();
            return;
        }

        // blocco duplicati logici (titolo+regista+anno) con ID diverso
        if (exists(film.getTitolo(), film.getRegista(), film.getAnno())) {
            throw new IllegalArgumentException( "Film duplicato (titolo, regista, anno): " + film.getTitolo() + " - " + film.getRegista() + " - " + film.getAnno());
        }

        archivio.add(film); //Altrimenti, aggiungo il nuovo film (nb: la validazione di unicità è gestita da aggiungiFilm in FilmService)
        salvaSuFile(); //per persistenza CSV
    }


    @Override
    public void delete(String id) {
        boolean rimosso = archivio.removeIf(f -> f.getId().equals(id));
        if (rimosso){
            salvaSuFile(); //per persistenza CSV
        }
    }

    @Override
    public Film findById(String id) {
        if (id == null || id.isBlank()) {
            return null;
        }
        for (Film f : archivio) {
            if (f.getId().equals(id)) {
                return f; 
            }
        }
        return null; 
    }

    @Override
    public List<Film> findAll() {
        return new ArrayList<>(archivio); // restituisce una copia

    }

    @Override
    public boolean exists(String titolo, String regista, int anno) {
        if (titolo == null || regista == null)
            return false;

        String titoloNorm = titolo.trim().toLowerCase();
        String registaNorm = regista.trim().toLowerCase();

        for (Film f : archivio) {
            String titoloFilm = f.getTitolo().trim().toLowerCase();
            String registaFilm = f.getRegista().trim().toLowerCase();

            if (titoloFilm.equals(titoloNorm) &&
                registaFilm.equals(registaNorm) &&
                f.getAnno() == anno) {
                return true; 
            }
        }
        return false; 
    }

    @Override
    public void clearAndSaveAll(List<Film> film) {
        this.archivio.clear(); //svuoto l'archivio corrente
        for (Film f : film){
            Film nuovo = new Film(f.getId(), 
                                  f.getTitolo(), 
                                  f.getRegista(), 
                                  f.getAnno(), 
                                  f.getGenere(), 
                                  f.getRating(), 
                                  f.getStato());
            archivio.add(nuovo);
        }
        salvaSuFile();
    }

}
