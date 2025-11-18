package com.dib.repository;

public class Queries {

    public static final String CREATE_PLAYER_TABLE = """
            CREATE TABLE IF NOT EXISTS players (
                player_id TEXT PRIMARY KEY,
                player_name TEXT NOT NULL
            )
            """;

    public static final String CREATE_ADVANCEMENT_TABLE = """
            CREATE TABLE IF NOT EXISTS advancements (
                advancement_id TEXT PRIMARY KEY,
                title TEXT NOT NULL,
                description TEXT NOT NULL,
                icon_name TEXT NOT NULL
            )
            """;

    public static final String CREATE_PLAYER_ADVANCEMENT_TABLE = """
            CREATE TABLE IF NOT EXISTS player_advancements (
                fk_player_id TEXT NOT NULL,
                fk_advancement_id TEXT NOT NULL,
                awarded_date DATETIME NOT NULL,
                PRIMARY KEY (fk_advancement_id, fk_player_id),
                FOREIGN KEY (fk_player_id) REFERENCES players(player_id),
                FOREIGN KEY (fk_advancement_id) REFERENCES advancements(advancement_id)
            )
            """;

    public static final String UPSERT_PLAYER = """
            INSERT INTO players (player_id, player_name)
            VALUES (?, ?)
            ON CONFLICT(player_id) DO UPDATE SET
                player_name = excluded.player_name
            """;

    public static final String INSERT_ADVANCEMENT = """
            INSERT INTO advancements (advancement_id, title, description, icon_name)
            VALUES (?, ?, ?, ?)
            """;

    public static final String COUNT_ADVANCEMENT_ID = """
            SELECT COUNT(*) FROM advancements WHERE advancement_id = ?
            """;

    public static final String COUNT_ADVANCEMENT = """
            SELECT COUNT(*) FROM advancements
            """;

    public static final String INSERT_PLAYER_ADVANCEMENT = """
            INSERT OR REPLACE INTO player_advancements (fk_player_id, fk_advancement_id, awarded_date)
            VALUES (?, ?, ?)
            """;

    public static final String SELECT_ALL_PLAYER_ADVANCEMENTS = """
            SELECT
                p.player_id,
                p.player_name,
                pa.fk_advancement_id,
                pa.awarded_date,
                a.title,
                a.description,
                a.icon_name
            FROM players p
            LEFT JOIN player_advancements pa ON p.player_id = pa.fk_player_id
            LEFT JOIN advancements a ON pa.fk_advancement_id = a.advancement_id
            ORDER BY
                (SELECT COUNT(*)
                FROM player_advancements pa2
                WHERE pa2.fk_player_id = p.player_id) DESC,
                pa.awarded_date
            """;
}
