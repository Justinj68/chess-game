package com.chessgame.backend.controller;

import com.chessgame.backend.model.ChessGame;
import com.chessgame.backend.service.GameManagementService;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class GameManagementController {

    private static final Logger logger = LoggerFactory.getLogger(GameManagementController.class);

    private final GameManagementService gameManagementService;

    public GameManagementController(GameManagementService gameManagementService) {
        this.gameManagementService = gameManagementService;
    }

    @PostMapping("/play")
    public ResponseEntity<Map<String, Object>> play() {
        String playerId = UUID.randomUUID().toString();
        try {
            ChessGame chessGame = gameManagementService.play(playerId);
            Map<String, Object> response = Map.of("gameId", chessGame.getId(), "playerId", playerId);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            logger.error("Internal server error while finding a game for playerId: {}", playerId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    

    @GetMapping("/games")
    public ResponseEntity<List<ChessGame>> getGames() {
        List<ChessGame> chessGames = gameManagementService.getGames();
        return ResponseEntity.status(HttpStatus.OK).body(chessGames);
    }
}