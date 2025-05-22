package com.example.pokemon.team;

import com.example.pokemon.model.Pokemon;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Path;
import java.util.List;

public class TeamManagerImpl implements TeamManager {

    private final Gson gson;
    private final ObjectMapper mapper;

    public TeamManagerImpl(Gson gson, ObjectMapper mapper) {
        this.gson = gson;
        this.mapper = mapper;
    }

    @Override
    public void save(List<Pokemon> team, String savefile) throws IOException {
        try {
            mapper.writeValue(new File(savefile), team);
        } catch (IOException e) {
            System.out.println("Fehler beim Speichern (" + e.getMessage() + ")");
        }
    }

    @Override
    public List<Pokemon> load(String loadfile) throws JsonSyntaxException {
        try {
            List<Pokemon> loaded = mapper.readValue(new File(loadfile), new TypeReference<List<Pokemon>>() {});
            if (loaded == null) {
                throw  new JsonSyntaxException("Invalid JSON oder Team leer");
            }
            return loaded;
        } catch (IOException e) {
            System.out.println("Fehler beim Laden (" + e.getMessage() + ")");
        }
        return null;
    }

}
