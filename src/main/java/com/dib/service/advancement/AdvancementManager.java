package com.dib.service.advancement;


import com.dib.model.AdvancementMetadata;
import com.dib.model.PlayerAdvancement;
import com.dib.model.PlayerAdvancementProgress;
import io.papermc.paper.advancement.AdvancementDisplay;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.advancement.Advancement;
import org.bukkit.advancement.AdvancementProgress;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

public class AdvancementManager implements ComponentSerializer {
    private final AdvancementCache advancementCache;

    public AdvancementManager(AdvancementCache advancementCache) {
        this.advancementCache = advancementCache;
    }

    public Map<UUID, PlayerAdvancement> get() {
        return advancementCache.get();
    }

    //TODO : Remove unused
    public Map<UUID, PlayerAdvancement> getCompleted() {
        return advancementCache.getCompleted();
    }

    public void updatePlayerAdvancement(Player player, Advancement advancement) {
        if (!isRecipe(advancement)) {
            UUID playerId = player.getUniqueId();
            PlayerAdvancementProgress progress = buildPlayerAdvancement(player, advancement);

            Map<String, PlayerAdvancementProgress> advancements = advancementCache.get(playerId).getAdvancements();
            advancements.put(advancement.getKey().toString(), progress);

            advancementCache.setPlayerAdvancement(playerId, new PlayerAdvancement(playerId, player.getName(), advancements));
        }
    }

    public void loadPlayerAdvancements(Player player) {
        UUID playerId = player.getUniqueId();
        Iterator<Advancement> advancementIterator = Bukkit.advancementIterator();
        Map<String, PlayerAdvancementProgress> advancements = new HashMap<>();
        while (advancementIterator.hasNext()) {
            Advancement advancement = advancementIterator.next();
            if (!isRecipe(advancement)) {
                PlayerAdvancementProgress progress = buildPlayerAdvancement(player, advancement);
                advancements.put(advancement.getKey().toString(), progress);
            }
        }
        if (!advancements.isEmpty()) {
            PlayerAdvancement playerAdvancement = new PlayerAdvancement(playerId, player.getName(), advancements);
            advancementCache.setPlayerAdvancement(playerId, playerAdvancement);
        }
    }

    private PlayerAdvancementProgress buildPlayerAdvancement(Player player, Advancement advancement) {
        AdvancementProgress progress = player.getAdvancementProgress(advancement);
        Map<String, Date> criteria = getCriteria(progress);
        AdvancementMetadata metadata = buildMetadata(advancement);

        return new PlayerAdvancementProgress(advancement.getKey().toString(), metadata, progress.isDone(), criteria);
    }

    private AdvancementMetadata buildMetadata(Advancement advancement) {
        AdvancementDisplay display = Objects.requireNonNull(advancement.getDisplay());
        String title = formatAdvancementTitle(display.displayName());
        String description = serializeComponent(display.description());
        String iconName = getIconPath(display.icon().getType());

        return new AdvancementMetadata(title, description, iconName);
    }

    private String getIconPath(Material iconMaterial) {
        return iconMaterial.name().toLowerCase();
    }

    private Map<String, Date> getCriteria(AdvancementProgress progress) {
        return progress.getAwardedCriteria()
                .stream()
                .map(criteria -> Map.entry(criteria, Objects.requireNonNull(progress.getDateAwarded(criteria))))
                .filter(entry -> entry.getValue() != null)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private boolean isRecipe(Advancement advancement) {
        return advancement.getKey().toString().contains("minecraft:recipes");
    }
}
