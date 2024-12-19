package com.chessgame.backend.domain.logic;

import java.util.List;

import com.chessgame.backend.domain.board.ChessBoard;
import com.chessgame.backend.domain.board.Piece;
import com.chessgame.backend.domain.enums.GameStatus;
import com.chessgame.backend.domain.enums.Team;
import com.chessgame.backend.model.ChessGame;

public class GameStatusUpdater {
    private final ChessGame chessGame;
    private final ChessBoard chessBoard;
    private final ControlMap controlMap;
    private final MoveValidity moveValidity;

    public GameStatusUpdater(ChessGame chessGame, ChessBoard chessBoard, MoveValidity moveValidity) {
        this.chessGame = chessGame;
        this.chessBoard = chessBoard;
        this.controlMap = new ControlMap(chessBoard);
        this.moveValidity = moveValidity;
    }

    public void updateGameStatus() {
        chessGame.setGameStatus(findGameStatus());
    }

    private GameStatus findGameStatus() {
        if (isCheckMate(Team.WHITE)) {
            return GameStatus.BLACK_WIN;
        }
        if (isCheckMate(Team.BLACK)) {
            return GameStatus.WHITE_WIN;
        }
        if (isStaleMate()) {
            return GameStatus.STALEMATE;
        }
        return GameStatus.IN_PROGRESS;
    }

    private boolean isKingInCheck(Team team) {
        String kingPosition = chessBoard.getKingPosition(team);
        Team enemyTeam = team == Team.WHITE ? Team.BLACK : Team.WHITE;

        int row = ChessBoard.positionToRow(kingPosition);
        int col = ChessBoard.positionToCol(kingPosition);

        return controlMap.isControlledBy(row, col, enemyTeam);
    }

    private boolean isCheckMate(Team team) {
        if (!isKingInCheck(team)) {
            return false;
        }
        return !hasPossibleMovesForTeam(team);
    }

    private boolean isStaleMate() {
        return (!isKingInCheck(Team.WHITE) && !hasPossibleMovesForTeam(Team.WHITE)) 
            || (!isKingInCheck(Team.BLACK) && !hasPossibleMovesForTeam(Team.BLACK));
    }

    private boolean hasPossibleMovesForTeam(Team team) {
        for (int i = 0; i < ChessBoard.SIZE; i++) {
            for (int j = 0; j < ChessBoard.SIZE; j++) {
                Piece piece = chessBoard.getPiece(i, j);
                if (piece != null && piece.getTeam() == team) {
                    List<String> possibleMoves = moveValidity.getPieceValidMoves(i, j, piece);
                    if (possibleMoves.size() > 0) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
