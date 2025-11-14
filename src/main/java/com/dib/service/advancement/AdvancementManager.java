package com.dib.service.advancement;


import com.dib.model.Player;
import com.dib.model.PlayerAdvancement;
import org.bukkit.Bukkit;
import org.bukkit.advancement.Advancement;
import org.bukkit.advancement.AdvancementProgress;

import java.util.*;
import java.util.stream.Collectors;

public class AdvancementManager {
    private final AdvancementCache advancementCache;

    public AdvancementManager(AdvancementCache advancementCache) {
        this.advancementCache = advancementCache;
    }

    public void updatePlayerAdvancement(org.bukkit.entity.Player player, Advancement advancement) {
        UUID playerId = player.getUniqueId();
        AdvancementProgress progress = player.getAdvancementProgress(advancement);
        Map<String, Date> criteria = getCriteria(progress);
        PlayerAdvancement playerAdvancement = new PlayerAdvancement(advancement.getKey().toString(), progress.isDone(), criteria);
        List<PlayerAdvancement> advancements = advancementCache.get(playerId).advancements();
        advancements.add(playerAdvancement);

        advancementCache.setPlayerAdvancement(playerId, new Player(playerId, player.getName(), advancements));
    }

    public void loadPlayerAdvancements(org.bukkit.entity.Player player) {
        UUID playerId = player.getUniqueId();
        Iterator<Advancement> advancementIterator = Bukkit.advancementIterator();
        List<PlayerAdvancement> advancements = new ArrayList<>();
        while (advancementIterator.hasNext()) {
            Advancement advancement = advancementIterator.next();
            AdvancementProgress progress = player.getAdvancementProgress(advancement);
            Map<String, Date> criteria = getCriteria(progress);
            PlayerAdvancement playerAdvancement = new PlayerAdvancement(advancement.getKey().toString(), progress.isDone(), criteria);
            advancements.add(playerAdvancement);
        }
        Player playerAdvancement = new Player(playerId, player.getName(), advancements);
        advancementCache.setPlayerAdvancement(playerId, playerAdvancement);
    }

    private Map<String, Date> getCriteria(AdvancementProgress progress) {
        return progress.getAwardedCriteria()
                .stream()
                .map(criteria -> Map.entry(criteria, Objects.requireNonNull(progress.getDateAwarded(criteria))))
                .filter(entry -> entry.getValue() != null)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}
