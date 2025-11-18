package com.dib.model;

import java.util.Comparator;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

public class PlayerAdvancement {
    private final UUID id;
    private final String name;
    private final Map<String, PlayerAdvancementProgress> advancements;

    public static final Comparator<PlayerAdvancement> BY_ADVANCEMENT_COUNT_AND_DATE = (p1, p2) -> {
        int countComparison = Integer.compare(p2.getAdvancements().size(), p1.getAdvancements().size());
        if (countComparison != 0) {
            return countComparison;
        }

        Date earliestDate1 = p1.getEarliestAwardedDate();
        Date earliestDate2 = p2.getEarliestAwardedDate();

        if (earliestDate1 == null && earliestDate2 == null) return 0;
        if (earliestDate1 == null) return 1;
        if (earliestDate2 == null) return -1;

        return earliestDate1.compareTo(earliestDate2);
    };

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

    public Date getEarliestAwardedDate() {
        return advancements.values().stream()
                .map(PlayerAdvancementProgress::awardedDate)
                .filter(Objects::nonNull)
                .min(Date::compareTo)
                .orElse(null);
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
