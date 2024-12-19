package com.chessgame.backend.service;

import com.chessgame.backend.domain.board.Piece;
import com.chessgame.backend.model.ChessGame;
import com.chessgame.backend.repository.ChessGameRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GamePlayService {

    private static final Logger logger = LoggerFactory.getLogger(GamePlayService.class);

    private final ChessGameRepository chessGameRepository;
    private final ChessEngine chessEngine;

    public GamePlayService(ChessGameRepository chessGameRepository, ChessEngine chessEngine) {
        this.chessGameRepository = chessGameRepository;
        this.chessEngine = chessEngine;
    }

    public ChessGame createGame() {
        ChessGame newGame = new ChessGame();
        ChessGame savedGame = chessGameRepository.save(newGame);
        logger.info("New game created with ID: {}", savedGame.getId());
        return savedGame;
    }

    public ChessGame getGame(Long id) {
        return chessGameRepository.findById(id)
            .orElseThrow(() -> {
                logger.error("No game found with ID: {}", id);
                return null;
            });
    }

    public List<String> getPossibleMoves(Long gameId, int row, int col, char piece) {
        ChessGame chessGame = getGame(gameId);
        chessEngine.initialize(chessGame);
        List<String> possibleMoves = chessEngine.getValidMoves(row, col, new Piece(piece));
        logger.info("Possible moves for piece {} at ({}, {}): {}", piece, row, col, possibleMoves);
        return possibleMoves;
    }

    public ChessGame move(Long gameId, int row, int col, int newRow, int newCol) {
        ChessGame chessGame = getGame(gameId);
        chessEngine.initialize(chessGame);
        chessEngine.move(row, col, newRow, newCol);
        ChessGame newChessGame = chessGameRepository.save(chessGame);
        logger.info("Moved piece from ({}, {}) to ({}, {}). New board state saved.", row, col, newRow, newCol);
        return newChessGame;
    }
}
