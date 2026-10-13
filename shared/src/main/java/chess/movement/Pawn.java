package chess.movement;

import chess.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class Pawn extends ChessMovementRule {

    public Pawn(){

    }

    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPiece piece, ChessPosition myPosition) {
        ArrayList<ChessMove> moves = new ArrayList<ChessMove>();
        int direction = 0;
        if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
            direction = 1;
        } else {
            direction = -1;
        }
        for (int i = -1; i < 2; i++) {
            int row = myPosition.getRow();
            int col = myPosition.getColumn();
            boolean promote = false;
            row += direction;
            col += i;
            if ((direction == 1 && row == 8) || (direction == -1) && row == 1) {
                promote = true;
            }
            ChessPosition pos = new ChessPosition(row, col);
            MoveResultState nextMove = moveResult(board, piece, pos);
            if (i == 0) {
                moves.addAll(moveStraight(board, piece, myPosition, pos, nextMove, promote));
            } else {
                if (nextMove == MoveResultState.CAPTURE) {
                    if(!promote){
                        moves.add(new ChessMove(myPosition, pos, null));
                    }
                    else{
                        moves.addAll(getPromotions(myPosition, pos));
                    }
                }
            }
        }
        return moves;
    }

    public Collection<ChessMove> getPromotions(ChessPosition start, ChessPosition end){
        ArrayList<ChessMove> moves = new ArrayList<>();
        ArrayList<ChessPiece.PieceType> promotionTypes = new ArrayList<>();
        promotionTypes.add(ChessPiece.PieceType.BISHOP);
        promotionTypes.add(ChessPiece.PieceType.KNIGHT);
        promotionTypes.add(ChessPiece.PieceType.QUEEN);
        promotionTypes.add(ChessPiece.PieceType.ROOK);
        for(ChessPiece.PieceType t : promotionTypes){
            moves.add(new ChessMove(start, end, t));
        }
        return moves;
    }

    public Collection<ChessMove> moveStraight(ChessBoard board, ChessPiece piece, ChessPosition startPos, ChessPosition endPos, MoveResultState nextMove, boolean promote){
        ArrayList<ChessMove> moves = new ArrayList<>();
        int direction = endPos.getRow() - startPos.getRow();
        int row = endPos.getRow();
        int col = endPos.getColumn();
        if (nextMove == MoveResultState.FREE) {
            if(!promote){
                moves.add(new ChessMove(startPos, endPos, null));
            }
            else{
                moves.addAll(getPromotions(startPos, endPos));
            }
            if ((direction == 1 && startPos.getRow() == 2) || (direction == -1) && startPos.getRow() == 7){
                endPos = new ChessPosition((row + direction), col);
                nextMove = moveResult(board, piece, endPos);
                if(nextMove == MoveResultState.FREE) moves.add(new ChessMove(startPos, endPos, null));
            }
        }
        return moves;
    }

}
