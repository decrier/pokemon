package com.example.pokemon.service;

import com.example.pokemon.api.PokemonApiAbrufer;
import com.example.pokemon.model.Pokemon;
import com.example.pokemon.model.TypeInfo;
import com.example.pokemon.team.TeamManagerImpl;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

public class TeamService {
    private final List<Pokemon> team = new ArrayList<>();
    private final TeamManagerImpl teamManager;
    private final PokemonApiAbrufer api;
    private final Path saveDir;

    public TeamService(TeamManagerImpl teamManager, PokemonApiAbrufer api, Path saveDir) {
        this.teamManager = teamManager;
        this.api = api;
        this.saveDir = saveDir;
    }

    public void add(Pokemon p) {
        team.add(p);
    }

    public List<Pokemon> getTeam() {
        return team;
    }

    public void saveTeam(String savefile) throws IOException {
        teamManager.save(team, savefile);
    }

    public void loadTeam(String loadfile) throws IOException {
        List<Pokemon> loaded = teamManager.load(loadfile);
        team.clear();
        team.addAll(loaded);
    }

    public List<String> searchByType(String typeName) throws IOException {
        return api.getByType(typeName);
    }

    public List<String> searchStrongWeak (String strongAgainst, String weakAgainst) throws IOException{
        TypeInfo strongInfo = api.getTypeInfo(strongAgainst);
        TypeInfo weakInfo = api.getTypeInfo(weakAgainst);

        Set<String> strongSet = strongInfo.getDamageRelations().getDoubleDamageTo()
                                            .stream().map(TypeInfo.TypeRef::getName)
                                            .collect(Collectors.toSet());

        Set<String> weakSet = weakInfo.getDamageRelations().getDoubleDamageFrom()
                                            .stream().map(TypeInfo.TypeRef::getName)
                                            .collect(Collectors.toSet());
        return strongSet.stream()
                .filter(weakSet::contains)
                .collect(Collectors.toList());
    }

    public List<String> searchWeaks (String typeName) throws IOException{
        TypeInfo typeInfo = api.getTypeInfo(typeName);

        return typeInfo.getDamageRelations().getDoubleDamageTo()
                            .stream().map(TypeInfo.TypeRef::getName)
                            .collect(Collectors.toList());
    }

    public List<String> searchStrongs (String typeName) throws IOException{
        TypeInfo typeInfo = api.getTypeInfo(typeName);

        return typeInfo.getDamageRelations().getDoubleDamageFrom()
                .stream().map(TypeInfo.TypeRef::getName)
                .collect(Collectors.toList());
    }

    public Map<String, Integer> searchWeaksForTeam() throws  IOException{
        Map<String, Integer> strengthCount = new HashMap<>();

        for (Pokemon p : team) {

            for (Pokemon.TypeBox types : p.getTypes()) {
                String type = types.getType().getName();
                List<String> strongAgainst = searchWeaks(type);

                for (String target: strongAgainst) {
                    strengthCount.merge(target, 1, Integer::sum);
                }
            }
        }
        return strengthCount;
    }

    public Map<String, Integer> searchStrongsForTeam() throws  IOException{
        Map<String, Integer> weaknessCount = new HashMap<>();

        for (Pokemon p : team) {

            for (Pokemon.TypeBox types : p.getTypes()) {
                String type = types.getType().getName();
                List<String> weakAgainst = searchStrongs(type);

                for (String target: weakAgainst) {
                    weaknessCount.merge(target, 1, Integer::sum);
                }
            }
        }
        return weaknessCount;
    }

    public void analyseTeamWeakness () {
        try {
            Map<String, Integer> strongs = searchStrongsForTeam();
            System.out.println("Gegen folgenden Typen ist dein Team stark:");
            System.out.println("  Typ\t\tMenge\n");
            strongs.entrySet().stream().forEach(e -> System.out.printf("%s : \t\t%d\n", e.getKey(), e.getValue()));
        } catch (IOException e) {
            System.out.println("Fehler" + e.getMessage());
        }
    }

    public void analyseTeamStrength () {
        try {
            Map<String, Integer> weaks = searchWeaksForTeam();
            System.out.println("Gegen folgenden Typen ist dein Team schwach:");
            System.out.println("  Typ\t\tMenge\n");
            weaks.entrySet().stream().forEach(e -> System.out.printf("%s : \t\t%d\n", e.getKey(), e.getValue()));
        } catch (IOException e) {
            System.out.println("Fehler" + e.getMessage());
        }
    }

    public void showTeamList() {
        System.out.println("Teamliste:");
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(saveDir, "*.json")) {
            boolean empty = true;
            for (Path file : stream) {
                System.out.println(" " + file.getFileName());
                empty = false;
            }
            if (empty) {
                System.out.println("Teams nicht gefunden");
            }
        } catch (NoSuchFileException e) {
            System.out.println("Keine Team");
        } catch (IOException e) {
            System.out.println("Fehler beim Verzeichnislesen" + e.getMessage());
        }
    }


    public static void printInfo(Pokemon p){
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
