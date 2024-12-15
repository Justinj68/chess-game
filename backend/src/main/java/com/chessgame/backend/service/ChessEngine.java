package com.chessgame.backend.service;
import org.springframework.stereotype.Service;

import com.chessgame.backend.domain.ChessBoard;
import com.chessgame.backend.domain.Piece;

import java.util.List;

@Service
public class ChessEngine {
    private ChessBoard chessBoard;
    private MoveValidity moveValidity;

    public ChessEngine() {
        this.chessBoard = new ChessBoard("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR");
        this.moveValidity = new MoveValidity(chessBoard);
    }

    public String getBoard() {
        return chessBoard.toFEN();
    }

    public List<String> getValidMoves(int row, int col, Piece piece) {
        return moveValidity.getPieceValidMoves(row, col, piece);
    }

    public String move(int oldRow, int oldCol, int newRow, int newCol) {
        if (moveValidity.isMoveValid(oldRow, oldCol, newRow, newCol)) {
            chessBoard.movePiece(oldRow, oldCol, newRow, newCol);
        }
        return chessBoard.toFEN();
    }
}
