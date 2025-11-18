package com.dib.service.advancement;

import com.dib.model.PlayerAdvancement;
import com.dib.model.PlayerAdvancementProgress;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class AdvancementCache {
    private final Map<UUID, PlayerAdvancement> playerAdvancements;

    public AdvancementCache(Map<UUID, PlayerAdvancement> playerAdvancements) {
        this.playerAdvancements = playerAdvancements;
    }

    public PlayerAdvancement get(UUID id) {
        return playerAdvancements.get(id);
    }

    public Map<UUID, PlayerAdvancement> getSorted() {
        return playerAdvancements.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(PlayerAdvancement.BY_ADVANCEMENT_COUNT_AND_DATE))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
    }

    public void putPlayerAdvancement(UUID id, String name, PlayerAdvancementProgress advancement) {
        if (playerAdvancements.containsKey(id)) {
            PlayerAdvancement existing = get(id);

            Map<String, PlayerAdvancementProgress> updatedAdvancements = new HashMap<>(existing.getAdvancements());
            updatedAdvancements.put(advancement.key(), advancement);

            playerAdvancements.put(id, new PlayerAdvancement(id, name, updatedAdvancements));
        } else {
            playerAdvancements.put(id, new PlayerAdvancement(id, name, Map.of(advancement.key(), advancement)));
        }
    }

    public void updatePlayerEntry(UUID playerId, String playerName, PlayerAdvancementProgress advancement) {
        if (playerAdvancements.containsKey(playerId)) {
            PlayerAdvancement existing = get(playerId);
            Map<String, PlayerAdvancementProgress> updatedAdvancements = new HashMap<>(existing.getAdvancements());
            updatedAdvancements.put(advancement.key(), advancement);
            playerAdvancements.put(playerId, new PlayerAdvancement(playerId, playerName, updatedAdvancements));
        } else {
            Map<String, PlayerAdvancementProgress> newAdvancements = new HashMap<>();
            newAdvancements.put(advancement.key(), advancement);
            playerAdvancements.put(playerId, new PlayerAdvancement(playerId, playerName, newAdvancements));
        }
    }

    public void reload(Map<UUID, PlayerAdvancement> advancements) {
        playerAdvancements.clear();
        playerAdvancements.putAll(advancements);
    }
}
