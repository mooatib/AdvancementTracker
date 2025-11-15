package com.dib.service.web;

import com.dib.service.advancement.AdvancementManager;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class WebServer {

    private final HttpServer server;
    private final AdvancementManager advancementManager;

    public WebServer(AdvancementManager advancementManager) throws IOException {
        String host = "0.0.0.0";
        int port = 8080;
        this.server = HttpServer.create(new InetSocketAddress(host, port), 0);
        this.advancementManager = advancementManager;
    }

    public void start() throws IOException {
        server.createContext("/", new Handler(advancementManager));
        server.start();
    }

    public void stop() throws IOException {
        server.stop(0);
    }

}
