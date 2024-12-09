package com.chessgame.backend.service;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ChessEngine {

    public List<String> getPossibleMoves(int row, int col, String piece) {
        List<String> possiblesMoves = new ArrayList<>();
        possiblesMoves.add("44");
        return possiblesMoves;
    }
}
