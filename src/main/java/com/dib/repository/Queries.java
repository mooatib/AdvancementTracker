package com.dib.repository;

public class Queries {

    private static final String CREATE_TABLE = """
            CREATE TABLE IF NOT EXISTS player_advancements (
                player_id TEXT PRIMARY KEY,
                player_name TEXT NOT NULL,
                advancements_data TEXT NOT NULL,
                last_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            )
            """;

    private static final String UPSERT_PLAYER_ADVANCEMENT = """
            INSERT INTO player_advancements (player_id, player_name, advancements_data, last_updated)
            VALUES (?, ?, ?, CURRENT_TIMESTAMP)
            ON CONFLICT(player_id) DO UPDATE SET
                player_name = excluded.player_name,
                advancements_data = excluded.advancements_data,
                last_updated = CURRENT_TIMESTAMP
            """;

    private static final String SELECT_ALL_PLAYER_ADVANCEMENTS =
            "SELECT player_id, player_name, advancements_data FROM player_advancements";

    public static String getCreateTableQuery() {
        return CREATE_TABLE;
    }

    public static String getUpsertPlayerAdvancementQuery() {
        return UPSERT_PLAYER_ADVANCEMENT;
    }

    public static String getSelectAllPlayerAdvancementsQuery() {
        return SELECT_ALL_PLAYER_ADVANCEMENTS;
    }
}
