package com.chessgame.backend.controller;

import com.chessgame.backend.model.ChessGame;
import com.chessgame.backend.service.GameManagementService;

import java.util.List;
import java.util.Map;

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
    public ResponseEntity<ChessGame> play(@RequestBody Map<String, String> request) {
        String playerId = request.get("playerId");
        try {
            ChessGame chessGame = gameManagementService.play(playerId);
            return ResponseEntity.status(HttpStatus.CREATED).body(chessGame);
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