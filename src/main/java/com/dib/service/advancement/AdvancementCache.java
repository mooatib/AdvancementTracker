package com.dib.service.advancement;

import com.dib.model.PlayerAdvancement;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class AdvancementCache {
    private final Map<UUID, PlayerAdvancement> playerAdvancements = new ConcurrentHashMap<>();

    public Map<UUID, PlayerAdvancement> get() {
        return playerAdvancements;
    }

    public Map<UUID, PlayerAdvancement> getCompleted() {
        return playerAdvancements.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> {
                            PlayerAdvancement pa = entry.getValue();
                            return new PlayerAdvancement(pa.getId(), pa.getName(), pa.completedAdvancements());
                        }
                ));
    }

    public PlayerAdvancement get(UUID id) {
        return playerAdvancements.get(id);
    }

    public void setPlayerAdvancement(UUID id, PlayerAdvancement playerAdvancement) {
        playerAdvancements.put(id, playerAdvancement);
    }
}
