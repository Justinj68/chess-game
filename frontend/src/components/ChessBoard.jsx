import React, { useState } from 'react';
import './ChessBoard.css';
import axios from 'axios';

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



function getPieceImage(piece) {
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
    
}

async function fetchPossiblesMoves(row, col, piece) {
    try {
        const response = await axios.get('http://localhost:8080/api/moves', {
            params: {
            row: row,
            col: col,
            piece: new String(piece), // Ton type de pièce
            },
            headers: {
                'Content-Type': 'application/json',
            },
            withCredentials: true
        });
        console.log('Possible moves:', response.data);
        return response.data;
    } catch (error) {
        console.error('Error while getting possible moves:', error);
    }
}

function ChessBoard({ fen = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR" }) {
    const chessBoard = parseFEN(fen);
    const [selectedPiece, setSelectedPiece] = useState(null);
    const [possibleMoves, setPossibleMoves] = useState([]);

    const handleMouseDown = async (row, col) => {
        const piece = chessBoard[col][row];
        if (piece !== "") {
            setSelectedPiece({ row, col });
            try {
                const moves = await fetchPossiblesMoves(row, col, piece);
                setPossibleMoves(moves || []); // Safeguard in case moves is null/undefined
            } catch (error) {
                console.error("Failed to fetch possible moves:", error);
                setPossibleMoves([]); // Reset to an empty array in case of error
            }
        } else {
            setSelectedPiece(null);
            setPossibleMoves([]);
        }
    };

    const generateBoard = () => {
        let board = [];
        for (let row = 0; row < 8; row++) {
            let rowCells = [];
            for (let col = 0; col < 8; col++) {
                const isWhite = (row + col) % 2 === 0;
                const piece = chessBoard[col][row];
                const highlighted = possibleMoves.some(
                    (move) => move === row.toString() + col.toString()
                ); // Safely check if the cell is highlighted
    
                rowCells.push(
                    <div
                        key={`${col}-${row}`}
                        className={`
                            square 
                            ${isWhite ? 'white' : 'black'} 
                            ${selectedPiece && selectedPiece.row === row && selectedPiece.col === col ? 'selected' : ''}
                            ${highlighted ? 'highlighted' : ''}
                        `}
                        onMouseDown={() => handleMouseDown(row, col)}
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
