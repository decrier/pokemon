package com.example.pokemon.web;

import com.example.pokemon.api.PokemonApiAbrufer;
import com.example.pokemon.cli.CommandHandler;
import com.example.pokemon.model.Pokemon;
import com.example.pokemon.service.TeamService;
import com.example.pokemon.team.TeamManagerImpl;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.net.http.HttpClient;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import static spark.Spark.*;

public class WebServer {
    public static void main(String[] args) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        HttpClient client = HttpClient.newHttpClient();
        ObjectMapper mapper = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        Scanner scan = new Scanner(System.in);


        Path saveDir = Paths.get("saves");
        if (Files.notExists(saveDir)) {
            try {
                Files.createDirectories(saveDir);
            } catch(IOException e) {
                System.out.println("Fehler beim Erstellen des Ordners" + e.getMessage());
            }
        }

        PokemonApiAbrufer apiAbrufer = new PokemonApiAbrufer(client, gson);
        TeamManagerImpl tm = new TeamManagerImpl(gson, mapper);
        TeamService teamService = new TeamService(tm, apiAbrufer, saveDir);
        CommandHandler commandHandler = new CommandHandler(apiAbrufer, teamService, scan);

        port(8080);
        staticFiles.location("/static");

        // Suche nach Name or ID
        get("/api/pokemon/:nameOrId", (req, res) -> {
            String nameOrId = req.params("nameOrId");
            try {
                Pokemon p = apiAbrufer.getJsonString(nameOrId);
                res.type("application/json");
                return gson.toJson(p);
            } catch (IOException e) {
                res.status(404);
                return gson.toJson(Map.of("error", "Pokemon nicht gefunden"));
            }
        });

        //Suche nach Typ
        get("/api/type/:typeName", (req, res) -> {
            String typeName = req.params("typeName").toLowerCase();
            try {
                List<String> pokemons = apiAbrufer.getByType(typeName);
                res.type("application/json");
                return gson.toJson(pokemons);
            } catch (IOException e) {
                res.status(404);
                return gson.toJson(Map.of("error", "Typ nicht gefunden"));
            }
        });

        // Team anzeigen
        get("/api/team", (req, res) -> {
            List<Pokemon> team = teamService.getTeam();
            res.type("application/json");
            return gson.toJson(team);
        });

        // ins Team hinzufügen
        post("/api/team", (req, res ) -> {
            Map<?, ? > body = gson.fromJson(req.body(), Map.class);
            String nameOrId = ((String) body.get("nameOrId")).trim().toLowerCase();
            try {
                Pokemon p = apiAbrufer.getJsonString(nameOrId);
                teamService.add(p);
                res.status(201);
                res.type("application/json");
                return gson.toJson(p);
            } catch (IOException e) {
                res.status(400);
                return gson.toJson(Map.of("error", "Pokemon nicht gefunden"));
            }
        });

        // Aus dem Team Löschen
        post("/api/pokemon/delete", (req, res) -> {
            Map<?, ? > body = gson.fromJson(req.body(), Map.class);
            String nameOrId = ((String) body.get("nameOrId")).trim().toLowerCase();
            try {
                Pokemon p = apiAbrufer.getJsonString(nameOrId);
                if(teamService.getTeam().contains(p)) {
                    teamService.delete(p);
                    res.status(201);
                    res.type("application/json");
                    return gson.toJson(Map.of("result", p.getName() +" gelöscht"));
                } else {
                    return gson.toJson(Map.of("result", p.getName() + " war nicht in deinem Team"));
                }
            } catch (IOException e) {
                res.status(404);
                return gson.toJson(Map.of("error", "Pokemon nicht gefunden"));
            }
        });

        // Save Team
        post("/api/team/save", (req, res) -> {
            Map<?, ?> body = gson.fromJson(req.body(), Map.class);
            String filename = ((String) body.get("filename")).trim() + ".json";
            try {
                String fullPath = "saves/" + filename;
                teamService.saveTeam(fullPath);
                res.type("application/json");
                return gson.toJson(Map.of("status", "saved", "file", filename));
            } catch (IOException e) {
                res.status(500);
                return gson.toJson(Map.of("Error", "Fehler beim Speichern"));
            }
        });

        get("/", (req, res) -> {
           res.redirect("/index.html");
           return null;
        });
    }
}
