package com.dib.model;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public record Player(UUID id, String name, List<PlayerAdvancement> advancements) {

    @Override
    public String toString() {
        return "Player{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", advancements=" + formatAdvancements() +
                '}';
    }

    private String formatAdvancements() {
        return advancements.stream()
                .map(PlayerAdvancement::toString)
                .collect(Collectors.joining(", ", "[", "]"));
    }
}
