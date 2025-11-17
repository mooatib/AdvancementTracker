package com.dib.service.web;

import com.dib.model.web.Homepage;
import com.dib.service.advancement.AdvancementManager;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.bukkit.Server;

import java.io.IOException;
import java.io.OutputStream;

public class HomepageHandler implements HttpHandler {

    private final AdvancementManager advancementManager;
    private final Server server;

    public HomepageHandler(AdvancementManager advancementManager, Server server) {
        this.advancementManager = advancementManager;
        this.server = server;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        int playerCount = server.getOnlinePlayers().size();
        byte[] page = Homepage.renderPage(advancementManager.get(), playerCount, advancementManager.countAdvancements());
        exchange.sendResponseHeaders(200, page.length);
        OutputStream response = exchange.getResponseBody();
        response.write(page);
        response.close();
    }
}
