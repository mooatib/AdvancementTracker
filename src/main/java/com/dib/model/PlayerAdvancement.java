package com.dib.model;

import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class PlayerAdvancement {
    private final UUID id;
    private final String name;
    private final Map<String, PlayerAdvancementProgress> advancements;
    private final int totalAdvancements;
    private final double progressPercentage;

    public PlayerAdvancement(UUID id, String name, Map<String, PlayerAdvancementProgress> advancements) {
        this.id = id;
        this.name = name;
        this.advancements = advancements;
        this.totalAdvancements = advancements.size();
        this.progressPercentage = computeProgressPercentage();
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Map<String, PlayerAdvancementProgress> getAdvancements() {
        return advancements;
    }

    public int getTotalAdvancements() {
        return totalAdvancements;
    }

    public double getProgressPercentage() {
        return progressPercentage;
    }

    private double computeProgressPercentage() {
        return totalAdvancements > 0
                ? (completedAdvancements().size() * 100.0 / totalAdvancements)
                : 0.0;
    }

    public Map<String, PlayerAdvancementProgress> completedAdvancements() {
        return advancements.entrySet().stream()
                .filter(advEntry -> advEntry.getValue().completed())
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
