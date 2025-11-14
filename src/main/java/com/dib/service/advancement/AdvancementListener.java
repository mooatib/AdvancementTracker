package com.dib.service.advancement;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class AdvancementListener implements Listener {

    private final AdvancementManager advancementManager;

    public AdvancementListener(AdvancementManager advancementManager) {
        this.advancementManager = advancementManager;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onAdvancementDone(PlayerAdvancementDoneEvent event) {
        Player player = event.getPlayer();
        advancementManager.updatePlayerAdvancement(player, event.getAdvancement());
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        advancementManager.loadPlayerAdvancements(player);
    }

    @EventHandler
    public void onPlayerLeft(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        advancementManager.loadPlayerAdvancements(player);
    }
}
