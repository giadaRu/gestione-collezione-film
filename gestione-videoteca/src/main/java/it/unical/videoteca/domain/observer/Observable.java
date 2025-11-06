package it.unical.videoteca.domain.observer;

//Oggetto osservabile: mantiene una lista di observer e li notifica quando cambia
public interface Observable {
    
    void addObserver(Observer o);
    void removeObserver(Observer o);
    void notifyObservers(String filmId);
}
