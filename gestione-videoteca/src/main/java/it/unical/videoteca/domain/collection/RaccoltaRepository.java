package it.unical.videoteca.domain.collection;

import java.util.List;

//Interfaccia del Repository per la gestione delle raccolte tematiche
 
public interface RaccoltaRepository {

    //Salva una raccolta (aggiunge o aggiorna se esiste già)
    void save(Raccolta r);

    //Elimina una raccolta per id
    void delete(String id);

    //Cerca una raccolta per id. Restituisce null se non trovata
    Raccolta findById(String id);

    //Restituisce la lista di tutte le raccolte
    List<Raccolta> findAll();

    //Controlla se esiste una raccolta con lo stesso nome 
    boolean existsByName(String nome);
}
