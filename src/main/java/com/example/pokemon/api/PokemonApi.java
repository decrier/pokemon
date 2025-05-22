package com.example.pokemon.api;

import com.example.pokemon.model.Pokemon;
import com.example.pokemon.model.TypeInfo;

import java.io.IOException;

public interface PokemonApi {
    Pokemon getJsonString(String nameOrId) throws IOException;


    // Gibt die Schwächen/Stärken eines Typs zurück
    TypeInfo getTypeInfo(String typeName) throws IOException;


}
