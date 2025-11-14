package com.dib;

import com.dib.service.advancement.AdvancementCache;
import com.dib.service.advancement.AdvancementListener;
import com.dib.service.advancement.AdvancementManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class AdvancementTrackerPlugin extends JavaPlugin {

    private final AdvancementListener advancementListener;

    private AdvancementTrackerPlugin() {
        AdvancementCache advancementCache = new AdvancementCache();
        AdvancementManager advancementManager = new AdvancementManager(advancementCache);
        this.advancementListener = new AdvancementListener(advancementManager);
    }

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(advancementListener, this);
    }
}
