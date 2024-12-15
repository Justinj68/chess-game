package com.chessgame.backend.domain;

import java.util.EnumMap;
import java.util.Map;

public class ChessBoard {
    private Piece[][] board;
    public static final int SIZE = 8;
    public static final String fenStart = "rnbqkbnr/PPPPPPPP/8/8/8/8/PPPPPPPP/RNBQKBNR";
    private Map<Team, String> kingPositions;

    public ChessBoard(String fen) {
        this.board = new Piece[SIZE][SIZE];
        this.kingPositions = new EnumMap<>(Team.class);
        initializeBoardFromFEN(fen);
    }

    public ChessBoard() {
        this.board = new Piece[SIZE][SIZE];
        this.kingPositions = new EnumMap<>(Team.class);
        initializeBoardFromFEN(fenStart);
    }

    private void initializeBoardFromFEN(String fen) {
        String[] rows = fen.split("/");
        for (int i = 0; i < SIZE; i++) {
            String row = rows[i];
            int colIndex = 0;
            for (int j = 0; j < row.length(); j++) {
                char c = row.charAt(j);
                if (Character.isDigit(c)) {
                    colIndex += Character.getNumericValue(c);
                } else {
                    Piece piece = new Piece(c);
                    board[i][colIndex] = piece;
                    if (piece.isKing()) {
                        kingPositions.put(piece.getTeam(), coordinatesToPosition(i, colIndex));
                    }
                    colIndex++;
                }
            }
        }
    }

    public Piece getPiece(int row, int col) {
        if (row < 0 || row >= SIZE || col < 0 || col >= SIZE) {
            throw new IllegalArgumentException(String.format("Position outside the chessboard: (%d, %d)", row, col));
        }
        return board[row][col];
    }

    public boolean isEmpty(int row, int col) {
        return board[row][col] == null;
    }

    public void movePiece(int startRow, int startCol, int endRow, int endCol) {
        if (isEmpty(startRow, startCol)) {
            throw new IllegalArgumentException(String.format("No piece to move at this position: (%d, %d)", startRow, startCol));
        }
        Piece piece = getPiece(startRow, startCol);
        board[endRow][endCol] = piece;
        board[startRow][startCol] = null;
        if (piece != null && piece.isKing()) {
            kingPositions.put(piece.getTeam(), coordinatesToPosition(endRow, endCol));
        }
    }

    public String toFEN() {
        return toFEN(this.board);
    }

    public static String toFEN(Piece[][] board) {
        StringBuilder fen = new StringBuilder();

        for (int i = 0; i < SIZE; i++) {
            int emptyCount = 0;

            for (int j = 0; j < SIZE; j++) {
                Piece piece = board[i][j];

                if (piece == null) {
                    emptyCount++;
                } else {
                    if (emptyCount > 0) {
                        fen.append(emptyCount);
                        emptyCount = 0;
                    }
                    fen.append(piece.toChar());
                }
            }

            if (emptyCount > 0) {
                fen.append(emptyCount);
            }

            if (i < SIZE - 1) {
                fen.append('/');
            }
        }

        return fen.toString();
    }

    public static String coordinatesToPosition(int row, int col) {
        return "" + row + col;
    }

    public static int positionToRow(String position) {
        return Character.getNumericValue(position.charAt(0));
    }

    public static int positionToCol(String position) {
        return Character.getNumericValue(position.charAt(0));
    }

    public static int[] positionToCoordinates(String position) {
        return new int[]{positionToRow(position), positionToCol(position)};
    }

    public static boolean isOutsideBoard(int row, int col) {
        return row < 0 || row >= SIZE || col < 0 || col >= SIZE;
    }

    @Override
    public ChessBoard clone() {
        ChessBoard clonedBoard = new ChessBoard();
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if (this.board[row][col] != null) {
                    clonedBoard.board[row][col] = this.board[row][col].clone();
                } else {
                    clonedBoard.board[row][col] = null;
                }
            }
        }
        clonedBoard.kingPositions = new EnumMap<>(this.kingPositions);
        return clonedBoard;
    }

    public String getKingPosition(Team team) {
        return kingPositions.get(team);
    }
}
