package com.chessgame.backend.websocket;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.TextMessage;

public class WebSocketServer {
    private final Map<String, Map<String, WebSocketSession>> rooms = new ConcurrentHashMap<>();

    public void addSessionToRoom(String room, WebSocketSession session) {
        rooms.computeIfAbsent(room, k -> new ConcurrentHashMap<>()).put(session.getId(), session);
    }

    public void removeSessionFromRoom(String room, String sessionId) {
        Map<String, WebSocketSession> roomSessions = rooms.get(room);
        if (roomSessions != null) {
            roomSessions.remove(sessionId);
            if (roomSessions.isEmpty()) {
                rooms.remove(room);
            }
        }
    }

    public void broadcastToRoom(String room, String message) {
        Map<String, WebSocketSession> roomSessions = rooms.get(room);
        if (roomSessions != null) {
            roomSessions.values().forEach(session -> {
                try {
                    session.sendMessage(new TextMessage(message));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }
}


