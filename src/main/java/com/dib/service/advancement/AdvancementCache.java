package com.dib.service.advancement;

import com.dib.model.PlayerAdvancement;
import com.dib.model.PlayerAdvancementProgress;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AdvancementCache {
    private final Map<UUID, PlayerAdvancement> playerAdvancements;

    public AdvancementCache(Map<UUID, PlayerAdvancement> playerAdvancements) {
        this.playerAdvancements = playerAdvancements;
    }

    public Map<UUID, PlayerAdvancement> get() {
        return playerAdvancements;
    }

    public PlayerAdvancement get(UUID id) {
        return playerAdvancements.get(id);
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
}
