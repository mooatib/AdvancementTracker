package com.dib.service.advancement;

import com.dib.model.PlayerAdvancement;
import com.dib.repository.AdvancementRepository;

import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class AdvancementCache {
    private final Map<UUID, PlayerAdvancement> playerAdvancements;
    private final AdvancementRepository repository;

    public AdvancementCache(AdvancementRepository repository) {
        this.repository = repository;
        this.playerAdvancements = repository.load();
    }

    public Map<UUID, PlayerAdvancement> get() {
        return playerAdvancements;
    }

    //TODO : Remove unused
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
        repository.save(id, playerAdvancement);
    }
}
