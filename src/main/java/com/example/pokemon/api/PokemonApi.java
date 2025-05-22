package com.example.pokemon.api;

import com.example.pokemon.model.Pokemon;
import com.example.pokemon.model.TypeInfo;

import java.io.IOException;
import java.util.List;

public interface PokemonApi {
    Pokemon getJsonString(String nameOrId) throws IOException;

    // Gibt die Pokemon-Liste eines bestimmten Typs zurück
    List<String> getByType (String typeName) throws IOException;

    // Gibt die Schwächen/Stärken eines Typs zurück
    TypeInfo getTypeInfo(String typeName) throws IOException;


}
