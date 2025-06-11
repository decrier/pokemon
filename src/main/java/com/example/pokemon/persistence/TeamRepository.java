package com.example.pokemon.persistence;

import com.example.pokemon.model.Pokemon;

import java.sql.SQLException;
import java.util.List;

public interface TeamRepository {
    void saveTeam(List<Pokemon> team, String filename) throws SQLException;
    List<Pokemon> loadTeam(String filename) throws SQLException;
    List<String> listTeams() throws SQLException;
    void deleteTeam(String teamName) throws SQLException;
}
