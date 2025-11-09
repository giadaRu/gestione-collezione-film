package it.unical.videoteca.domain.memento;

import java.util.ArrayList;
import java.util.List;
import it.unical.videoteca.domain.entity.*;


public class VideotecaMemento {

    private final List<Film> statoSalvato;

    //crea un nuovo Memento copiando lo stato corrente della videoteca
    public VideotecaMemento(List<Film> statoCorrente) {
        this.statoSalvato = new ArrayList<>();
        for (Film f : statoCorrente) {
            Film copia = new Film(
                f.getId(),
                f.getTitolo(),
                f.getRegista(),
                f.getAnno(),
                f.getGenere(),
                f.getRating(),
                f.getStato()
            );
            statoSalvato.add(copia);
        }
    }

    //restituisce la lista di film salvata in questo memento
    public List<Film> getStatoSalvato() {
        List<Film> copia = new ArrayList<>();
        for (Film f : statoSalvato) {
            Film nuovo = new Film(
                f.getId(),
                f.getTitolo(),
                f.getRegista(),
                f.getAnno(),
                f.getGenere(),
                f.getRating(),
                f.getStato()
            );
            copia.add(nuovo);
        }
        return copia;
    }
}
