import React, { useState } from 'react';
import './ChessBoard.css';

function parseFEN(fen) {
    let board = [];
    let rows = fen.split("/");

    for (let row of rows) {
        let rowArr = [];
        for (let char of row) {
            if (isNaN(char)) {
                rowArr.push(char);
            } else {
                let emptySquares = parseInt(char);
                for (let i = 0; i < emptySquares; i++) {
                    rowArr.push("");
                }
            }
        }
        board.push(rowArr);
    }
    return board;
}

function ChessBoard({ fen = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR" }) {
    const chessBoard = parseFEN(fen);

    const getPieceImage = (piece) => {
        const pieceMap = {
            'r': 'rook',
            'n': 'knight',
            'b': 'bishop',
            'q': 'queen',
            'k': 'king',
            'p': 'pawn',
        };

        const color = piece === piece.toUpperCase() ? 'white' : 'black';
        const pieceType = pieceMap[piece.toLowerCase()];

        return `./src/assets/pieces/${color}_${pieceType}.png`;
    };

    const [selectedPiece, setSelectedPiece] = useState(null);

    const handleSquareClick = (row, col) => {
        if (chessBoard[col][row] !== "") {
            setSelectedPiece({ row, col });
        }
    };

    const generateBoard = () => {
        let board = [];
        for (let row = 0; row < 8; row++) { // Boucle des colonnes
            let rowCells = [];
            for (let col = 0; col < 8; col++) { // Boucle des lignes
                const isWhite = (row + col) % 2 === 0; // Couleur des cases
                const piece = chessBoard[col][row]; // Inversion des indices
    
                rowCells.push(
                    <div
                        key={`${col}-${row}`}
                        className={`
                            square 
                            ${isWhite ? 'white' : 'black'} 
                            ${selectedPiece && selectedPiece.row === row && selectedPiece.col === col ? 'selected' : ''}
                        `}
                        onClick={() => handleSquareClick(row, col)}
                    >
                        {piece && (
                            <img
                                src={getPieceImage(piece)}
                                alt={`${piece}`}
                                className="piece"
                            />
                        )}
                    </div>
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
