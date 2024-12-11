package com.chessgame.backend.service;
import org.springframework.stereotype.Service;

import com.chessgame.backend.model.ChessBoard;

import java.util.ArrayList;
import java.util.List;

@Service
public class ChessEngine {

    private final int SIZE = ChessBoard.SIZE;
    private ChessBoard chessBoard = new ChessBoard("rnbqkbnr/1p6/3K4/p7/1p1P1P1P/8/8/RNBQKBNR");
    private boolean[][] threats;

    private void refreshThreats(String color) {
        this.threats = new boolean[SIZE][SIZE];
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                char piece = chessBoard.getPieceAtPosition(row, col);
                if (piece == '\0' || areSameTeam(color, piece) || Character.toUpperCase(piece) == 'K') {
                    continue;
                }
                List<String> possibleMoves;// = getPossibleMoves(row, col, piece);
                if (Character.toUpperCase(piece) == 'P') {
                    possibleMoves = getPawnAttackRange(row, col, piece);
                } else {
                    possibleMoves = getPossibleMoves(row, col, piece);
                }
                for (String threat : possibleMoves) {
                    int targetRow = Character.getNumericValue(threat.charAt(0));
                    int targetCol = Character.getNumericValue(threat.charAt(1));
                    this.threats[targetRow][targetCol] = true;
                }
            }
        }
    }

    public List<String> getPossibleMoves(int row, int col, char piece) {
        char type = Character.toUpperCase(piece);
        switch (type) {
            case 'P':
                return getPawnPossibleMoves(row, col, piece);

            case 'R':
                return getRookPossibleMoves(row, col, piece);
            
            case 'N':
                return getKnightPossibleMoves(row, col, piece);

            case 'B':
                return getBishopPossibleMoves(row, col, piece);

            case 'Q':
                return getQueenPossibleMoves(row, col, piece);

            case 'K':
                return getKingPossibleMoves(row, col, piece);
        
            default:
                List<String> possibleMoves = new ArrayList<>();
                return possibleMoves;
        }
    }

    public String coordinatesToPosition(int row, int col) {
        return "" + row + col;
    }

    public List<String> getPawnPossibleMoves(int row, int col, char piece) {
        List<String> possibleMoves = new ArrayList<>();
        int direction = Character.isUpperCase(piece) ? -1 : 1;
        if (row > 0 && row < 7) {
            if (chessBoard.isEmpty(row + direction, col)) {
                possibleMoves.add(coordinatesToPosition(row + direction, col));
                if ((direction == -1 && row == 6) || (direction == 1 && row == 1) && chessBoard.isEmpty(row + 2 * direction, col)) {
                    possibleMoves.add(coordinatesToPosition(row + 2 * direction, col));
                }
            } 
            if (col < 7 && !chessBoard.isEmpty(row + direction, col + 1) && canKill(row + direction, col + 1, piece)) {
                possibleMoves.add(coordinatesToPosition(row + direction, col + 1));
            }
            if (col > 0 && !chessBoard.isEmpty(row + direction, col - 1) && canKill(row + direction, col - 1, piece)) {
                possibleMoves.add(coordinatesToPosition(row + direction, col - 1));
            }
        }
        return possibleMoves;
    }

    public List<String> getRookPossibleMoves(int row, int col, char piece) {
        List<String> possibleMoves = new ArrayList<>();
        int[][] directions = {{ 0, -1}, { 0,  1}, {-1,  0}, {1, 0}};
        for (int[] direction : directions) {
            addMovesInDirection(row, col, direction[0], direction[1], piece, possibleMoves);
        }
        return possibleMoves;
    }

    public List<String> getBishopPossibleMoves(int row, int col, char piece) {
        List<String> possibleMoves = new ArrayList<>();
        int[][] directions = {{-1, -1}, {-1,  1}, { 1,  1}, { 1, -1}};
        for (int[] direction : directions) {
            addMovesInDirection(row, col, direction[0], direction[1], piece, possibleMoves);
        }
        return possibleMoves;
    }
    
    private void addMovesInDirection(int row, int col, int rowOffset, int colOffset, char piece, List<String> moves) {
        int currentRow = row + rowOffset;
        int currentCol = col + colOffset;
        System.out.println(piece);
    
        while (currentRow >= 0 && currentRow < SIZE && currentCol >= 0 && currentCol < SIZE) {
            String position = coordinatesToPosition(currentRow, currentCol);
            moves.add(position);
    
            if (!chessBoard.isEmpty(currentRow, currentCol)) {
                if (!canKill(currentRow, currentCol, piece)) {
                    moves.remove(position);
                }
                break;
            }
    
            currentRow += rowOffset;
            currentCol += colOffset;
        }
    }

    public List<String> getKnightPossibleMoves(int row, int col, char piece) {
        List<String> possibleMoves = new ArrayList<>();
        
        int[][] directions = {
            {-2, -1}, {-2, 1},
            {-1, -2}, {-1, 2},
            { 1, -2}, { 1, 2},
            { 2, -1}, { 2, 1}
        };
    
        for (int[] direction : directions) {
            int newRow = row + direction[0];
            int newCol = col + direction[1];
    
            if (newRow >= 0 && newRow < SIZE && newCol >= 0 && newCol < SIZE) {
                if (canKill(newRow, newCol, piece)) {
                    possibleMoves.add(coordinatesToPosition(newRow, newCol));
                }
            }
        }
    
        return possibleMoves;
    }

    public List<String> getKingPossibleMoves(int row, int col, char piece) {
        this.refreshThreats(Character.isUpperCase(piece) ? "white" : "black");
        List<String> possibleMoves = new ArrayList<>();
        
        int[][] directions = {
            {-1, -1}, {-1,  0}, {-1,  1},
            { 0, -1},           { 0,  1},
            { 1, -1}, { 1,  0}, { 1,  1}
        };
    
        for (int[] direction : directions) {
            int x = row + direction[0];
            int y = col + direction[1];
    
            if (x >= 0 && x < SIZE && y >= 0 && y < SIZE) {
                if (chessBoard.isEmpty(x, y) || canKill(x, y, piece)) {
                    if (!isNextToEnemyKing(x, y, piece) && !this.threats[x][y]) {
                        possibleMoves.add(coordinatesToPosition(x, y));
                    }
                }
            }
        }
    
        return possibleMoves;
    }

    private boolean isNextToEnemyKing(int row, int col, char piece) {
        int[][] directions = {
            {-1, -1}, {-1,  0}, {-1,  1},
            { 0, -1},           { 0,  1},
            { 1, -1}, { 1,  0}, { 1,  1}
        };
    
        for (int[] direction : directions) {
            int adjacentRow = row + direction[0];
            int adjacentCol = col + direction[1];

            if (adjacentRow < 0 || adjacentRow >= SIZE && adjacentCol < 0 && adjacentCol >= SIZE) {
                continue;
            }
    
            char adjacentPiece = chessBoard.getPieceAtPosition(adjacentRow, adjacentCol);
            if (Character.toUpperCase(adjacentPiece) == 'K' && !areSameTeam(piece, adjacentPiece)) {
                return true;
            }
        }
    
        return false;
    }

    public List<String> getQueenPossibleMoves(int row, int col, char piece) {
        List<String> possibleMoves = new ArrayList<>();
        getRookPossibleMoves(row, col, piece).forEach(possibleMoves::add);
        getBishopPossibleMoves(row, col, piece).forEach(possibleMoves::add);
        return possibleMoves;
    }

    public boolean canKill(int row, int col, char piece) {
        if (row < 0 || row > SIZE - 1 || col < 0 || col > SIZE - 1) {
            return false;
        }
        char victim = chessBoard.getPieceAtPosition(row, col);
        if (areSameTeam(piece, victim)) {
            return false;
        }
        return true;
    }

    public boolean areSameTeam(char c1, char c2) {
        return 
            (Character.isLowerCase(c1) && Character.isLowerCase(c2)) 
            || 
            (Character.isUpperCase(c1) && Character.isUpperCase(c2));
    }

    public boolean areSameTeam(String color, char piece) {
        return 
            (color.equals("black") && Character.isLowerCase(piece)) 
            || 
            (color.equals("white") && Character.isUpperCase(piece));
    }

    private List<String> getPawnAttackRange(int row, int col, char piece) {
        List<String> attackZone = new ArrayList<>();
        int direction = Character.isUpperCase(piece) ? -1 : 1;
        if (row > 0 && row < SIZE - 1) {
            if (col < 7) {
                attackZone.add(coordinatesToPosition(row + direction, col + 1));
            }
            if (col > 0) {
                attackZone.add(coordinatesToPosition(row + direction, col - 1));
            }
        }
        return attackZone;
    }
}
