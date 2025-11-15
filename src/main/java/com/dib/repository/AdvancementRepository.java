package com.dib.repository;

import com.dib.model.PlayerAdvancement;
import com.dib.model.PlayerAdvancementProgress;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.sql.*;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AdvancementRepository {
    private final Logger logger;
    private final ObjectMapper objectMapper;
    private final AdvancementDatabase advancementDatabase;

    public AdvancementRepository(Logger logger, File resourcesFolder) {
        this.logger = logger;
        this.objectMapper = new ObjectMapper();
        this.advancementDatabase = new AdvancementDatabase(logger, resourcesFolder, "player-advancements.db");
        this.advancementDatabase.initializeDatabase();
    }

    public void save(UUID playerId, PlayerAdvancement playerAdvancement) {
        try {
            Connection conn = advancementDatabase.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(Queries.getUpsertPlayerAdvancementQuery());

            pstmt.setString(1, playerId.toString());
            pstmt.setString(2, playerAdvancement.getName());
            pstmt.setString(3, serializeAdvancements(playerAdvancement.getAdvancements()));
            pstmt.executeUpdate();
            pstmt.close();

            logger.info("Successfully saved advancement for player: " + playerAdvancement.getName());
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Failed to save player advancement for " + playerId, e);
        }
    }

    public Map<UUID, PlayerAdvancement> load() {
        Map<UUID, PlayerAdvancement> playerAdvancements = new ConcurrentHashMap<>();

        try {
            Connection conn = advancementDatabase.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(Queries.getSelectAllPlayerAdvancementsQuery());

            while (rs.next()) {
                UUID playerId = UUID.fromString(rs.getString("player_id"));
                String playerName = rs.getString("player_name");
                String advancementsJson = rs.getString("advancements_data");

                Map<String, PlayerAdvancementProgress> advancements = deserializeAdvancements(advancementsJson);
                PlayerAdvancement playerAdvancement = new PlayerAdvancement(playerId, playerName, advancements);
                playerAdvancements.put(playerId, playerAdvancement);
            }

            rs.close();
            stmt.close();

            logger.info("Successfully loaded " + playerAdvancements.size() + " player advancements from database");
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Failed to load player advancements from database", e);
        }

        return playerAdvancements;
    }

    public void close() {
        advancementDatabase.closeConnection();
    }

    private String serializeAdvancements(Map<String, PlayerAdvancementProgress> advancements) {
        try {
            return objectMapper.writeValueAsString(advancements);
        } catch (JsonProcessingException e) {
            logger.log(Level.SEVERE, "Failed to serialize advancements", e);
            return "{}";
        }
    }

    private Map<String, PlayerAdvancementProgress> deserializeAdvancements(String json) {
        try {
            return objectMapper.readValue(json, new TypeReference<Map<String, PlayerAdvancementProgress>>() {
            });
        } catch (JsonProcessingException e) {
            logger.log(Level.SEVERE, "Failed to deserialize advancements", e);
            return new ConcurrentHashMap<>();
        }
    }
}
