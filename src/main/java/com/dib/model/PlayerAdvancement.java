package com.dib.model;

import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class PlayerAdvancement {
    private final UUID id;
    private final String name;
    private final Map<String, PlayerAdvancementProgress> advancements;

    public PlayerAdvancement(UUID id, String name, Map<String, PlayerAdvancementProgress> advancements) {
        this.id = id;
        this.name = name;
        this.advancements = advancements;
    }

    public String getName() {
        return name;
    }

    public Map<String, PlayerAdvancementProgress> getAdvancements() {
        return advancements;
    }

    public Map<String, PlayerAdvancementProgress> completedAdvancements() {
        return advancements.entrySet().stream()
                .filter(advEntry -> advEntry.getValue().awarded())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    @Override
    public String toString() {
        return "PlayerAdvancement{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", advancements=" + formatAdvancements() +
                '}';
    }

    private String formatAdvancements() {
        return advancements.values().stream()
                .map(PlayerAdvancementProgress::toString)
                .collect(Collectors.joining(", ", "[", "]"));
    }
}
