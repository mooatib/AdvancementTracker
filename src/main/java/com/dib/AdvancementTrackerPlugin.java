package com.dib;

import com.dib.commands.ProgressCommand;
import com.dib.repository.AdvancementRepository;
import com.dib.service.advancement.AdvancementCache;
import com.dib.service.advancement.AdvancementListener;
import com.dib.service.advancement.AdvancementManager;
import com.dib.service.web.WebServer;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.List;

public class AdvancementTrackerPlugin extends JavaPlugin {

    private final WebServer webServer;
    private final AdvancementListener listener;
    private final AdvancementRepository repository;

    private AdvancementTrackerPlugin() throws IOException {
        this.repository = new AdvancementRepository(this.getLogger(), this.getDataFolder());
        AdvancementCache cache = new AdvancementCache(repository.load());
        AdvancementManager manager = new AdvancementManager(cache, repository);
        this.listener = new AdvancementListener(manager);
        this.webServer = new WebServer(manager, this.getServer());
    }

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(listener, this);
        this.getServer().getCommandMap().register(
                this.getName().toLowerCase(),
                new ProgressCommand("progress", "Get advancement progress", "/progress <advancement>", List.of())
        );
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
