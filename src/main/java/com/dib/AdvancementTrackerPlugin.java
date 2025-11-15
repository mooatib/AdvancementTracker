package com.dib;

import com.dib.repository.AdvancementRepository;
import com.dib.service.advancement.AdvancementCache;
import com.dib.service.advancement.AdvancementListener;
import com.dib.service.advancement.AdvancementManager;
import com.dib.service.web.WebServer;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

public class AdvancementTrackerPlugin extends JavaPlugin {

    private final WebServer webServer;
    private final AdvancementListener listener;
    private final AdvancementRepository repository;

    private AdvancementTrackerPlugin() throws IOException {
        this.repository = new AdvancementRepository(this.getLogger(), this.getDataFolder());
        AdvancementCache cache = new AdvancementCache(repository);
        AdvancementManager manager = new AdvancementManager(cache);
        this.listener = new AdvancementListener(manager);
        this.webServer = new WebServer(manager, this.getServer());
    }

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(listener, this);
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
            repository.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
