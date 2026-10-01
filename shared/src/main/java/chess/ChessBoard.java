package chess;

import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {
    private ChessPiece[][] board = new ChessPiece[8][8];
    public ChessBoard() {
    }

    public ChessBoard(ChessBoard other){
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                if(other.board[i][j] == null){
                    this.board[i][j] = null;
                }
                else{
                    this.board[i][j] = new ChessPiece(other.board[i][j]);
                }
            }
        }
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        board[position.getRow()-1][position.getColumn()-1] = piece;
    }

    public void removePiece(ChessPosition position){
        board[position.getRow()-1][position.getColumn()-1] = null;
    }

    public void movePiece(ChessMove move){
        ChessPiece piece = getPiece(move.getStartPosition());
        addPiece(move.getEndPosition(), piece);
        //This should capture opposing piece as well
        removePiece(move.getStartPosition());
    }

    public void promotePiece(ChessPosition position, ChessPiece.PieceType promotion){
        ChessGame.TeamColor color = getColor(position);
        removePiece(position);
        addPiece(position, new ChessPiece(color, promotion));
    }
    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return board[position.getRow()-1][position.getColumn()-1];
    }

    public boolean hasPiece(ChessPosition position){
        return (!(board[position.getRow()-1][position.getColumn()-1] == null));
    }

    public ChessGame.TeamColor getColor(ChessPosition position){
        return getPiece(position).getTeamColor();
    }
    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        board = new ChessPiece[8][8];
        for(int i = 0; i < 2; i++){
            ChessGame.TeamColor color;
            if (i == 0){
                color = ChessGame.TeamColor.WHITE;
            }
            else{
                color = ChessGame.TeamColor.BLACK;
            }
            for(int column = 1; column < 9; column++){
                int backRow;
                int frontRow;
                ChessPiece.PieceType type;
                if(i == 0){
                    backRow = 1;
                    frontRow = 2;
                }
                else{
                    backRow = 8;
                    frontRow = 7;
                }
                ChessPiece pawn = new ChessPiece(color, ChessPiece.PieceType.PAWN);
                ChessPiece back = new ChessPiece(color, pieceByColumn(column));
                ChessPosition frontPos = new ChessPosition(frontRow, column);
                ChessPosition backPos = new ChessPosition(backRow, column);
                addPiece(frontPos, pawn);
                addPiece(backPos, back);
            }
        }
    }

    private ChessPiece.PieceType pieceByColumn(int column){
        if(column == 1 || column == 8){
            return ChessPiece.PieceType.ROOK;
        }
        if(column == 2 || column == 7){
            return ChessPiece.PieceType.KNIGHT;
        }
        if(column == 3 || column == 6){
            return ChessPiece.PieceType.BISHOP;
        }
        if(column == 4){
            return ChessPiece.PieceType.QUEEN;
        }
        if(column == 5){
            return ChessPiece.PieceType.KING;
        }
        throw new RuntimeException("Error finding piece type");
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessBoard that = (ChessBoard) o;
        return Objects.deepEquals(board, that.board);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(board);
    }
}
