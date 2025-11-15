package com.dib;

import com.dib.service.advancement.AdvancementCache;
import com.dib.service.advancement.AdvancementListener;
import com.dib.service.advancement.AdvancementManager;
import com.dib.service.web.WebServer;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

public class AdvancementTrackerPlugin extends JavaPlugin {

    private final WebServer webServer;
    private final AdvancementListener advancementListener;

    private AdvancementTrackerPlugin() throws IOException {
        AdvancementCache advancementCache = new AdvancementCache();
        AdvancementManager advancementManager = new AdvancementManager(advancementCache);
        this.advancementListener = new AdvancementListener(advancementManager);
        this.webServer = new WebServer(advancementManager);
    }

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(advancementListener, this);
        try {
            webServer.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onDisable() {
        try {
            webServer.stop();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
