package com.dib.service.web;

import com.dib.service.advancement.AdvancementManager;
import com.sun.net.httpserver.HttpServer;
import org.bukkit.Server;

import java.io.IOException;
import java.net.InetSocketAddress;

public class WebServer {

    private final HttpServer httpServer;
    private final AdvancementManager advancementManager;
    private final Server server;

    public WebServer(AdvancementManager advancementManager, Server server) throws IOException {
        String host = "0.0.0.0";
        int port = 80;
        this.server = server;
        this.httpServer = HttpServer.create(new InetSocketAddress(host, port), 0);
        this.advancementManager = advancementManager;
    }

    public void start() throws IOException {
        httpServer.createContext("/", new HomepageHandler(advancementManager, server));
        httpServer.createContext("/textures", new ResourceHandler("image/png"));
        httpServer.createContext("/font", new ResourceHandler());
        httpServer.start();
    }

    public void stop() throws IOException {
        httpServer.stop(0);
    }

}
