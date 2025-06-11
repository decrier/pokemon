package com.example.pokemon.persistence;

import com.example.pokemon.database.Database;
import com.example.pokemon.model.Pokemon;
import com.google.gson.Gson;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TeamRepositoryImpl implements  TeamRepository{
    private Gson gson;

    public TeamRepositoryImpl(Gson gson) {
        this.gson = gson;
    }

    @Override
    public void saveTeam(List<Pokemon> pokemons, String teamName) throws SQLException {
        try(Connection conn = Database.connect()) {
            conn.setAutoCommit(false);

            // Insert or get Team-ID
            int teamId;
            String sqlId = "INSERT INTO teams (name) VALUES (?)" +
                        "ON CONFLICT (name) DO UPDATE SET created_at = now() RETURNING id";
            try (PreparedStatement stmt = conn.prepareStatement(sqlId)) {
                stmt.setString(1, teamName);
                try (ResultSet rs = stmt.executeQuery()) {
                    rs.next();
                    teamId = rs.getInt(1);
                }
            }

            // Delete all "old" Pokemons
            String sqlDelete = "DELETE FROM team_pokemon WHERE team_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sqlDelete)){
                stmt.setInt(1, teamId);
                stmt.executeUpdate();
            }

            // Insert "new" Pokemons
            String insert = "INSERT INTO team_pokemon (team_id, pokemon_id, name, data) VALUES (?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(insert)){
                for (Pokemon p: pokemons) {
                    stmt.setInt(1, teamId);
                    stmt.setInt(2, p.getId());
                    stmt.setString(3, p.getName());
                    stmt.setObject(4, gson.toJson(p), Types.OTHER);
                    stmt.addBatch();
                }
                stmt.executeBatch();
            }
            conn.commit();
        }
    }

    @Override
    public List<Pokemon> loadTeam(String teamname) throws SQLException{
        List<Pokemon> result = new ArrayList<>();
        String sql = "SELECT tp.data FROM team_pokemon tp JOIN teams t on tp.team_id = t.id WHERE t.name  = ?";

        try(Connection conn = Database.connect()) {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, teamname);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String json = rs.getString("data");
                Pokemon p = gson.fromJson(json, Pokemon.class);
                result.add(p);
            }
        }
        return result;
    }

    @Override
    public List<String> listTeams() throws SQLException {
        List<String> names = new ArrayList<>();
        String sqlList = "SELECT name FROM teams ORDER BY created_at DESC";
        try (Connection conn = Database.connect()){
            PreparedStatement stmt = conn.prepareStatement(sqlList);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                names.add(rs.getString(1));
            }
        }
        return names;
    }

    @Override
    public void deleteTeam(String teamName) throws SQLException {
        String sqlDelete = "DELETE FROM teams WHERE name = ?";
        try (Connection conn = Database.connect()){
            PreparedStatement stmt = conn.prepareStatement(sqlDelete);
            stmt.setString(1, teamName);
            stmt.executeUpdate();
        }
    }
}
