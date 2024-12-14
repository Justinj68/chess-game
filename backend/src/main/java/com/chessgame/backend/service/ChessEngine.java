package com.chessgame.backend.service;
import org.springframework.stereotype.Service;

import com.chessgame.backend.domain.ChessBoard;
import com.chessgame.backend.domain.Piece;

import java.util.List;

@Service
public class ChessEngine {
    private ChessBoard chessBoard;
    private ControlMap controlMap;
    private MoveValidity moveValidity;

    public ChessEngine() {
        this.chessBoard = new ChessBoard("1RR1K2Q/5P2/6b1/2r5/3q4/2kr4/6N1/8");
        this.controlMap = new ControlMap(chessBoard);
        this.moveValidity = new MoveValidity(chessBoard, controlMap);
    }

    public List<String> getValidMoves(int row, int col, Piece piece) {
        return moveValidity.getPieceValidMoves(row, col, piece);
    }

    public boolean isMoveValid(int oldRow, int oldCol, int newRow, int newCol) {
        return moveValidity.isMoveValid(oldRow, oldCol, newRow, newCol);
    }
}
