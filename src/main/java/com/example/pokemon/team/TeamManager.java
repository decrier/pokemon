package com.example.pokemon.team;

import com.example.pokemon.model.Pokemon;
import com.google.gson.JsonSyntaxException;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public interface TeamManager {
    void save(List<Pokemon> team, String savefile) throws IOException;
    List<Pokemon> load(String loadfile) throws IOException, JsonSyntaxException;
}
