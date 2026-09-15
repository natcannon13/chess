package chess.movement;

import chess.*;

import java.util.ArrayList;
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
            ArrayList<ChessPiece.PieceType> promotionTypes = new ArrayList<>();
            row += direction;
            col += i;
            if ((direction == 1 && row == 8) || (direction == -1) && row == 1) {
                promote = true;
                promotionTypes.add(ChessPiece.PieceType.BISHOP);
                promotionTypes.add(ChessPiece.PieceType.KNIGHT);
                promotionTypes.add(ChessPiece.PieceType.QUEEN);
                promotionTypes.add(ChessPiece.PieceType.ROOK);
            }
            ChessPosition pos = new ChessPosition(row, col);
            MoveResultState nextMove = moveResult(board, piece, pos);
            if (i == 0) {
                if (nextMove == MoveResultState.FREE) {
                    if(!promote){
                        moves.add(new ChessMove(myPosition, pos, null));
                    }
                    else{
                        for(ChessPiece.PieceType t : promotionTypes){
                            moves.add(new ChessMove(myPosition, pos, t));
                        }
                    }
                    if ((direction == 1 && myPosition.getRow() == 2) || (direction == -1) && myPosition.getRow() == 7){
                        pos = new ChessPosition((row + direction), col);
                        nextMove = moveResult(board, piece, pos);
                        if(nextMove == MoveResultState.FREE){
                            moves.add(new ChessMove(myPosition, pos, null));
                        }
                    }
                }
            } else {
                if (nextMove == MoveResultState.CAPTURE) {
                    if(!promote){
                        moves.add(new ChessMove(myPosition, pos, null));
                    }
                    else{
                        for(ChessPiece.PieceType t : promotionTypes){
                            moves.add(new ChessMove(myPosition, pos, t));
                        }
                    }
                }
            }
        }
        return moves;
    }

}
