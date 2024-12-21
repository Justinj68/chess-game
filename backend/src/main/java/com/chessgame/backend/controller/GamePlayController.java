package com.chessgame.backend.controller;

import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.chessgame.backend.model.ChessGame;
import com.chessgame.backend.service.GameManagementService;
import com.chessgame.backend.service.GamePlayService;
import com.chessgame.backend.websocket.WebSocketServer;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/api/game")
public class GamePlayController {

    private final WebSocketServer server;
    private static final Logger logger = LoggerFactory.getLogger(GamePlayController.class);
    private final GamePlayService gamePlayService;
    
    public GamePlayController(GamePlayService gamePlayService, GameManagementService gameManagementService, WebSocketServer server) {
        this.gamePlayService = gamePlayService;
        this.server = server;
    }

    @GetMapping("/{gameId}")
    public ResponseEntity<ChessGame> getGame(
            @PathVariable Long gameId) {
        ChessGame chessGame = gamePlayService.getGame(gameId);
        return ResponseEntity.status(HttpStatus.OK).body(chessGame);
    }

    @GetMapping("/{gameId}/moves")
    public ResponseEntity<List<String>> getPossibleMoves(
            @PathVariable Long gameId,
            @RequestParam String playerId,
            @RequestParam int row,
            @RequestParam int col,
            @RequestParam char piece) {
        try {
            List<String> possibleMoves = gamePlayService.getPossibleMoves(gameId, playerId, row, col, piece);
            return ResponseEntity.status(HttpStatus.OK).body(possibleMoves);
        } catch (Exception e) {
            logger.error("Error while getting possible moves for gameId={} row={} col={} piece={}", gameId, row, col, piece, e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.emptyList());
        }
    }

    @PutMapping("/{gameId}/move")
    public ResponseEntity<ChessGame> move(
            @PathVariable Long gameId,
            @RequestParam String playerId,
            @RequestParam int row,
            @RequestParam int col,
            @RequestParam int newRow, 
            @RequestParam int newCol) {
        try {
            ChessGame newChessGame = gamePlayService.move(gameId, playerId, row, col, newRow, newCol);
            String gameUpdateMessage = "{\"type\":\"move\",\"data\":" + new ObjectMapper().writeValueAsString(newChessGame) + "}";
            server.broadcastToRoom("GAME#" + gameId, gameUpdateMessage);

            return ResponseEntity.status(HttpStatus.OK).body(newChessGame);
        } catch (Exception e) {
            logger.error("Error while trying to move piece for gameId={} from ({},{}) to ({},{}):", gameId, row, col, newRow, newCol, e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

}