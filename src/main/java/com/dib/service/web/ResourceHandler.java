package com.dib.service.web;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ResourceHandler implements HttpHandler {

    private final String contentType;

    public ResourceHandler() {
        this(null);
    }

    public ResourceHandler(String contentType) {
        this.contentType = contentType;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();

        String resourcePath = path.substring(1);

        try (InputStream is = this.getClass().getClassLoader().getResourceAsStream(resourcePath)) {
            if (is == null) {
                exchange.sendResponseHeaders(404, 0);
                exchange.close();
                return;
            }

            byte[] bytes = is.readAllBytes();

            if (contentType != null) {
                exchange.getResponseHeaders().set("Content-Type", contentType);
            }
            exchange.sendResponseHeaders(200, bytes.length);

            try (OutputStream os = exchange.getResponseBody()) {
                os.write(bytes);
            }
            exchange.close();
        }
    }
}
