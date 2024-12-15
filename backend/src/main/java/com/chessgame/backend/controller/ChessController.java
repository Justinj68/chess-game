package com.chessgame.backend.controller;

import com.chessgame.backend.domain.Piece;
import com.chessgame.backend.service.ChessEngine;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ChessController {
    @Autowired
    private final ChessEngine chessEngine;

    public ChessController(ChessEngine chessEngine) {
        this.chessEngine = chessEngine;
    }

    @GetMapping("/board")
    @CrossOrigin(origins = "http://localhost:5173")
    public String getBoard() {
        System.out.println("Request received to get the game board");
        String board = chessEngine.getBoard();
        System.out.println("Game board received: " + board);
        return board;
    }

    @GetMapping("/moves")
    @CrossOrigin(origins = "http://localhost:5173")
    public List<String> getPossibleMoves(
            @RequestParam int row,
            @RequestParam int col,
            @RequestParam char piece) {
        System.out.println("Request received for possible moves for piece: " + piece + " at (" + row + ", " + col + ")");
        List<String> possibleMoves = chessEngine.getValidMoves(row, col, new Piece(piece));
        System.out.println("Possible moves: " + possibleMoves);
        return possibleMoves;
    }

    @PostMapping("/move")
    @CrossOrigin(origins = "http://localhost:5173")
    public String move(
            @RequestParam int oldRow,
            @RequestParam int oldCol,
            @RequestParam int newRow,
            @RequestParam int newCol) {
        System.out.println("Request received to move: from (" + oldRow + ", " + oldCol + ") to (" + newRow + ", " + newCol + ")");
        String newBoard = chessEngine.move(oldRow, oldCol, newRow, newCol);
        System.out.println("New board: " + newBoard);
        return newBoard;
    }
}

