package com.chessgame.backend.service;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ChessEngine {

    public List<String> getPossibleMoves(int row, int col, char piece) {
        char type = Character.toUpperCase(piece);
        switch (type) {
            case 'P':
                return getPawnPossibleMoves(row, col, piece);

            case 'R':
                return getRookPossibleMoves(row, col);
            
            case 'N':
                return getKnightPossibleMoves(row, col);

            case 'B':
                return getBishopPossibleMoves(row, col);

            case 'Q':
                return getQueenPossibleMoves(row, col);

            case 'K':
                return getKingPossibleMoves(row, col);
        
            default:
                List<String> possibleMoves = new ArrayList<>();
                return possibleMoves;
        }
    }

    public String coordinatesToPosition(int row, int col) {
        row = Math.max(0, Math.min(7, row));
        col = Math.max(0, Math.min(7, col));
        StringBuilder sb = new StringBuilder().append(row).append(col);
        return sb.toString();
    }

    public List<String> getPawnPossibleMoves(int row, int col, char piece) {
        List<String> possibleMoves = new ArrayList<>();
        int isWhite = Character.isUpperCase(piece) ? -1 : 1;
        possibleMoves.add(coordinatesToPosition(row, (col + 1 * isWhite)));
        if (isWhite == -1 && col == 6 || isWhite == 1 && col == 1) {
            possibleMoves.add(coordinatesToPosition(row, (col + 2 * isWhite)));
        }
        return possibleMoves;
    }

    public List<String> getRookPossibleMoves(int row, int col) {
        List<String> possibleMoves = new ArrayList<>();

        for (int i = col - 1; i >= 0; i--) {
            possibleMoves.add(coordinatesToPosition(row, i));
        }
        for (int i = col + 1; i < 8; i++) {
            possibleMoves.add(coordinatesToPosition(row, i));
        }
        for (int i = row - 1; i >= 0; i--) {
            possibleMoves.add(coordinatesToPosition(i, col));
        }
        for (int i = row + 1; i < 8; i++) {
            possibleMoves.add(coordinatesToPosition(i, col));
        }

        return possibleMoves;
    }

    public List<String> getKnightPossibleMoves(int row, int col) {
        List<String> possibleMoves = new ArrayList<>();
        
        int[][] directions = {
            {-2, -1}, {-2, 1},
            {-1, -2}, {-1, 2},
            {1, -2}, {1, 2},
            {2, -1}, {2, 1}
        };
    
        for (int[] direction : directions) {
            int newRow = row + direction[0];
            int newCol = col + direction[1];
    
            if (newRow >= 0 && newRow < 8 && newCol >= 0 && newCol < 8) {
                possibleMoves.add(coordinatesToPosition(newRow, newCol));
            }
        }
    
        return possibleMoves;
    }

    public List<String> getBishopPossibleMoves(int row, int col) {
        List<String> possibleMoves = new ArrayList<>();
        int[][] directions = {
            {-1, -1}, {-1, 1},
            {1, -1}, {1, 1}
        };
    
        for (int[] direction : directions) {
            int x = row;
            int y = col;
            while (true) {
                x += direction[0];
                y += direction[1];
                if (x < 0 || x >= 8 || y < 0 || y >= 8) {
                    break;
                }
                possibleMoves.add(coordinatesToPosition(x, y));
            }
        }
    
        return possibleMoves;
    }

    public List<String> getKingPossibleMoves(int row, int col) {
        List<String> possibleMoves = new ArrayList<>();
        
        int[][] directions = {
            {-1, -1}, {-1, 0}, {-1, 1},
            {0, -1}, {0, 1},
            {1, -1}, {1, 0}, {1, 1}
        };
    
        for (int[] direction : directions) {
            int x = row + direction[0];
            int y = col + direction[1];
    
            if (x >= 0 && x < 8 && y >= 0 && y < 8) {
                possibleMoves.add(coordinatesToPosition(x, y));
            }
        }
    
        return possibleMoves;
    }

    public List<String> getQueenPossibleMoves(int row, int col) {
        List<String> possibleMoves = new ArrayList<>();
        List<String> rookMoves = getRookPossibleMoves(row, col);
        List<String> bishopMoves = getBishopPossibleMoves(row, col);
        possibleMoves.addAll(rookMoves);
        possibleMoves.addAll(bishopMoves);
    
        return possibleMoves;
    }
}
