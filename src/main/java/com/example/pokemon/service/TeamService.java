package com.example.pokemon.service;

import com.example.pokemon.api.PokemonApiAbrufer;
import com.example.pokemon.model.Pokemon;
import com.example.pokemon.model.TypeInfo;
import com.example.pokemon.team.TeamManagerImpl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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

    public List<String> searchStrongWeak (String strongAgainst, String weakAgainst) throws IOException{
        TypeInfo strongInfo = api.getTypeInfo(strongAgainst);
        TypeInfo weakInfo = api.getTypeInfo(weakAgainst);

        Set<String> strongSet = strongInfo.getDamageRelations().getDoubleDamageTo()
                                            .stream().map(TypeInfo.TypeRef::getName)
                                            .collect(Collectors.toSet());

        Set<String> weakSet = weakInfo.getDamageRelations().getDoubleDamageFrom()
                                            .stream().map(TypeInfo.TypeRef::getName)
                                            .collect(Collectors.toSet());
        return strongSet.stream()
                .filter(weakSet::contains)
                .collect(Collectors.toList());
    }

    public List<String> searchWeaks (String typeName) throws IOException{
        TypeInfo typeInfo = api.getTypeInfo(typeName);

        return typeInfo.getDamageRelations().getDoubleDamageTo()
                            .stream().map(TypeInfo.TypeRef::getName)
                            .collect(Collectors.toList());
    }

    public List<String> searchStrongs (String typeName) throws IOException{
        TypeInfo typeInfo = api.getTypeInfo(typeName);

        return typeInfo.getDamageRelations().getDoubleDamageFrom()
                .stream().map(TypeInfo.TypeRef::getName)
                .collect(Collectors.toList());
    }

}
