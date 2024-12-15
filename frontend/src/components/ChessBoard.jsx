import React, { useState, useEffect } from 'react';
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
                piece: piece,
            },
            headers: {
                'Content-Type': 'application/json',
            },
            withCredentials: true
        });
        return response.data;
    } catch (error) {
        console.error('Error while getting possible moves:', error);
    }
}

async function fetchBoard() {
    try {
        const response = await axios.get('http://localhost:8080/api/board', {
            headers: {
                'Content-Type': 'application/json',
            },
            withCredentials: true,
        });
        return response.data;
    } catch (error) {
        console.error('Error while getting the game board:', error);
        return null;
    }
}

async function movePiece(oldRow, oldCol, newRow, newCol) {
    try {
        const response = await axios.post('http://localhost:8080/api/move', null, {
            params: {
                oldRow,
                oldCol,
                newRow,
                newCol
            }
        });
        const newFen = response.data; // La réponse est supposée être le FEN mis à jour
        if (newFen && typeof newFen === 'string') {
            return newFen;
        } else {
            console.error("Invalid FEN received:", newFen);
            return null; // Ou une valeur par défaut, comme un FEN initial valide
        }
    } catch (error) {
        console.error('Error while moving the piece:', error);
        return null; // Si l'appel échoue, on renvoie null pour éviter de planter l'application
    }
}


function ChessBoard() {
    const [fen, setFen] = useState("");
    const [chessBoard, setChessBoard] = useState([]);
    const [selectedPiece, setSelectedPiece] = useState(null);
    const [possibleMoves, setPossibleMoves] = useState([]);
    const [loading, setLoading] = useState(true);

    // Charger le board au début
    useEffect(() => {
        const loadBoard = async () => {
            setLoading(true);
            const fetchedFen = await fetchBoard();
            if (fetchedFen) {
                setFen(fetchedFen);
                setChessBoard(parseFEN(fetchedFen));
            }
            setLoading(false);
        };
        loadBoard();
    }, []);

    // Sélectionner ou déplacer la pièce
    const handlePieceSelection = async (row, col) => {
        const piece = chessBoard[row][col];

        // Si une pièce est déjà sélectionnée et qu'on clique sur une case valide
        if (selectedPiece != null && possibleMoves.includes('' + row + col)) {
            try {
                // Déplacer la pièce via le backend
                const newFen = await movePiece(selectedPiece.row, selectedPiece.col, row, col);
                setFen(newFen); // Mise à jour du FEN après le mouvement
                setChessBoard(parseFEN(newFen)); // Mise à jour de l'échiquier
                setSelectedPiece(null); // Désélectionner la pièce
                setPossibleMoves([]); // Effacer les cases possibles
            } catch (error) {
                console.error('Error while moving the piece:', error);
            }
        } else if (piece !== "") {
            // Si une pièce est sélectionnée, afficher les coups possibles
            setSelectedPiece({ row, col });
            try {
                const moves = await fetchPossiblesMoves(row, col, piece);
                setPossibleMoves(moves || []);
            } catch (error) {
                console.error("Failed to fetch possible moves:", error);
                setPossibleMoves([]);
            }
        } else {
            // Si la case est vide ou la pièce est déjà sélectionnée, annuler la sélection
            setSelectedPiece(null);
            setPossibleMoves([]);
        }
    };

    // Générez le tableau de l'échiquier
    const generateBoard = () => {
        let board = [];
        for (let row = 0; row < 8; row++) {
            let rowCells = [];
            for (let col = 0; col < 8; col++) {
                const isWhite = (row + col) % 2 === 0;
                const piece = chessBoard[row][col];
                const highlighted = possibleMoves.some(
                    (move) => move === row.toString() + col.toString()
                );
    
                rowCells.push(
                    <div
                        key={`${row}-${col}`}
                        className={`square 
                            ${isWhite ? 'white' : 'black'} 
                            ${selectedPiece && selectedPiece.row === row && selectedPiece.col === col ? 'selected' : ''} 
                            ${highlighted ? 'highlighted' : ''}`}
                        onMouseDown={() => handlePieceSelection(row, col)}
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

    return (
        <div className="chessboard">
            {loading ? <div>Loading...</div> : generateBoard()}
        </div>
    );
}

export default ChessBoard;
