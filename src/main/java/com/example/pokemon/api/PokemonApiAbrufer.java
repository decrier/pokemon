package com.example.pokemon.api;

import com.example.pokemon.model.Pokemon;
import com.google.gson.Gson;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class PokemonApiAbrufer implements PokemonApi{

    private final HttpClient client;
    private final Gson gson;

    public PokemonApiAbrufer(HttpClient client, Gson gson) {
        this.client = client;
        this.gson = gson;
    }

    @Override
    public Pokemon getJsonString(String nameOrId) throws IOException {
        String url = "https://pokeapi.co/api/v2/pokemon/" + nameOrId;
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).GET().build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                throw new IOException("Pokemon not found");
            }
            return gson.fromJson( response.body(), Pokemon.class);
        } catch (IOException | InterruptedException e) {
            throw new IOException(e.getMessage());
        }
    }
}

