package com.dib.service.advancement;

import com.dib.model.Player;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class AdvancementCache {
    private final Map<UUID, Player> playerAdvancements = new ConcurrentHashMap<>();

    public Map<UUID, Player> get() {
        return playerAdvancements;
    }

    public Player get(UUID id) {
        return playerAdvancements.get(id);
    }

    public void setPlayerAdvancement(UUID id, Player player) {
        playerAdvancements.put(id, player);
    }
}
