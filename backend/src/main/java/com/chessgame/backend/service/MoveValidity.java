package com.chessgame.backend.service;

import java.util.ArrayList;
import java.util.List;

import com.chessgame.backend.domain.ChessBoard;
import com.chessgame.backend.domain.Piece;

public class MoveValidity {
    private final ChessBoard chessBoard;
    public MoveValidity(ChessBoard chessBoard) {
        this.chessBoard = chessBoard;
    }

    public List<String> getPieceValidMoves(int row, int col, Piece piece) {
        if (piece == null) {
            return new ArrayList<>();
        }
        switch (piece.getType()) {
            case PAWN:
                return getPawnValidMoves(row, col, piece);
            case ROOK:
                return getRookValidMoves(row, col, piece);
            case KNIGHT:
                return getKnightValidMoves(row, col, piece);
            case BISHOP:
                return getBishopValidMoves(row, col, piece);
            case QUEEN:
                return getQueenValidMoves(row, col, piece);
            case KING:
                return getKingValidMoves(row, col, piece);
            default:
                return new ArrayList<>();
        }
    }

    private List<String> getPawnValidMoves(int row, int col, Piece piece) {
        int direction = piece.isWhite() ? -1 : 1;
        List<int[]> steps = new ArrayList<>();
        int[] normalStep = {direction, 0};
        if (!ChessBoard.isOutsideBoard(row + normalStep[0], col + normalStep[1]) && chessBoard.isEmpty(row + normalStep[0], col + normalStep[1])) {
            steps.add(normalStep);
            if ((piece.isWhite() && row == ChessBoard.SIZE - 2) || (piece.isBlack() && row == 1)) {
                int[] startStep = {direction * 2, 0};
                if (!ChessBoard.isOutsideBoard(row + startStep[0], col + startStep[1]) && chessBoard.isEmpty(row + startStep[0], col + startStep[1])) {
                    steps.add(startStep);
                }
            }
        }
        int[][] captureSteps = {
            {direction, -1},
            {direction, 1}
        };
        for (int[] captureStep : captureSteps) {
            int currentRow = row + captureStep[0];
            int currentCol = col + captureStep[1];
            if (!ChessBoard.isOutsideBoard(currentRow, currentCol) && !chessBoard.isEmpty(currentRow, currentCol)) {
                steps.add(captureStep);
            }
        }
        return getStepValidMoves(row, col, piece, steps.toArray(new int[0][2]));
    }
    

    private List<String> getRookValidMoves(int row, int col, Piece piece) {
        int[][] directions = {{ 0, -1}, { 0,  1}, {-1,  0}, {1, 0}};
        return getRayValidMoves(row, col, piece, directions);
    }

    private List<String> getValidMoves(int row, int col, Piece piece, int[][] vectors, boolean isRay) {
        List<String> validMoves = new ArrayList<>();
        for (int[] vector : vectors) {
            int currentRow = row + vector[0];
            int currentCol = col + vector[1];
            while (!ChessBoard.isOutsideBoard(currentRow, currentCol)) {
                Piece currentPiece = chessBoard.getPiece(currentRow, currentCol);
                if (!canLegallyMoveTo(row, col, currentRow, currentCol)) {
                    break;
                }
                if (currentPiece == null || (!piece.areSameTeam(currentPiece) && !currentPiece.isKing())) {
                    validMoves.add(coordinatesToPosition(currentRow, currentCol));
                }
                if (!chessBoard.isEmpty(currentRow, currentCol) || !isRay) {
                    break;
                }
                currentRow += vector[0];
                currentCol += vector[1];
            }
        }
        return validMoves;
    }

    private List<String> getStepValidMoves(int row, int col, Piece piece, int[][] steps) {
        return getValidMoves(row, col, piece, steps, false);
    }
    
    private List<String> getRayValidMoves(int row, int col, Piece piece, int[][] directions) {
        return getValidMoves(row, col, piece, directions, true);
    }
    

    private List<String> getBishopValidMoves(int row, int col, Piece piece) {
        int[][] directions = {{1, 1}, {1, -1}, {-1, 1}, {-1, -1}};
        return getRayValidMoves(row, col, piece, directions);
    }

    private List<String> getQueenValidMoves(int row, int col, Piece piece) {
        List<String> validMoves = new ArrayList<>();
        getRookValidMoves(row, col, piece).forEach(validMoves::add);
        getBishopValidMoves(row, col, piece).forEach(validMoves::add);
        return validMoves;
    }

    private List<String> getKingValidMoves(int row, int col, Piece piece) {
        int[][] steps = {{1, 1}, {1, 0}, {1, -1}, {0, 1}, {0, -1}, {-1, 1}, {-1, 0}, {-1, -1}};
        return getStepValidMoves(row, col, piece, steps);
    }

    private List<String> getKnightValidMoves(int row, int col, Piece piece) {
        int[][] steps = {{-2, -1}, {-2, 1}, {-1, -2}, {-1, 2}, { 1, -2}, { 1, 2}, { 2, -1}, { 2, 1}};
        return getStepValidMoves(row, col, piece, steps);
    }

    public String coordinatesToPosition(int row, int col) {
        return "" + row + col;
    }

    public int positionToRow(String position) {
        return Character.getNumericValue(position.charAt(0));
    }

    public int positionToCol(String position) {
        return Character.getNumericValue(position.charAt(1));
    }

    public boolean isMoveValid(int oldRow, int oldCol, int newRow, int newCol) {
        Piece piece = chessBoard.getPiece(oldRow, oldCol);
        List<String> validMoves = getPieceValidMoves(oldRow, oldCol, piece);
        String newPosition = coordinatesToPosition(newRow, newCol);
        return validMoves.contains(newPosition);
    }

    public boolean canLegallyMoveTo(int oldRow, int oldCol, int newRow, int newCol) {
        ChessBoard simulationBoard = chessBoard.clone();
        simulationBoard.movePiece(oldRow, oldCol, newRow, newCol);
        ControlMap simulationControlMap = new ControlMap(simulationBoard);
        Piece piece = chessBoard.getPiece(oldRow, oldCol);
        String kingPosition = simulationBoard.getKingPosition(piece.getTeam());
        int kingRow = positionToRow(kingPosition);
        int kingCol = positionToCol(kingPosition);
        boolean isKingInCheck = simulationControlMap.isControlledBy(kingRow, kingCol, piece.getEnemyTeam());
        return !isKingInCheck;
    }
}
