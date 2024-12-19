package com.chessgame.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.chessgame.backend.domain.enums.GameStatus;
import com.chessgame.backend.model.ChessGame;

@Repository
public interface ChessGameRepository extends JpaRepository<ChessGame, Long> {
    List<ChessGame> findByGameStatus(GameStatus gameStatus);
}