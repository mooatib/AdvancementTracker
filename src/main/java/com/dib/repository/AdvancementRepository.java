package com.dib.repository;

import com.dib.model.AdvancementMetadata;
import com.dib.model.PlayerAdvancement;
import com.dib.model.PlayerAdvancementProgress;

import java.io.File;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AdvancementRepository {
    private final Logger logger;
    private final AdvancementDatabase advancementDatabase;

    public AdvancementRepository(Logger logger, File resourcesFolder) {
        this.logger = logger;
        this.advancementDatabase = new AdvancementDatabase(logger, resourcesFolder, "player-advancements.db");
        this.advancementDatabase.initializeDatabase();
    }

    public boolean isAdvancementExisting(String id) {
        try {
            Connection conn = advancementDatabase.getConnection();
            PreparedStatement stmt = conn.prepareStatement(Queries.COUNT_ADVANCEMENT_ID);
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();
            boolean exists = rs.getInt(1) == 1;
            rs.close();
            stmt.close();
            return exists;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed check if advancement " + id + " exists", e);
            return false;
        }
    }

    public int countAdvancements() {
        try {
            Connection conn = advancementDatabase.getConnection();
            ResultSet rs = conn.createStatement().executeQuery(Queries.COUNT_ADVANCEMENT);
            int count = rs.getInt(1);
            rs.close();
            return count;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed counting advancements exists", e);
            return 0;
        }
    }

    public void saveAdvancement(String key, AdvancementMetadata advancement) {
        try {
            Connection conn = advancementDatabase.getConnection();
            conn.setAutoCommit(false);

            try {
                PreparedStatement advStmt = conn.prepareStatement(Queries.INSERT_ADVANCEMENT);

                String title = advancement.title();
                String description = advancement.description();
                String iconName = advancement.iconName();

                // Insert advancement data
                advStmt.setString(1, key);
                advStmt.setString(2, title);
                advStmt.setString(3, description);
                advStmt.setString(4, iconName);
                advStmt.executeUpdate();
                advStmt.close();

                conn.commit();
                logger.info("Successfully saved advancement : " + key);
            } catch (Exception e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to save advancement " + key, e);
        }
    }


    public void savePlayerAdvancement(UUID playerId, String playerName, PlayerAdvancementProgress advancement) {
        try {
            Connection conn = advancementDatabase.getConnection();
            conn.setAutoCommit(false);

            try {
                PreparedStatement playerStmt = conn.prepareStatement(Queries.UPSERT_PLAYER);
                playerStmt.setString(1, playerId.toString());
                playerStmt.setString(2, playerName);
                playerStmt.executeUpdate();
                playerStmt.close();

                PreparedStatement paStmt = conn.prepareStatement(Queries.INSERT_PLAYER_ADVANCEMENT);
                paStmt.setString(1, playerId.toString());
                paStmt.setString(2, advancement.key());

                //TODO : Refactor
                paStmt.setTimestamp(3, new Timestamp(Timestamp.valueOf(LocalDateTime.now()).getTime()));

                paStmt.executeUpdate();

                paStmt.close();

                conn.commit();
                logger.info("Successfully saved advancement for player: " + playerName);
            } catch (Exception e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to save player advancement for " + playerId, e);
        }
    }

    public Map<UUID, PlayerAdvancement> load() {
        Map<UUID, PlayerAdvancement> playerAdvancements = new LinkedHashMap<>();

        try {
            Connection conn = advancementDatabase.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(Queries.SELECT_ALL_PLAYER_ADVANCEMENTS);

            while (rs.next()) {
                UUID playerId = UUID.fromString(rs.getString("player_id"));
                String playerName = rs.getString("player_name");

                PlayerAdvancement playerAdvancement = playerAdvancements.computeIfAbsent(
                        playerId,
                        id -> new PlayerAdvancement(playerId, playerName, new LinkedHashMap<>())
                );

                String advancementId = rs.getString("fk_advancement_id");
                if (advancementId != null) {
                    Timestamp awardedDate = rs.getTimestamp("awarded_date");
                    String title = rs.getString("title");
                    String description = rs.getString("description");
                    String iconName = rs.getString("icon_name");

                    PlayerAdvancementProgress progress = new PlayerAdvancementProgress(
                            advancementId,
                            new AdvancementMetadata(title, description, iconName),
                            true,
                            awardedDate
                    );

                    playerAdvancement.getAdvancements().put(advancementId, progress);
                }
            }

            rs.close();
            stmt.close();

            logger.info("Successfully loaded " + playerAdvancements.size() + " player advancements from database");
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to load player advancements from database", e);
        }

        return playerAdvancements;
    }

    public void close() {
        advancementDatabase.closeConnection();
    }
}
