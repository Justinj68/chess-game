package com.chessgame.backend.domain;

public class Piece {
    private Team team;
    private PieceType type;

    public Piece(char pieceCode) {
        this.team = findTeam(pieceCode);
        this.type = findType(pieceCode);
    }

    private Piece(Team team, PieceType type) {
        this.team = team;
        this.type = type;
    }

    private PieceType findType(char pieceCode) {
        pieceCode = Character.toUpperCase(pieceCode);
        switch (pieceCode) {
            case 'P':
                return PieceType.PAWN;
            case 'R':
                return PieceType.ROOK;
            case 'N':
                return PieceType.KNIGHT;
            case 'B':
                return PieceType.BISHOP;
            case 'Q':
                return PieceType.QUEEN;
            case 'K':
                return PieceType.KING;
            default:
                return null;
        }
    }

    private Team findTeam(char pieceCode) {
        if (pieceCode == '\0') {
            return null;
        }
        return Character.isUpperCase(pieceCode) ? Team.WHITE : Team.BLACK;
    }

    public PieceType getType() {
        return this.type;
    }

    public Team getTeam() {
        return this.team;
    }

    public boolean isWhite() {
        return team.equals(Team.WHITE);
    }

    public boolean isBlack() {
        return team.equals(Team.BLACK);
    }

    public char toChar() {
        char piece = '\0';
        switch (this.type) {
            case PAWN:
                piece = 'P';
                break;
            case ROOK:
                piece = 'R';
                break;
            case KNIGHT:
                piece = 'N';
                break;
            case BISHOP:
                piece = 'B';
                break;
            case QUEEN:
                piece = 'Q';
                break;
            case KING:
                piece = 'K';
                break;
        }
        return isBlack() ? Character.toLowerCase(piece) : piece;
    }

    public boolean exists() {
        return team != null && type != null;
    }

    public Team getEnemyTeam() {
        if (team == null) {
            return null;
        }
        return team.equals(Team.WHITE) ? Team.BLACK : Team.WHITE;
    }

    public boolean areSameTeam(Piece piece) {
        if (piece.getTeam() == null) {
            return false;
        }
        return team.equals(piece.getTeam());
    }

    public boolean isKing() {
        return type.equals(PieceType.KING);
    }

    @Override
    public Piece clone() {
        return new Piece(this.team, this.type);
    }
}
