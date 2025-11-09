package it.unical.videoteca;

import it.unical.videoteca.domain.dto.FilmDTO;
import it.unical.videoteca.domain.entity.Film;
import it.unical.videoteca.domain.entity.StatoVisione;
import it.unical.videoteca.domain.facade.VideotecaFacade;
import it.unical.videoteca.domain.filter.FiltroFactory;
import it.unical.videoteca.domain.filter.FiltroFilm;

import java.util.*;

public class MainVideoteca {

    private static final Scanner scanner = new Scanner(System.in);
    private static final VideotecaFacade facade = new VideotecaFacade();

    public static void main(String[] args) {
        System.out.println("\n\nBenvenuto nella tua Collezione di Film Personale");
        int scelta;
        do {
            mostraMenu();
            System.out.print("\nScegli un'opzione: ");
            scelta = leggiInt();

            switch (scelta) {
                case 1 -> aggiungiFilm();
                case 2 -> modificaFilm();
                case 3 -> rimuoviFilm();
                case 4 -> {
                    List<Film> catalogo = facade.listaFilm();
                    stampaTabellaFilm(catalogo);
                    }
                case 5 -> cercaPerTitolo();
                case 6 -> cercaFiltrata();
                case 7 -> undoUltimaOperazione();
                case 0 -> System.out.println("Stai abbandonando il programma.");
                default -> System.out.println("Scelta non valida.");
            }

        } while (scelta != 0);
    }

    private static void mostraMenu() {
        System.out.println("\n    MENU PRINCIPALE");
        System.out.println("1. Aggiungi nuovo film");
        System.out.println("2. Modifica un film esistente");
        System.out.println("3. Rimuovi un film");
        System.out.println("4. Mostra tutti i film");
        System.out.println("5. Cerca film per titolo");
        System.out.println("6. Ricerca filtrata");
        System.out.println("7. Annulla ultima operazione");
        System.out.println("0. Esci");
    }

    private static void aggiungiFilm() {
        System.out.println("\n--- Aggiungi film ---");
        System.out.print("Titolo: ");
        String titolo = scanner.nextLine();
        System.out.print("Regista: ");
        String regista = scanner.nextLine();
        System.out.print("Anno: ");
        Integer anno = leggiInt();
        System.out.print("Genere: ");
        String genere = scanner.nextLine();
        System.out.print("Valutazione (1 - 5): ");
        Double rating = leggiDouble();
        System.out.print("Stato (VISTO / DA_VEDERE / IN_VISIONE): ");
        StatoVisione stato = StatoVisione.valueOf(scanner.nextLine().trim().toUpperCase());

        facade.aggiungiFilm(new FilmDTO(null, titolo, regista, anno, genere, rating, stato));
        System.out.println("Film aggiunto con successo.");
    }

    private static void modificaFilm() {
        System.out.println("\n--- Modifica film ---");
        System.out.print("ID del film da modificare: ");
        String id = scanner.nextLine();
        System.out.print("Nuovo titolo: ");
        String titolo = scanner.nextLine();
        System.out.print("Nuovo regista: ");
        String regista = scanner.nextLine();
        System.out.print("Nuovo anno: ");
        Integer anno = leggiInt();
        System.out.print("Nuovo genere: ");
        String genere = scanner.nextLine();
        System.out.print("Nuova valutazione (1–5): ");
        Double rating = leggiDouble();
        System.out.print("Nuovo stato (VISTO / DA_VEDERE / IN_VISIONE): ");
        StatoVisione stato = StatoVisione.valueOf(scanner.nextLine().trim().toUpperCase());

        facade.modificaFilm(new FilmDTO(id, titolo, regista, anno, genere, rating, stato));
        System.out.println("Film aggiornato.");
    }

    private static void rimuoviFilm() {
        System.out.print("\nID del film da rimuovere: ");
        String id = scanner.nextLine();
        facade.rimuoviFilm(id);
        System.out.println("Film rimosso correttamente.");
    }


    private static void cercaPerTitolo() {
        System.out.print("\nInserisci parte del titolo: ");
        String t = scanner.nextLine();
        var risultati = facade.cercaPerTitolo(t);
        if (risultati.isEmpty()) System.out.println("Nessun film trovato.");
        else risultati.forEach(f -> System.out.println("- " + f.getTitolo() + " (" + f.getAnno() + ")"));
    }

    private static void cercaFiltrata() {
        System.out.println("\n--- Ricerca filtrata ---");
        List<FiltroFilm> filtri = new ArrayList<>();
        boolean continua;

        do {
            mostraMenuFiltri();
            System.out.print("Scegli il tipo di filtro: ");
            int tipo = leggiInt();

            switch (tipo) {
                case 1 -> { // genere
                    System.out.print("Genere: ");
                    String g = scanner.nextLine();
                    filtri.add(FiltroFactory.creaFiltro("genere", g));
                }
                case 2 -> { // stato
                    System.out.print("Stato (VISTO / DA_VEDERE / IN_VISIONE): ");
                    StatoVisione s = StatoVisione.valueOf(scanner.nextLine().trim().toUpperCase());
                    filtri.add(FiltroFactory.creaFiltro("stato", s));
                }
                case 3 -> { // regista
                    System.out.print("Regista: ");
                    String r = scanner.nextLine();
                    filtri.add(FiltroFactory.creaFiltro("regista", r));
                }
                case 4 -> { // anno minimo
                    System.out.print("Anno minimo (incluso): ");
                    int amin = leggiInt();
                    filtri.add(FiltroFactory.creaFiltro("anno_min", amin));
                }
                case 5 -> { // anno massimo
                    System.out.print("Anno massimo (incluso): ");
                    int amax = leggiInt();
                    filtri.add(FiltroFactory.creaFiltro("anno_max", amax));
                }
                case 6 -> { // rating minimo
                    System.out.print("Rating minimo (>=): ");
                    double rmin = leggiDouble();
                    filtri.add(FiltroFactory.creaFiltro("rating_min", rmin));
                }
                case 7 -> { // rating massimo
                    System.out.print("Rating massimo (<=): ");
                    double rmax = leggiDouble();
                    filtri.add(FiltroFactory.creaFiltro("rating_max", rmax));
                }
                default -> System.out.println("Tipo non valido.");
            }

            System.out.print("Aggiungere un altro filtro? (si/no): ");
            continua = scanner.nextLine().trim().equalsIgnoreCase("s");
        } while (continua);

        List<Film> risultati = facade.cercaConFiltri(filtri);

        if (risultati.isEmpty()) System.out.println("Nessun film trovato con i filtri selezionati.");
        else {
            System.out.println("\nRisultati:");
            risultati.forEach(f -> System.out.printf("- [%s] %s (%d) %s %.1f %s%n",
                    f.getId(), f.getTitolo(), f.getAnno(), f.getRegista(), f.getRating(), f.getStato()));
        }
    }

    private static void mostraMenuFiltri() {
        System.out.println("\n[ Filtri disponibili ]");
        System.out.println("1. Genere ");
        System.out.println("2. Stato (VISTO / DA_VEDERE / IN_VISIONE)");
        System.out.println("3. Regista ");
        System.out.println("4. Anno minimo");
        System.out.println("5. Anno massimo");
        System.out.println("6. Rating minimo");
        System.out.println("7. Rating massimo");
    }

    private static void undoUltimaOperazione() {
        System.out.println("\n Annullamento ultima operazione...");
        facade.annullaUltimaOperazione();
    }

    private static int leggiInt() {
        while (true) {
            try { return Integer.parseInt(scanner.nextLine()); }
            catch (NumberFormatException e) { System.out.print("Valore non valido. Riprova: "); }
        }
    }

    private static double leggiDouble() {
        while (true) {
            try { return Double.parseDouble(scanner.nextLine()); }
            catch (NumberFormatException e) { System.out.print("Valore non valido. Riprova: "); }
        }
    }

    private static void stampaTabellaFilm(List<Film> filmList) {
        if (filmList == null || filmList.isEmpty()) {
            System.out.println("\n Nessun film presente nel catalogo.\n");
            return;
        }

        System.out.println("\n                         Ecco il tuo catalogo \n");
        System.out.printf("%-8s %-20s %-20s %-6s %-7s %-10s%n",
                "ID", "Titolo", "Regista", "Anno", "Rating", "Stato");
        System.out.println("-------------------------------------------------------------------------------------");

        for (Film f : filmList) {
            System.out.printf("%-8s %-20s %-20s %-6s %-7s %-10s%n",
                    f.getId(),
                    Optional.ofNullable(f.getTitolo()).orElse("N/D"),
                    Optional.ofNullable(f.getRegista()).orElse("N/D"),
                    f.getAnno() != null ? f.getAnno().toString() : "N/D",
                    f.getRating() != null ? String.format("%.1f", f.getRating()) : "N/D",
                    f.getStato() != null ? f.getStato().name() : "N/D");
        }
        System.out.println("-------------------------------------------------------------------------------------\n");
}

}
