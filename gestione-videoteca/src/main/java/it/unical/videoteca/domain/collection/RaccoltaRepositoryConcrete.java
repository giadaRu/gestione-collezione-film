package it.unical.videoteca.domain.collection;

import java.util.ArrayList;
import java.util.List;

//Implementazione in memoria del repository delle raccolte tematiche
public class RaccoltaRepositoryConcrete implements RaccoltaRepository {

    private final List<Raccolta> archivio = new ArrayList<>();
    private static final String RACCOLTA_FILE_PATH = "raccolte_data.csv"; //per persistenza CSV
    
    public RaccoltaRepositoryConcrete() {
        caricaDaFile();
    }

    private void caricaDaFile() {
        File file = new File(RACCOLTA_FILE_PATH);
        if (!file.exists()) 
            return;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length >= 3) { //una raccolta valida deve avere almeno id,regista,descrizione
                    try {
                        String id = parts[0];
                        String nome = parts[1];
                        String descrizione = parts[2];
                        
                        Raccolta r = new Raccolta(id, nome, descrizione);
                        
                        if (parts.length > 3) { //se ci sono id di film, oltre i primi 3 campi visti prima
                            String[] filmIds = parts[3].split(",");
                            for (String filmId : filmIds) {
                                r.addFilm(filmId.trim()); //aggiungo ogni idFilm alla raccolta
                            }
                        }
                        archivio.add(r);
                    } catch (IllegalArgumentException e) {
                        System.err.println("Errore nel parsing raccolta: " + line);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Errore di lettura dal file raccolte: ");
        }
    }
    
    private void salvaSuFile() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(RACCOLTA_FILE_PATH))) {
            for (Raccolta r : archivio) {
                //converto la lista di id dei film in un'unica stringa serparta solo da virgole
                String filmIdsCsv = String.join(",", r.getFilmIds());
                
                //creo la riga CSV unendo tutti i campi con punto e virgola
                String line = String.join(";",
                    r.getId(),
                    r.getNome(),
                    r.getDescrizione(),
                    filmIdsCsv //può essere anche una stringa vuota se la raccolta non ha film
                );
                //scrivo la riga nel file
                bw.write(line);
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Errore durante il salvataggio delle raccolte su file: " );
        }
    }

    //Aggiunge o aggiorna una raccolta
    @Override
    public void save(Raccolta r) {
        if (r == null) 
            throw new IllegalArgumentException("Raccolta nulla");
        Raccolta esistente = findById(r.getId());
        if (esistente != null) 
            archivio.remove(esistente);
        archivio.add(r);
        salvaSuFile(); //salvo per persistenza CSV
    }

    //Elimina una raccolta in base all'id
    @Override
    public void delete(String id) {
        Raccolta target = findById(id);
        if (target != null){ 
            archivio.remove(target);
            salvaSuFile(); //salvo per persistenza CSV
        }
    }

    //Restituisce la raccolta corrispondente all'id, o null se non esiste
    @Override
    public Raccolta findById(String id) {
        if (id == null || id.isBlank())    
            return null;
        for (Raccolta r : archivio) {
            if (r.getId().equals(id)) 
                return r;
        }
        return null;
    }

    //Restituisce una copia della lista di raccolte
    @Override
    public List<Raccolta> findAll() {
        return new ArrayList<>(archivio);
    }

    //Controlla se esiste una raccolta con lo stesso nome
    @Override
    public boolean existsByName(String nome) {
        if (nome == null || nome.isBlank()) 
            return false;
        for (Raccolta r : archivio) {
            if (r.getNome().equalsIgnoreCase(nome)) 
                return true;
        }
        return false;
    }
}
