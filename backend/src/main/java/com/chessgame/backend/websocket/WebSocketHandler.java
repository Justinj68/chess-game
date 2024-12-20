package com.chessgame.backend.websocket;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

public class WebSocketHandler extends TextWebSocketHandler {
    private final WebSocketServer webSocketServer;

    public WebSocketHandler(WebSocketServer webSocketServer) {
        this.webSocketServer = webSocketServer;
    }

    @Override
    public void afterConnectionEstablished(@SuppressWarnings("null") WebSocketSession session) throws Exception {
        URI uri = session.getUri();
        if (uri != null) {
            Map<String, String> queryParams = parseQueryParams(uri.getQuery());
            String room = queryParams.get("room");
            if (room != null) {
                webSocketServer.addSessionToRoom(room, session);
                System.out.println("Websocket connection established in room: " + room);
            } else {
                System.out.println("Missing 'room' parameter int the URI.");
                session.close();
            }
        }
    }

    private Map<String, String> parseQueryParams(String query) {
        Map<String, String> queryParams = new HashMap<>();
        if (query != null && !query.isEmpty()) {
            String[] pairs = query.split("&");
            for (String pair : pairs) {
                String[] keyValue = pair.split("=", 2);
                if (keyValue.length == 2) {
                    queryParams.put(keyValue[0], keyValue[1]);
                } else if (keyValue.length == 1) {
                    queryParams.put(keyValue[0], "");
                }
            }
        }
        return queryParams;
    }

    @SuppressWarnings("null")
    @Override
    public void afterConnectionClosed(WebSocketSession session, org.springframework.web.socket.CloseStatus status) throws Exception {
        URI uri = session.getUri();
        if (uri != null) {
            Map<String, String> queryParams = parseQueryParams(uri.getQuery());
            String room = queryParams.get("room");
            if (room != null) {
                webSocketServer.removeSessionFromRoom(room, session.getId());
                System.out.println("Websocket connection closed for: " + room);
            }
        }
    }
}
