package com.example.pokemon.api;

import com.example.pokemon.model.Pokemon;

import java.io.IOException;

public interface PokemonApi {
    Pokemon getJsonString(String nameOrId) throws IOException;
}
