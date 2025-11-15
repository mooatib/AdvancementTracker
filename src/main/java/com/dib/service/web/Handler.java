package com.dib.service.web;

import com.dib.model.web.MainPage;
import com.dib.service.advancement.AdvancementManager;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;

public class Handler implements HttpHandler {

    private final AdvancementManager advancementManager;

    public Handler(AdvancementManager advancementManager) {
        this.advancementManager = advancementManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        byte[] page = MainPage.renderPage(advancementManager.get());
        exchange.sendResponseHeaders(200, page.length);
        OutputStream response = exchange.getResponseBody();
        response.write(page);
        response.close();
    }
}
