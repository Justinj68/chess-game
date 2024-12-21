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

async function fetchPossiblesMoves(gameId, row, col, piece) {
    try {
        const response = await axios.get(`http://localhost:8080/api/game/${gameId}/moves`, {
            params: {
                row: row,
                col: col,
                piece: piece,
                playerId: localStorage.getItem('playerId'),
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

async function fetchBoard(gameId) {
    try {
        const response = await axios.get(`http://localhost:8080/api/game/${gameId}`, {
            headers: {
                'Content-Type': 'application/json',
            },
            withCredentials: true,
        });
        return response.data.board;
    } catch (error) {
        console.error('Error while getting the game board:', error);
        return null;
    }
}

async function movePiece(gameId, row, col, newRow, newCol) {
    try {
        const response = await axios.put(`http://localhost:8080/api/game/${gameId}/move`, null, {
            params: {
                row,
                col,
                newRow,
                newCol,
                playerId: localStorage.getItem('playerId'),
            },
        });
        const newFen = response.data.board;
        console.log(response.data);
        if (newFen && typeof newFen === 'string') {
            return newFen;
        } else {
            console.error("Invalid FEN received:", newFen);
            return null;
        }
    } catch (error) {
        console.error('Error while moving the piece:', error);
        return null;
    }
}

function ChessBoard({ gameId }) {
    const [fen, setFen] = useState("");
    const [chessBoard, setChessBoard] = useState([]);
    const [selectedPiece, setSelectedPiece] = useState(null);
    const [possibleMoves, setPossibleMoves] = useState([]);
    const [loading, setLoading] = useState(true);

    // Charger le board au début
    useEffect(() => {
        const loadBoard = async () => {
            setLoading(true);
            const fetchedFen = await fetchBoard(gameId);
            if (fetchedFen) {
                setFen(fetchedFen);
                setChessBoard(parseFEN(fetchedFen));
            }
            setLoading(false);
        };

        loadBoard();

        const socket = new WebSocket("ws://localhost:8080/ws?room=" + encodeURIComponent("GAME#" + gameId));

        socket.onopen = () => {
            console.log("WebSocket connection established.");
        };
        
        socket.onmessage = (event) => {
            const data = JSON.parse(event.data);
            if (data.type === "move") {
                console.log("Mouvement reçu : ", data.data);
                setFen(data.data.board);
                setChessBoard(parseFEN(data.data.board));
            }
        };
        
        socket.onclose = () => {
            console.log("WebSocket connection closed.");
        };
    }, [gameId]);

    const handlePieceSelection = async (row, col) => {
        const piece = chessBoard[row][col];

        if (selectedPiece != null && possibleMoves.includes('' + row + col)) {
            try {
                const newFen = await movePiece(gameId, selectedPiece.row, selectedPiece.col, row, col);
                setFen(newFen);
                setChessBoard(parseFEN(newFen));
                setSelectedPiece(null);
                setPossibleMoves([])
            } catch (error) {
                console.error('Error while moving the piece:', error);
            }
        } else if (piece !== "") {
            setSelectedPiece({ row, col });
            try {
                const moves = await fetchPossiblesMoves(gameId, row, col, piece);
                setPossibleMoves(moves || []);
            } catch (error) {
                console.error("Failed to fetch possible moves:", error);
                setPossibleMoves([]);
            }
        } else {
            setSelectedPiece(null);
            setPossibleMoves([]);
        }
    };

    const generateBoard = () => {
        let board = [];
        for (let col = 0; col < 8; col++) {
            let rowCells = [];
            for (let row = 0; row < 8; row++) {
                const isBlack = (row + col) % 2 === 0;
                const piece = chessBoard[row][col];
                const highlighted = possibleMoves.some(
                    (move) => move === row.toString() + col.toString()
                );

                rowCells.push(
                    <div
                        key={`${row}-${col}`}
                        className={`square 
                            ${isBlack ? 'black' : 'white'} 
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
                <div key={col} className="row">
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
