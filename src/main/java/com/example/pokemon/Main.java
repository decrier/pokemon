package com.example.pokemon;

import com.example.pokemon.api.PokemonApiAbrufer;
import com.example.pokemon.cli.CommandHandler;
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
import java.util.Scanner;

public class Main {
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

        boolean flag = true;
        while (flag) {
            flag = commandHandler.handle();
        }
    }
}
