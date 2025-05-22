package com.example.pokemon.service;

import com.example.pokemon.api.PokemonApiAbrufer;
import com.example.pokemon.model.Pokemon;
import com.example.pokemon.team.TeamManagerImpl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TeamService {
    private final List<Pokemon> team = new ArrayList<>();
    private final TeamManagerImpl teamManager;
    private final PokemonApiAbrufer api;

    public TeamService(TeamManagerImpl teamManager, PokemonApiAbrufer api) {
        this.teamManager = teamManager;
        this.api = api;
    }

    public void add(Pokemon p) {
        team.add(p);
    }

    public List<Pokemon> getTeam() {
        return team;
    }

    public void saveTeam(String savefile) throws IOException {
        teamManager.save(team, savefile);
    }

    public void loadTeam(String loadfile) throws IOException {
        List<Pokemon> loaded = teamManager.load(loadfile);
        team.clear();
        team.addAll(loaded);
    }

    public List<String> searchByType(String typeName) throws IOException {
        return api.getByType(typeName);
    }

}
