package com.example.pokemon.vers1;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static final Gson gson = new GsonBuilder().create();
    private static final HttpClient client = HttpClient.newHttpClient();
    private static final List<Pokemon> team = new ArrayList<>();

    public static void main(String[] args) {

        ObjectMapper mapper = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        Scanner scan = new Scanner(System.in);


        while (true){
            System.out.println("\nPokemon-MENU:");
            System.out.println("1. Suchen\n2. Team anzeigen\n3. Team speichern\n4. Team laden\n5. Exit");
            System.out.print("Ihre Eingabe: ");
            int cmd = Integer.parseInt(scan.nextLine());

            switch (cmd) {
                case 1:
                    System.out.print("Geben Sie den Pokemon-Name oder -ID: ");
                    String nameOrId = scan.nextLine().trim().toLowerCase();
                    String url = "https://pokeapi.co/api/v2/pokemon/" + nameOrId;

                    HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).GET().build();
                    try {
                        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                        if (response.statusCode() == 200) {
                            System.out.println("\nSuchergebnis:");
                            Pokemon pokemon = gson.fromJson(response.body(), Pokemon.class);
                            printInfo(pokemon);

                            System.out.println("Ins Team hinzufügen?  [J] / [N]");
                            String answer = scan.nextLine().trim().toLowerCase();
                            if (answer.equals("j")) {
                                team.add(pokemon);
                                System.out.println("\nSpieler hinzugefügt.");
                            }
                        }
                    } catch (IOException | InterruptedException e) {
                        System.out.println("Fehler beim Abfragen" + e.getMessage());
                    }
                    break;

                case 2:
                    if (team.isEmpty()) {
                        System.out.println("\nIhr Team ist leer");
                    } else {
                        System.out.println("\nIhr Team:");
                        team.stream().forEach(p -> System.out.println(p.getName()));
                    }
                    break;

                case 3:
                    System.out.print("In die Datei speichern: ");
                    String savefile = scan.nextLine().trim();
                    try {
                        //mapper.findAndRegisterModules();
                        mapper.writeValue(new File(savefile), team);
                        System.out.println("Als \"" + savefile + "\" gespeichert");
                    } catch (IOException e) {
                        System.out.println("Fehler beim Speichern (" + e.getMessage() + ")");
                    }
                    break;

                case 4:
                    System.out.print("Aus der Datei laden: ");
                    String loadfile = scan.nextLine().trim();
                    try {
                        List<Pokemon> loaded = mapper.readValue(new File(loadfile), new TypeReference<List<Pokemon>>() {});
                        team.clear();
                        team.addAll(loaded);
                        System.out.println("Team geladen");
                    } catch (IOException e) {
                        System.out.println("Fehler beim Laden (" + e.getMessage() + ")");
                    }
                    break;

                case 5:
                    System.out.println("Ciao!");
                    return;

                default:
                    System.out.println("Unbekanntes Befehl, versuchen Sie noch einmal!\n\n");
                    break;
            }
        }
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
