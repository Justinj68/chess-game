import React from 'react';
import './ChessBoard.css';

function ChessBoard() {
  const generateBoard = () => {
    let board = [];
    for (let row = 0; row < 8; row++) {
      let rowCells = [];
      for (let col = 0; col < 8; col++) {
        // Alternance des couleurs
        const isWhite = (row + col) % 2 === 0;
        rowCells.push(
          <div
            key={`${row}-${col}`}
            className={`square ${isWhite ? 'white' : 'black'}`}
          />
        );
      }
      board.push(
        <div key={row} className="row">
          {rowCells}
        </div>
      );
    }
    return board;
  };

  return <div className="chessboard">{generateBoard()}</div>;
}

export default ChessBoard;
