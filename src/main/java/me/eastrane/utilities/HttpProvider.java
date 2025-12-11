package me.eastrane.utilities;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import me.eastrane.EastWhitelist;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

public class HttpProvider {
    public final EastWhitelist plugin;
    public final DebugProvider debugProvider;
    public HttpServer server;

    public HttpProvider(EastWhitelist plugin) {
        this.plugin = plugin;
        debugProvider = plugin.getDebugProvider();
        if (plugin.getConfigProvider().isHttpServerEnabled()) {
            try {
                server = HttpServer.create(new InetSocketAddress(8080), 0);
                server.createContext("/whitelist/add", this::handleWhitelistAdd);
                server.setExecutor(null);
                server.start();
            } catch (IOException e) {
                debugProvider.sendException(e);
            }
        }
    }

    public void disableHttpServer() {
        server.stop(0);
    }

    private void handleWhitelistAdd(HttpExchange exchange) throws IOException {
        if (!"POST".equals(exchange.getRequestMethod())) {
            exchange.sendResponseHeaders(405, -1);
            return;
        }
        InputStream requestBody = exchange.getRequestBody();
        String requestBodyStr = new String(requestBody.readAllBytes(), StandardCharsets.UTF_8);

        String nickname = parseMessage(requestBodyStr, "user_nickname");
        String moderatorId = parseMessage(requestBodyStr, "moderator_id");
        String moderatorNickname = parseMessage(requestBodyStr, "moderator_nickname");
        if (nickname != null) {
            plugin.getBaseStorage().addPlayer(nickname, moderatorNickname + " [" + moderatorId + "]");
            String response = "Игрок " + nickname + " добавлен в вайтлист.";
            exchange.sendResponseHeaders(200, response.getBytes().length);
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        } else {
            String response = "Никнейм не найден.";
            exchange.sendResponseHeaders(400, response.getBytes().length);
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }

    private String parseMessage(String requestBody, String string) {
        try {
            JsonObject jsonObject = JsonParser.parseString(requestBody).getAsJsonObject();
            if (jsonObject.has(string)) {
                return jsonObject.get(string).getAsString();
            }
        } catch (Exception e) {
            debugProvider.sendException(e);
        }
        return null;
    }
}
