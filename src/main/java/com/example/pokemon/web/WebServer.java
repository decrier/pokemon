package com.example.pokemon.web;

import com.example.pokemon.api.PokemonApiAbrufer;
import com.example.pokemon.database.Database;
import com.example.pokemon.model.Pokemon;
import com.example.pokemon.persistence.TeamRepositoryImpl;
import com.example.pokemon.service.TeamService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.net.http.HttpClient;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static spark.Spark.*;

public class WebServer {
    public static void main(String[] args) throws SQLException {

        //Connection conn = Database.connect();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        HttpClient client = HttpClient.newHttpClient();

        PokemonApiAbrufer api = new PokemonApiAbrufer(client, gson);
        TeamRepositoryImpl repo = new TeamRepositoryImpl(gson);
        TeamService teamService = new TeamService(api, repo);

        port(8080);
        staticFiles.location("/static");

        // Suche nach Name or ID
        get("/api/pokemon/:nameOrId", (req, res) -> {
            String nameOrId = req.params("nameOrId");
            try {
                Pokemon p = api.getJsonString(nameOrId);
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
                List<String> pokemons = api.getByType(typeName);
                res.type("application/json");
                return gson.toJson(pokemons);
            } catch (IOException e) {
                res.status(404);
                return gson.toJson(Map.of("error", "Typ nicht gefunden"));
            }
        });

        // Suche nach Schwäche oder Stärke
        get("/api/typeinfo/weaks/:typeName", (req, res) -> {
           String typeName = req.params("typeName").toLowerCase();
           try {
               List<String> weaks = teamService.searchWeaks(typeName);
               res.type("application/json");
               return gson.toJson(weaks);
           } catch (IOException e) {
               res.status(404);
               return gson.toJson(Map.of("error", "Typ nicht gefunden"));
           }
        });

        get("/api/typeinfo/strongs/:typeName", (req, res) -> {
            String typeName = req.params("typeName").toLowerCase();
            try {
                List<String> strongs = teamService.searchStrongs(typeName);
                res.type("application/json");
                return gson.toJson(strongs);
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
                Pokemon p = api.getJsonString(nameOrId);
                teamService.add(p);
                res.status(201);
                res.type("application/json");
                return gson.toJson(p);
            } catch (IOException e) {
                res.status(400);
                return gson.toJson(Map.of("error", "Pokemon nicht gefunden"));
            }
        });

        // Team clear
        get("api/team/clear", (req, res) ->{
            teamService.clear();
            res.type("application/json");
            return gson.toJson(Map.of("status", "list is empty"));
        });

        // Aus dem Team Löschen
        post("/api/pokemon/delete", (req, res) -> {
            Map<?, ? > body = gson.fromJson(req.body(), Map.class);
            String nameOrId = ((String) body.get("nameOrId")).trim().toLowerCase();
            try {
                Pokemon p = api.getJsonString(nameOrId);
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
        post("/api/teams/:name", (req, res) -> {
            String name = req.params("name");
            try {
                teamService.save(name);
                res.status(201);
                return gson.toJson(Map.of("status", "saved", "team", name));
            } catch (SQLException e) {
                res.status(500);
                return gson.toJson(Map.of("error", "Speicherfehler"));
            }
        });

        // Load Team
        get("/api/teams/:name", (req, res) -> {
            String name = req.params("name");
            try {
                teamService.load(name);
                List<Pokemon> pokemons = teamService.getTeam();
                res.type("application/json");
                return gson.toJson(pokemons);
            }catch (SQLException e) {
                res.status(404);
                return gson.toJson(Map.of("error", "Team nicht gefunden"));
            }
        });

        // analyse team
        get("/api/team/weakness", (req, res) -> {
            Map<String, Integer> strongs = teamService.searchStrongsForTeam();
            res.type("application/json");
            return gson.toJson(strongs);
        });
        get("/api/team/strength", (req, res) -> {
            Map<String, Integer> strongs = teamService.searchWeaksForTeam();
            res.type("application/json");
            return gson.toJson(strongs);
        });

        // Show Team List
        get("/api/team/l", (req, res) -> {
            List<String> teams = teamService.showTeamList();
            res.type("application/json");

            return gson.toJson(teams);
        });

        // Delete Team
        delete("/api/teams/:name", (req, res) -> {

            String name = req.params("name");
            try {
                teamService.deleteTeam(name);
                res.status(201);
                res.type("application/json");
                return gson.toJson(Map.of("status", "deleted", "team", name));
            } catch (SQLException e) {
                res.status(500);
                return gson.toJson(Map.of("error", "Löschfehler"));
            }
        });

        get("/", (req, res) -> {
           res.redirect("/index.html");
           return null;
        });
    }
}
