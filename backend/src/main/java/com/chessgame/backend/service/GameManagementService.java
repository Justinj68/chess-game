package com.chessgame.backend.service;

import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chessgame.backend.domain.enums.GameStatus;
import com.chessgame.backend.model.ChessGame;
import com.chessgame.backend.repository.ChessGameRepository;

import jakarta.transaction.Transactional;

@Service
public class GameManagementService {
    @Autowired
    private ChessGameRepository chessGameRepository;

    private ChessGame createGame(String playerId) {
        ChessGame chessGame = new ChessGame();
        Random random = new Random();
        int randomNumber = random.nextInt(2);
        if (randomNumber == 0) {
            chessGame.setWhitePlayerId(playerId);
        } else {
            chessGame.setBlackPlayerId(playerId);
        }
        chessGameRepository.save(chessGame);
        return chessGame;
    }

    private ChessGame joinGame(String playerId, Long gameId) throws Exception {
        ChessGame chessGame = getGame(gameId);
        String whitePlayerId = chessGame.getWhitePlayerId();
        String blackPlayerId = chessGame.getBlackPlayerId();
        if (whitePlayerId == null) {
            if (playerId.equals(blackPlayerId)) {
                throw new Exception("Player #" + playerId + " is already in the game #" + gameId + " as Black team.");
            }
            chessGame.setWhitePlayerId(playerId);
            chessGame.setGameStatus(GameStatus.IN_PROGRESS);
            return chessGameRepository.save(chessGame);
        } else if (blackPlayerId == null) {
            if (playerId.equals(whitePlayerId)) {
                throw new Exception("Player #" + playerId + " is already in the game #" + gameId + " as White team.");
            }
            chessGame.setBlackPlayerId(playerId);
            chessGame.setGameStatus(GameStatus.IN_PROGRESS);
            return chessGameRepository.save(chessGame);
        } else {
            throw new Exception("Game #" + gameId + " is already full. Player #" + playerId + " cannot join.");
        }
    }
    

    public ChessGame getGame(Long id) throws Exception {
        Optional<ChessGame> chessGame = chessGameRepository.findById(id);
        ChessGame game = chessGame.orElse(null);
        if (game == null) {
            throw new Exception("No game found with id:" + id);
        }
        return game;
    }

    private List<ChessGame> getWaitingGames() {
        return chessGameRepository.findByGameStatus(GameStatus.WAITING);
    }

    @Transactional
    public synchronized ChessGame play(String playerId) throws Exception {
        List<ChessGame> chessGames = getWaitingGames();
        if (chessGames.isEmpty()) {
            return createGame(playerId);
        }
        Random random = new Random();
        int randomIndex = random.nextInt(chessGames.size());
        Long gameId = chessGames.get(randomIndex).getId();
        try {
            return joinGame(playerId, gameId);
        } catch (Exception e) {
            return createGame(playerId);
        }
    }
    
    public List<ChessGame> getGames() {
        List<ChessGame> chessGames = chessGameRepository.findAll();
        return chessGames;
    }
}
