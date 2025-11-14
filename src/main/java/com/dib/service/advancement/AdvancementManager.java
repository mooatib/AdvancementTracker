package com.dib.service.advancement;


import com.dib.model.PlayerAdvancement;
import com.dib.model.PlayerAdvancementProgress;
import org.bukkit.Bukkit;
import org.bukkit.advancement.Advancement;
import org.bukkit.advancement.AdvancementProgress;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

public class AdvancementManager {
    private final AdvancementCache advancementCache;

    public AdvancementManager(AdvancementCache advancementCache) {
        this.advancementCache = advancementCache;
    }

    public void updatePlayerAdvancement(Player player, Advancement advancement) {
        UUID playerId = player.getUniqueId();
        PlayerAdvancementProgress progress = buildPlayerAdvancement(player, advancement);
        List<PlayerAdvancementProgress> advancements = advancementCache.get(playerId).advancements();
        advancements.add(progress);

        advancementCache.setPlayerAdvancement(playerId, new PlayerAdvancement(playerId, player.getName(), advancements));
    }

    public void loadPlayerAdvancements(Player player) {
        UUID playerId = player.getUniqueId();
        Iterator<Advancement> advancementIterator = Bukkit.advancementIterator();
        List<PlayerAdvancementProgress> advancements = new ArrayList<>();
        while (advancementIterator.hasNext()) {
            Advancement advancement = advancementIterator.next();
            PlayerAdvancementProgress progress = buildPlayerAdvancement(player, advancement);
            advancements.add(progress);
        }
        PlayerAdvancement playerAdvancement = new PlayerAdvancement(playerId, player.getName(), advancements);
        advancementCache.setPlayerAdvancement(playerId, playerAdvancement);
    }

    private PlayerAdvancementProgress buildPlayerAdvancement(Player player, Advancement advancement) {
        AdvancementProgress progress = player.getAdvancementProgress(advancement);
        Map<String, Date> criteria = getCriteria(progress);
        return new PlayerAdvancementProgress(advancement.getKey().toString(), progress.isDone(), criteria);
    }

    private Map<String, Date> getCriteria(AdvancementProgress progress) {
        return progress.getAwardedCriteria()
                .stream()
                .map(criteria -> Map.entry(criteria, Objects.requireNonNull(progress.getDateAwarded(criteria))))
                .filter(entry -> entry.getValue() != null)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}
