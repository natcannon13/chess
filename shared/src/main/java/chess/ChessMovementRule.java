package chess;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;

public class ChessMovementRule {

    public ChessMovementRule(){

    }

    public enum MoveResultState{
        FREE,
        INVALID,
        BLOCKED,
        CAPTURE
    }

    public MoveResultState moveResult(ChessBoard board, ChessPiece piece, ChessPosition myPosition){
        if(myPosition.getRow() < 1 || myPosition.getColumn() < 1 || myPosition.getRow() > 8 || myPosition.getColumn() > 8) {
            return MoveResultState.INVALID;
        }
        ChessPiece pieceInDestination = board.getPiece(myPosition);
        if(pieceInDestination != null){
            if(pieceInDestination.getTeamColor() == piece.getTeamColor()){
                return MoveResultState.BLOCKED;
            }
            else{
                return MoveResultState.CAPTURE;
            }
        }
        return MoveResultState.FREE;
    }



    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPiece piece, ChessPosition myPosition){
        return new ArrayList<ChessMove>();
    }

}
