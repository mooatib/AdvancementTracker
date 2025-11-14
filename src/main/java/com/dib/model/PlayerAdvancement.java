package com.dib.model;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public record PlayerAdvancement(UUID id, String name, List<PlayerAdvancementProgress> advancements) {

    @Override
    public String toString() {
        return "PlayerAdvancement{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", advancements=" + formatAdvancements() +
                '}';
    }

    private String formatAdvancements() {
        return advancements.stream()
                .map(PlayerAdvancementProgress::toString)
                .collect(Collectors.joining(", ", "[", "]"));
    }
}
