package com.chessgame.backend.model;

public class ChessBoard {
    private char[][] board;
    public static final int SIZE = 8;

    public ChessBoard(String fen) {
        this.board = new char[SIZE][SIZE];
        initializeBoardFromFEN(fen);
    }

    public ChessBoard() {
        this.board = new char[SIZE][SIZE];
        initializeBoardFromFEN("rnbqkbnr/PPPPPPPP/8/8/8/8/PPPPPPPP/RNBQKBNR");
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
                    board[i][colIndex] = c;
                    colIndex++;
                }
            }
        }
    }

    public char getPieceAtPosition(int row, int col) {
        if (row < 0 || row >= SIZE || col < 0 || col >= SIZE) {
            throw new IllegalArgumentException(String.format("Position outside of the chessboard: (%d, %d)", row, col));
        }
        return board[row][col];
    }

    public void displayBoard() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
    }

    public boolean isEmpty(int row, int col) {
        return board[row][col] == '\0';
    }

    public void movePiece(int startRow, int startCol, int endRow, int endCol) {
        if (isEmpty(startRow, startCol)) {
            throw new IllegalArgumentException(String.format("No piece to move at this position: (%d, %d)", startRow, startCol));
        }
        board[endRow][endCol] = board[startRow][startCol];
        board[startRow][startCol] = '\0';
    }

    public String toFEN() {
        return toFEN(this.board);
    }

    public static String toFEN(char[][] board) {
        StringBuilder fen = new StringBuilder();

        for (int i = 0; i < SIZE; i++) {
            int emptyCount = 0;

            for (int j = 0; j < SIZE; j++) {
                char piece = board[i][j];

                if (piece == '\0') {
                    emptyCount++;
                } else {
                    if (emptyCount > 0) {
                        fen.append(emptyCount);
                        emptyCount = 0;
                    }
                    fen.append(piece);
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
}
