package com.example.pokemon.cli;

import com.example.pokemon.api.PokemonApiAbrufer;
import com.example.pokemon.service.TeamService;
import com.example.pokemon.model.Pokemon;

import java.sql.SQLOutput;
import java.util.List;
import java.util.Scanner;

public class CommandHandler {

    private final PokemonApiAbrufer api;
    private final TeamService teamService;
    private final Scanner scan;

    public CommandHandler(PokemonApiAbrufer api, TeamService teamService, Scanner scan) {
        this.api = api;
        this.teamService = teamService;
        this.scan= scan;
    }

    public boolean handle() {
        System.out.println("\nPokemon-MENU:");
        System.out.println("1. Suche Pokemon nach Name oder ID\n2. Suche nach Typ\n\n" +
                        "3. Finde schwache Typen gegen [Typ]\n4. Finde starke Typen gegen[Typ]\n5. Finde Starken und Schwachen gegen [Typ]\n\n" +
                        "6. Team anzeigen\n7. Team speichern\n8. Team laden\n9. Team Analyse\n\n" +
                        "0. Exit\n");
        System.out.print("Ihre Eingabe: ");
        int cmd = Integer.parseInt(scan.nextLine());

        try {
            switch (cmd) {
                case 1 -> {
                    System.out.println("Gebe den Pokemon-Name oder -ID ein: ");
                    String nameOrID = scan.nextLine().trim().toLowerCase();
                    Pokemon p = api.getJsonString(nameOrID);
                    teamService.printInfo(p);
                    System.out.println("Ins Team hinzufügen?  [J] / [N]");
                    String answer = scan.nextLine().trim().toLowerCase();
                    if (answer.equals("j")) {
                        teamService.add(p);
                        System.out.println("\nSpieler hinzugefügt");
                    }
                }

                case 2 -> {
                    System.out.print("Gebe den Pokemon-Typ ein: ");
                    String type = scan.nextLine().trim().toLowerCase();
                    List<String> pokemons = teamService.searchByType(type);
                    System.out.printf("Pokemons des Typs %s", type);
                    pokemons.forEach(System.out::println);
                }

                case 3 -> {
                    System.out.print("Gebe den Typ ein: ");
                    String strong = scan.nextLine();
                    List<String> types = teamService.searchWeaks(strong);
                    System.out.printf("\nTyp \"%s\" richtet doppelten Schaden an:\n", strong);
                    types.forEach(System.out::println);
                }

                case 4 -> {
                    System.out.print("Gebe den Typ ein: ");
                    String weak = scan.nextLine();
                    List<String> types = teamService.searchStrongs(weak);
                    System.out.printf("\nTyp \"%s\" erleidet doppelten Schaden durch:\n", weak);
                    types.forEach(System.out::println);
                }

                case 5 -> {
                    System.out.print("Stark gegen (Typ): ");
                    String strong = scan.nextLine();
                    System.out.print("Schwach gegen (Typ): ");
                    String weak = scan.nextLine();
                    List<String> types = teamService.searchStrongWeak(strong, weak);
                    if (types.isEmpty()) {
                        System.out.println("Die Liste ist leer");
                    } else {
                        System.out.printf("Typen, die stark gegen %s und schwach gegen %s sind:\n", strong, weak);
                        types.forEach(System.out::println);
                    }
                }
                case 6 -> {
                    if (teamService.getTeam().isEmpty()) {
                        System.out.println("\nEs gibt keine Pokemonummmmm in deinem Team");
                    } else {
                        System.out.println("\nDein Team");
                        teamService.getTeam().stream().forEach(
                                p -> System.out.println(p.getName()));
                    }
                }

                case 7 -> {
                    System.out.print("In die Datei speichern: ");
                    String savefile = scan.nextLine().trim();
                    teamService.saveTeam(savefile);
                    System.out.println("Als \"" + savefile + "\" gespeichert");
                }

                case 8 -> {
                    System.out.print("Aus der Datei laden: ");
                    String loadfile = scan.nextLine().trim();
                    teamService.loadTeam(loadfile);
                    System.out.println("Team geladen");
                }

                case 9 -> {
                    teamService.analyseTeamWeakness();
                    System.out.println();
                    teamService.analyseTeamStrength();
                }

                case 0 -> {
                    System.out.println("Ciao!");
                    return false;
                }

                default -> System.out.println("Unbekannter Befehl");
            }
        } catch (Exception e) {
            System.out.println("Fehler: " + e.getMessage());
        }
        return true;
    }
}
