package chess;

import java.util.Collection;

public class ChessRulesEngine {

    public ChessRulesEngine(){

    }

    public boolean isInCheck(ChessGame.TeamColor color, ChessBoard board) throws InvalidBoardStateException{
        ChessPosition kingPosition = findKing(color, board);
        if(kingPosition == null){
            throw new InvalidBoardStateException("No king found!");
        }
        for(int i = 1; i < 9; i++){
            for(int j = 1; j < 9; j++){
                ChessPosition searchPosition = new ChessPosition(i, j);
                if(checkHelper(searchPosition, kingPosition, board, color)){
                    return true;
                }
            }
        }
        return false;
    }

    private ChessPosition findKing(ChessGame.TeamColor color, ChessBoard board){
        for(int i = 1; i < 9; i++){
            for(int j = 1; j < 9; j++){
                ChessPosition searchPosition = new ChessPosition(i, j);
                if(board.hasPiece(searchPosition)){
                    ChessPiece piece = board.getPiece(searchPosition);
                    if(piece.getTeamColor() == color && piece.getPieceType() == ChessPiece.PieceType.KING){
                        return searchPosition;
                    }
                }
            }
        }
        return null;
    }

    public boolean checkHelper(ChessPosition piecePosition, ChessPosition kingPosition, ChessBoard board, ChessGame.TeamColor color){
        if(!board.hasPiece(piecePosition)){
            return false;
        }
        if(board.getColor(piecePosition) == color){
            return false;
        }
        else{
            ChessPiece movingPiece = board.getPiece(piecePosition);
            for(ChessMove move : movingPiece.pieceMoves(board, piecePosition)){
                ChessPosition endPosition = move.getEndPosition();
                if(endPosition.equals(kingPosition)){
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isInCheckmate(ChessGame.TeamColor color, ChessBoard board) throws InvalidBoardStateException {
        if(!isInCheck(color, board)){
            return false;
        }
        ChessPosition kingPosition = findKing(color, board);
        ChessPiece king = board.getPiece(kingPosition);
        Collection<ChessMove> moves = king.pieceMoves(board, kingPosition);
        boolean kingCanMove = false;
        for(ChessMove move: moves){
            if(canMakeMoveInCheck(color, move, board)){
                kingCanMove = true;
            }
        }
        return !kingCanMove;
    }

    public boolean canMakeMoveInCheck(ChessGame.TeamColor color, ChessMove move, ChessBoard board) throws InvalidBoardStateException {
        ChessBoard mockBoard = new ChessBoard(board);
        mockBoard.movePiece(move);
        return !isInCheck(color, mockBoard);
    }

}
