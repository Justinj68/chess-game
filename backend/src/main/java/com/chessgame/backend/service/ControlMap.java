package com.chessgame.backend.service;

import java.util.EnumMap;
import java.util.Map;

import com.chessgame.backend.domain.ChessBoard;
import com.chessgame.backend.domain.Piece;
import com.chessgame.backend.domain.Team;

public class ControlMap {
    private final ChessBoard chessBoard;
    private Map<Team, boolean[][]> controlMaps;

    private final int SIZE = ChessBoard.SIZE;

    public ControlMap(ChessBoard chessBoard) {
        this.chessBoard = chessBoard;
        this.controlMaps = new EnumMap<>(Team.class);
        refreshControlMaps();
        diplay();
    }

    public void refreshControlMaps() {
        controlMaps.put(Team.BLACK, new boolean[SIZE][SIZE]);
        controlMaps.put(Team.WHITE, new boolean[SIZE][SIZE]);
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                computePieceCoverage(chessBoard.getPiece(row, col), row, col);
            }
        }
    }

    private void computePieceCoverage(Piece piece, int row, int col) {
        if (piece == null) {
            return;
        }
        switch (piece.getType()) {
            case PAWN:
                computePawnCoverage(row, col, piece);
                break;
            case ROOK:
                computeRookCoverage(row, col, piece);
                break;
            case KNIGHT:
                computeKnightCoverage(row, col, piece);
                break;
            case BISHOP:
                computeBishopCoverage(row, col, piece);
                break;
            case QUEEN:
                computeQueenCoverage(row, col, piece);
                break;
            case KING:
                computeKingCoverage(row, col, piece);
                break;
            default:
                break;
        }
    }

    private void computePawnCoverage(int row, int col, Piece piece) {
        int[][] blackSteps = {{1, -1}, {1, 1}};
        int[][] whiteSteps = {{-1, -1}, {-1, 1}};
        int[][] steps = piece.getTeam() == Team.WHITE ? whiteSteps : blackSteps;
        computeStepCoverage(row, col, piece, steps);
    }

    private void computeRookCoverage(int row, int col, Piece piece) {
        int[][] directions = {{ 0, -1}, { 0,  1}, {-1,  0}, {1, 0}};
        computeRayCoverage(row, col, piece, directions);
    }

    private void computeRayCoverage(int row, int col, Piece piece, int[][] directions) {
        for (int[] direction : directions) {
            int currentRow = row + direction[0];
            int currentCol = col + direction[1];
            while (!ChessBoard.isOutsideBoard(currentRow, currentCol)) {
                controlMaps.get(piece.getTeam())[currentRow][currentCol] = true;
                if (!chessBoard.isEmpty(currentRow, currentCol)) {
                    break;
                }
                currentRow += direction[0];
                currentCol += direction[1];
            }
        }
    }

    private void computeBishopCoverage(int row, int col, Piece piece) {
        int[][] directions = {{1, 1}, {1, -1}, {-1, 1}, {-1, -1}};
        computeRayCoverage(row, col, piece, directions);
    }

    private void computeQueenCoverage(int row, int col, Piece piece) {
        computeRookCoverage(row, col, piece);
        computeBishopCoverage(row, col, piece);
    }

    private void computeStepCoverage(int row, int col, Piece piece, int[][] steps) {
        for (int[] step : steps) {
            int currentRow = row + step[0];
            int currentCol = col + step[1];
            if (!ChessBoard.isOutsideBoard(currentRow, currentCol)) {
                controlMaps.get(piece.getTeam())[currentRow][currentCol] = true;
            }
        }
    }

    private void computeKingCoverage(int row, int col, Piece piece) {
        int[][] steps = {{1, 1}, {1, 0}, {1, -1}, {0, 1}, {0, -1}, {-1, 1}, {-1, 0}, {-1, -1}};
        computeStepCoverage(row, col, piece, steps);
    }

    private void computeKnightCoverage(int row, int col, Piece piece) {
        int[][] steps = {{-2, -1}, {-2, 1}, {-1, -2}, {-1, 2}, { 1, -2}, { 1, 2}, { 2, -1}, { 2, 1}};
        computeStepCoverage(row, col, piece, steps);
    }

    public boolean isControlledBy(int row, int col, Team team) {
        if (row < 0 || row >= SIZE || col < 0 || col >= SIZE) {
            return false;
        }
        return controlMaps.get(team)[row][col];
    }

    public void diplay() {
        System.out.println("White-controlled area:\n");
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if (controlMaps.get(Team.WHITE)[col][row]) {
                    System.out.print(" W ");
                } else {
                    System.out.print(" - ");
                }
            }
            System.out.println("\n");
        }

        System.out.println("Black-controlled area:\n");
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if (controlMaps.get(Team.BLACK)[col][row]) {
                    System.out.print(" B ");
                } else {
                    System.out.print(" - ");
                }
            }
            System.out.println("\n");
        }
    }
}
