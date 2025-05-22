package com.example.pokemon.cli;

import com.example.pokemon.api.PokemonApiAbrufer;
import com.example.pokemon.service.TeamService;
import com.example.pokemon.model.Pokemon;

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
        System.out.println("1. Suchen\n2. Team anzeigen\n3. Team speichern\n4. Team laden\n5. Exit");
        System.out.print("Ihre Eingabe: ");
        int cmd = Integer.parseInt(scan.nextLine());

        try {
            switch (cmd) {
                case 1 -> {
                    System.out.println("Gebe den Pokemon-Name oder -ID ein: ");
                    String nameOrID = scan.nextLine().trim().toLowerCase();
                    Pokemon p = api.getJsonString(nameOrID);
                    printInfo(p);
                    System.out.println("Ins Team hinzufügen?  [J] / [N]");
                    String answer = scan.nextLine().trim().toLowerCase();
                    if (answer.equals("j")) {
                        teamService.add(p);
                        System.out.println("\nSpieler hinzugefügt");
                    }
                }

                case 2 -> {
                    if (teamService.getTeam().isEmpty()) {
                        System.out.println("\nEs gibt keine Pokemonummmmm in deinem Team");
                    } else {
                        System.out.println("\nDein Team");
                        teamService.getTeam().stream().forEach(
                                p -> System.out.println(p.getName()));
                    }
                }

                case 3 -> {
                    System.out.print("In die Datei speichern: ");
                    String savefile = scan.nextLine().trim();
                    teamService.saveTeam(savefile);
                    System.out.println("Als \"" + savefile + "\" gespeichert");
                }

                case 4 -> {
                    System.out.print("Aus der Datei laden: ");
                    String loadfile = scan.nextLine().trim();
                    teamService.loadTeam(loadfile);
                    System.out.println("Team geladen");
                }


                case 5 -> {
                    System.out.println("Ciao!");
                    return false;
                }

                case 6 -> {

                }

                default -> System.out.println("Unbekannter Befehl");

            }
        } catch (Exception e) {
            System.out.println("Fehler: " + e.getMessage());
        }
        return true;
    }

    private static void printInfo(Pokemon p){
        System.out.println("---------------------");
        System.out.println("Name:\t\t" + p.getName());

        String types = p.getTypes().stream()
                .map(t -> t.getType().getName())
                .reduce((a, b) -> a + ", " + b)
                .orElse("Unknown");
        System.out.println("Type: \t\t" +types);

        p.getStats().forEach(statBox -> {
            String statName = statBox.getStat().getName();
            int value = statBox.getBaseStat();
            if (statName.equals("hp")) {
                System.out.printf("%s: \t\t%d\n", statName, value);
            }
            if (statName.equals("attack") || statName.equals("defense")) {
                System.out.printf("%s: \t%d\n", statName, value);
            }
        });
        System.out.println("---------------------");
        System.out.println();
    }
}
