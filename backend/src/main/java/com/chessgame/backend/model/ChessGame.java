package com.chessgame.backend.model;

import com.chessgame.backend.domain.enums.GameStatus;
import com.chessgame.backend.domain.enums.Team;

import jakarta.persistence.*;

@Entity
public class ChessGame {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String board;

    private String whitePlayerId;
    private String blackPlayerId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private GameStatus gameStatus;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Team turn;

    public ChessGame() {
        this.board = "rnbkqbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBKQBNR";
        this.gameStatus = GameStatus.WAITING;
        this.turn = Team.WHITE;
        this.blackPlayerId = null;
        this.whitePlayerId = null;
    }

    private ChessGame(Long id, String board, GameStatus gameStatus, Team turn) {
        this.id = id;
        this.board = board;
        this.gameStatus = gameStatus;
        this.turn = turn;
    }

    public Team getPlayerTeam(String playerId) {
        if (playerId.equals(whitePlayerId)) {
            return Team.WHITE;
        }
        if (playerId.equals(blackPlayerId)) {
            return Team.BLACK;
        }
        return null;
    }

    @Override
    public ChessGame clone() {
        return new ChessGame(id, board, gameStatus, turn);
    }

    public Long getId() {
        return id;
    }

    public String getBoard() {
        return board;
    }

    public void setBoard(String board) {
        this.board = board;
    }

    public GameStatus getGameStatus() {
        return gameStatus;
    }

    public void setGameStatus(GameStatus gameStatus) {
        this.gameStatus = gameStatus;
    }

    public Team getTurn() {
        return turn;
    }

    public void setTurn(Team turn) {
        this.turn = turn;
    }

    public String getWhitePlayerId() {
        return whitePlayerId;
    }

    public void setWhitePlayerId(String id) {
        this.whitePlayerId = id;
    }

    public String getBlackPlayerId() {
        return blackPlayerId;
    }

    public void setBlackPlayerId(String id) {
        this.blackPlayerId = id;
    }
}
