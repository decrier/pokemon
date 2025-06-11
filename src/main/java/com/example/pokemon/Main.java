package com.example.pokemon;

import com.example.pokemon.api.PokemonApiAbrufer;
import com.example.pokemon.cli.CommandHandler;
import com.example.pokemon.persistence.TeamRepositoryImpl;
import com.example.pokemon.service.TeamService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.net.http.HttpClient;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        HttpClient client = HttpClient.newHttpClient();
        PokemonApiAbrufer api = new PokemonApiAbrufer(client, gson);
        TeamRepositoryImpl repo = new TeamRepositoryImpl(gson);
        Scanner scan = new Scanner(System.in);

        TeamService teamService = new TeamService(api, repo);
        CommandHandler commandHandler = new CommandHandler(api, teamService, scan);

        boolean flag = true;
        while (flag) {
            flag = commandHandler.handle();
        }
    }
}
