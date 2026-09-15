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
            ChessPiece.PieceType promotionPiece = null;
            row += direction;
            col += i;
            if ((direction == 1 && row == 8) || (direction == -1) && row == 1) {
                promotionPiece = ChessPiece.PieceType.QUEEN; //hardcoded queen for now, will change to an input later
            }
            ChessPosition pos = new ChessPosition(row, col);
            MoveResultState nextMove = moveResult(board, piece, pos);
            if (i == 0) {
                if (nextMove == MoveResultState.FREE) {
                    moves.add(new ChessMove(myPosition, pos, promotionPiece));
                }
            } else {
                if (nextMove == MoveResultState.CAPTURE) {
                    moves.add(new ChessMove(myPosition, pos, promotionPiece));
                }
            }
        }
        return moves;
    }

}
