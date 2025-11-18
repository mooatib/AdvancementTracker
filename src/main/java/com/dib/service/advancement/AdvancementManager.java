package com.dib.service.advancement;


import com.dib.model.AdvancementMetadata;
import com.dib.model.PlayerAdvancement;
import com.dib.model.PlayerAdvancementProgress;
import com.dib.repository.AdvancementRepository;
import io.papermc.paper.advancement.AdvancementDisplay;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.advancement.Advancement;
import org.bukkit.advancement.AdvancementProgress;
import org.bukkit.entity.Player;

import java.util.*;

public class AdvancementManager implements ComponentSerializer {
    private final AdvancementCache cache;
    private final AdvancementRepository repository;

    public AdvancementManager(AdvancementCache cache, AdvancementRepository repository) {
        this.cache = cache;
        this.repository = repository;
    }

    public Map<UUID, PlayerAdvancement> getCache() {
        return cache.getSorted();
    }

    public void updatePlayerAdvancement(Player player, Advancement advancement) {
        if (!isRecipe(advancement)) {
            PlayerAdvancementProgress progress = buildPlayerAdvancement(player, advancement);
            if (!repository.isAdvancementExisting(progress.key())) {
                repository.saveAdvancement(progress.key(), progress.metadata());
            }
            if (progress.awarded()) {
                repository.savePlayerAdvancement(player.getUniqueId(), player.getName(), progress);
                cache.updatePlayerEntry(player.getUniqueId(), player.getName(), progress);
            }
        }
    }

    public int countAdvancements() {
        return repository.countAdvancements();
    }

    public void loadPlayerAdvancements(Player player) {
        Iterator<Advancement> advancementIterator = Bukkit.advancementIterator();
        while (advancementIterator.hasNext()) {
            updatePlayerAdvancement(player, advancementIterator.next());
        }
    }

    private PlayerAdvancementProgress buildPlayerAdvancement(Player player, Advancement advancement) {
        AdvancementProgress progress = player.getAdvancementProgress(advancement);
        Date awardedDate = getAwardedDate(progress);
        AdvancementMetadata metadata = buildMetadata(advancement);

        return new PlayerAdvancementProgress(advancement.getKey().toString(), metadata, progress.isDone(), awardedDate);
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

    private Date getAwardedDate(AdvancementProgress progress) {
        return progress.getAwardedCriteria()
                .stream()
                .map(progress::getDateAwarded)
                .filter(Objects::nonNull)
                .max(Date::compareTo)
                .orElse(null);
    }

    private boolean isRecipe(Advancement advancement) {
        return advancement.getKey().toString().contains("minecraft:recipes");
    }
}
