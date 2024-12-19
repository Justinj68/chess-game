package com.chessgame.backend.service;
import com.chessgame.backend.domain.board.ChessBoard;
import com.chessgame.backend.domain.board.Piece;
import com.chessgame.backend.domain.enums.GameStatus;
import com.chessgame.backend.domain.logic.GameStatusUpdater;
import com.chessgame.backend.domain.logic.MoveValidity;
import com.chessgame.backend.model.ChessGame;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class ChessEngine {
    private ChessGame chessGame;
    private ChessBoard chessBoard;
    private MoveValidity moveValidity;
    private GameStatusUpdater gameStatusUpdater;

    public ChessEngine() {}

    public void initialize(ChessGame chessGame) {
        this.chessGame = chessGame;
        this.chessBoard = new ChessBoard(chessGame);
        this.moveValidity = new MoveValidity(chessBoard);
        this.gameStatusUpdater = new GameStatusUpdater(chessGame, chessBoard, moveValidity);
    }

    public String getBoard() {
        return chessBoard.toFEN();
    }

    public List<String> getValidMoves(int row, int col, Piece piece) {
        return moveValidity.getPieceValidMoves(row, col, piece);
    }

    public String move(int oldRow, int oldCol, int newRow, int newCol) {
        if (chessGame.getGameStatus() == GameStatus.IN_PROGRESS && moveValidity.isMoveValid(oldRow, oldCol, newRow, newCol)) {
            chessBoard.movePiece(oldRow, oldCol, newRow, newCol);
            gameStatusUpdater.updateGameStatus();
        }
        return chessBoard.toFEN();
    }
}
